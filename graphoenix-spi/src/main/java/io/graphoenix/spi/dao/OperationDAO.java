package io.graphoenix.spi.dao;

import io.nozdormu.spi.async.Async;
import io.nozdormu.spi.async.Asyncable;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.Map;

public interface OperationDAO extends Asyncable {

    @Async
    <T> T find(String sql, Map<String, Object> parameters, Class<T> beanClass);

    @Async
    <T> T find(String sql, Map<String, Object> parameters, Type type);

    @Async
    <T> T save(String sql, Map<String, Object> parameters, Class<T> beanClass);

    @Async
    <T> T save(String sql, Map<String, Object> parameters, Type type);

    <T> Mono<T> findAsync(String sql, Map<String, Object> parameters, Class<T> beanClass);

    <T> Mono<T> findAsync(String sql, Map<String, Object> parameters, Type type);

    <T> Mono<T> saveAsync(String sql, Map<String, Object> parameters, Class<T> beanClass);

    <T> Mono<T> saveAsync(String sql, Map<String, Object> parameters, Type type);
}
