package io.graphoenix.r2dbc.executor;

import io.graphoenix.r2dbc.connection.ConnectionProvider;
import io.r2dbc.spi.Batch;
import io.r2dbc.spi.Statement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@ApplicationScoped
public class TableManager {

    private static final Logger logger = LoggerFactory.getLogger(TableManager.class.getName());

    private final ConnectionProvider connectionProvider;

    @Inject
    public TableManager(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public Mono<Void> mergeTable(String sql) {
        if (sql.isBlank()) {
            return Mono.empty();
        }
        return Mono
                .usingWhen(
                        connectionProvider.get(),
                        connection -> {
                            logger.info("merge table:\r\n{}", sql);
                            return Mono.from(connection.createStatement(sql).execute());
                        },
                        connectionProvider::close
                )
                .then();
    }

    public Mono<Void> mergeTable(Stream<String> sqlStream) {
        return Flux
                .usingWhen(
                        connectionProvider.get(),
                        connection -> {
                            Batch batch = connection.createBatch();
                            sqlStream.forEach(sql -> {
                                        logger.info("merge table:\r\n{}", sql);
                                        batch.add(sql);
                                    }
                            );
                            return Flux.from(batch.execute());
                        },
                        connectionProvider::close
                )
                .then();
    }

    public Mono<List<Tuple2<String, String>>> selectColumns(String sql) {
        return Flux
                .usingWhen(
                        connectionProvider.get(),
                        connection -> {
                            logger.info("execute select:\r\n{}", sql);
                            logger.info("sql parameters:\r\n{}");
                            Statement statement = connection.createStatement(sql);
                            return Flux.from(statement.execute());
                        },
                        connectionProvider::close
                )
                .flatMap(result ->
                        Flux.from(
                                result.map((row, rowMetadata) ->
                                        Tuples.of(
                                                Objects.requireNonNull(row.get(0, String.class)),
                                                Objects.requireNonNull(row.get(1, String.class))
                                        )
                                )
                        )
                )
                .collectList();
    }

    public Mono<List<String>> selectTables(String sql) {
        return Flux
                .usingWhen(
                        connectionProvider.get(),
                        connection -> {
                            logger.info("execute select:\r\n{}", sql);
                            logger.info("sql parameters:\r\n{}");
                            Statement statement = connection.createStatement(sql);
                            return Flux.from(statement.execute());
                        },
                        connectionProvider::close
                )
                .flatMap(result ->
                        Flux.from(
                                result.map((row, rowMetadata) ->
                                        row.get(0, String.class)
                                )
                        )
                )
                .collectList();
    }

    public Mono<List<Tuple2<String, String>>> selectIndexes(String sql) {
        return Flux
                .usingWhen(
                        connectionProvider.get(),
                        connection -> {
                            logger.info("execute select:\r\n{}", sql);
                            logger.info("sql parameters:\r\n{}");
                            Statement statement = connection.createStatement(sql);
                            return Flux.from(statement.execute());
                        },
                        connectionProvider::close
                )
                .flatMap(result ->
                        Flux.from(
                                result.map((row, rowMetadata) ->
                                        Tuples.of(
                                                Objects.requireNonNull(row.get(0, String.class)),
                                                Objects.requireNonNull(row.get(1, String.class))
                                        )
                                )
                        )
                )
                .collectList();
    }
}
