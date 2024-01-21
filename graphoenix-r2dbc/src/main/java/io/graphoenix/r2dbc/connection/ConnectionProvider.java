package io.graphoenix.r2dbc.connection;

import io.graphoenix.r2dbc.context.TransactionScopeInstanceFactory;
import io.r2dbc.spi.Connection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

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
        return transactionScopeInstanceFactory
                .get(Connection.class, connectionCreator::createConnection)
                .switchIfEmpty(connectionCreator.createConnection());
    }
}
