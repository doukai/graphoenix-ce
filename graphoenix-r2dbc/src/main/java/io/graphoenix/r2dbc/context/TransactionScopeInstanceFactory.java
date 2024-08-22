package io.graphoenix.r2dbc.context;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.Maps;
import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.r2dbc.config.R2DBCConfig;
import io.nozdormu.spi.context.ScopeInstances;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.TransactionScoped;

import java.time.Duration;
import java.util.Map;

@ApplicationScoped
@Named("jakarta.transaction.TransactionScoped")
public class TransactionScopeInstanceFactory extends CacheScopeInstanceFactory {
    public static final String TRANSACTION_ID = "transactionId";

    private final LoadingCache<String, ScopeInstances> CACHE;

    @Inject
    public TransactionScopeInstanceFactory(R2DBCConfig r2DBCConfig) {
        CACHE = buildCache(Duration.ofMillis(r2DBCConfig.getConnectTimeoutMillis()));
    }

    @Override
    protected String getCacheId() {
        return TRANSACTION_ID;
    }

    @Override
    public LoadingCache<String, ScopeInstances> getCache() {
        return CACHE;
    }

    @Override
    protected void onBuild(String key) {
        ScopeEventResolver.initialized(Maps.newHashMap(Map.of("key", key)), TransactionScoped.class).subscribe();
    }

    @Override
    protected void onEviction(Object key, Object value) {
        ScopeEventResolver.beforeDestroyed(Maps.newHashMap(Map.of("key", key, "value", value)), TransactionScoped.class).subscribe();
    }

    @Override
    protected void onRemoval(Object key, Object value) {
        ScopeEventResolver.destroyed(Maps.newHashMap(Map.of("key", key, "value", value)), TransactionScoped.class).subscribe();
    }
}
