package io.graphoenix.r2dbc.executor;

import io.graphoenix.r2dbc.connection.ConnectionProvider;
import io.graphoenix.r2dbc.handler.ParameterBinder;
import io.graphoenix.r2dbc.utils.ResultUtil;
import io.r2dbc.spi.Batch;
import io.r2dbc.spi.Statement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class MutationExecutor {

    private static final Logger logger = LoggerFactory.getLogger(MutationExecutor.class);

    private final ConnectionProvider connectionProvider;
    private final ParameterBinder parameterBinder;

    @Inject
    public MutationExecutor(ConnectionProvider connectionProvider, ParameterBinder parameterBinder) {
        this.connectionProvider = connectionProvider;
        this.parameterBinder = parameterBinder;
    }

    public Flux<Long> executeMutationsInBatch(Stream<String> mutationSqlStream) {
        return Flux.fromStream(mutationSqlStream)
                .collectList()
                .filter(sqlList -> !sqlList.isEmpty())
                .flatMapMany(sqlList ->
                        Flux
                                .usingWhen(
                                        connectionProvider.get(),
                                        connection -> {
                                            Batch batch = connection.createBatch();
                                            logger.info("execute statement count:\r\n{}", sqlList.size());
                                            sqlList.forEach(batch::add);
                                            return Flux.from(batch.execute())
                                                    .flatMap(ResultUtil::getUpdateCountFromResult);
                                        },
                                        connectionProvider::close
                                )
                );
    }

    public Flux<Long> executeMutationsInBatchByGroup(Stream<String> mutationSqlStream, int groupSize) {
        return Flux
                .usingWhen(
                        connectionProvider.get(),
                        connection ->
                                Flux.fromStream(mutationSqlStream)
                                        .window(groupSize)
                                        .concatMap(sqlFlux ->
                                                sqlFlux.collectList()
                                                        .filter(sqlList -> !sqlList.isEmpty())
                                                        .flatMapMany(sqlList -> {
                                                                    Batch batch = connection.createBatch();
                                                                    logger.info("execute statement count:\r\n{}", sqlList.size());
                                                                    sqlList.forEach(batch::add);
                                                                    return Flux.from(batch.execute())
                                                                            .concatMap(ResultUtil::getUpdateCountFromResult);
                                                                }
                                                        )
                                        ),
                        connectionProvider::close
                );
    }

    public Mono<Long> executeMutations(Stream<String> mutationSqlStream) {
        return executeMutations(mutationSqlStream, null);
    }

    public Mono<Long> executeMutations(Stream<String> mutationSqlStream, Map<String, Object> parameters) {
        return Flux.fromStream(mutationSqlStream)
                .collectList()
                .filter(sqlList -> !sqlList.isEmpty())
                .flatMap(sqlList ->
                        Mono
                                .usingWhen(
                                        connectionProvider.get(),
                                        connection -> {
                                            String mutation = String.join(";", sqlList);
                                            logger.info("execute mutation:\r\n{}", mutation);
                                            logger.info("sql parameters:\r\n{}", parameters);
                                            Statement mutationStatement = connection.createStatement(mutation);
                                            parameterBinder.bindParameters(mutation, mutationStatement, parameters);
                                            return Mono.from(mutationStatement.execute())
                                                    .flatMap(ResultUtil::getUpdateCountFromResult);
                                        },
                                        connectionProvider::close
                                )
                );
    }

    public Flux<Long> executeMutationsFlux(Stream<String> mutationSqlStream) {
        return executeMutationsFlux(mutationSqlStream, null);
    }

    public Flux<Long> executeMutationsFlux(Stream<String> mutationSqlStream, Map<String, Object> parameters) {
        return Flux
                .usingWhen(
                        connectionProvider.get(),
                        connection -> Flux.fromStream(mutationSqlStream)
                                .concatMap(sql -> {
                                            logger.info("execute mutation:\r\n{}", sql);
                                            logger.info("sql parameters:\r\n{}", parameters);
                                            Statement mutationStatement = connection.createStatement(sql);
                                            parameterBinder.bindParameters(sql, mutationStatement, parameters);
                                            return Flux.from(mutationStatement.execute())
                                                    .concatMap(ResultUtil::getUpdateCountFromResult);
                                        }
                                ),
                        connectionProvider::close
                );
    }

    public Mono<Long> executeMutation(String sql) {
        return executeMutation(sql, null);
    }

    public Mono<Long> executeMutation(String sql, Map<String, Object> parameters) {
        if (sql.isBlank()) {
            return Mono.empty();
        }
        return Mono
                .usingWhen(
                        connectionProvider.get(),
                        connection -> {
                            logger.info("execute mutation:\r\n{}", sql);
                            logger.info("sql parameters:\r\n{}", parameters);
                            Statement mutationStatement = connection.createStatement(sql);
                            parameterBinder.bindParameters(sql, mutationStatement, parameters);
                            return Mono.from(mutationStatement.execute())
                                    .flatMap(ResultUtil::getUpdateCountFromResult);
                        },
                        connectionProvider::close
                );
    }
}
