package io.graphoenix.spi.bootstrap;

public interface Runner extends Runnable {

    default String protocol() {
        return null;
    }

    int port();
}
