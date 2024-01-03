package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface QueryBeforeHandler {

    Mono<Operation> query(Operation operation, Map<String, JsonValue> variables);
}
