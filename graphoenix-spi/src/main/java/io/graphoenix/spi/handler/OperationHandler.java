package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import org.reactivestreams.Publisher;

import java.util.Map;

public interface OperationHandler {

    default Publisher<JsonValue> handle(Operation operation) {
        return handle(operation, null, null);
    }

    default Publisher<JsonValue> handle(Operation operation, Integer groupSize) {
        return handle(operation, null, groupSize, null, null);
    }

    default Publisher<JsonValue> handle(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables, null, null, null);
    }

    default Publisher<JsonValue> handle(Operation operation, Map<String, JsonValue> variables, Integer groupSize) {
        return handle(operation, variables, groupSize, null, null);
    }

    default Publisher<JsonValue> handle(Operation operation, Map<String, JsonValue> variables, String token, String operationId) {
        return handle(operation, variables, null, token, operationId);
    }

    Publisher<JsonValue> handle(Operation operation, Map<String, JsonValue> variables, Integer groupSize, String token, String operationId);
}
