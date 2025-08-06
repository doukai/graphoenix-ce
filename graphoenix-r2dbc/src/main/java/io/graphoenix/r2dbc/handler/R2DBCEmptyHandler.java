package io.graphoenix.r2dbc.handler;

import io.graphoenix.r2dbc.connection.ConnectionProvider;
import io.graphoenix.spi.handler.TypeEmptyHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import java.util.Collection;

@ApplicationScoped
public class R2DBCEmptyHandler implements TypeEmptyHandler {

    private final ConnectionProvider connectionProvider;

    @Inject
    public R2DBCEmptyHandler(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Mono<Void> empty(String... typeNames) {
        return null;
    }

    @Override
    public Mono<Void> empty(Collection<String> typeNames) {
        return null;
    }
}
