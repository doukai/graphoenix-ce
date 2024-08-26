package io.graphoenix.spi.handler;

import reactor.core.publisher.Mono;

public interface FileSaveHandler {

    Mono<String> save(byte[] data, String filename, String contentType);
}
