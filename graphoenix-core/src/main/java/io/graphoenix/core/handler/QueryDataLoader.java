package io.graphoenix.core.handler;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.json.JsonPointer;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.spi.JsonProvider;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.OPERATION_QUERY_NAME;

@Dependent
public class QueryDataLoader {

    private final JsonProvider jsonProvider;

    private final Jsonb jsonb;

    private final Map<String, Map<String, Map<String, Map<String, Map<String, Map<JsonValue.ValueType, Map<String, Field>>>>>>> fetchMap = new HashMap<>();

    private final Map<String, Map<String, Map<String, Field>>> operationFetchMap = new HashMap<>();

    private final Map<String, Map<String, Map<String, Map<String, Field>>>> fieldMap = new HashMap<>();

    private final Map<String, List<String>> removeFiledMap = new HashMap<>();

    private final Map<String, List<Map<String, String>>> addFiledMap = new HashMap<>();

    private final Map<String, Map<String, JsonValue>> resultMap = new HashMap<>();

    @Inject
    public QueryDataLoader(JsonProvider jsonProvider, Jsonb jsonb) {
        this.jsonProvider = jsonProvider;
        this.jsonb = jsonb;
    }

    public void register(String packageName, String protocol, String typeName, String fieldName, JsonValue keyValue, String jsonPointer, Field field) {
        register(packageName, protocol, typeName, fieldName, keyValue, JsonValue.ValueType.OBJECT, jsonPointer, field);
    }

    public void registerArray(String packageName, String protocol, String typeName, String fieldName, JsonValue keyValue, JsonValue.ValueType valueType, String jsonPointer, Field field) {
        register(packageName, protocol, typeName, fieldName, keyValue, JsonValue.ValueType.ARRAY, jsonPointer, field);
    }

    public void register(String packageName, String protocol, String typeName, String fieldName, JsonValue keyValue, JsonValue.ValueType valueType, String jsonPointer, Field field) {
        mergeSelection(packageName, protocol, typeName, fieldName);
        mergeSelection(packageName, protocol, typeName, fieldName, field.getFields());
        addCondition(packageName, protocol, typeName, fieldName, getKeyValue(keyValue), valueType, jsonPointer, field);
    }

    private void mergeSelection(String packageName, String protocol, String typeName, String fieldName) {
        mergeSelection(packageName, protocol, typeName, fieldName, Set.of(new Field(fieldName)));
    }

    private void mergeSelection(String packageName, String protocol, String typeName, String fieldName, Collection<Field> fieldSet) {
        fieldMap.computeIfAbsent(packageName, k -> new HashMap<>());
        fieldMap.get(packageName).computeIfAbsent(protocol, k -> new HashMap<>());
        fieldMap.get(packageName).get(protocol).computeIfAbsent(typeName, k -> new HashMap<>());
        fieldMap.get(packageName).get(protocol).get(typeName).get(fieldName).mergeSelection(fieldSet);
    }

    private void addCondition(String packageName, String protocol, String typeName, String fieldName, String key, JsonValue.ValueType valueType, String jsonPointer, Field field) {
        fetchMap.computeIfAbsent(packageName, k -> new HashMap<>());
        fetchMap.get(packageName).computeIfAbsent(protocol, k -> new HashMap<>());
        fetchMap.get(packageName).get(protocol).computeIfAbsent(typeName, k -> new HashMap<>());
        fetchMap.get(packageName).get(protocol).get(typeName).computeIfAbsent(fieldName, k -> new HashMap<>());
        fetchMap.get(packageName).get(protocol).get(typeName).get(fieldName).computeIfAbsent(key, k -> new HashMap<>());
        fetchMap.get(packageName).get(protocol).get(typeName).get(fieldName).get(key).computeIfAbsent(valueType, k -> new HashMap<>());
        fetchMap.get(packageName).get(protocol).get(typeName).get(fieldName).get(key).get(valueType).put(jsonPointer, field);
    }

    public void registerOperation(String packageName, String protocol, String jsonPointer, Field field) {
        operationFetchMap.computeIfAbsent(packageName, k -> new ConcurrentHashMap<>());
        operationFetchMap.get(packageName).computeIfAbsent(protocol, k -> new HashMap<>());
        operationFetchMap.get(packageName).get(protocol).put(jsonPointer, field);
    }

    private String getKeyValue(JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.STRING)) {
            return ((JsonString) jsonValue).getString();
        } else {
            return jsonValue.toString();
        }
    }

    protected Mono<Operation> build(String packageName, String protocol) {
        Optional.ofNullable(fetchMap.get(packageName))
                .flatMap(protocolMap ->
                        Optional.ofNullable(protocolMap.get(protocol))
                                .flatMap(typeMap ->
                                        new Operation(OPERATION_QUERY_NAME)
                                                .addSelections(
                                                        Stream.ofNullable(operationFetchMap.get(packageName))
                                                                .flatMap(packageMap -> Stream.ofNullable(packageMap.get(protocol)))
                                                                .flatMap(pathMap -> pathMap.values().stream())
                                                                .collect(Collectors.toSet())
                                                )
                                                .addSelections(

                                                )
                                )
                )
    }
}
