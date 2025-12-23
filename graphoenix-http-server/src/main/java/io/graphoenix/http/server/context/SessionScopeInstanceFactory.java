package io.graphoenix.http.server.context;

import com.github.benmanes.caffeine.cache.LoadingCache;
import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.http.server.config.HttpServerConfig;
import io.nozdormu.spi.context.ScopeInstances;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

@ApplicationScoped
@Named("jakarta.enterprise.context.SessionScoped")
public class SessionScopeInstanceFactory extends CacheScopeInstanceFactory {

    private static final Logger logger = LoggerFactory.getLogger(SessionScopeInstanceFactory.class);

    public static final String SESSION_ID = "sessionId";

    private final LoadingCache<String, ScopeInstances> CACHE;

    @Inject
    public SessionScopeInstanceFactory(HttpServerConfig httpServerConfig) {
        CACHE = buildCache(Duration.ofMillis(httpServerConfig.getSessionTimeOutMillis()));
    }

    @Override
    protected String getCacheId() {
        return SESSION_ID;
    }

    @Override
    public LoadingCache<String, ScopeInstances> getCache() {
        return CACHE;
    }

    @Override
    protected void onBuild(String key) {
        logger.info("session : {} build", key);
    }

    @Override
    protected void onEviction(Object key, Object value) {
        ScopeEventResolver.beforeDestroyed(Map.of("key", key, "value", value), SessionScoped.class).subscribe();
    }

    @Override
    protected void onRemoval(Object key, Object value) {
        ScopeEventResolver.destroyed(Map.of("key", key, "value", value), SessionScoped.class).subscribe();
    }
}
