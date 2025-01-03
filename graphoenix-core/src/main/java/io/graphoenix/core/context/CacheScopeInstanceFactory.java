package io.graphoenix.core.context;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.nozdormu.spi.context.ScopeInstanceFactory;
import io.nozdormu.spi.context.ScopeInstances;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import java.time.Duration;

public abstract class CacheScopeInstanceFactory extends ScopeInstanceFactory {

    protected LoadingCache<String, ScopeInstances> buildCache(Duration timeout) {
        return Caffeine.newBuilder()
                .expireAfterAccess(timeout)
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
                .build(key -> {
                            Logger.info("{} key: {} build", getCacheId(), key);
                            onBuild(key);
                            return new ScopeInstances();
                        }
                );
    }

    protected abstract String getCacheId();

    protected abstract LoadingCache<String, ScopeInstances> getCache();

    protected abstract void onBuild(String key);

    protected abstract void onEviction(Object key, Object value);

    protected abstract void onRemoval(Object key, Object value);

    @Override
    public Mono<ScopeInstances> getScopeInstances() {
        return Mono.deferContextual(contextView ->
                Mono.justOrEmpty(contextView.getOrEmpty(getCacheId()))
                        .map(id -> getCache().get((String) id))
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, E extends T> Mono<T> compute(String id, Class<T> beanClass, String name, E instance) {
        return Mono.just(getCache().get(id))
                .map(scopeInstances -> (T) scopeInstances.get(beanClass).compute(name, (k, v) -> instance));
    }

    public void invalidate(String id) {
        getCache().invalidate(id);
    }
}
