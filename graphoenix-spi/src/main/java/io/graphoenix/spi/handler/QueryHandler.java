package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

public interface QueryHandler {

    Mono<JsonValue> query(Operation operation);

    Mono<JsonValue> query(OperationHandler operationHandler, Operation operation);
}
