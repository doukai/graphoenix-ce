package io.graphoenix.grpc.server;

import io.graphoenix.grpc.server.config.GrpcServerConfig;
import io.graphoenix.spi.bootstrap.Runner;
import io.graphoenix.spi.constant.Hammurabi;
import io.grpc.Server;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
@Named(Hammurabi.ENUM_PROTOCOL_ENUM_VALUE_GRPC)
public class GraphQLGrpcServer implements Runner {

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
                                    Logger.error(e);
                                }
                            })
                    );
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            Logger.error(e);
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
