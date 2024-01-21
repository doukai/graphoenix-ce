package io.graphoenix.r2dbc.connection;

import io.graphoenix.r2dbc.config.R2DBCConfig;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
                .backgroundEvictionInterval(r2DBCConfig.getBackgroundEvictionInterval())
                .initialSize(r2DBCConfig.getInitialSize())
                .maxSize(r2DBCConfig.getMaxSize())
                .minIdle(r2DBCConfig.getMinIdle())
                .maxAcquireTime(r2DBCConfig.getMaxAcquireTime())
                .maxCreateConnectionTime(r2DBCConfig.getMaxCreateConnectionTime())
                .maxIdleTime(r2DBCConfig.getMaxIdleTime())
                .maxLifeTime(r2DBCConfig.getMaxLifeTime())
                .maxLifeTime(r2DBCConfig.getMaxLifeTime())
                .build();

        return new ConnectionPool(poolConfiguration);
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
