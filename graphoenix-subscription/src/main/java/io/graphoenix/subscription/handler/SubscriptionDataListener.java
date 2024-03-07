package io.graphoenix.subscription.handler;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Dependent
public class SubscriptionDataListener {

    private final DocumentManager documentManager;

    private final SubscriptionDataFilter subscriptionDataFilter;

    private final Map<String, List<String>> typeIDMap = new HashMap<>();

    @Inject
    public SubscriptionDataListener(DocumentManager documentManager, Provider<SubscriptionDataFilter> subscriptionDataFilterProvider) {
        this.documentManager = documentManager;
        this.subscriptionDataFilter = subscriptionDataFilterProvider.get();
    }

    public boolean merged(JsonValue jsonValue) {
        JsonObject jsonObject = jsonValue.asJsonObject();
        String typeName = jsonObject.getString("type");
        JsonValue arguments = jsonObject.get("arguments");
        JsonValue mutation = jsonObject.get("mutation");
        return arguments.asJsonArray().stream().anyMatch(argument -> merged(typeName, argument)) || subscriptionDataFilter.merged(mutation);
    }

    private boolean merged(String typeName, JsonValue jsonValue) {
        ObjectType objectType = documentManager.getDocument().getObjectTypeOrError(typeName);
        return objectType.getIDField()
                .filter(fieldDefinition -> typeIDMap.containsKey(typeName))
                .filter(fieldDefinition -> jsonValue.asJsonObject().containsKey(fieldDefinition.getName()))
                .filter(fieldDefinition -> !jsonValue.asJsonObject().isNull(fieldDefinition.getName())).stream()
                .anyMatch(fieldDefinition -> typeIDMap.get(typeName).contains(jsonValue.asJsonObject().getString(fieldDefinition.getName()))) ||

                jsonValue.asJsonObject().entrySet().stream()
                        .anyMatch(entry -> {
                                    FieldDefinition fieldDefinition = objectType.getField(entry.getKey());
                                    if (entry.getValue().getValueType().equals(JsonValue.ValueType.ARRAY)) {
                                        return entry.getValue().asJsonArray().stream()
                                                .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                .anyMatch(item -> merged(documentManager.getFieldTypeDefinition(fieldDefinition).getName(), item));
                                    } else if (entry.getValue().getValueType().equals(JsonValue.ValueType.OBJECT)) {
                                        return merged(documentManager.getFieldTypeDefinition(fieldDefinition).getName(), entry.getValue());
                                    } else {
                                        return false;
                                    }
                                }
                        );
    }

    public SubscriptionDataListener indexData(Operation operation, JsonValue jsonValue) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        this.typeIDMap
                .putAll(
                        operation.getFields().stream()
                                .map(field -> operationType.getField(field.getName()))
                                .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                .filter(fieldDefinition -> jsonValue.asJsonObject().containsKey(fieldDefinition.getName()))
                                .filter(fieldDefinition -> jsonValue.asJsonObject().get(fieldDefinition.getName()).getValueType().equals(JsonValue.ValueType.OBJECT))
                                .flatMap(fieldDefinition -> indexData(fieldDefinition, jsonValue.asJsonObject().get(fieldDefinition.getName())))
                                .collect(
                                        Collectors.groupingBy(
                                                Map.Entry::getKey,
                                                Collectors.mapping(
                                                        Map.Entry::getValue,
                                                        Collectors.toList()
                                                )
                                        )
                                )
                );
        return this;
    }

    private Stream<Map.Entry<String, String>> indexData(FieldDefinition fieldDefinition, JsonValue jsonValue) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        return Stream
                .concat(
                        fieldTypeDefinition.asObject().getIDField()
                                .filter(idFieldDefinition -> jsonValue.asJsonObject().containsKey(idFieldDefinition.getName()))
                                .filter(idFieldDefinition -> !jsonValue.asJsonObject().isNull(idFieldDefinition.getName())).stream()
                                .map(idFieldDefinition -> new AbstractMap.SimpleEntry<>(fieldTypeDefinition.getName(), jsonValue.asJsonObject().getString(idFieldDefinition.getName()))),
                        jsonValue.asJsonObject().entrySet().stream()
                                .flatMap(entry -> {
                                            FieldDefinition subFieldDefinition = fieldTypeDefinition.asObject().getField(entry.getKey());
                                            if (entry.getValue().getValueType().equals(JsonValue.ValueType.ARRAY)) {
                                                return entry.getValue().asJsonArray().stream()
                                                        .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                        .flatMap(item -> indexData(subFieldDefinition, item));
                                            } else if (entry.getValue().getValueType().equals(JsonValue.ValueType.OBJECT)) {
                                                return indexData(subFieldDefinition, entry.getValue());
                                            } else {
                                                return Stream.empty();
                                            }
                                        }
                                )
                )
                .distinct();
    }

    public SubscriptionDataListener indexFilter(Operation operation) {
        subscriptionDataFilter.indexFilter(operation);
        return this;
    }
}
