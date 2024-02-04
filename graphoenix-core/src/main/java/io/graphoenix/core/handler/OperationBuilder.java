package io.graphoenix.core.handler;

import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonCollectors;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@ApplicationScoped
public class OperationBuilder {

    private final DocumentManager documentManager;

    @Inject
    public OperationBuilder(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public JsonObject updateJsonObject(JsonObject original, JsonObject jsonObject) {
        return jsonObject.entrySet().stream()
                .filter(entry ->
                        !entry.getValue().getValueType().equals(JsonValue.ValueType.NULL) ||
                                entry.getValue().getValueType().equals(JsonValue.ValueType.NULL) && original != null && original.containsKey(entry.getKey())
                )
                .map(entry ->
                        new AbstractMap.SimpleEntry<>(
                                entry.getKey(),
                                updateJsonValue(original != null ? original.get(entry.getKey()) : null, entry.getValue())
                        )
                )
                .collect(JsonCollectors.toJsonObject());
    }

    public JsonValue updateJsonValue(JsonValue original, JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.OBJECT)) {
            return updateJsonObject(original != null ? original.asJsonObject() : null, jsonValue.asJsonObject());
        } else if (jsonValue.getValueType().equals(JsonValue.ValueType.ARRAY)) {
            return IntStream.range(0, jsonValue.asJsonArray().size())
                    .mapToObj(index -> updateJsonValue(original != null ? original.asJsonArray().get(index) : null, jsonValue.asJsonArray().get(index)))
                    .collect(JsonCollectors.toJsonArray());
        } else {
            return jsonValue;
        }
    }

    public JsonObject updateJsonObject(Field field, FieldDefinition fieldDefinition, JsonObject jsonObject) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        return Stream.ofNullable(field.getFields())
                .flatMap(Collection::stream)
                .map(subField -> {
                            String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                            return new AbstractMap.SimpleEntry<>(
                                    subSelectionName,
                                    updateJsonValue(subField, fieldTypeDefinition.asObject().getField(subField.getName()), jsonObject.get(subSelectionName))
                            );
                        }
                )
                .collect(JsonCollectors.toJsonObject());
    }

    public JsonValue updateJsonValue(Field field, FieldDefinition fieldDefinition, JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return jsonValue;
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            if (fieldDefinition.getType().hasList()) {
                return jsonValue.asJsonArray().stream()
                        .map(value -> updateJsonObject(field, fieldDefinition, value.asJsonObject()))
                        .collect(JsonCollectors.toJsonArray());
            } else {
                return updateJsonObject(field, fieldDefinition, jsonValue.asJsonObject());
            }
        } else {
            return jsonValue;
        }
    }
}
