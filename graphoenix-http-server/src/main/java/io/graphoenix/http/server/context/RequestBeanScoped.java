package io.graphoenix.http.server.context;

import com.github.benmanes.caffeine.cache.LoadingCache;
import io.graphoenix.core.context.CaffeineReactorBeanScoped;
import io.graphoenix.http.server.config.HttpServerConfig;
import io.nozdormu.spi.event.ScopeEventPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.Duration;
import java.util.Map;

@ApplicationScoped
@Named("jakarta.enterprise.context.RequestScoped")
public class RequestBeanScoped extends CaffeineReactorBeanScoped {

    public static final String REQUEST_ID = "requestId";

    private final LoadingCache<String, Map<String, Object>> CACHE;

    @Inject
    public RequestBeanScoped(HttpServerConfig httpServerConfig) {
        CACHE = buildCache(Duration.ofMillis(httpServerConfig.getConnectTimeOutMillis()));
    }

    @Override
    protected String getScopedKeyName() {
        return REQUEST_ID;
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
        ScopeEventPublisher.destroyed(Map.of("key", key, "value", value), RequestScoped.class).subscribe();
    }
}
