package io.graphoenix.http.server.handler;

import com.google.common.collect.Maps;
import io.graphoenix.http.server.context.RequestScopeInstanceFactory;
import io.graphoenix.spi.dto.FileInfo;
import io.graphoenix.spi.handler.FileHandler;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.util.context.Context;

import java.util.Map;

import static io.graphoenix.http.server.context.RequestScopeInstanceFactory.REQUEST_ID;
import static io.netty.handler.codec.http.HttpHeaderNames.*;

@ApplicationScoped
public class DownloadRequestHandler extends BaseHandler {

    public static final String FILE_PARAM_ID = "id";

    private final FileHandler fileHandler;
    private final RequestScopeInstanceFactory requestScopeInstanceFactory;

    @Inject
    public DownloadRequestHandler(FileHandler fileHandler, RequestScopeInstanceFactory requestScopeInstanceFactory) {
        this.fileHandler = fileHandler;
        this.requestScopeInstanceFactory = requestScopeInstanceFactory;
    }

    public Publisher<Void> handle(HttpServerRequest request, HttpServerResponse response) {
        String requestId = request.requestId();
        return response
                .sendByteArray(
                        ScopeEventResolver.initialized(Maps.newHashMap(Map.of(REQUEST, request, RESPONSE, response)), RequestScoped.class)
                                .then(requestScopeInstanceFactory.compute(requestId, HttpServerRequest.class, request))
                                .then(requestScopeInstanceFactory.compute(requestId, HttpServerResponse.class, response))
                                .then(
                                        fileHandler.get(request.param(FILE_PARAM_ID))
                                                .doOnSuccess(fileInfo -> response.addHeader(CONTENT_DISPOSITION, "attachment; filename=\"" + fileInfo.getFilename() + "\""))
                                                .doOnSuccess(fileInfo -> response.addHeader(CONTENT_TYPE, fileInfo.getContentType()))
                                                .map(FileInfo::getData)
                                )
                                .doOnSuccess(bytes -> response.status(HttpResponseStatus.OK))
                                .contextWrite(Context.of(REQUEST_ID, requestId))
                );
    }
}
