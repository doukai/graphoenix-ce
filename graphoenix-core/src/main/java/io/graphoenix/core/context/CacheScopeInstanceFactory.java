package io.graphoenix.core.context;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.nozdormu.spi.context.ScopeInstanceFactory;
import io.nozdormu.spi.context.ScopeInstances;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import java.time.Duration;

public abstract class CacheScopeInstanceFactory extends ScopeInstanceFactory {

    private final AsyncLoadingCache<String, ScopeInstances> CACHE = buildCache();

    private AsyncLoadingCache<String, ScopeInstances> buildCache() {
        return Caffeine.newBuilder()
                .expireAfterAccess(getTimeout())
                .evictionListener((key, value, cause) -> {
                            Logger.info("{} key: {} eviction", getCacheId(), key);
                            onEviction(key, value);
                        }
                )
                .removalListener((key, value, cause) -> {
                            Logger.info("{} key: {} removed", getCacheId(), key);
                            onRemoval(key, value);
                        }
                )
                .buildAsync(key -> {
                            Logger.info("{} key: {} build", getCacheId(), key);
                            onBuild(key);
                            return new ScopeInstances();
                        }
                );
    }

    protected abstract Duration getTimeout();

    protected abstract String getCacheId();

    protected abstract void onBuild(String key);

    protected abstract void onEviction(Object key, Object value);

    protected abstract void onRemoval(Object key, Object value);

    @Override
    public Mono<ScopeInstances> getScopeInstances() {
        return Mono.deferContextual(contextView ->
                Mono.justOrEmpty(contextView.getOrEmpty(getCacheId()))
                        .flatMap(id -> Mono.fromFuture(CACHE.get((String) id)))
        );
    }

    public void invalidate(String id) {
        CACHE.synchronous().invalidate(id);
    }
}
