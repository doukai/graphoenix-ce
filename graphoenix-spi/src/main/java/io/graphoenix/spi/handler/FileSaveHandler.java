package io.graphoenix.spi.handler;

import io.graphoenix.spi.dto.UploadInfo;
import reactor.core.publisher.Mono;

public interface FileSaveHandler {

    Mono<String> save(UploadInfo uploadInfo);
}
