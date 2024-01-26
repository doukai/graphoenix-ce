package io.graphoenix.spi.bootstrap;

public interface Launcher {

    Launcher addServers(Runner... servers);

    Launcher with(Runner... servers);

    @SuppressWarnings("unchecked")
    Launcher with(Class<? extends Runner>... classes);

    void run(String... args);
}
