package io.graphoenix.core.bootstrap;

import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.bootstrap.Launcher;
import io.graphoenix.spi.bootstrap.Runner;
import io.nozdormu.spi.context.BeanContext;
import io.nozdormu.spi.event.ScopeEventResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ApplicationLauncher implements Launcher {

    private final PackageManager packageManager;
    private final PackageConfig packageConfig;
    private final List<Runnable> serverList;

    @Inject
    public ApplicationLauncher(PackageManager packageManager, PackageConfig packageConfig) {
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
        this.serverList = new ArrayList<>();
    }

    public ApplicationLauncher addServers(Runner... servers) {
        this.serverList.addAll(List.of(servers));
        return this;
    }

    public ApplicationLauncher with(Runner... servers) {
        return addServers(servers);
    }

    @SafeVarargs
    public final ApplicationLauncher with(Class<? extends Runner>... classes) {
        return addServers(Arrays.stream(classes).map(BeanContext::get).toArray(Runner[]::new));
    }

    public void run(String... args) {
        if (packageConfig.getPackageName() == null) {
            packageManager.getDefaultPackageName().ifPresent(packageConfig::setPackageName);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(1);
        if (serverList.isEmpty()) {
            serverList.addAll(BeanContext.getMap(Runner.class).values());
        } else {
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
                .then(Mono.fromRunnable(latch::countDown))
                .doOnTerminate(() -> ScopeEventResolver.beforeDestroyed(ApplicationScoped.class).block())
                .doAfterTerminate(() -> ScopeEventResolver.destroyed(ApplicationScoped.class).block())
                .block();

        executorService.shutdown();
    }
}
