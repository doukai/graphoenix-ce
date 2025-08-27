package io.graphoenix.http.server.handler;

import com.google.common.collect.Maps;
import io.graphoenix.http.server.config.HttpServerConfig;
import io.graphoenix.http.server.context.RequestScopeInstanceFactory;
import io.graphoenix.http.server.http.GetHandler;
import io.graphoenix.spi.handler.FileHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.util.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.graphoenix.http.server.context.RequestScopeInstanceFactory.REQUEST_ID;
import static io.netty.handler.codec.http.HttpHeaderNames.*;

@ApplicationScoped
public class DownloadRequestHandler extends BaseHandler implements GetHandler {

    public static final String FILE_PARAM_ID = "id";

    private final FileHandler fileHandler;
    private final RequestScopeInstanceFactory requestScopeInstanceFactory;
    private final HttpServerConfig httpServerConfig;

    @Inject
    public DownloadRequestHandler(FileHandler fileHandler, RequestScopeInstanceFactory requestScopeInstanceFactory, HttpServerConfig httpServerConfig) {
        this.fileHandler = fileHandler;
        this.requestScopeInstanceFactory = requestScopeInstanceFactory;
        this.httpServerConfig = httpServerConfig;
    }

    @Override
    public String path() {
        return httpServerConfig.getDownloadContextPath() + "/{" + FILE_PARAM_ID + "}";
    }

    public Publisher<Void> handle(HttpServerRequest request, HttpServerResponse response) {
        String requestId = request.requestId();
        String fileId = request.param(FILE_PARAM_ID);
        return ScopeEventResolver.initialized(Maps.newHashMap(Map.of(REQUEST, request, RESPONSE, response)), RequestScoped.class)
                .then(requestScopeInstanceFactory.compute(requestId, HttpServerRequest.class, request))
                .then(requestScopeInstanceFactory.compute(requestId, HttpServerResponse.class, response))
                .then(
                        fileHandler.getFileInfo(fileId)
                                .doOnSuccess(fileInfo -> response.addHeader(CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileInfo.getFilename(), StandardCharsets.UTF_8) + "\""))
                                .doOnSuccess(fileInfo -> response.addHeader(CONTENT_TYPE, fileInfo.getContentType()))
                )
                .flatMapMany(fileInfo -> {
                            try {
                                InputStream inputStream = fileHandler.get(fileId);
                                Flux<ByteBuf> byteBufFlux = Flux.<ByteBuf>generate(sink -> {
                                                    try {
                                                        byte[] buffer = new byte[4096];
                                                        int bytesRead = inputStream.read(buffer);
                                                        if (bytesRead != -1) {
                                                            ByteBuf byteBuf = Unpooled.wrappedBuffer(buffer, 0, bytesRead);
                                                            sink.next(byteBuf);
                                                        } else {
                                                            sink.complete();
                                                        }
                                                    } catch (Exception e) {
                                                        sink.error(e);
                                                    }
                                                }
                                        )
                                        .doFinally((signalType) -> {
                                                    try {
                                                        inputStream.close();
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                        );
                                return response.send(byteBufFlux);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .doOnError(throwable -> errorHandler(throwable, response))
                .contextWrite(Context.of(REQUEST_ID, requestId));
    }
}
