package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

public interface OperationAfterHandler {

    default Mono<JsonValue> query(Operation operation, JsonValue jsonValue) {
        return handle(operation, jsonValue);
    }

    default Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue) {
        return handle(operation, jsonValue);
    }

    default Mono<JsonValue> subscription(Operation operation, JsonValue jsonValue) {
        return handle(operation, jsonValue);
    }

    default Mono<JsonValue> handle(Operation operation, JsonValue jsonValue) {
        return Mono.just(jsonValue);
    }
}
