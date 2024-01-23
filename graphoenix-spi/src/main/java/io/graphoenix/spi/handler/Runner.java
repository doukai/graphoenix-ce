package io.graphoenix.spi.handler;

public interface Runner extends Runnable {

    String protocol();

    int port();
}
