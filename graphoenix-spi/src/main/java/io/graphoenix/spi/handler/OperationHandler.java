package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface OperationHandler {

    Publisher<JsonValue> handle(Operation operation, Map<String, JsonValue> variables);

    Publisher<JsonValue> handle(Operation operation, Map<String, JsonValue> variables, String token, String operationId);
}
