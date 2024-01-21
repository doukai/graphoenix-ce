package io.graphoenix.r2dbc.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import java.time.Duration;

@ConfigProperties(prefix = "r2dbc")
public class R2DBCConfig {

    @Optional
    private String url;

    @Optional
    private String driver;

    @Optional
    private String protocol = "pipes";

    private String database;

    @Optional
    private String host = "localhost";

    @Optional
    private Integer port = 3306;

    private String user;

    private String password;

    @Optional
    private Boolean ssl;

    @Optional
    private Duration connectTimeout;

    @Optional
    private Duration lockWaitTimeout;

    @Optional
    private Duration statementTimeout;

    @Optional
    private Boolean allowMultiQueries = true;

    @Optional
    private Boolean usePool = false;

    @Optional
    private Integer acquireRetry;

    @Optional
    private Duration backgroundEvictionInterval;

    @Optional
    private Integer initialSize;

    @Optional
    private Integer maxSize;

    @Optional
    private Integer minIdle;

    @Optional
    private Duration maxAcquireTime;

    @Optional
    private Duration maxCreateConnectionTime;

    @Optional
    private Duration maxIdleTime;

    @Optional
    private Duration maxLifeTime;

    @Optional
    private Duration maxValidationTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getLockWaitTimeout() {
        return lockWaitTimeout;
    }

    public void setLockWaitTimeout(Duration lockWaitTimeout) {
        this.lockWaitTimeout = lockWaitTimeout;
    }

    public Duration getStatementTimeout() {
        return statementTimeout;
    }

    public void setStatementTimeout(Duration statementTimeout) {
        this.statementTimeout = statementTimeout;
    }

    public Boolean getAllowMultiQueries() {
        return allowMultiQueries;
    }

    public void setAllowMultiQueries(Boolean allowMultiQueries) {
        this.allowMultiQueries = allowMultiQueries;
    }

    public Boolean getUsePool() {
        return usePool;
    }

    public void setUsePool(Boolean usePool) {
        this.usePool = usePool;
    }

    public Integer getAcquireRetry() {
        return acquireRetry;
    }

    public void setAcquireRetry(Integer acquireRetry) {
        this.acquireRetry = acquireRetry;
    }

    public Duration getBackgroundEvictionInterval() {
        return backgroundEvictionInterval;
    }

    public void setBackgroundEvictionInterval(Duration backgroundEvictionInterval) {
        this.backgroundEvictionInterval = backgroundEvictionInterval;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Duration getMaxAcquireTime() {
        return maxAcquireTime;
    }

    public void setMaxAcquireTime(Duration maxAcquireTime) {
        this.maxAcquireTime = maxAcquireTime;
    }

    public Duration getMaxCreateConnectionTime() {
        return maxCreateConnectionTime;
    }

    public void setMaxCreateConnectionTime(Duration maxCreateConnectionTime) {
        this.maxCreateConnectionTime = maxCreateConnectionTime;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(Duration maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public Duration getMaxLifeTime() {
        return maxLifeTime;
    }

    public void setMaxLifeTime(Duration maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
    }

    public Duration getMaxValidationTime() {
        return maxValidationTime;
    }

    public void setMaxValidationTime(Duration maxValidationTime) {
        this.maxValidationTime = maxValidationTime;
    }
}
