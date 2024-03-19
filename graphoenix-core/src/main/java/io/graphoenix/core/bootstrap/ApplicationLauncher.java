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
import reactor.core.publisher.Flux;
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
    private final List<Runnable> runnerList;

    @Inject
    public ApplicationLauncher(PackageManager packageManager, PackageConfig packageConfig) {
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
        this.runnerList = new ArrayList<>();
    }

    public ApplicationLauncher addServers(Runner... servers) {
        this.runnerList.addAll(List.of(servers));
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

        if (runnerList.isEmpty()) {
            runnerList.addAll(BeanContext.getList(Runner.class));
        }

        if (!runnerList.isEmpty()) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            CountDownLatch latch = new CountDownLatch(1);
            for (Runnable runner : runnerList) {
                executorService.execute(
                        new Thread(() -> {
                            try {
                                latch.await();
                            } catch (InterruptedException e) {
                                Logger.error(e);
                            }
                            runner.run();
                        })
                );
            }

            Runtime.getRuntime().addShutdownHook(
                    new Thread(() ->
                            Flux
                                    .concat(
                                            ScopeEventResolver.beforeDestroyed(ApplicationScoped.class),
                                            ScopeEventResolver.destroyed(ApplicationScoped.class)
                                    )
                                    .then()
                                    .block()
                    )
            );

            ScopeEventResolver.initialized(Map.of("args", args), ApplicationScoped.class)
                    .then(Mono.fromRunnable(latch::countDown))
                    .block();

            executorService.shutdown();
        }
    }
}
