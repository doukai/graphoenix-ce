package io.graphoenix.r2dbc.executor;

import io.graphoenix.r2dbc.connection.ConnectionCreator;
import io.graphoenix.r2dbc.connection.ConnectionProvider;
import io.graphoenix.r2dbc.utils.ResultUtil;
import io.r2dbc.spi.Batch;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tinylog.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class MutationExecutor {

    private final ConnectionProvider connectionProvider;

    private final ConnectionCreator connectionCreator;

    @Inject
    public MutationExecutor(ConnectionProvider connectionProvider, ConnectionCreator connectionCreator) {
        this.connectionProvider = connectionProvider;
        this.connectionCreator = connectionCreator;
    }

    public Mono<String> executeMutationsInBatch(Stream<String> mutationSqlStream, String querySQL) {
        return Mono
                .usingWhen(
                        connectionCreator.createConnection(),
                        connection ->
                                Flux.fromStream(mutationSqlStream)
                                        .collectList()
                                        .filter(sqlList -> !sqlList.isEmpty())
                                        .flatMap(sqlList -> {
                                                    Batch batch = connection.createBatch();
                                                    Logger.info("execute statement count:\r\n{}", sqlList.size());
                                                    sqlList.forEach(batch::add);
                                                    return Flux.from(batch.execute())
                                                            .flatMap(ResultUtil::getUpdateCountFromResult)
                                                            .doOnNext(count -> Logger.info("mutation count: {}", count))
                                                            .then(
                                                                    Mono.from(connection.createStatement(querySQL).execute())
                                                                            .flatMap(ResultUtil::getJsonStringFromResult)
                                                            );
                                                }
                                        ),
                        Connection::close
                );
    }

    public Mono<String> executeMutationsInBatchByGroup(Stream<String> mutationSqlStream, String querySQL, int groupSize) {
        return Mono
                .usingWhen(
                        connectionCreator.createConnection(),
                        connection ->
                                Flux.fromStream(mutationSqlStream)
                                        .window(groupSize)
                                        .flatMap(sqlFlux ->
                                                sqlFlux.collectList()
                                                        .filter(sqlList -> !sqlList.isEmpty())
                                                        .flatMapMany(sqlList -> {
                                                                    Batch batch = connection.createBatch();
                                                                    Logger.info("execute statement count:\r\n{}", sqlList.size());
                                                                    sqlList.forEach(batch::add);
                                                                    return Flux.from(batch.execute())
                                                                            .flatMap(ResultUtil::getUpdateCountFromResult)
                                                                            .doOnNext(count -> Logger.info("mutation count: {}", count));
                                                                }
                                                        )
                                        )
                                        .then()
                                        .then(
                                                Mono.from(connection.createStatement(querySQL).execute())
                                                        .flatMap(ResultUtil::getJsonStringFromResult)
                                        ),
                        Connection::close
                );
    }

    public Mono<String> executeMutations(Stream<String> mutationSqlStream, String querySQL) {
        return executeMutations(mutationSqlStream, querySQL, null);
    }

    public Mono<String> executeMutations(Stream<String> mutationSqlStream, String querySQL, Map<String, Object> parameters) {
        return Mono
                .usingWhen(
                        connectionProvider.get(),
                        connection -> Flux.fromStream(mutationSqlStream)
                                .collectList()
                                .filter(sqlList -> !sqlList.isEmpty())
                                .flatMap(sqlList -> {
                                            String mutation = String.join(";", sqlList);
                                            Logger.info("execute mutation:\r\n{}", mutation);
                                            Logger.info("sql parameters:\r\n{}", parameters);
                                            Statement mutationStatement = connection.createStatement(mutation);
                                            Statement queryStatement = connection.createStatement(querySQL);
                                            if (parameters != null) {
                                                parameters.forEach(mutationStatement::bind);
                                                parameters.forEach(queryStatement::bind);
                                            }
                                            return Mono.from(mutationStatement.execute())
                                                    .flatMap(ResultUtil::getUpdateCountFromResult)
                                                    .doOnSuccess(count -> Logger.info("mutation count: {}", count))
                                                    .then(
                                                            Mono.from(queryStatement.execute())
                                                                    .flatMap(ResultUtil::getJsonStringFromResult)
                                                    );
                                        }
                                ),
                        connectionProvider::close
                );
    }
}
