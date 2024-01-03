package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

public interface OperationAfterHandler {

    Mono<JsonValue> query(Operation operation, JsonValue jsonValue);

    Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue);

    Mono<JsonValue> subscription(Operation operation, JsonValue jsonValue);
}
