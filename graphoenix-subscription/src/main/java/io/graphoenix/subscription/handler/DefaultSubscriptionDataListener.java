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

@Dependent
public class DefaultSubscriptionDataListener implements SubscriptionDataListener {

    private final DocumentManager documentManager;

    private final ArgumentsToFilter argumentsToFilter;

    private final ParseContext parseContext;

    private final Map<String, List<String>> typeIDMap = new HashMap<>();

    private final Map<String, List<String>> typeFilterMap = new HashMap<>();

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
                                                return argumentsToFilter.argumentsToMultipleExpression(fieldDefinition, field).stream()
                                                        .map(expression -> "$[?" + expression + "]")
                                                        .distinct()
                                                        .map(filter ->
                                                                new AbstractMap.SimpleEntry<>(
                                                                        fieldTypeDefinition.getName(),
                                                                        filter
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
                                                        Collectors.toList()
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
                                .map(field -> operationType.getField(field.getName()))
                                .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                .filter(fieldDefinition -> jsonValue.asJsonObject().containsKey(fieldDefinition.getName()))
                                .filter(fieldDefinition -> jsonValue.asJsonObject().get(fieldDefinition.getName()).getValueType().equals(JsonValue.ValueType.OBJECT))
                                .flatMap(fieldDefinition -> indexID(fieldDefinition, jsonValue.asJsonObject().get(fieldDefinition.getName())))
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

    private Stream<Map.Entry<String, String>> indexID(FieldDefinition fieldDefinition, JsonValue jsonValue) {
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
                                                        .flatMap(item -> indexID(subFieldDefinition, item));
                                            } else if (entry.getValue().getValueType().equals(JsonValue.ValueType.OBJECT)) {
                                                return indexID(subFieldDefinition, entry.getValue());
                                            } else {
                                                return Stream.empty();
                                            }
                                        }
                                )
                )
                .distinct();
    }

    @Override
    public boolean changed(String typeName, JsonArray arguments, JsonArray mutation) {
        return idChanged(typeName, arguments) || mutationChanged(typeName, mutation);
    }

    public boolean idChanged(String typeName, JsonArray arguments) {
        return arguments.asJsonArray().stream().anyMatch(argument -> idChanged(typeName, argument));
    }

    private boolean idChanged(String typeName, JsonValue argument) {
        ObjectType objectType = documentManager.getDocument().getObjectTypeOrError(typeName);
        return objectType.getIDField()
                .filter(fieldDefinition -> typeIDMap.containsKey(typeName))
                .filter(fieldDefinition -> argument.asJsonObject().containsKey(fieldDefinition.getName()))
                .filter(fieldDefinition -> !argument.asJsonObject().isNull(fieldDefinition.getName())).stream()
                .anyMatch(fieldDefinition -> typeIDMap.get(typeName).contains(argument.asJsonObject().getString(fieldDefinition.getName()))) ||

                argument.asJsonObject().entrySet().stream()
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
                        );
    }

    @SuppressWarnings("unchecked")
    private boolean mutationChanged(String typeName, JsonArray mutation) {
        return Stream.ofNullable(typeFilterMap.get(typeName))
                .flatMap(Collection::stream)
                .map(filter -> parseContext.parse(mutation).read(filter))
                .map(jsonValues -> (List<JsonValue>) jsonValues)
                .anyMatch(jsonValues -> !jsonValues.isEmpty());
    }
}
