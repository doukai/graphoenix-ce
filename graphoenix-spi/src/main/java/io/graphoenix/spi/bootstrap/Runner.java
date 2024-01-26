package io.graphoenix.spi.bootstrap;

public interface Runner extends Runnable {

    String protocol();

    int port();
}
