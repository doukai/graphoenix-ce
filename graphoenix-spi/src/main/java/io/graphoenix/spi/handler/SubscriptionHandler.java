package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Flux;

public interface SubscriptionHandler {

    Flux<JsonValue> subscription(Operation operation, String token, String operationId);
}
