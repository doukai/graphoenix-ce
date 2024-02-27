package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface OperationBeforeHandler {

    default Mono<Operation> query(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    default Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    default Mono<Operation> subscription(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    default Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        return Mono.just(operation);
    }
}
