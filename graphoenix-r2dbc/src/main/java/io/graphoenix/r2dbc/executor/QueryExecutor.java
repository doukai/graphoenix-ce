package io.graphoenix.r2dbc.executor;

import io.graphoenix.r2dbc.connection.ConnectionProvider;
import io.graphoenix.r2dbc.utils.ResultUtil;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.tinylog.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@ApplicationScoped
public class QueryExecutor {

    private final ConnectionProvider connectionProvider;

    @Inject
    public QueryExecutor(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public Mono<String> executeQuery(String sql) {
        return executeQuery(sql, null);
    }

    public Mono<String> executeQuery(String sql, Map<String, Object> parameters) {

        return connectionProvider.get()
                .flatMap(connection -> {
                    Logger.info("execute select:\r\n{}", sql);
                    Logger.info("sql parameters:\r\n{}", parameters);
                    Statement statement = connection.createStatement(sql);
                    if (parameters != null) {
                        parameters.forEach(statement::bind);
                    }
                    return Mono.from(statement.execute());
                        }
                )
                .flatMap(ResultUtil::getJsonStringFromResult);




        return Mono
                .usingWhen(
                        connectionProvider.get(),
                        connection -> {
                            Logger.info("execute select:\r\n{}", sql);
                            Logger.info("sql parameters:\r\n{}", parameters);
                            Statement statement = connection.createStatement(sql);
                            if (parameters != null) {
                                parameters.forEach(statement::bind);
                            }
                            return Mono.from(statement.execute());
                        },
                        connection -> Mono.deferContextual(contextView -> Mono.just(contextView.hasKey(TRANSACTION_ID) && contextView.hasKey(TRANSACTION_TYPE) && contextView.get(TRANSACTION_TYPE).equals(IN_TRANSACTION)))
                                .filter(inTransaction -> !inTransaction)
                                .flatMap(inTransaction -> Mono.from(connection.close()))
                )
                .flatMap(RESULT_UTIL::getJsonStringFromResult);
    }
}
