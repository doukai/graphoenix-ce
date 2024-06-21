package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

public interface FetchAfterHandler extends OperationAfterHandler {

    default Mono<JsonValue> query(Operation operation) {
        return query(operation, null);
    }

    default Mono<JsonValue> mutation(Operation operation) {
        return mutation(operation, null);
    }

    default Mono<JsonValue> subscription(Operation operation) {
        return subscription(operation, null);
    }

    default Mono<JsonValue> handle(Operation operation) {
        return handle(operation, null);
    }
}
