package io.graphoenix.r2dbc.executor;

import io.graphoenix.r2dbc.connection.ConnectionProvider;
import io.graphoenix.r2dbc.handler.ParameterBinder;
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
    private final ParameterBinder parameterBinder;

    @Inject
    public QueryExecutor(ConnectionProvider connectionProvider, ParameterBinder parameterBinder) {
        this.connectionProvider = connectionProvider;
        this.parameterBinder = parameterBinder;
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
                            parameterBinder.bindParameters(sql, statement, parameters);
                            return Mono.from(statement.execute())
                                    .flatMap(ResultUtil::getJsonStringFromResult);
                        },
                        connectionProvider::close
                );
    }
}
