package io.graphoenix.core.dao;

import io.graphoenix.core.handler.before.VariableHandler;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.PackageFetchHandler;
import io.nozdormu.spi.async.Async;
import io.nozdormu.spi.async.Asyncable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class PackageOperationDAO implements Asyncable {

    private final VariableHandler variableHandler;

    private final Jsonb jsonb;

    @Inject
    public PackageOperationDAO(VariableHandler variableHandler, Jsonb jsonb) {
        this.variableHandler = variableHandler;
        this.jsonb = jsonb;
    }

    @Async
    public <T> T fetch(String packageName, String protocol, String graphql, Map<String, Object> parameters, Class<T> beanClass) {
        return await(fetchAsync(packageName, protocol, graphql, parameters, beanClass));
    }

    @Async
    public <T> T fetch(String packageName, String protocol, String graphql, Map<String, Object> parameters, Type type) {
        return await(fetchAsync(packageName, protocol, graphql, parameters, type));
    }

    public <T> Mono<T> fetchAsync(String packageName, String protocol, String graphql, Map<String, Object> parameters, Class<T> beanClass) {
        return fetchAsync(packageName, protocol, graphql, parameters)
                .mapNotNull(json -> jsonb.fromJson(json.toString(), beanClass));
    }

    public <T> Mono<T> fetchAsync(String packageName, String protocol, String graphql, Map<String, Object> parameters, Type type) {
        return fetchAsync(packageName, protocol, graphql, parameters)
                .mapNotNull(json -> jsonb.fromJson(json.toString(), type));
    }

    private Mono<JsonValue> fetchAsync(String packageName, String protocol, String graphql, Map<String, Object> parameters) {
        return variableHandler.handle(
                        Operation.fromString(graphql),
                        parameters != null ?
                                parameters.entrySet().stream()
                                        .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), ValueWithVariable.of(entry.getValue())))
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) :
                                null
                )
                .flatMap(operation ->
                        CDI.current().select(PackageFetchHandler.class, NamedLiteral.of(protocol)).get()
                                .request(packageName, operation)
                );
    }
}
