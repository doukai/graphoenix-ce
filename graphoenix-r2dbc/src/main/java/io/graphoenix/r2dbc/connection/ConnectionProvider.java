package io.graphoenix.r2dbc.connection;

import io.r2dbc.spi.Connection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import reactor.core.publisher.Mono;

import static io.graphoenix.spi.constant.Hammurabi.IN_TRANSACTION;

@ApplicationScoped
public class ConnectionProvider {

    private final ConnectionCreator connectionCreator;

    private final Provider<Mono<Connection>> connectionMonoProvider;

    @Inject
    public ConnectionProvider(ConnectionCreator connectionCreator, Provider<Mono<Connection>> connectionMonoProvider) {
        this.connectionCreator = connectionCreator;
        this.connectionMonoProvider = connectionMonoProvider;
    }

    public Mono<Connection> get() {
        return inTransaction()
                .flatMap(inTransaction ->
                        inTransaction ?
                                connectionMonoProvider.get() :
                                connectionCreator.createConnection()
                );
    }

    public Mono<Boolean> inTransaction() {
        return Mono
                .deferContextual(contextView ->
                        Mono.justOrEmpty(contextView.getOrDefault(IN_TRANSACTION, false))
                                .defaultIfEmpty(false)
                );
    }

    public Mono<Void> close(Connection connection) {
        return inTransaction()
                .flatMap(inTransaction ->
                        inTransaction ?
                                Mono.empty() :
                                Mono.from(connection.close())
                );
    }
}
