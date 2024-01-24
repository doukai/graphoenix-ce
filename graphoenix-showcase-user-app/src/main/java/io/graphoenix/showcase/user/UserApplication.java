package io.graphoenix.showcase.user;

import io.graphoenix.http.server.GraphQLHttpServer;
import io.graphoenix.spi.annotation.Application;

import static io.graphoenix.core.bootstrap.Launcher.APP;

@Application
public class UserApplication {

    public static void main(String[] args) {
        APP.with(GraphQLHttpServer.class).run();
    }
}
