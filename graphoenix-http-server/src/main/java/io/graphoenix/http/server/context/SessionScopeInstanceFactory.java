package io.graphoenix.http.server.context;

import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.http.server.config.HttpServerConfig;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.Duration;
import java.util.Map;

@ApplicationScoped
@Named("jakarta.enterprise.context.SessionScoped")
public class SessionScopeInstanceFactory extends CacheScopeInstanceFactory {
    public static final String SESSION_ID = "sessionId";

    @Inject
    public SessionScopeInstanceFactory(HttpServerConfig httpServerConfig) {
        super(Duration.ofMillis(httpServerConfig.getSessionTimeOutMillis()));
    }

    @Override
    protected String getCacheId() {
        return SESSION_ID;
    }

    @Override
    protected void onBuild(String key) {
        ScopeEventResolver.initialized(Map.of("key", key), SessionScoped.class).subscribe();
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
