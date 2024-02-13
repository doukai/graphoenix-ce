package io.graphoenix.core.handler.fetch;

import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.FetchHandler;
import io.graphoenix.spi.handler.OperationHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

@ApplicationScoped
@Named("local")
public class LocalFetchHandler implements FetchHandler {

    private final OperationHandler operationHandler;

    @Inject
    public LocalFetchHandler(OperationHandler operationHandler) {
        this.operationHandler = operationHandler;
    }

    @Override
    public Mono<JsonValue> request(String packageName, Operation operation) {
        return Mono.from(operationHandler.handle(operation, null));
    }
}
