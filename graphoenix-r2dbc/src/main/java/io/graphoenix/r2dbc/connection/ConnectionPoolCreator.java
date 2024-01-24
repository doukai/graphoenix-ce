package io.graphoenix.r2dbc.connection;

import io.graphoenix.r2dbc.config.R2DBCConfig;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;

@ApplicationScoped
public class ConnectionPoolCreator {

    private final ConnectionFactoryCreator connectionFactoryCreator;
    private final ConnectionPool connectionPool;

    @Inject
    public ConnectionPoolCreator(ConnectionFactoryCreator connectionFactoryCreator, R2DBCConfig r2DBCConfig) {
        this.connectionFactoryCreator = connectionFactoryCreator;
        this.connectionPool = createConnectionPool(r2DBCConfig);
    }

    private ConnectionPool createConnectionPool(R2DBCConfig r2DBCConfig) {
        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration
                .builder(connectionFactoryCreator.createFactory())
                .acquireRetry(r2DBCConfig.getAcquireRetry())
                .backgroundEvictionInterval(Duration.ofMillis(r2DBCConfig.getBackgroundEvictionIntervalMillis()))
                .initialSize(r2DBCConfig.getInitialSize())
                .maxSize(r2DBCConfig.getMaxSize())
                .minIdle(r2DBCConfig.getMinIdle())
                .maxAcquireTime(Duration.ofMillis(r2DBCConfig.getMaxAcquireTimeMillis()))
                .maxCreateConnectionTime(Duration.ofMillis(r2DBCConfig.getMaxCreateConnectionTimeMillis()))
                .maxIdleTime(Duration.ofMillis(r2DBCConfig.getMaxIdleTimeMillis()))
                .maxLifeTime(Duration.ofMillis(r2DBCConfig.getMaxLifeTimeMillis()))
                .maxLifeTime(Duration.ofMillis(r2DBCConfig.getMaxLifeTimeMillis()))
                .build();

        return new ConnectionPool(poolConfiguration);
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
