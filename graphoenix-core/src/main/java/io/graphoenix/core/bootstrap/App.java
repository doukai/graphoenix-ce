package io.graphoenix.core.bootstrap;

import io.graphoenix.spi.bootstrap.Launcher;
import io.graphoenix.spi.bootstrap.Runner;
import io.nozdormu.spi.context.BeanContext;

public enum App {
    APP;

    public Launcher addServers(Runner... servers) {
        return BeanContext.get(Launcher.class).addServers(servers);
    }

    public Launcher with(Runner... servers) {
        return BeanContext.get(Launcher.class).with(servers);
    }

    @SafeVarargs
    public final Launcher with(Class<? extends Runner>... classes) {
        return BeanContext.get(Launcher.class).with(classes);
    }

    public void run(String... args) {
        BeanContext.get(Launcher.class).run(args);
    }
}
