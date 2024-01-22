package io.graphoenix.r2dbc.executor;

import io.graphoenix.r2dbc.connection.ConnectionProvider;
import io.graphoenix.r2dbc.utils.ResultUtil;
import io.r2dbc.spi.Statement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tinylog.Logger;
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
                        connectionProvider::close
                )
                .flatMap(ResultUtil::getJsonStringFromResult);
    }
}
