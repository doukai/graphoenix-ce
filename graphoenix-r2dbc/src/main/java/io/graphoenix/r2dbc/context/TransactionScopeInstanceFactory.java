package io.graphoenix.r2dbc.context;

import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.r2dbc.config.R2DBCConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import reactor.core.publisher.Mono;

import java.time.Duration;

@ApplicationScoped
@Named("jakarta.transaction.TransactionScoped")
public class TransactionScopeInstanceFactory extends CacheScopeInstanceFactory {
    public static final String TRANSACTION_ID = "transactionId";

    private final R2DBCConfig r2DBCConfig;

    @Inject
    public TransactionScopeInstanceFactory(R2DBCConfig r2DBCConfig) {
        this.r2DBCConfig = r2DBCConfig;
    }

    @Override
    public <T, E extends T> Mono<T> compute(String s, Class<T> aClass, String s1, E e) {
        return null;
    }

    @Override
    protected Duration getTimeout() {
        return r2DBCConfig.getConnectTimeout();
    }

    @Override
    protected String getCacheId() {
        return TRANSACTION_ID;
    }
}
