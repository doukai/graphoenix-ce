package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface OperationBeforeHandler {

    Mono<Operation> query(Operation operation, Map<String, JsonValue> variables);

    Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables);

    Mono<Operation> subscription(Operation operation, Map<String, JsonValue> variables);
}
