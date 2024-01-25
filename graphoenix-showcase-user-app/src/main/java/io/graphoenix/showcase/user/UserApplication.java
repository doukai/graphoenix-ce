package io.graphoenix.showcase.user;

import io.graphoenix.spi.annotation.Application;

import static io.graphoenix.core.bootstrap.Launcher.MAIN;

@Application
public class UserApplication {

    public static void main(String[] args) {
        MAIN.run(args);
    }
}
