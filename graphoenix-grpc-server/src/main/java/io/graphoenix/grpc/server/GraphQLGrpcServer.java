package io.graphoenix.grpc.server;

import io.graphoenix.grpc.server.config.GrpcServerConfig;
import io.graphoenix.spi.bootstrap.Runner;
import io.graphoenix.spi.constant.Hammurabi;
import io.grpc.Server;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
@Named(Hammurabi.ENUM_PROTOCOL_ENUM_VALUE_GRPC)
public class GraphQLGrpcServer implements Runner {

    private static final Logger logger = LoggerFactory.getLogger(GraphQLGrpcServer.class);

    private final Server server;

    private final GrpcServerConfig grpcServerConfig;


    @Inject
    public GraphQLGrpcServer(Server server, GrpcServerConfig grpcServerConfig) {
        this.server = server;
        this.grpcServerConfig = grpcServerConfig;
    }

    @Override
    public void run() {
        try {
            server.start();
            Runtime.getRuntime()
                    .addShutdownHook(
                            new Thread(() -> {
                                try {
                                    server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                                } catch (InterruptedException e) {
                                    logger.error(e.getMessage(), e);
                                }
                            })
                    );
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public String protocol() {
        return Hammurabi.ENUM_PROTOCOL_ENUM_VALUE_GRPC;
    }

    @Override
    public int port() {
        return grpcServerConfig.getPort();
    }
}
