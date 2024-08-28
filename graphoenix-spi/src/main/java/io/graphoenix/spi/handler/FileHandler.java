package io.graphoenix.spi.handler;

import io.graphoenix.spi.dto.FileInfo;
import reactor.core.publisher.Mono;

public interface FileHandler {

    Mono<String> save(FileInfo fileInfo);

    Mono<FileInfo> get(String id);
}
