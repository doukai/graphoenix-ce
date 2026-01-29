package io.graphoenix.core.context;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.nozdormu.spi.context.ReactorBeanScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public abstract class CaffeineReactorBeanScoped implements ReactorBeanScoped {

    private static final Logger logger = LoggerFactory.getLogger(CaffeineReactorBeanScoped.class);

    protected LoadingCache<String, Map<String, Object>> buildCache(Duration ttl) {
        return Caffeine.newBuilder()
                .expireAfterAccess(ttl)
                .evictionListener((key, value, cause) -> {
                    logger.info("{} key: {} eviction, cause: {}", getScopedKeyName(), key, cause);
                    onEviction(key, value);
                })
                .removalListener((key, value, cause) -> {
                    logger.info("{} key: {} removal, cause: {}", getScopedKeyName(), key, cause);
                    onRemoval(key, value);
                })
                .build(key -> {
                    logger.info("{} key: {} build", getScopedKeyName(), key);
                    onBuild(key);
                    return new HashMap<>();
                });
    }

    protected abstract String getScopedKeyName();

    protected abstract LoadingCache<String, Map<String, Object>> getCache();

    protected abstract void onBuild(String key);

    protected abstract void onEviction(Object key, Object value);

    protected abstract void onRemoval(Object key, Object value);

    @Override
    public Mono<String> getScopedKey() {
        return Mono.deferContextual(contextView ->
                Mono.justOrEmpty(contextView.getOrEmpty(getScopedKeyName()))
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Mono<T> get(Class<T> beanClass) {
        return getScopedKey()
                .mapNotNull(key -> {
                    if (key == null) {
                        return null;
                    }
                    Map<String, Object> scopedMap = getCache().get(key);
                    if (scopedMap == null) {
                        return null;
                    }
                    return (T) scopedMap.get(beanClass.getName());
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, R extends T> Mono<T> get(Class<T> beanClass, Supplier<R> supplier) {
        return getScopedKey()
                .mapNotNull(key -> {
                    if (key == null) {
                        return null;
                    }
                    Map<String, Object> scopedMap = getCache().get(key, k -> new ConcurrentHashMap<>());
                    return (T) scopedMap.computeIfAbsent(beanClass.getName(), k -> supplier.get());
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, R extends T> Mono<T> getMono(Class<T> beanClass, Supplier<Mono<R>> supplier) {
        return getScopedKey()
                .flatMap(key -> {
                    if (key == null) {
                        return Mono.empty();
                    }
                    Map<String, Object> scopedMap = getCache().get(key, k -> new ConcurrentHashMap<>());
                    if(scopedMap.containsKey(beanClass.getName())) {
                        return Mono.just((T) scopedMap.get(beanClass.getName()));
                    }
                    return supplier.get().map(bean -> (T) scopedMap.compute(beanClass.getName(), (k, v) -> bean));
                });
    }

    @Override
    public <T, R extends T> Mono<Boolean> put(Class<T> beanClass, R bean) {
        return getScopedKey()
                .doOnSuccess(key ->
                        getCache().get(key, k -> new ConcurrentHashMap<>())
                                .put(beanClass.getName(), bean)
                )
                .map(key -> getCache().get(key) != null);
    }

    @Override
    public <T, R extends T> boolean put(String key, Class<T> beanClass, R bean) {
        getCache().get(key, k -> new ConcurrentHashMap<>())
                .put(beanClass.getName(), bean);
        return getCache().get(key) != null;
    }

    @Override
    public Mono<Boolean> destroy() {
        return getScopedKey()
                .doOnSuccess(key -> getCache().invalidate(key))
                .map(key -> getCache().get(key) == null);
    }

    @Override
    public boolean destroy(String key) {
        getCache().invalidate(key);
        return getCache().get(key) == null;
    }
}
