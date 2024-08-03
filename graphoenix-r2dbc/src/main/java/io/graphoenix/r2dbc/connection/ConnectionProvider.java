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
                .filter(inTransaction -> inTransaction)
                .defaultIfEmpty(false)
                .flatMap(inTransaction ->
                        inTransaction ?
                                transactionScopeInstanceFactory.get(Connection.class, connectionCreator::createConnection) :
                                connectionCreator.createConnection()
                );
    }

    public Mono<Boolean> inTransaction() {
        return Mono.deferContextual(contextView ->
                Mono.justOrEmpty(contextView.getOrEmpty(IN_TRANSACTION))
                        .map(inTransaction -> (Boolean) inTransaction)
        );
    }

    public Mono<Void> close(Connection connection) {
        return inTransaction()
                .filter(inTransaction -> inTransaction)
                .defaultIfEmpty(false)
                .flatMap(inTransaction ->
                        inTransaction ?
                                Mono.empty() :
                                Mono.from(connection.close())
                );
    }
}
