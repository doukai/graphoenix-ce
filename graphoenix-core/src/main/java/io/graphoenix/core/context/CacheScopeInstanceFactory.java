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
                .evictionListener((key, value, cause) -> Logger.info("{} id: {} eviction", getCacheId(), key))
                .removalListener((key, value, cause) -> Logger.info("{} id: {} removed", getCacheId(), key))
                .buildAsync(key -> new ScopeInstances());
    }

    protected abstract Duration getTimeout();

    protected abstract String getCacheId();

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
