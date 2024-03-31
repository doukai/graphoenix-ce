package io.graphoenix.http.server.context;

import io.graphoenix.core.context.CacheScopeInstanceFactory;
import io.graphoenix.http.server.config.HttpServerConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.tinylog.Logger;

import java.time.Duration;

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
        Logger.info("request : " + key + " build");
    }

    @Override
    protected void onEviction(Object key, Object value) {
        Logger.info("request : " + key + " eviction");
    }

    @Override
    protected void onRemoval(Object key, Object value) {
        Logger.info("request : " + key + " removal");
    }
}
