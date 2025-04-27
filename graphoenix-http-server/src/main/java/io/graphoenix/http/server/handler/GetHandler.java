package io.graphoenix.http.server.handler;

import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public interface GetHandler {
    String path();

    Publisher<Void> handle(HttpServerRequest request, HttpServerResponse response);
}
