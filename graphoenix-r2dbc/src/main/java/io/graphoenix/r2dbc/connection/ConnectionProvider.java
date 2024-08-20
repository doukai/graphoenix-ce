package io.graphoenix.r2dbc.connection;

import io.graphoenix.r2dbc.context.TransactionScopeInstanceFactory;
import io.r2dbc.spi.Connection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import static io.graphoenix.spi.constant.Hammurabi.IN_TRANSACTION;

@ApplicationScoped
public class ConnectionProvider {

    private final ConnectionCreator connectionCreator;

    private final TransactionScopeInstanceFactory transactionScopeInstanceFactory;

    @Inject
    public ConnectionProvider(ConnectionCreator connectionCreator, TransactionScopeInstanceFactory transactionScopeInstanceFactory) {
        this.connectionCreator = connectionCreator;
        this.transactionScopeInstanceFactory = transactionScopeInstanceFactory;
    }

    public Mono<Connection> get() {
        return inTransaction()
                .flatMap(inTransaction ->
                        inTransaction ?
                                transactionScopeInstanceFactory.get(Connection.class) :
                                connectionCreator.createConnection()
                );
    }

    public Mono<Boolean> inTransaction() {
        return Mono
                .deferContextual(contextView ->
                        Mono
                                .just(
                                        contextView.getOrEmpty(IN_TRANSACTION)
                                                .map(inTransaction -> (Boolean) inTransaction)
                                                .orElse(false)
                                )
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
