package io.graphoenix.r2dbc.connection;

import io.graphoenix.r2dbc.config.R2DBCConfig;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;

@ApplicationScoped
public class ConnectionFactoryCreator {

    private final R2DBCConfig r2DBCConfig;

    @Inject
    public ConnectionFactoryCreator(R2DBCConfig r2DBCConfig) {
        this.r2DBCConfig = r2DBCConfig;
    }

    public ConnectionFactory createFactory() {
        if (r2DBCConfig.getUrl() != null) {
            return ConnectionFactories.get(r2DBCConfig.getUrl());
        } else {
            ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                    .option(ConnectionFactoryOptions.DRIVER, r2DBCConfig.getDriver())
                    .option(ConnectionFactoryOptions.PROTOCOL, r2DBCConfig.getProtocol())
                    .option(ConnectionFactoryOptions.HOST, r2DBCConfig.getHost())
                    .option(ConnectionFactoryOptions.DATABASE, r2DBCConfig.getDatabase())
                    .option(ConnectionFactoryOptions.PORT, r2DBCConfig.getPort())
                    .option(ConnectionFactoryOptions.USER, r2DBCConfig.getUser())
                    .option(ConnectionFactoryOptions.PASSWORD, r2DBCConfig.getPassword())
                    .option(ConnectionFactoryOptions.SSL, r2DBCConfig.getSsl())
                    .option(ConnectionFactoryOptions.CONNECT_TIMEOUT, Duration.ofMillis(r2DBCConfig.getConnectTimeoutMillis()))
                    .option(ConnectionFactoryOptions.LOCK_WAIT_TIMEOUT, Duration.ofMillis(r2DBCConfig.getLockWaitTimeoutMillis()))
                    .option(ConnectionFactoryOptions.STATEMENT_TIMEOUT, Duration.ofMillis(r2DBCConfig.getStatementTimeoutMillis()))
                    .option(Option.valueOf("allowMultiQueries"), r2DBCConfig.getAllowMultiQueries())
                    .build();

            return ConnectionFactories.get(options);
        }
    }
}
