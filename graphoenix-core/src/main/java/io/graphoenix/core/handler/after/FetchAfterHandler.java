package io.graphoenix.core.handler.after;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.fetch.QueryDataLoader;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationAfterHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
@Priority(Integer.MAX_VALUE - 400)
public class FetchAfterHandler implements OperationAfterHandler {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final Provider<QueryDataLoader> queryDataLoaderProvider;

    @Inject
    public FetchAfterHandler(DocumentManager documentManager, JsonProvider jsonProvider, Provider<QueryDataLoader> queryDataLoaderProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
        this.queryDataLoaderProvider = queryDataLoaderProvider;
    }

    @Override
    public Mono<JsonValue> query(Operation operation, JsonValue jsonValue) {
        return null;
    }

    @Override
    public Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue) {
        return null;
    }

    @Override
    public Mono<JsonValue> subscription(Operation operation, JsonValue jsonValue) {
        return null;
    }

    public Mono<JsonValue> handle(Operation operation, JsonValue jsonValue) {
        QueryDataLoader queryDataLoader = queryDataLoaderProvider.get();
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.just(
                jsonProvider
                        .createPatchBuilder(
                                operation.getFields().stream()
                                        .flatMap(field -> {
                                                    String selectionName = Optional.ofNullable(field.getAlias()).orElse(field.getName());
                                                    return register(queryDataLoader, "/" + selectionName, operationType.getField(field.getName()), field, jsonValue.asJsonObject().get(selectionName));
                                                }
                                        )
                                        .collect(JsonCollectors.toJsonArray())
                        )
                        .build()
                        .apply(jsonValue.asJsonObject())
        );
    }

    public Stream<JsonObject> register(QueryDataLoader queryDataLoader, String path, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {

    }
}
