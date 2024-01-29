package io.graphoenix.r2dbc.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

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
    private Boolean createTables = true;

    @Optional
    private Boolean ssl = false;

    @Optional
    private Integer connectTimeoutMillis = 10 * 1000;

    @Optional
    private Integer lockWaitTimeoutMillis = -1;

    @Optional
    private Integer statementTimeoutMillis = -1;

    @Optional
    private Boolean allowMultiQueries = true;

    @Optional
    private Boolean usePool = false;

    @Optional
    private Integer acquireRetry = 1;

    @Optional
    private Integer backgroundEvictionIntervalMillis = -1;

    @Optional
    private Integer initialSize;

    @Optional
    private Integer maxSize;

    @Optional
    private Integer minIdle;

    @Optional
    private Integer maxAcquireTimeMillis = -1;

    @Optional
    private Integer maxCreateConnectionTimeMillis = -1;

    @Optional
    private Integer maxIdleTimeMillis = -1;

    @Optional
    private Integer maxLifeTimeMillis = -1;

    @Optional
    private Integer maxValidationTimeMillis = -1;

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

    public Boolean getCreateTables() {
        return createTables;
    }

    public void setCreateTables(Boolean createTables) {
        this.createTables = createTables;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public Integer getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(Integer connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public Integer getLockWaitTimeoutMillis() {
        return lockWaitTimeoutMillis;
    }

    public void setLockWaitTimeoutMillis(Integer lockWaitTimeoutMillis) {
        this.lockWaitTimeoutMillis = lockWaitTimeoutMillis;
    }

    public Integer getStatementTimeoutMillis() {
        return statementTimeoutMillis;
    }

    public void setStatementTimeoutMillis(Integer statementTimeoutMillis) {
        this.statementTimeoutMillis = statementTimeoutMillis;
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

    public Integer getBackgroundEvictionIntervalMillis() {
        return backgroundEvictionIntervalMillis;
    }

    public void setBackgroundEvictionIntervalMillis(Integer backgroundEvictionIntervalMillis) {
        this.backgroundEvictionIntervalMillis = backgroundEvictionIntervalMillis;
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

    public Integer getMaxAcquireTimeMillis() {
        return maxAcquireTimeMillis;
    }

    public void setMaxAcquireTimeMillis(Integer maxAcquireTimeMillis) {
        this.maxAcquireTimeMillis = maxAcquireTimeMillis;
    }

    public Integer getMaxCreateConnectionTimeMillis() {
        return maxCreateConnectionTimeMillis;
    }

    public void setMaxCreateConnectionTimeMillis(Integer maxCreateConnectionTimeMillis) {
        this.maxCreateConnectionTimeMillis = maxCreateConnectionTimeMillis;
    }

    public Integer getMaxIdleTimeMillis() {
        return maxIdleTimeMillis;
    }

    public void setMaxIdleTimeMillis(Integer maxIdleTimeMillis) {
        this.maxIdleTimeMillis = maxIdleTimeMillis;
    }

    public Integer getMaxLifeTimeMillis() {
        return maxLifeTimeMillis;
    }

    public void setMaxLifeTimeMillis(Integer maxLifeTimeMillis) {
        this.maxLifeTimeMillis = maxLifeTimeMillis;
    }

    public Integer getMaxValidationTimeMillis() {
        return maxValidationTimeMillis;
    }

    public void setMaxValidationTimeMillis(Integer maxValidationTimeMillis) {
        this.maxValidationTimeMillis = maxValidationTimeMillis;
    }
}
