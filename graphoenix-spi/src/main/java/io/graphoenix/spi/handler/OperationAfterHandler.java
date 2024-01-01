package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

public interface OperationAfterHandler {

    Mono<JsonValue> handle(Operation operation, JsonValue jsonValue);
}
