package io.graphoenix.r2dbc.executor;

import io.graphoenix.r2dbc.connection.ConnectionProvider;
import io.graphoenix.r2dbc.utils.ResultUtil;
import io.r2dbc.spi.Batch;
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

    @Inject
    public MutationExecutor(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public Flux<Long> executeMutationsInBatch(Stream<String> mutationSqlStream) {
        return Flux
                .usingWhen(
                        connectionProvider.get(),
                        connection ->
                                Flux.fromStream(mutationSqlStream)
                                        .collectList()
                                        .filter(sqlList -> !sqlList.isEmpty())
                                        .flatMapMany(sqlList -> {
                                                    Batch batch = connection.createBatch();
                                                    Logger.info("execute statement count:\r\n{}", sqlList.size());
                                                    sqlList.forEach(batch::add);
                                                    return Flux.from(batch.execute())
                                                            .flatMap(ResultUtil::getUpdateCountFromResult);
                                                }
                                        ),
                        connectionProvider::close
                );
    }

    public Flux<Long> executeMutationsInBatchByGroup(Stream<String> mutationSqlStream, int groupSize) {
        return Flux
                .usingWhen(
                        connectionProvider.get(),
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
                                                                            .flatMap(ResultUtil::getUpdateCountFromResult);
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
                                            if (parameters != null) {
                                                parameters.forEach(mutationStatement::bind);
                                            }
                                            return Mono.from(mutationStatement.execute())
                                                    .flatMap(ResultUtil::getUpdateCountFromResult);
                                        }
                                ),
                        connectionProvider::close
                );
    }
}
