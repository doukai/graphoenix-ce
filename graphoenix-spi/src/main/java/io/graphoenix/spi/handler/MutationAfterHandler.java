package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

public interface MutationAfterHandler {

    Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue);
}
