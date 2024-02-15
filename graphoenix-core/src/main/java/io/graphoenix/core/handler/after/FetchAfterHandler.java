package io.graphoenix.core.handler.after;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchHandler;
import io.graphoenix.spi.handler.OperationAfterHandler;
import io.nozdormu.spi.context.BeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;
import static jakarta.json.JsonValue.NULL;

@ApplicationScoped
@Priority(Integer.MAX_VALUE - 400)
public class FetchAfterHandler implements OperationAfterHandler {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final JsonProvider jsonProvider;
    private final Map<String, FetchHandler> fetchHandlerMap = BeanContext.getMap(FetchHandler.class);

    @Inject
    public FetchAfterHandler(DocumentManager documentManager, PackageManager packageManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<JsonValue> query(Operation operation, JsonValue jsonValue) {
        return handle(operation, jsonValue);
    }

    @Override
    public Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue) {
        return handle(operation, jsonValue);
    }

    @Override
    public Mono<JsonValue> subscription(Operation operation, JsonValue jsonValue) {
        return handle(operation, jsonValue);
    }

    public Mono<JsonValue> handle(Operation operation, JsonValue jsonValue) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .concat(
                        Flux
                                .fromIterable(
                                        operation.getFields().stream()
                                                .flatMap(field -> {
                                                            String selectionName = Optional.ofNullable(field.getAlias()).orElse(field.getName());
                                                            return buildFetchFields(operationType, "/" + selectionName, operationType.getField(field.getName()), field, jsonValue);
                                                        }
                                                )
                                                .collect(
                                                        Collectors.groupingBy(
                                                                Tuple4::getT1,
                                                                Collectors.mapping(
                                                                        tuple4 -> Tuples.of(tuple4.getT2(), tuple4.getT3(), tuple4.getT4()),
                                                                        Collectors.groupingBy(
                                                                                Tuple3::getT1,
                                                                                Collectors.mapping(
                                                                                        tuple3 -> Tuples.of(tuple3.getT2(), tuple3.getT3()),
                                                                                        Collectors.toList()
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                                .entrySet()
                                )
                                .flatMap(packageEntries ->
                                        Flux.fromIterable(packageEntries.getValue().entrySet())
                                                .flatMap(protocolEntries ->
                                                        fetchHandlerMap.get(protocolEntries.getKey())
                                                                .request(
                                                                        packageEntries.getKey(),
                                                                        new Operation()
                                                                                .setSelections(
                                                                                        protocolEntries.getValue().stream()
                                                                                                .map(Tuple2::getT1)
                                                                                                .collect(Collectors.toList())
                                                                                )
                                                                )
                                                                .flatMapMany(fetchJsonValue ->
                                                                        Flux.fromStream(
                                                                                protocolEntries.getValue().stream()
                                                                                        .map(tuple2 -> {
                                                                                                    String path = getPathFromAlias(tuple2.getT1().getAlias());
                                                                                                    JsonValue fieldJsonValue = fetchJsonValue.asJsonObject().get(tuple2.getT1().getAlias());
                                                                                                    if (tuple2.getT2().isBlank()) {
                                                                                                        return jsonProvider.createObjectBuilder()
                                                                                                                .add("op", "add")
                                                                                                                .add("path", path)
                                                                                                                .add("value", fieldJsonValue)
                                                                                                                .build();
                                                                                                    } else {
                                                                                                        if (fieldJsonValue.getValueType().equals(JsonValue.ValueType.ARRAY)) {
                                                                                                            return jsonProvider.createObjectBuilder()
                                                                                                                    .add("op", "add")
                                                                                                                    .add("path", path)
                                                                                                                    .add("value",
                                                                                                                            fieldJsonValue.asJsonArray().stream()
                                                                                                                                    .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                                                                                                    .map(item -> item.asJsonObject().get(tuple2.getT2()))
                                                                                                                                    .collect(JsonCollectors.toJsonArray())
                                                                                                                    )
                                                                                                                    .build();
                                                                                                        } else if (fieldJsonValue.getValueType().equals(JsonValue.ValueType.OBJECT)) {
                                                                                                            return jsonProvider.createObjectBuilder()
                                                                                                                    .add("op", "add")
                                                                                                                    .add("path", path)
                                                                                                                    .add("value", fieldJsonValue.asJsonObject().get(tuple2.getT2()))
                                                                                                                    .build();
                                                                                                        } else {
                                                                                                            return jsonProvider.createObjectBuilder()
                                                                                                                    .add("op", "add")
                                                                                                                    .add("path", path)
                                                                                                                    .add("value", fieldJsonValue)
                                                                                                                    .build();
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                        )
                                                                        )
                                                                )
                                                )
                                ),
                        Flux.fromStream(
                                operation.getFields().stream()
                                        .flatMap(field -> {
                                                    String selectionName = Optional.ofNullable(field.getAlias()).orElse(field.getName());
                                                    return buildNullFetchFields("/" + selectionName, operationType.getField(field.getName()), field, jsonValue);
                                                }
                                        )
                        )
                )
                .collectList()
                .map(patchList ->
                        jsonProvider.createPatchBuilder(patchList.stream().collect(JsonCollectors.toJsonArray()))
                                .build()
                                .apply(jsonValue.asJsonObject())
                );
    }

    public Stream<Tuple4<String, String, Field, String>> buildFetchFields(ObjectType objectType, String path, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (documentManager.isOperationType(objectType) && !packageManager.isLocalPackage(fieldDefinition)) {
            String packageName = fieldDefinition.getPackageNameOrError();
            String protocol = fieldDefinition.getFetchProtocolOrError();
            return Stream.of(Tuples.of(packageName, protocol, field.setAlias(getAliasFromPath(path)), ""));
        } else if (fieldDefinition.isFetchField()) {
            String fetchFrom = fieldDefinition.getFetchFromOrError();
            if (jsonValue.asJsonObject().isNull(fetchFrom)) {
                return Stream.empty();
            }
            String protocol = fieldDefinition.getFetchProtocolOrError();
            Field fetchField = new Field();
            if (fieldDefinition.hasFetchWith()) {
                ObjectType fetchWithType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithTypeOrError());
                String fetchWithFrom = fieldDefinition.getFetchWithFromOrError();
                String fetchWithTo = fieldDefinition.getFetchWithToOrError();
                String packageName = fetchWithType.getPackageNameOrError();

                fetchField
                        .setAlias(getAliasFromPath(path))
                        .setArguments(
                                Map.of(
                                        fetchWithFrom,
                                        Map.of(
                                                INPUT_OPERATOR_INPUT_VALUE_OPR_NAME,
                                                new EnumValue(INPUT_OPERATOR_INPUT_VALUE_EQ),
                                                INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                getKey(jsonValue.asJsonObject().get(fetchFrom))
                                        )
                                )
                        )
                        .addSelection(
                                fieldDefinition.getFetchTo()
                                        .flatMap(fetchTo ->
                                                fetchWithType.getFields().stream()
                                                        .filter(withTypeFieldDefinition -> withTypeFieldDefinition.getFetchFrom().isPresent())
                                                        .filter(withTypeFieldDefinition -> withTypeFieldDefinition.getFetchFromOrError().equals(fetchWithTo))
                                                        .findFirst()
                                                        .map(withTypeFieldDefinition ->
                                                                new Field(withTypeFieldDefinition.getName())
                                                                        .setSelections(field.getSelections())
                                                        )
                                        )
                                        .orElseGet(() -> new Field(fetchWithTo))
                                        .setArguments(
                                                fieldDefinition.getArguments().stream()
                                                        .flatMap(inputValue ->
                                                                field.getArguments().getArgument(inputValue.getName())
                                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                        .map(valueWithVariable -> new AbstractMap.SimpleEntry<>(inputValue.getName(), valueWithVariable))
                                                        )
                                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y))
                                        )
                        )
                        .setName(typeNameToFieldName(fetchWithType.getName()) + (fieldDefinition.getType().hasList() ? SUFFIX_LIST : ""));

                String target = fieldDefinition.getFetchTo()
                        .flatMap(fetchTo ->
                                fetchWithType.getFields().stream()
                                        .filter(withTypeFieldDefinition -> withTypeFieldDefinition.getFetchFrom().isPresent())
                                        .filter(withTypeFieldDefinition -> withTypeFieldDefinition.getFetchFromOrError().equals(fetchWithTo))
                                        .findFirst()
                                        .map(AbstractDefinition::getName)
                        )
                        .orElse(fetchWithTo);

                return Stream.of(Tuples.of(packageName, protocol, fetchField, target));
            } else {
                String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                String fetchTo = fieldDefinition.getFetchToOrError();
                fetchField
                        .setAlias(getAliasFromPath(path))
                        .setArguments(
                                Stream
                                        .concat(
                                                Stream.of(
                                                        new AbstractMap.SimpleEntry<>(
                                                                fetchTo,
                                                                Map.of(
                                                                        INPUT_OPERATOR_INPUT_VALUE_OPR_NAME,
                                                                        new EnumValue(INPUT_OPERATOR_INPUT_VALUE_EQ),
                                                                        INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                                        getKey(jsonValue.asJsonObject().get(fetchFrom))
                                                                )
                                                        )
                                                ),
                                                fieldDefinition.getArguments().stream()
                                                        .flatMap(inputValue ->
                                                                field.getArguments().getArgument(inputValue.getName())
                                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                        .map(valueWithVariable -> new AbstractMap.SimpleEntry<>(inputValue.getName(), valueWithVariable))
                                                        )
                                        )
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y))
                        )
                        .setSelections(field.getSelections())
                        .setName(typeNameToFieldName(objectType.getName()) + (fieldDefinition.getType().hasList() ? SUFFIX_LIST : ""));

                return Stream.of(Tuples.of(packageName, protocol, fetchField, ""));
            }
        } else if (fieldTypeDefinition.isObject()) {
            String selectionName = Optional.ofNullable(field.getAlias()).orElse(field.getName());
            if (fieldDefinition.getType().hasList()) {
                return IntStream.range(0, jsonValue.asJsonObject().get(selectionName).asJsonArray().size())
                        .mapToObj(index ->
                                Stream.ofNullable(field.getFields())
                                        .flatMap(Collection::stream)
                                        .flatMap(subField -> {
                                                    String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                                    return buildFetchFields(fieldTypeDefinition.asObject(), path + "/" + index + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonObject().get(selectionName).asJsonArray().get(index));
                                                }
                                        )
                        )
                        .flatMap(stream -> stream);
            } else {
                return Stream.ofNullable(field.getFields())
                        .flatMap(Collection::stream)
                        .flatMap(subField -> {
                                    String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                    return buildFetchFields(fieldTypeDefinition.asObject(), path + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonObject().get(selectionName));
                                }
                        );
            }
        }
        return Stream.empty();
    }

    public Stream<JsonObject> buildNullFetchFields(String path, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isFetchField()) {
            String fetchFrom = fieldDefinition.getFetchFromOrError();
            if (jsonValue.asJsonObject().isNull(fetchFrom)) {
                return Stream.of(
                        jsonProvider.createObjectBuilder()
                                .add("op", "add")
                                .add("path", path)
                                .add("value", NULL)
                                .build()
                );
            }
        } else if (fieldTypeDefinition.isObject()) {
            String selectionName = Optional.ofNullable(field.getAlias()).orElse(field.getName());
            if (fieldDefinition.getType().hasList()) {
                return IntStream.range(0, jsonValue.asJsonObject().get(selectionName).asJsonArray().size())
                        .mapToObj(index ->
                                Stream.ofNullable(field.getFields())
                                        .flatMap(Collection::stream)
                                        .flatMap(subField -> {
                                                    String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                                    return buildNullFetchFields(path + "/" + index + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonObject().get(selectionName).asJsonArray().get(index));
                                                }
                                        )
                        )
                        .flatMap(stream -> stream);
            } else {
                return Stream.ofNullable(field.getFields())
                        .flatMap(Collection::stream)
                        .flatMap(subField -> {
                                    String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                    return buildNullFetchFields(path + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonObject().get(selectionName));
                                }
                        );
            }
        }
        return Stream.empty();
    }

    private String getKey(JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.STRING)) {
            return ((JsonString) jsonValue).getString();
        } else {
            return jsonValue.toString();
        }
    }

    private String getAliasFromPath(String path) {
        return path.replaceAll("/", "_");
    }

    private String getPathFromAlias(String alias) {
        return alias.replaceAll("_", "/");
    }
}
