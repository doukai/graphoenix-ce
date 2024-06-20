package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import reactor.core.publisher.Mono;

public interface FetchBeforeHandler extends OperationBeforeHandler {


    default Mono<Operation> query(Operation operation) {
        return handle(operation, null);
    }

    default Mono<Operation> mutation(Operation operation) {
        return handle(operation, null);
    }

    default Mono<Operation> subscription(Operation operation) {
        return handle(operation, null);
    }

    default Mono<Operation> handle(Operation operation) {
        return handle(operation, null);
    }
}
