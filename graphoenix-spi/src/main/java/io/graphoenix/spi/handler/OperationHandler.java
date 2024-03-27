package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import org.reactivestreams.Publisher;

import java.util.Map;

public interface OperationHandler {

    default Publisher<JsonValue> handle(Operation operation) {
        return handle(operation, null);
    }

    default Publisher<JsonValue> handle(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables, null, null);
    }

    Publisher<JsonValue> handle(Operation operation, Map<String, JsonValue> variables, String token, String operationId);
}
