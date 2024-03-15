package io.graphoenix.spi.bootstrap;

public interface Launcher {

    Launcher addServers(Runnable... servers);

    Launcher with(Runnable... servers);

    @SuppressWarnings("unchecked")
    Launcher with(Class<? extends Runnable>... classes);

    void run(String... args);
}
