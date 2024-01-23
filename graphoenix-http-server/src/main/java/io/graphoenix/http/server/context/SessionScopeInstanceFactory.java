package io.graphoenix.http.server.context;

import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.http.server.config.HttpServerConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import reactor.core.publisher.Mono;

import java.time.Duration;

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
}
