package io.graphoenix.core.handler;

import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonPointer;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import jakarta.transaction.TransactionScoped;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@TransactionScoped
public class TransactionCompensator {

    private final DocumentManager documentManager;

    private final JsonProvider jsonProvider;

    private final Map<String, List<JsonValue>> typeValueListMap = new HashMap<>();

    private final Map<String, List<FetchItem>> newTypeFetchItemListMap = new HashMap<>();

    @Inject
    public TransactionCompensator(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    public void addNewTypePath(String typeName, FetchItem fetchItem) {
        List<FetchItem> fetchItemList = newTypeFetchItemListMap.computeIfAbsent(typeName, k -> new ArrayList<>());
        fetchItemList.add(fetchItem);
    }

    public void addTypeValue(String typeName, JsonValue jsonValue) {
        List<JsonValue> valueList = typeValueListMap.computeIfAbsent(typeName, k -> new ArrayList<>());
        valueList.add(jsonValue);
    }

    public TransactionCompensator addTypeFetchItemListMap(Map<String, List<FetchItem>> newTypeFetchItemListMap) {
        this.newTypeFetchItemListMap.putAll(newTypeFetchItemListMap);
        return this;
    }

    public TransactionCompensator addTypeValueListMap(Map<String, List<JsonValue>> typeValueListMap) {
        this.typeValueListMap.putAll(typeValueListMap);
        return this;
    }

    public Optional<Operation> compensating(JsonObject jsonObject) {
        if (typeValueListMap.isEmpty() && newTypeFetchItemListMap.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                new Operation()
                        .setOperationType(OPERATION_MUTATION_NAME)
                        .setSelections(
                                Stream
                                        .concat(
                                                typeValueListMap.entrySet().stream()
                                                        .map(entry -> {
                                                                    String id = documentManager.getDocument().getObjectTypeOrError(entry.getKey()).getIDFieldOrError().getName();
                                                                    return new Field(typeNameToFieldName(entry.getKey()) + SUFFIX_LIST)
                                                                            .addSelection(new Field(id))
                                                                            .addArgument(
                                                                                    INPUT_VALUE_LIST_NAME,
                                                                                    entry.getValue().stream()
                                                                                            .filter(jsonValue -> !jsonValue.getValueType().equals(JsonValue.ValueType.NULL))
                                                                                            .flatMap(jsonValue ->
                                                                                                    jsonValue.getValueType().equals(JsonValue.ValueType.ARRAY) ?
                                                                                                            jsonValue.asJsonArray().stream()
                                                                                                                    .map(JsonValue::asJsonObject) :
                                                                                                            Stream.of(jsonValue.asJsonObject())
                                                                                            )
                                                                                            .map(item ->
                                                                                                    Stream
                                                                                                            .concat(
                                                                                                                    item.entrySet().stream()
                                                                                                                            .filter(fieldEntry -> !fieldEntry.getKey().equals(id)),
                                                                                                                    Stream.of(
                                                                                                                            new AbstractMap.SimpleEntry<>(
                                                                                                                                    INPUT_VALUE_WHERE_NAME,
                                                                                                                                    (JsonValue) jsonProvider.createObjectBuilder()
                                                                                                                                            .add(
                                                                                                                                                    id,
                                                                                                                                                    jsonProvider.createObjectBuilder()
                                                                                                                                                            .add(
                                                                                                                                                                    INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                                                                                                                                    item.get(id)
                                                                                                                                                            )
                                                                                                                                            )
                                                                                                                                            .build()
                                                                                                                            )
                                                                                                                    )
                                                                                                            )
                                                                                                            .collect(JsonCollectors.toJsonObject())
                                                                                            )
                                                                                            .collect(JsonCollectors.toJsonArray())
                                                                            );
                                                                }
                                                        ),
                                                newTypeFetchItemListMap.entrySet().stream()
                                                        .flatMap(entry -> {
                                                                    String id = documentManager.getDocument().getObjectTypeOrError(entry.getKey()).getIDFieldOrError().getName();
                                                                    return entry.getValue().stream()
                                                                            .flatMap(fetchItem -> {
                                                                                        JsonPointer pointer = jsonProvider.createPointer(fetchItem.getPath());
                                                                                        JsonValue fieldJsonValue = jsonObject.get(Optional.ofNullable(fetchItem.getField().getAlias()).orElseGet(fetchItem.getField()::getName));
                                                                                        if (pointer.containsValue((JsonStructure) fieldJsonValue)) {
                                                                                            JsonValue idValue = pointer.getValue((JsonStructure) fieldJsonValue);
                                                                                            Field field = new Field(typeNameToFieldName(entry.getKey()) + SUFFIX_LIST)
                                                                                                    .addSelection(new Field(id));
                                                                                            if (fetchItem.getTarget() != null) {
                                                                                                field.setArguments(
                                                                                                        jsonProvider.createObjectBuilder()
                                                                                                                .add(
                                                                                                                        INPUT_VALUE_WHERE_NAME,
                                                                                                                        jsonProvider.createObjectBuilder()
                                                                                                                                .add(
                                                                                                                                        fetchItem.getTarget(),
                                                                                                                                        jsonProvider.createObjectBuilder()
                                                                                                                                                .add(
                                                                                                                                                        id,
                                                                                                                                                        jsonProvider.createObjectBuilder()
                                                                                                                                                                .add(
                                                                                                                                                                        INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                                                                                                                                        idValue
                                                                                                                                                                )
                                                                                                                                                )
                                                                                                                                )
                                                                                                                )
                                                                                                                .add(
                                                                                                                        FIELD_DEPRECATED_NAME,
                                                                                                                        true
                                                                                                                )
                                                                                                                .build()
                                                                                                );
                                                                                            } else {
                                                                                                field.setArguments(
                                                                                                        jsonProvider.createObjectBuilder()
                                                                                                                .add(
                                                                                                                        INPUT_VALUE_WHERE_NAME,
                                                                                                                        jsonProvider.createObjectBuilder()
                                                                                                                                .add(
                                                                                                                                        id,
                                                                                                                                        jsonProvider.createObjectBuilder()
                                                                                                                                                .add(
                                                                                                                                                        INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                                                                                                                        idValue
                                                                                                                                                )
                                                                                                                                )
                                                                                                                )
                                                                                                                .add(
                                                                                                                        FIELD_DEPRECATED_NAME,
                                                                                                                        true
                                                                                                                )
                                                                                                                .build()
                                                                                                );
                                                                                            }
                                                                                            return Stream.of(field);
                                                                                        }
                                                                                        return Stream.empty();
                                                                                    }
                                                                            );
                                                                }
                                                        )
                                        )
                                        .collect(Collectors.toList())
                        )
        );
    }
}
