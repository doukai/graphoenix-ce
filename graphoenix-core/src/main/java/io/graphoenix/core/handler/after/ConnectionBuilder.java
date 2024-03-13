package io.graphoenix.core.handler.after;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationAfterHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.OBJECT_SELECTION_NOT_EXIST;
import static jakarta.json.JsonValue.NULL;

@ApplicationScoped
@Priority(Integer.MAX_VALUE - 300)
public class ConnectionBuilder implements OperationAfterHandler {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;

    @Inject
    public ConnectionBuilder(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<JsonValue> handle(Operation operation, JsonValue jsonValue) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.just(
                jsonProvider
                        .createPatchBuilder(
                                operation.getFields().stream()
                                        .flatMap(field -> {
                                                    String selectionName = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
                                                    return buildConnections("/" + selectionName, operationType.getField(field.getName()), field, jsonValue);
                                                }
                                        )
                                        .collect(JsonCollectors.toJsonArray())
                        )
                        .build()
                        .apply(jsonValue.asJsonObject())
        );
    }

    public Stream<JsonValue> buildConnections(String path, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            if (jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
                return Stream.empty();
            }
            String selectionName = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
            if (fieldDefinition.getType().hasList()) {
                return IntStream.range(0, jsonValue.asJsonObject().get(selectionName).asJsonArray().size())
                        .mapToObj(index ->
                                Stream.ofNullable(field.getFields())
                                        .flatMap(Collection::stream)
                                        .flatMap(subField -> {
                                                    String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                                    return buildConnections(path + "/" + index + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonObject().get(selectionName).asJsonArray().get(index));
                                                }
                                        )
                        )
                        .flatMap(stream -> stream);
            } else {
                if (fieldDefinition.isConnectionField()) {
                    String filedName = fieldDefinition.getConnectionFieldOrError();
                    String aggName = fieldDefinition.getConnectionAggOrError();
                    FieldDefinition nodeFieldDefinition = documentManager.getFieldTypeDefinition(fieldTypeDefinition.asObject().getField(FIELD_EDGES_NAME)).asObject().getField(FIELD_NODE_NAME);
                    JsonValue connectionJsonValue = buildConnection(nodeFieldDefinition, field, jsonValue.asJsonObject().get(filedName), jsonValue.asJsonObject().get(aggName));

                    JsonObject patchItem = jsonProvider.createObjectBuilder()
                            .add("op", "add")
                            .add("path", path)
                            .add("value", connectionJsonValue)
                            .build();
                    JsonValue edgesJsonValue = connectionJsonValue.asJsonObject().get(FIELD_EDGES_NAME);
                    if (edgesJsonValue.getValueType().equals(JsonValue.ValueType.ARRAY)) {
                        return Stream.concat(
                                Stream.of(patchItem),
                                IntStream.range(0, edgesJsonValue.asJsonArray().size())
                                        .filter(index -> !edgesJsonValue.asJsonArray().get(index).getValueType().equals(JsonValue.ValueType.NULL))
                                        .filter(index -> !edgesJsonValue.asJsonArray().get(index).asJsonObject().get(FIELD_NODE_NAME).getValueType().equals(JsonValue.ValueType.NULL))
                                        .mapToObj(index ->
                                                Stream.ofNullable(field.getField(FIELD_EDGES_NAME).getField(FIELD_NODE_NAME).getFields())
                                                        .flatMap(Collection::stream)
                                                        .flatMap(subField -> {
                                                                    String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                                                    return buildConnections(
                                                                            path + "/" + FIELD_EDGES_NAME + "/" + index + "/" + FIELD_NODE_NAME + "/" + subSelectionName,
                                                                            documentManager.getFieldTypeDefinition(nodeFieldDefinition).asObject().getField(subField.getName()),
                                                                            subField,
                                                                            edgesJsonValue.asJsonArray().get(index).asJsonObject().get(FIELD_NODE_NAME).asJsonObject()
                                                                    );
                                                                }
                                                        )
                                        )
                                        .flatMap(stream -> stream)
                        );
                    } else {
                        return Stream.of(patchItem);
                    }
                } else {
                    return Stream.ofNullable(field.getFields())
                            .flatMap(Collection::stream)
                            .flatMap(subField -> {
                                        String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                        return buildConnections(path + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonObject().get(selectionName));
                                    }
                            );
                }
            }
        } else {
            return Stream.empty();
        }
    }

    public JsonValue buildConnection(FieldDefinition nodeFieldDefinition, Field connectionField, JsonValue fieldJsonValue, JsonValue aggJsonValue) {
        if (connectionField.getFields() != null && !connectionField.getFields().isEmpty()) {
            JsonObjectBuilder connectionObjectBuilder = jsonProvider.createObjectBuilder();
            ObjectType nodeFieldTypeDefinition = documentManager.getFieldTypeDefinition(nodeFieldDefinition).asObject();
            FieldDefinition cursorFieldDefinition = nodeFieldTypeDefinition.getCursorField()
                    .orElseGet(nodeFieldTypeDefinition::getIDFieldOrError);

            for (Field field : connectionField.getFields()) {
                int size = fieldJsonValue.getValueType().equals(JsonValue.ValueType.NULL) ? 0 : fieldJsonValue.asJsonArray().size();
                int limit = size;
                boolean isLast = false;
                boolean isBefore = false;
                boolean isAfter = false;
                if (field.getArguments() != null && !field.getArguments().isEmpty()) {
                    limit = field.getArguments().getArguments().entrySet().stream()
                            .filter(entry -> entry.getKey().equals(INPUT_VALUE_FIRST_NAME))
                            .filter(entry -> entry.getValue().isInt())
                            .findFirst()
                            .map(entry -> entry.getValue().asInt().getIntegerValue())
                            .orElseGet(() ->
                                    field.getArguments().getArguments().entrySet().stream()
                                            .filter(entry -> entry.getKey().equals(INPUT_VALUE_AFTER_NAME))
                                            .filter(entry -> entry.getValue().isInt())
                                            .findFirst()
                                            .map(entry -> entry.getValue().asInt().getIntegerValue())
                                            .orElse(size)
                            );
                    isLast = field.getArguments().getArguments().entrySet().stream().anyMatch(entry -> entry.getKey().equals(INPUT_VALUE_LAST_NAME));
                    isBefore = field.getArguments().getArguments().entrySet().stream().anyMatch(entry -> entry.getKey().equals(INPUT_VALUE_BEFORE_NAME));
                    isAfter = field.getArguments().getArguments().entrySet().stream().anyMatch(entry -> entry.getKey().equals(INPUT_VALUE_AFTER_NAME));
                }
                boolean isFirst = !isLast;

                switch (field.getName()) {
                    case FIELD_TOTAL_COUNT_NAME:
                        FieldDefinition idField = nodeFieldTypeDefinition.getIDFieldOrError();
                        connectionObjectBuilder.add(FIELD_TOTAL_COUNT_NAME, aggJsonValue.asJsonObject().get(idField.getName() + SUFFIX_COUNT));
                        break;
                    case FIELD_EDGES_NAME:
                        if (field.getFields() == null || field.getFields().isEmpty()) {
                            throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(field.toString()));
                        }
                        JsonValue edges;
                        if (fieldJsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
                            edges = NULL;
                        } else {
                            edges = fieldJsonValue.asJsonArray().stream()
                                    .limit(limit)
                                    .map(node -> {
                                                JsonObjectBuilder edge = jsonProvider.createObjectBuilder();
                                                for (Field edgeField : field.getFields()) {
                                                    if (edgeField.getName().equals(FIELD_CURSOR_NAME)) {
                                                        edge.add(FIELD_CURSOR_NAME, node.asJsonObject().get(cursorFieldDefinition.getName()));
                                                    } else if (edgeField.getName().equals(FIELD_NODE_NAME)) {
                                                        edge.add(FIELD_NODE_NAME, node);
                                                    }
                                                }
                                                return edge.build();
                                            }
                                    )
                                    .collect(JsonCollectors.toJsonArray());

                            if (isLast) {
                                Collections.reverse(edges.asJsonArray());
                            }
                        }
                        connectionObjectBuilder.add(FIELD_EDGES_NAME, edges);
                        break;
                    case FIELD_PAGE_INFO_NAME:
                        if (field.getFields() == null || field.getFields().isEmpty()) {
                            throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(field.toString()));
                        }
                        JsonObjectBuilder pageInfoBuilder = jsonProvider.createObjectBuilder();
                        for (Field pageInfoField : field.getFields()) {
                            switch (pageInfoField.getName()) {
                                case FIELD_HAS_NEXT_PAGE_NAME:
                                    if (fieldJsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
                                        pageInfoBuilder.add(FIELD_HAS_NEXT_PAGE_NAME, false);
                                    } else if (isFirst) {
                                        pageInfoBuilder.add(FIELD_HAS_NEXT_PAGE_NAME, limit < fieldJsonValue.asJsonArray().size());
                                    } else {
                                        pageInfoBuilder.add(FIELD_HAS_NEXT_PAGE_NAME, isBefore);
                                    }
                                    break;
                                case FIELD_HAS_PREVIOUS_PAGE_NAME:
                                    if (fieldJsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
                                        pageInfoBuilder.add(FIELD_HAS_NEXT_PAGE_NAME, false);
                                    } else if (isLast) {
                                        pageInfoBuilder.add(FIELD_HAS_PREVIOUS_PAGE_NAME, limit < fieldJsonValue.asJsonArray().size());
                                    } else {
                                        pageInfoBuilder.add(FIELD_HAS_PREVIOUS_PAGE_NAME, isAfter);
                                    }
                                    break;
                                case FIELD_START_CURSOR_NAME:
                                    if (!fieldJsonValue.getValueType().equals(JsonValue.ValueType.NULL) && !fieldJsonValue.asJsonArray().isEmpty()) {
                                        if (limit < fieldJsonValue.asJsonArray().size()) {
                                            if (isFirst) {
                                                pageInfoBuilder.add(FIELD_START_CURSOR_NAME, fieldJsonValue.asJsonArray().get(0).asJsonObject().get(cursorFieldDefinition.getName()));
                                            } else {
                                                pageInfoBuilder.add(FIELD_START_CURSOR_NAME, fieldJsonValue.asJsonArray().get(fieldJsonValue.asJsonArray().size() - 2).asJsonObject().get(cursorFieldDefinition.getName()));
                                            }
                                        } else {
                                            if (isFirst) {
                                                pageInfoBuilder.add(FIELD_START_CURSOR_NAME, fieldJsonValue.asJsonArray().get(0).asJsonObject().get(cursorFieldDefinition.getName()));
                                            } else {
                                                pageInfoBuilder.add(FIELD_START_CURSOR_NAME, fieldJsonValue.asJsonArray().get(fieldJsonValue.asJsonArray().size() - 1).asJsonObject().get(cursorFieldDefinition.getName()));
                                            }
                                        }
                                    } else {
                                        pageInfoBuilder.add(FIELD_START_CURSOR_NAME, NULL);
                                    }
                                    break;
                                case FIELD_END_CURSOR_NAME:
                                    if (!fieldJsonValue.getValueType().equals(JsonValue.ValueType.NULL) && !fieldJsonValue.asJsonArray().isEmpty()) {
                                        if (limit < fieldJsonValue.asJsonArray().size()) {
                                            if (isFirst) {
                                                pageInfoBuilder.add(FIELD_END_CURSOR_NAME, fieldJsonValue.asJsonArray().get(fieldJsonValue.asJsonArray().size() - 2).asJsonObject().get(cursorFieldDefinition.getName()));
                                            } else {
                                                pageInfoBuilder.add(FIELD_END_CURSOR_NAME, fieldJsonValue.asJsonArray().get(0).asJsonObject().get(cursorFieldDefinition.getName()));
                                            }
                                        } else {
                                            if (isFirst) {
                                                pageInfoBuilder.add(FIELD_END_CURSOR_NAME, fieldJsonValue.asJsonArray().get(fieldJsonValue.asJsonArray().size() - 1).asJsonObject().get(cursorFieldDefinition.getName()));
                                            } else {
                                                pageInfoBuilder.add(FIELD_END_CURSOR_NAME, fieldJsonValue.asJsonArray().get(0).asJsonObject().get(cursorFieldDefinition.getName()));
                                            }
                                        }
                                    } else {
                                        pageInfoBuilder.add(FIELD_END_CURSOR_NAME, NULL);
                                    }
                                    break;
                            }
                        }
                        connectionObjectBuilder.add(FIELD_PAGE_INFO_NAME, pageInfoBuilder);
                        break;
                }
            }
            return connectionObjectBuilder.build();
        }
        throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(connectionField.toString()));
    }
}
