package io.graphoenix.http.server.context;

import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.http.server.config.HttpServerConfig;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.Duration;
import java.util.Map;

@ApplicationScoped
@Named("jakarta.enterprise.context.RequestScoped")
public class RequestScopeInstanceFactory extends CacheScopeInstanceFactory {
    public static final String REQUEST_ID = "requestId";

    @Inject
    public RequestScopeInstanceFactory(HttpServerConfig httpServerConfig) {
        super(Duration.ofMillis(httpServerConfig.getConnectTimeOutMillis()));
    }

    @Override
    protected String getCacheId() {
        return REQUEST_ID;
    }

    @Override
    protected void onBuild(String key) {
        ScopeEventResolver.initialized(Map.of("key", key), RequestScoped.class).subscribe();
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
