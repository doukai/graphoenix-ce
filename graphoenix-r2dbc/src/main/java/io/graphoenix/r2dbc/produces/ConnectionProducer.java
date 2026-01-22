package io.graphoenix.r2dbc.produces;

import io.graphoenix.r2dbc.connection.ConnectionCreator;
import io.r2dbc.spi.Connection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.transaction.TransactionScoped;
import reactor.core.publisher.Mono;

@ApplicationScoped
public class ConnectionProducer {

    private final ConnectionCreator connectionCreator;

    @Inject
    public ConnectionProducer(ConnectionCreator connectionCreator) {
        this.connectionCreator = connectionCreator;
    }

    @TransactionScoped
    @Produces
    public Mono<Connection> connection() {
        return connectionCreator.createConnection();
    }
}
