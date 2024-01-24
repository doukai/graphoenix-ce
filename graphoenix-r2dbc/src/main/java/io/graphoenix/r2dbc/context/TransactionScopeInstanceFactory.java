package io.graphoenix.r2dbc.context;

import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.r2dbc.config.R2DBCConfig;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.TransactionScoped;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

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
        return Duration.ofMillis(r2DBCConfig.getConnectTimeoutMillis());
    }

    @Override
    protected String getCacheId() {
        return TRANSACTION_ID;
    }

    @Override
    protected void onBuild(String key) {
        ScopeEventResolver.initialized(Map.of("key", key), TransactionScoped.class).block();
    }

    @Override
    protected void onEviction(Object key, Object value) {
        ScopeEventResolver.beforeDestroyed(Map.of("key", key, "value", value), TransactionScoped.class).block();
    }

    @Override
    protected void onRemoval(Object key, Object value) {
        ScopeEventResolver.destroyed(Map.of("key", key, "value", value), TransactionScoped.class).block();
    }
}
