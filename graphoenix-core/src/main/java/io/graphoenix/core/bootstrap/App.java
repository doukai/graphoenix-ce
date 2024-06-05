package io.graphoenix.core.bootstrap;

import io.graphoenix.spi.bootstrap.Launcher;
import io.graphoenix.spi.bootstrap.Runner;
import jakarta.enterprise.inject.spi.CDI;

public enum App {
    APP;

    private final Launcher launcher = CDI.current().select(Launcher.class).get();

    public Launcher addServers(Runner... servers) {
        return launcher.addServers(servers);
    }

    public Launcher with(Runner... servers) {
        return launcher.with(servers);
    }

    @SafeVarargs
    public final Launcher with(Class<? extends Runner>... classes) {
        return launcher.with(classes);
    }

    public void run(String... args) {
        launcher.run(args);
    }
}
