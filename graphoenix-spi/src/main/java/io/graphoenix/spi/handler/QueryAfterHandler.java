package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

public interface QueryAfterHandler {

    Mono<JsonValue> query(Operation operation, JsonValue jsonValue);
}
