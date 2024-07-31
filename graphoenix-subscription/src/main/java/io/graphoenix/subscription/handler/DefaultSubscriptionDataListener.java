package io.graphoenix.subscription.handler;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.JakartaJsonProvider;
import com.jayway.jsonpath.spi.mapper.JakartaMappingProvider;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.jsonpath.translator.ArgumentsToFilter;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.SubscriptionDataListener;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonValue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.INPUT_OPERATOR_INPUT_VALUE_VAL_NAME;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_WHERE_NAME;

@Dependent
public class DefaultSubscriptionDataListener implements SubscriptionDataListener {

    private final DocumentManager documentManager;

    private final ArgumentsToFilter argumentsToFilter;

    private final ParseContext parseContext;

    private final Map<String, Set<String>> typeIDMap = new HashMap<>();

    private final Map<String, Set<String>> typeFilterMap = new HashMap<>();

    @Inject
    public DefaultSubscriptionDataListener(DocumentManager documentManager, ArgumentsToFilter argumentsToFilter) {
        this.documentManager = documentManager;
        this.argumentsToFilter = argumentsToFilter;
        this.parseContext = JsonPath.using(
                Configuration.builder()
                        .jsonProvider(new JakartaJsonProvider())
                        .mappingProvider(new JakartaMappingProvider())
                        .options(EnumSet.noneOf(Option.class))
                        .build()
        );
    }

    @Override
    public SubscriptionDataListener beforeSubscription(Operation operation) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        this.typeFilterMap
                .putAll(
                        operation.getFields().stream()
                                .flatMap(field -> {
                                            FieldDefinition fieldDefinition = operationType.getField(field.getName());
                                            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
                                            if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
                                                return argumentsToFilter.argumentsToMultipleFilter(fieldDefinition, field).stream()
                                                        .map(filter ->
                                                                new AbstractMap.SimpleEntry<>(
                                                                        fieldTypeDefinition.getName(),
                                                                        filter.toString()
                                                                )
                                                        );
                                            }
                                            return Stream.empty();
                                        }
                                )
                                .collect(
                                        Collectors.groupingBy(
                                                Map.Entry::getKey,
                                                Collectors.mapping(
                                                        Map.Entry::getValue,
                                                        Collectors.toSet()
                                                )
                                        )
                                )
                );
        return this;
    }

    @Override
    public SubscriptionDataListener afterSubscription(Operation operation, JsonValue jsonValue) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        this.typeIDMap
                .putAll(
                        operation.getFields().stream()
                                .flatMap(field -> indexID(operationType.getField(field.getName()), jsonValue.asJsonObject().get(Optional.ofNullable(field.getAlias()).orElseGet(field::getName))))
                                .collect(
                                        Collectors.groupingBy(
                                                Map.Entry::getKey,
                                                Collectors.mapping(
                                                        Map.Entry::getValue,
                                                        Collectors.toSet()
                                                )
                                        )
                                )
                );
        return this;
    }

    private Stream<Map.Entry<String, String>> indexID(FieldDefinition fieldDefinition, JsonValue jsonValue) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return Stream.empty();
        } else if (jsonValue.getValueType().equals(JsonValue.ValueType.ARRAY)) {
            return jsonValue.asJsonArray().stream()
                    .flatMap(item -> indexID(fieldDefinition, item));
        } else {
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
                                                            .flatMap(item -> indexID(subFieldDefinition, item));
                                                } else if (entry.getValue().getValueType().equals(JsonValue.ValueType.OBJECT)) {
                                                    return indexID(subFieldDefinition, entry.getValue());
                                                } else {
                                                    return Stream.empty();
                                                }
                                            }
                                    )
                    );
        }
    }

    @Override
    public boolean changed(String typeName, JsonArray mutations) {
        return idChanged(typeName, mutations) || mutationChanged(typeName, mutations);
    }

    public boolean idChanged(String typeName, JsonArray mutations) {
        return mutations.stream().anyMatch(mutation -> idChanged(typeName, mutation));
    }

    private boolean idChanged(String typeName, JsonValue mutation) {
        ObjectType objectType = documentManager.getDocument().getObjectTypeOrError(typeName);
        return objectType.getIDField()
                .map(idFieldDefinition ->
                        typeIDMap.containsKey(typeName) &&
                                (mutation.asJsonObject().containsKey(idFieldDefinition.getName()) &&
                                        !mutation.asJsonObject().isNull(idFieldDefinition.getName()) &&
                                        typeIDMap.get(typeName).contains(mutation.asJsonObject().getString(idFieldDefinition.getName())) ||
                                        mutation.asJsonObject().containsKey(INPUT_VALUE_WHERE_NAME) &&
                                                !mutation.asJsonObject().isNull(INPUT_VALUE_WHERE_NAME) &&
                                                mutation.asJsonObject().getJsonObject(INPUT_VALUE_WHERE_NAME).containsKey(idFieldDefinition.getName()) &&
                                                !mutation.asJsonObject().getJsonObject(INPUT_VALUE_WHERE_NAME).isNull(idFieldDefinition.getName()) &&
                                                mutation.asJsonObject().getJsonObject(INPUT_VALUE_WHERE_NAME).getJsonObject(idFieldDefinition.getName()).containsKey(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME) &&
                                                !mutation.asJsonObject().getJsonObject(INPUT_VALUE_WHERE_NAME).getJsonObject(idFieldDefinition.getName()).isNull(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME) &&
                                                typeIDMap.get(typeName).contains(mutation.asJsonObject().getJsonObject(INPUT_VALUE_WHERE_NAME).getJsonObject(idFieldDefinition.getName()).getString(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
                                )
                )
                .orElseGet(() ->
                        mutation.asJsonObject().entrySet().stream()
                                .filter(entry -> !entry.getKey().equals(INPUT_VALUE_WHERE_NAME))
                                .anyMatch(entry -> {
                                            FieldDefinition fieldDefinition = objectType.getField(entry.getKey());
                                            if (entry.getValue().getValueType().equals(JsonValue.ValueType.ARRAY)) {
                                                return entry.getValue().asJsonArray().stream()
                                                        .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                        .anyMatch(item -> idChanged(documentManager.getFieldTypeDefinition(fieldDefinition).getName(), item));
                                            } else if (entry.getValue().getValueType().equals(JsonValue.ValueType.OBJECT)) {
                                                return idChanged(documentManager.getFieldTypeDefinition(fieldDefinition).getName(), entry.getValue());
                                            } else {
                                                return false;
                                            }
                                        }
                                )
                );
    }

    @SuppressWarnings("unchecked")
    private boolean mutationChanged(String typeName, JsonArray mutations) {
        return Stream.ofNullable(typeFilterMap.get(typeName))
                .flatMap(Collection::stream)
                .map(filter -> parseContext.parse(mutations).read(filter))
                .map(jsonValues -> (List<JsonValue>) jsonValues)
                .anyMatch(jsonValues -> !jsonValues.isEmpty());
    }
}
