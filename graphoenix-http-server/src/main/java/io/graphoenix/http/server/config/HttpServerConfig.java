package io.graphoenix.http.server.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "http")
public class HttpServerConfig {

    @Optional
    private String graphqlContextPath = "graphql";

    @Optional
    private String subscriptionsContextPath = "subscriptions";

    @Optional
    private String schemaContextPath = "schema";

    @Optional
    private Integer port = 8080;

    @Optional
    private Boolean tcpNoDelay = true;

    @Optional
    private Boolean soKeepAlive = false;

    @Optional
    private Integer soBackLog = 128;

    @Optional
    private Integer connectTimeOutMillis = 30 * 1000;

    @Optional
    private Integer sessionTimeOutMillis = 30 * 60 * 1000;

    public String getGraphqlContextPath() {
        if (graphqlContextPath.startsWith("/")) {
            return graphqlContextPath;
        } else {
            return "/" + graphqlContextPath;
        }
    }

    public void setGraphqlContextPath(String graphqlContextPath) {
        this.graphqlContextPath = graphqlContextPath;
    }

    public String getSubscriptionsContextPath() {
        if (subscriptionsContextPath.startsWith("/")) {
            return subscriptionsContextPath;
        } else {
            return "/" + subscriptionsContextPath;
        }
    }

    public void setSubscriptionsContextPath(String subscriptionsContextPath) {
        this.subscriptionsContextPath = subscriptionsContextPath;
    }

    public String getSchemaContextPath() {
        if (schemaContextPath.startsWith("/")) {
            return schemaContextPath;
        } else {
            return "/" + schemaContextPath;
        }
    }

    public void setSchemaContextPath(String schemaContextPath) {
        this.schemaContextPath = schemaContextPath;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(Boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public Boolean getSoKeepAlive() {
        return soKeepAlive;
    }

    public void setSoKeepAlive(Boolean soKeepAlive) {
        this.soKeepAlive = soKeepAlive;
    }

    public Integer getSoBackLog() {
        return soBackLog;
    }

    public void setSoBackLog(Integer soBackLog) {
        this.soBackLog = soBackLog;
    }

    public Integer getConnectTimeOutMillis() {
        return connectTimeOutMillis;
    }

    public void setConnectTimeOutMillis(Integer connectTimeOutMillis) {
        this.connectTimeOutMillis = connectTimeOutMillis;
    }

    public Integer getSessionTimeOutMillis() {
        return sessionTimeOutMillis;
    }

    public void setSessionTimeOutMillis(Integer sessionTimeOutMillis) {
        this.sessionTimeOutMillis = sessionTimeOutMillis;
    }
}
