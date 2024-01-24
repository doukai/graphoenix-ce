package io.graphoenix.http.server.context;

import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.http.server.config.HttpServerConfig;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@ApplicationScoped
@Named("jakarta.enterprise.context.SessionScoped")
public class SessionScopeInstanceFactory extends CacheScopeInstanceFactory {
    public static final String SESSION_ID = "sessionId";

    private final HttpServerConfig httpServerConfig;

    @Inject
    public SessionScopeInstanceFactory(HttpServerConfig httpServerConfig) {
        this.httpServerConfig = httpServerConfig;
    }

    @Override
    public <T, E extends T> Mono<T> compute(String s, Class<T> aClass, String s1, E e) {
        return null;
    }

    @Override
    protected Duration getTimeout() {
        return Duration.ofMillis(httpServerConfig.getSessionTimeOutMillis());
    }

    @Override
    protected String getCacheId() {
        return SESSION_ID;
    }

    @Override
    protected void onBuild(String key) {
        ScopeEventResolver.initialized(Map.of("key", key), SessionScoped.class).block();
    }

    @Override
    protected void onEviction(Object key, Object value) {
        ScopeEventResolver.beforeDestroyed(Map.of("key", key, "value", value), SessionScoped.class).block();
    }

    @Override
    protected void onRemoval(Object key, Object value) {
        ScopeEventResolver.destroyed(Map.of("key", key, "value", value), SessionScoped.class).block();
    }
}
