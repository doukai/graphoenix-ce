package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface OperationBeforeHandler {

    Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables);
}
