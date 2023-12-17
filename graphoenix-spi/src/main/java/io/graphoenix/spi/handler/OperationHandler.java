package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import reactor.core.publisher.Mono;

public interface OperationHandler {

    Mono<String> query(Operation operation);

    Mono<String> mutation(Operation operation);
}
