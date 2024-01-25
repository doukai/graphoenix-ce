package io.graphoenix.core.bootstrap;

import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.handler.Runner;
import io.nozdormu.spi.context.BeanContext;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum Launcher {
    MAIN;

    private CountDownLatch latch;

    private List<Runnable> serverList;

    public Launcher addServers(Runner... servers) {
        if (this.serverList == null) {
            this.serverList = new ArrayList<>();
        }
        this.serverList.addAll(List.of(servers));
        return this;
    }

    public Launcher with(Runner... servers) {
        return addServers(servers);
    }

    @SafeVarargs
    public final Launcher with(Class<? extends Runner>... classes) {
        return addServers(Arrays.stream(classes).map(BeanContext::get).toArray(Runner[]::new));
    }

    public void run(String... args) {
        PackageConfig packageConfig = BeanContext.get(PackageConfig.class);
        if (packageConfig.getPackageName() == null) {
            PackageManager packageManager = BeanContext.get(PackageManager.class);
            packageManager.getDefaultPackageName().ifPresent(packageConfig::setPackageName);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        this.latch = new CountDownLatch(1);
        if (serverList == null || serverList.isEmpty()) {
            serverList = new ArrayList<>(BeanContext.getMap(Runner.class).values());
        }
        if (!serverList.isEmpty()) {
            for (Runnable server : serverList) {
                executorService.execute(
                        new Thread(() -> {
                            try {
                                latch.await();
                            } catch (InterruptedException e) {
                                Logger.error(e);
                            }
                            server.run();
                        })
                );
            }
        }

        ScopeEventResolver.initialized(Map.of("args", args), ApplicationScoped.class)
                .then(Mono.fromRunnable(() -> latch.countDown()))
                .doOnTerminate(() -> ScopeEventResolver.beforeDestroyed(ApplicationScoped.class).block())
                .doAfterTerminate(() -> ScopeEventResolver.destroyed(ApplicationScoped.class).block())
                .block();

        executorService.shutdown();
    }
}
