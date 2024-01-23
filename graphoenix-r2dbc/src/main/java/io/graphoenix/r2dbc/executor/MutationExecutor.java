package io.graphoenix.r2dbc.executor;

import com.google.common.collect.Lists;
import io.graphoenix.r2dbc.connection.ConnectionProvider;
import io.graphoenix.r2dbc.utils.ResultUtil;
import io.r2dbc.spi.Batch;
import io.r2dbc.spi.Statement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tinylog.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class MutationExecutor {

    private final ConnectionProvider connectionProvider;

    @Inject
    public MutationExecutor(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public Mono<String> executeMutationsInBatch(Stream<String> sqlStream) {
        return Flux
                .usingWhen(
                        connectionProvider.get(),
                        connection -> {
                            Batch batch = connection.createBatch();
                            sqlStream.forEach(sql -> {
                                        Logger.info("execute statement:\r\n{}", sql);
                                        batch.add(sql);
                                    }
                            );
                            return Flux.from(batch.execute());
                        },
                        connectionProvider::close
                )
                .onErrorResume(Mono::error)
                .last()
                .flatMap(ResultUtil::getJsonStringFromResult);
    }

    public Flux<Integer> executeMutationsInBatchByGroup(Stream<String> sqlStream, int itemCount) {
        List<List<String>> sqlListGroup = Lists.partition(sqlStream.collect(Collectors.toList()), itemCount);
        return Flux
                .usingWhen(
                        connectionProvider.get(),
                        connection -> Flux.fromIterable(sqlListGroup)
                                .flatMap(sqlList -> {
                                            Batch batch = connection.createBatch();
                                            Logger.info("execute statement count:\r\n{}", sqlList.size());
                                            sqlList.forEach(batch::add);
                                            return Flux.from(batch.execute()).then().thenReturn(sqlList.size());
                                        }
                                ),
                        connectionProvider::close
                );
    }

    public Mono<String> executeMutations(Stream<String> sqlStream) {
        return executeMutations(sqlStream, null);
    }

    public Mono<String> executeMutations(Stream<String> sqlStream, Map<String, Object> parameters) {
        return Mono
                .usingWhen(
                        connectionProvider.get(),
                        connection -> Flux.fromStream(sqlStream)
                                .collectList()
                                .flatMap(sqlList -> {
                                            if (sqlList.isEmpty()) {
                                                return Mono.empty();
                                            }
                                            String mutation = String.join(";", sqlList.subList(0, sqlList.size() - 1));
                                            String query = sqlList.get(sqlList.size() - 1);
                                            Logger.info("execute mutation:\r\n{}", mutation);
                                            Logger.info("execute query:\r\n{}", query);
                                            Logger.info("sql parameters:\r\n{}", parameters);

                                            Statement mutationStatement = connection.createStatement(mutation);
                                            Statement queryStatement = connection.createStatement(query);

                                            if (parameters != null) {
                                                parameters.forEach(mutationStatement::bind);
                                                parameters.forEach(queryStatement::bind);
                                            }
                                            return Mono.from(mutationStatement.execute())
                                                    .flatMap(ResultUtil::getUpdateCountFromResult)
                                                    .then(Mono.from(queryStatement.execute()));
                                        }
                                ),
                        connectionProvider::close
                )
                .flatMap(ResultUtil::getJsonStringFromResult);
    }

    public Mono<String> executeMutations(String sql) {
        return executeMutations(sql, null);
    }

    public Mono<String> executeMutations(String sql, Map<String, Object> parameters) {
        return Mono
                .usingWhen(
                        connectionProvider.get(),
                        connection -> {
                            Logger.info("execute statement:\r\n{}", sql);
                            Logger.info("sql parameters:\r\n{}", parameters);
                            Statement statement = connection.createStatement(sql);
                            if (parameters != null) {
                                parameters.forEach(statement::bind);
                            }
                            return Mono.from(statement.execute());
                        },
                        connectionProvider::close
                )
                .flatMap(ResultUtil::getJsonStringFromResult);
    }
}
