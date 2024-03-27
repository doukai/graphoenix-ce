package io.graphoenix.r2dbc.handler;

import io.graphoenix.r2dbc.config.R2DBCConfig;
import io.graphoenix.r2dbc.executor.MutationExecutor;
import io.graphoenix.r2dbc.executor.QueryExecutor;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.MutationHandler;
import io.graphoenix.sql.translator.MutationTranslator;
import io.graphoenix.sql.translator.QueryTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import java.io.StringReader;

@ApplicationScoped
@Named("r2dbc")
public class R2DBCMutationHandler implements MutationHandler {

    private final R2DBCConfig r2DBCConfig;

    private final MutationTranslator mutationTranslator;

    private final MutationExecutor mutationExecutor;

    private final QueryTranslator queryTranslator;

    private final QueryExecutor queryExecutor;

    private final JsonProvider jsonProvider;

    @Inject
    public R2DBCMutationHandler(R2DBCConfig r2DBCConfig, MutationTranslator mutationTranslator, MutationExecutor mutationExecutor, QueryTranslator queryTranslator, QueryExecutor queryExecutor, JsonProvider jsonProvider) {
        this.r2DBCConfig = r2DBCConfig;
        this.mutationTranslator = mutationTranslator;
        this.mutationExecutor = mutationExecutor;
        this.queryTranslator = queryTranslator;
        this.queryExecutor = queryExecutor;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<JsonValue> mutation(Operation operation, Integer groupSize) {
        if (r2DBCConfig.getAllowMultiQueries()) {
            if (groupSize != null) {
                return mutationExecutor.executeMutationsInBatchByGroup(mutationTranslator.operationToStatementSQLStream(operation), groupSize)
                        .doOnNext(count -> Logger.info("group mutation count: {}", count))
                        .reduce(Long::sum)
                        .doOnSuccess(count -> Logger.info("group mutation total count: {}", count))
                        .then(queryExecutor.executeQuery(queryTranslator.operationToSelectSQL(operation)))
                        .map(json -> jsonProvider.createReader(new StringReader(json)).readValue());
            } else {
                return mutationExecutor.executeMutations(mutationTranslator.operationToStatementSQLStream(operation))
                        .doOnSuccess(count -> Logger.info("mutation count: {}", count))
                        .then(queryExecutor.executeQuery(queryTranslator.operationToSelectSQL(operation)))
                        .map(json -> jsonProvider.createReader(new StringReader(json)).readValue());
            }
        } else {
            return mutationExecutor.executeMutationsFlux(mutationTranslator.operationToStatementSQLStream(operation))
                    .doOnNext(count -> Logger.info("mutation count: {}", count))
                    .reduce(Long::sum)
                    .doOnSuccess(count -> Logger.info("mutation total count: {}", count))
                    .then(queryExecutor.executeQuery(queryTranslator.operationToSelectSQL(operation)))
                    .map(json -> jsonProvider.createReader(new StringReader(json)).readValue());
        }
    }
}
