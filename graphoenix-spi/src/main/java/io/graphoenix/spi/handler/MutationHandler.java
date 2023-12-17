package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

public interface MutationHandler {

    Mono<JsonValue> mutation(Operation operation);

    Mono<JsonValue> mutation(OperationHandler operationHandler, Operation operation);
}
