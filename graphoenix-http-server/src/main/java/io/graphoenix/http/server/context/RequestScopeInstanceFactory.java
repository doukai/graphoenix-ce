package io.graphoenix.http.server.context;

import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.http.server.config.HttpServerConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import reactor.core.publisher.Mono;

import java.time.Duration;

@ApplicationScoped
@Named("jakarta.enterprise.context.RequestScoped")
public class RequestScopeInstanceFactory extends CacheScopeInstanceFactory {
    public static final String REQUEST_ID = "requestId";

    private final HttpServerConfig httpServerConfig;

    @Inject
    public RequestScopeInstanceFactory(HttpServerConfig httpServerConfig) {
        this.httpServerConfig = httpServerConfig;
    }

    @Override
    public <T, E extends T> Mono<T> compute(String s, Class<T> aClass, String s1, E e) {
        return null;
    }

    @Override
    protected Duration getTimeout() {
        return Duration.ofMillis(httpServerConfig.getConnectTimeoutMillis());
    }

    @Override
    protected String getCacheId() {
        return REQUEST_ID;
    }
}
