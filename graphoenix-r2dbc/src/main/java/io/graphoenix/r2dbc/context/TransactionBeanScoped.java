package io.graphoenix.r2dbc.context;

import com.github.benmanes.caffeine.cache.LoadingCache;
import io.graphoenix.core.context.CaffeineReactorBeanScoped;
import io.graphoenix.r2dbc.config.R2DBCConfig;
import io.nozdormu.spi.event.ScopeEventPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.TransactionScoped;

import java.time.Duration;
import java.util.Map;

@ApplicationScoped
@Named("jakarta.transaction.TransactionScoped")
public class TransactionBeanScoped extends CaffeineReactorBeanScoped {
    public static final String TRANSACTION_ID = "transactionId";

    private final LoadingCache<String, Map<String, Object>> CACHE;

    @Inject
    public TransactionBeanScoped(R2DBCConfig r2DBCConfig) {
        CACHE = buildCache(Duration.ofMillis(r2DBCConfig.getConnectTimeoutMillis()));
    }

    @Override
    protected String getScopedKeyName() {
        return TRANSACTION_ID;
    }

    @Override
    public LoadingCache<String, Map<String, Object>> getCache() {
        return CACHE;
    }

    @Override
    protected void onBuild(String key) {
    }

    @Override
    protected void onEviction(Object key, Object value) {
    }

    @Override
    protected void onRemoval(Object key, Object value) {
        ScopeEventPublisher.destroyed(Map.of("key", key, "value", value), TransactionScoped.class).subscribe();
    }
}
