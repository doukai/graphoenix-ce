package io.graphoenix.r2dbc.handler;

import io.graphoenix.r2dbc.executor.MutationExecutor;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.MutationHandler;
import io.graphoenix.sql.translator.MutationTranslator;
import io.graphoenix.sql.translator.QueryTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import reactor.core.publisher.Mono;

import java.io.StringReader;

@ApplicationScoped
@Named("r2dbc")
public class R2DBCMutationHandler implements MutationHandler {

    private final MutationTranslator mutationTranslator;

    private final MutationExecutor mutationExecutor;

    private final QueryTranslator queryTranslator;

    private final JsonProvider jsonProvider;

    @Inject
    public R2DBCMutationHandler(MutationTranslator mutationTranslator, MutationExecutor mutationExecutor, QueryTranslator queryTranslator, JsonProvider jsonProvider) {
        this.mutationTranslator = mutationTranslator;
        this.mutationExecutor = mutationExecutor;
        this.queryTranslator = queryTranslator;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<JsonValue> mutation(Operation operation) {
        return mutationExecutor
                .executeMutations(
                        mutationTranslator.operationToStatementSQLStream(operation),
                        queryTranslator.operationToSelectSQL(operation)
                )
                .map(json -> jsonProvider.createReader(new StringReader(json)).readValue());
    }
}
