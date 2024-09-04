package io.graphoenix.spi.handler;

import io.graphoenix.spi.dto.FileInfo;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface FileHandler {

    Mono<String> save(byte[] data, FileInfo fileInfo);

    Mono<FileInfo> getFileInfo(String id);

    InputStream get(String id);
}
