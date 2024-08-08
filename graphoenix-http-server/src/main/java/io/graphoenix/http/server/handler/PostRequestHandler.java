package io.graphoenix.http.server.handler;

import com.google.common.collect.Maps;
import io.graphoenix.core.dto.GraphQLRequest;
import io.graphoenix.http.server.codec.MimeType;
import io.graphoenix.http.server.context.RequestScopeInstanceFactory;
import io.graphoenix.http.server.utils.ResponseUtil;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.OperationHandler;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.nozdormu.spi.context.PublisherBeanContext;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.util.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static io.graphoenix.http.server.context.RequestScopeInstanceFactory.REQUEST_ID;
import static io.graphoenix.http.server.utils.ResponseUtil.next;
import static io.netty.handler.codec.http.HttpHeaderNames.ACCEPT;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

@ApplicationScoped
public class PostRequestHandler extends BaseHandler {

    private final OperationHandler operationHandler;
    private final RequestScopeInstanceFactory requestScopeInstanceFactory;

    @Inject
    public PostRequestHandler(OperationHandler operationHandler, RequestScopeInstanceFactory requestScopeInstanceFactory) {
        this.operationHandler = operationHandler;
        this.requestScopeInstanceFactory = requestScopeInstanceFactory;
    }

    public Publisher<Void> handle(HttpServerRequest request, HttpServerResponse response) {
        String requestId = request.requestId();

        String accept = request.requestHeaders().get(ACCEPT);
        String contentType = request.requestHeaders().get(CONTENT_TYPE);
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());

        if (accept.contains(MimeType.Text.EVENT_STREAM)) {
            String token = Optional.ofNullable(request.requestHeaders().get("X-GraphQL-Event-Stream-Token")).orElseGet(() -> decoder.parameters().containsKey("token") ? decoder.parameters().get("token").get(0) : null);
            String operationId = decoder.parameters().containsKey("operationId") ? decoder.parameters().get("operationId").get(0) : null;
            return response.sse()
                    .addHeader(HttpHeaderNames.CACHE_CONTROL, "no-cache")
                    .addHeader(HttpHeaderNames.CONNECTION, "keep-alive")
                    .status(HttpResponseStatus.ACCEPTED)
                    .send(
                            request.receive().aggregate().asString()
                                    .map(requestString -> contentType.contains(MimeType.Application.GRAPHQL) ? new GraphQLRequest(requestString) : GraphQLRequest.fromJson(requestString))
                                    .flatMapMany(graphQLRequest ->
                                            Flux.just(new Document(graphQLRequest.getQuery()))
                                                    .flatMap(document ->
                                                            ScopeEventResolver.initialized(Maps.newHashMap(Map.of(REQUEST, request, RESPONSE, response, OPERATION, document.getOperationOrError())), RequestScoped.class)
                                                                    .then(requestScopeInstanceFactory.compute(requestId, HttpServerRequest.class, request))
                                                                    .then(requestScopeInstanceFactory.compute(requestId, HttpServerResponse.class, response))
                                                                    .then(requestScopeInstanceFactory.compute(requestId, Operation.class, document.getOperationOrError()))
                                                                    .flatMapMany(operation -> Flux.from(operationHandler.handle(operation, graphQLRequest.getVariables(), token, operationId)))
                                                                    .contextWrite(PublisherBeanContext.of(Document.class, document))
                                                    )
                                    )
                                    .map(jsonValue -> next(jsonValue, operationId))
                                    .onErrorResume(throwable -> this.errorSSEHandler(throwable, response, operationId))
                                    .map(eventString -> ByteBufAllocator.DEFAULT.buffer().writeBytes(eventString.getBytes(StandardCharsets.UTF_8)))
                                    .contextWrite(Context.of(REQUEST_ID, requestId)),
                            byteBuf -> true
                    );
        } else {
            return response
                    .addHeader(CONTENT_TYPE, MimeType.Application.JSON)
                    .sendString(
                            request.receive().aggregate().asString()
                                    .map(requestString -> contentType.contains(MimeType.Application.GRAPHQL) ? new GraphQLRequest(requestString) : GraphQLRequest.fromJson(requestString))
                                    .flatMap(graphQLRequest ->
                                            Mono.just(new Document(graphQLRequest.getQuery()))
                                                    .flatMap(document ->
                                                            ScopeEventResolver.initialized(Maps.newHashMap(Map.of(REQUEST, request, RESPONSE, response, OPERATION, document.getOperationOrError())), RequestScoped.class)
                                                                    .then(requestScopeInstanceFactory.compute(requestId, HttpServerRequest.class, request))
                                                                    .then(requestScopeInstanceFactory.compute(requestId, HttpServerResponse.class, response))
                                                                    .then(requestScopeInstanceFactory.compute(requestId, Operation.class, document.getOperationOrError()))
                                                                    .flatMap(operation -> Mono.from(operationHandler.handle(operation, graphQLRequest.getVariables())))
                                                                    .contextWrite(PublisherBeanContext.of(Document.class, document))
                                                    )
                                    )
                                    .map(ResponseUtil::success)
                                    .doOnSuccess(jsonString -> response.status(HttpResponseStatus.OK))
                                    .onErrorResume(throwable -> this.errorHandler(throwable, response))
                                    .contextWrite(Context.of(REQUEST_ID, requestId))
                    );
        }
    }
}
