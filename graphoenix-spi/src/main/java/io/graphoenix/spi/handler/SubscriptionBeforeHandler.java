package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface SubscriptionBeforeHandler {

    Mono<Operation> subscription(Operation operation, Map<String, JsonValue> variables);
}
