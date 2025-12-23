package io.graphoenix.http.server.context;

import com.github.benmanes.caffeine.cache.LoadingCache;
import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.http.server.config.HttpServerConfig;
import io.nozdormu.spi.context.ScopeInstances;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

@ApplicationScoped
@Named("jakarta.enterprise.context.RequestScoped")
public class RequestScopeInstanceFactory extends CacheScopeInstanceFactory {

    private static final Logger logger = LoggerFactory.getLogger(RequestScopeInstanceFactory.class);

    public static final String REQUEST_ID = "requestId";

    private final LoadingCache<String, ScopeInstances> CACHE;

    @Inject
    public RequestScopeInstanceFactory(HttpServerConfig httpServerConfig) {
        CACHE = buildCache(Duration.ofMillis(httpServerConfig.getConnectTimeOutMillis()));
    }

    @Override
    protected String getCacheId() {
        return REQUEST_ID;
    }

    @Override
    public LoadingCache<String, ScopeInstances> getCache() {
        return CACHE;
    }

    @Override
    protected void onBuild(String key) {
        logger.info("request : {} build", key);
    }

    @Override
    protected void onEviction(Object key, Object value) {
        ScopeEventResolver.beforeDestroyed(Map.of("key", key, "value", value), RequestScoped.class).subscribe();
    }

    @Override
    protected void onRemoval(Object key, Object value) {
        ScopeEventResolver.destroyed(Map.of("key", key, "value", value), RequestScoped.class).subscribe();
    }
}
