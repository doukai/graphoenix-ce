package io.graphoenix.r2dbc.dao;

import io.graphoenix.r2dbc.config.R2DBCConfig;
import io.graphoenix.r2dbc.executor.MutationExecutor;
import io.graphoenix.r2dbc.executor.QueryExecutor;
import io.graphoenix.r2dbc.handler.R2DBCParameterHandler;
import io.graphoenix.spi.dao.OperationDAO;
import io.nozdormu.spi.async.Async;
import io.nozdormu.spi.async.Asyncable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class R2DBCOperationDAO implements OperationDAO, Asyncable {

    private final R2DBCConfig r2DBCConfig;

    private final QueryExecutor queryExecutor;

    private final MutationExecutor mutationExecutor;

    private final R2DBCParameterHandler r2DBCParameterHandler;

    private final Jsonb jsonb;

    @Inject
    public R2DBCOperationDAO(R2DBCConfig r2DBCConfig, QueryExecutor queryExecutor, MutationExecutor mutationExecutor, R2DBCParameterHandler r2DBCParameterHandler, Jsonb jsonb) {
        this.r2DBCConfig = r2DBCConfig;
        this.queryExecutor = queryExecutor;
        this.mutationExecutor = mutationExecutor;
        this.r2DBCParameterHandler = r2DBCParameterHandler;
        this.jsonb = jsonb;
    }

    @Override
    @Async
    public <T> T find(String sql, Map<String, Object> parameters, Class<T> beanClass) {
        return await(findAsync(sql, parameters, beanClass));
    }

    @Override
    @Async
    public <T> T find(String sql, Map<String, Object> parameters, Type type) {
        return await(findAsync(sql, parameters, type));
    }

    @Override
    @Async
    public <T> T save(String sql, Map<String, Object> parameters, Class<T> beanClass) {
        return await(saveAsync(sql, parameters, beanClass));
    }

    @Override
    @Async
    public <T> T save(String sql, Map<String, Object> parameters, Type type) {
        return await(saveAsync(sql, parameters, type));
    }

    @Override
    public <T> Mono<T> findAsync(String sql, Map<String, Object> parameters, Class<T> beanClass) {
        return queryExecutor.executeQuery(sql, r2DBCParameterHandler.process(parameters))
                .mapNotNull(json -> jsonb.fromJson(json, beanClass));
    }

    @Override
    public <T> Mono<T> findAsync(String sql, Map<String, Object> parameters, Type type) {
        return queryExecutor.executeQuery(sql, r2DBCParameterHandler.process(parameters))
                .mapNotNull(json -> jsonb.fromJson(json, type));
    }

    @Override
    @Transactional
    public <T> Mono<T> saveAsync(String sql, Map<String, Object> parameters, Class<T> beanClass) {
        String[] statements = sql.split(";");
        String[] mutations = Arrays.copyOfRange(statements, 0, statements.length - 1);
        String query = statements[statements.length - 1];
        Map<String, Object> processedParameters = r2DBCParameterHandler.process(parameters);
        if (r2DBCConfig.getAllowMultiQueries()) {
            return mutationExecutor.executeMutations(Stream.of(mutations), processedParameters)
                    .doOnSuccess(count -> Logger.info("mutation count: {}", count))
                    .then(queryExecutor.executeQuery(query, processedParameters))
                    .mapNotNull(json -> jsonb.fromJson(json, beanClass));
        } else {
            return mutationExecutor.executeMutationsFlux(Stream.of(mutations), processedParameters)
                    .doOnNext(count -> Logger.info("mutation count: {}", count))
                    .reduce(Long::sum)
                    .doOnSuccess(count -> Logger.info("mutation total count: {}", count))
                    .then(queryExecutor.executeQuery(query, processedParameters))
                    .mapNotNull(json -> jsonb.fromJson(json, beanClass));
        }
    }

    @Override
    @Transactional
    public <T> Mono<T> saveAsync(String sql, Map<String, Object> parameters, Type type) {
        String[] statements = sql.split(";");
        String[] mutations = Arrays.copyOfRange(statements, 0, statements.length - 1);
        String query = statements[statements.length - 1];
        Map<String, Object> processedParameters = r2DBCParameterHandler.process(parameters);
        if (r2DBCConfig.getAllowMultiQueries()) {
            return mutationExecutor.executeMutations(Stream.of(mutations), processedParameters)
                    .doOnSuccess(count -> Logger.info("mutation count: {}", count))
                    .then(queryExecutor.executeQuery(query, processedParameters))
                    .mapNotNull(json -> jsonb.fromJson(json, type));
        } else {
            return mutationExecutor.executeMutationsFlux(Stream.of(mutations), processedParameters)
                    .doOnNext(count -> Logger.info("mutation count: {}", count))
                    .reduce(Long::sum)
                    .doOnSuccess(count -> Logger.info("mutation total count: {}", count))
                    .then(queryExecutor.executeQuery(query, processedParameters))
                    .mapNotNull(json -> jsonb.fromJson(json, type));
        }
    }
}
