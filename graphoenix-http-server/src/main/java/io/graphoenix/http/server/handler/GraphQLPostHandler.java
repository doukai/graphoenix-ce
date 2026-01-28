package io.graphoenix.http.server.handler;

import com.google.common.collect.Maps;
import io.graphoenix.core.dto.GraphQLRequest;
import io.graphoenix.http.server.codec.MimeType;
import io.graphoenix.http.server.config.HttpServerConfig;
import io.graphoenix.http.server.context.RequestBeanScoped;
import io.graphoenix.http.server.http.PostHandler;
import io.graphoenix.http.server.utils.ResponseUtil;
import io.graphoenix.spi.dto.FileInfo;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.FileHandler;
import io.graphoenix.spi.handler.OperationHandler;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.nozdormu.spi.event.ScopeEventPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.util.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static io.graphoenix.http.server.context.RequestBeanScoped.REQUEST_ID;
import static io.graphoenix.http.server.utils.ResponseUtil.next;
import static io.netty.handler.codec.http.HttpHeaderNames.ACCEPT;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

@ApplicationScoped
public class GraphQLPostHandler extends BaseHandler implements PostHandler {

    private final OperationHandler operationHandler;
    private final FileHandler fileHandler;
    private final RequestBeanScoped requestBeanScoped;
    private final HttpServerConfig httpServerConfig;

    @Inject
    public GraphQLPostHandler(OperationHandler operationHandler, FileHandler fileHandler, RequestBeanScoped requestBeanScoped, HttpServerConfig httpServerConfig) {
        this.operationHandler = operationHandler;
        this.fileHandler = fileHandler;
        this.requestBeanScoped = requestBeanScoped;
        this.httpServerConfig = httpServerConfig;
    }

    @Override
    public String path() {
        return httpServerConfig.getGraphqlContextPath();
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
                    .addHeader(HttpHeaderNames.CACHE_CONTROL, HttpHeaderValues.NO_CACHE)
                    .addHeader(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                    .addHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_EVENT_STREAM + "; charset=utf-8")
                    .status(HttpResponseStatus.ACCEPTED)
                    .send(
                            request.receive().aggregate().asString()
                                    .map(requestString -> contentType.contains(MimeType.Application.GRAPHQL) ? new GraphQLRequest(requestString) : GraphQLRequest.fromJson(requestString))
                                    .flatMapMany(graphQLRequest ->
                                            Mono.just(new Document(graphQLRequest.getQuery()))
                                                    .doOnSuccess(document -> requestBeanScoped.put(requestId, Document.class, document))
                                                    .map(Document::getOperationOrError)
                                                    .flatMapMany(operation ->
                                                            ScopeEventPublisher.initialized(Maps.newHashMap(Map.of(REQUEST, request, RESPONSE, response, OPERATION, operation)), RequestScoped.class)
                                                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, HttpServerRequest.class, request))
                                                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, HttpServerResponse.class, response))
                                                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, Operation.class, operation))
                                                                    .thenMany(Flux.from(operationHandler.handle(operation, graphQLRequest.getVariables(), token, operationId)))
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
                            (contentType.contains(MimeType.Multipart.FORM_DATA) ?
                                    request.receiveForm()
                                            .reduce(Mono.just(new GraphQLRequest()),
                                                    (graphQLRequestMono, data) -> {
                                                        try {
                                                            if ("operations".equals(data.getName())) {
                                                                return Mono.just(new String(data.get()))
                                                                        .flatMap(operstionsString ->
                                                                                graphQLRequestMono
                                                                                        .map(graphQLRequest -> {
                                                                                                    graphQLRequest.setOperations(operstionsString);
                                                                                                    return graphQLRequest;
                                                                                                }
                                                                                        )
                                                                        );
                                                            } else if ("map".equals(data.getName())) {
                                                                return Mono.just(new String(data.get()))
                                                                        .flatMap(mapString ->
                                                                                graphQLRequestMono
                                                                                        .map(graphQLRequest -> {
                                                                                                    graphQLRequest.setMap(mapString);
                                                                                                    return graphQLRequest;
                                                                                                }
                                                                                        )
                                                                        );
                                                            } else if (data instanceof FileUpload && ((FileUpload) data).getFilename() != null) {
                                                                FileUpload fileUpload = (FileUpload) data;
                                                                FileInfo fileInfo = new FileInfo(fileUpload.getFilename(), fileUpload.getContentType());
                                                                return fileHandler.save(fileUpload.get(), fileInfo)
                                                                        .flatMap(id ->
                                                                                graphQLRequestMono
                                                                                        .map(graphQLRequest -> {
                                                                                                    graphQLRequest.setFileID(data.getName(), id);
                                                                                                    return graphQLRequest;
                                                                                                }
                                                                                        )
                                                                        );
                                                            }
                                                            return graphQLRequestMono;
                                                        } catch (IOException e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                    })
                                            .flatMap(mono -> mono) :
                                    request.receive().aggregate().asString()
                                            .map(requestString ->
                                                    contentType.contains(MimeType.Application.GRAPHQL) ?
                                                            new GraphQLRequest(requestString) :
                                                            GraphQLRequest.fromJson(requestString)
                                            )
                            )
                                    .flatMap(graphQLRequest ->
                                            Mono.just(new Document(graphQLRequest.getQuery()))
                                                    .doOnSuccess(document -> requestBeanScoped.put(requestId, Document.class, document))
                                                    .map(Document::getOperationOrError)
                                                    .flatMap(operation ->
                                                            ScopeEventPublisher.initialized(Maps.newHashMap(Map.of(REQUEST, request, RESPONSE, response, OPERATION, operation)), RequestScoped.class)
                                                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, HttpServerRequest.class, request))
                                                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, HttpServerResponse.class, response))
                                                                    .doOnSuccess(v -> requestBeanScoped.put(requestId, Operation.class, operation))
                                                                    .then(Mono.from(operationHandler.handle(operation, graphQLRequest.getVariables())))
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
