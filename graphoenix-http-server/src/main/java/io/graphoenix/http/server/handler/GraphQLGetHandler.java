package io.graphoenix.http.server.handler;

import com.google.common.collect.Maps;
import io.graphoenix.core.dto.GraphQLRequest;
import io.graphoenix.http.server.codec.MimeType;
import io.graphoenix.http.server.config.HttpServerConfig;
import io.graphoenix.http.server.context.RequestBeanScoped;
import io.graphoenix.http.server.http.GetHandler;
import io.graphoenix.http.server.utils.ResponseUtil;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.OperationHandler;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.nozdormu.spi.event.ScopeEventPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.spi.JsonProvider;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.util.context.Context;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static io.graphoenix.http.server.context.RequestBeanScoped.REQUEST_ID;
import static io.graphoenix.http.server.utils.ResponseUtil.next;
import static io.netty.handler.codec.http.HttpHeaderNames.ACCEPT;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

@ApplicationScoped
public class GraphQLGetHandler extends BaseHandler implements GetHandler {

    private final OperationHandler operationHandler;
    private final RequestBeanScoped requestBeanScoped;
    private final JsonProvider jsonProvider;
    private final HttpServerConfig httpServerConfig;

    @Inject
    public GraphQLGetHandler(OperationHandler operationHandler, RequestBeanScoped requestBeanScoped, JsonProvider jsonProvider, HttpServerConfig httpServerConfig) {
        this.operationHandler = operationHandler;
        this.requestBeanScoped = requestBeanScoped;
        this.jsonProvider = jsonProvider;
        this.httpServerConfig = httpServerConfig;
    }

    @Override
    public String path() {
        return httpServerConfig.getGraphqlContextPath();
    }

    public Publisher<Void> handle(HttpServerRequest request, HttpServerResponse response) {
        String requestId = request.requestId();
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        String accept = decoder.parameters().getOrDefault(ACCEPT.toString(), Collections.singletonList(request.requestHeaders().get(ACCEPT))).get(0);
        GraphQLRequest graphQLRequest = new GraphQLRequest(
                decoder.parameters().get("query").get(0),
                decoder.parameters().containsKey("operationName") ? decoder.parameters().get("operationName").get(0) : null,
                decoder.parameters().containsKey("variables") ? jsonProvider.createReader(new StringReader(Objects.requireNonNull(decoder.parameters().get("variables").get(0)))).readObject() : null
        );

        Document document = new Document(graphQLRequest.getQuery());
        Operation operation = document.getOperationOrError();

        if (accept.contains(MimeType.Text.EVENT_STREAM)) {
            String token = Optional.ofNullable(request.requestHeaders().get("X-GraphQL-Event-Stream-Token"))
                    .orElseGet(() -> decoder.parameters().containsKey("token") ? decoder.parameters().get("token").get(0) : null);
            String operationId = decoder.parameters().containsKey("operationId") ? decoder.parameters().get("operationId").get(0) : null;
            return response.sse()
                    .addHeader(HttpHeaderNames.CACHE_CONTROL, HttpHeaderValues.NO_CACHE)
                    .addHeader(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                    .addHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_EVENT_STREAM + "; charset=utf-8")
                    .status(HttpResponseStatus.ACCEPTED)
                    .send(
                            ScopeEventPublisher.initialized(Maps.newHashMap(Map.of(REQUEST, request, RESPONSE, response, OPERATION, document.getOperationOrError())), RequestScoped.class)
                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, HttpServerRequest.class, request))
                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, HttpServerResponse.class, response))
                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, Operation.class, operation))
                                    .thenMany(Flux.from(operationHandler.handle(operation, graphQLRequest.getVariables(), token, operationId)))
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
                            ScopeEventPublisher.initialized(Maps.newHashMap(Map.of(REQUEST, request, RESPONSE, response, OPERATION, document.getOperationOrError())), RequestScoped.class)
                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, HttpServerRequest.class, request))
                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, HttpServerResponse.class, response))
                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, Operation.class, operation))
                                    .then(Mono.from(operationHandler.handle(operation, graphQLRequest.getVariables())))
                                    .map(ResponseUtil::success)
                                    .doOnSuccess(jsonString -> response.status(HttpResponseStatus.OK))
                                    .onErrorResume(throwable -> this.errorHandler(throwable, response))
                                    .contextWrite(Context.of(REQUEST_ID, requestId))
                    );
        }
    }
}
