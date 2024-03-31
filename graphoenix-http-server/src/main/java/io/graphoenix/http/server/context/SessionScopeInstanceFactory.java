package io.graphoenix.http.server.context;

import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.http.server.config.HttpServerConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.tinylog.Logger;

import java.time.Duration;

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
        Logger.info("session : " + key + " build");
    }

    @Override
    protected void onEviction(Object key, Object value) {
        Logger.info("session : " + key + " eviction");
    }

    @Override
    protected void onRemoval(Object key, Object value) {
        Logger.info("session : " + key + " removal");
    }
}
