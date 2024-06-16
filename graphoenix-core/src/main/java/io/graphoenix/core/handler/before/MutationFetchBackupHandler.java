package io.graphoenix.core.handler.before;

import io.graphoenix.core.config.MutationConfig;
import io.graphoenix.core.handler.OperationBuilder;
import io.graphoenix.core.handler.TransactionCompensator;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.graphoenix.spi.handler.OperationHandler;
import io.nozdormu.spi.context.PublisherBeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Map;

import static io.graphoenix.core.handler.before.MutationBeforeFetchHandler.MUTATION_BEFORE_FETCH_HANDLER_PRIORITY;

@ApplicationScoped
@Priority(MutationFetchBackupHandler.MUTATION_FETCH_BACKUP_HANDLER_PRIORITY)
public class MutationFetchBackupHandler implements OperationBeforeHandler {

    public static final int MUTATION_FETCH_BACKUP_HANDLER_PRIORITY = MUTATION_BEFORE_FETCH_HANDLER_PRIORITY - 1;

    private final OperationBuilder operationBuilder;
    private final MutationConfig mutationConfig;
    private final Provider<OperationHandler> operationHandlerProvider;
    private final Provider<TransactionCompensator> transactionCompensatorProvider;

    @Inject
    public MutationFetchBackupHandler(OperationBuilder operationBuilder, MutationConfig mutationConfig, Provider<OperationHandler> operationHandlerProvider, Provider<TransactionCompensator> transactionCompensatorProvider) {
        this.operationBuilder = operationBuilder;
        this.mutationConfig = mutationConfig;
        this.operationHandlerProvider = operationHandlerProvider;
        this.transactionCompensatorProvider = transactionCompensatorProvider;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        if (!mutationConfig.getCompensatingTransaction() || !operationBuilder.hasFetchArguments(operation)) {
            return Mono.just(operation);
        }
        Operation backupOperation = operationBuilder.toBackupOperation(operation);
        return Mono.from(operationHandlerProvider.get().handle(operationBuilder.toBackupOperation(operation)))
                .flatMap(jsonValue ->
                        Mono.just(operation)
                                .contextWrite(
                                        PublisherBeanContext.of(
                                                TransactionCompensator.class,
                                                transactionCompensatorProvider.get()
                                                        .setBackupOperation(backupOperation)
                                                        .setBackup(jsonValue)
                                        )
                                )
                );
    }
}
