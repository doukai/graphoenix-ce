package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

public interface MutationHandler {
    default Mono<JsonValue> mutation(Operation operation) {
        return mutation(operation, null);
    }

    Mono<JsonValue> mutation(Operation operation, Integer groupSize);
}
