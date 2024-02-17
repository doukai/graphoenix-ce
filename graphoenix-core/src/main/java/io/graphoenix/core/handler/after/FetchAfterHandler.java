package io.graphoenix.core.handler.after;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.core.handler.fetch.FetchItem;
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
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
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
                .fromIterable(
                        operation.getFields().stream()
                                .flatMap(field -> {
                                            String selectionName = Optional.ofNullable(field.getAlias()).orElse(field.getName());
                                            return buildFetchItems(operationType, "/" + selectionName, operationType.getField(field.getName()), field, jsonValue);
                                        }
                                )
                                .collect(
                                        Collectors.groupingBy(
                                                FetchItem::getPackageName,
                                                Collectors.mapping(
                                                        fetchItem -> fetchItem,
                                                        Collectors.groupingBy(
                                                                FetchItem::getProtocol,
                                                                Collectors.mapping(
                                                                        fetchItem -> fetchItem,
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
                                                                                .map(FetchItem::getFetchField)
                                                                                .filter(Objects::nonNull)
                                                                                .collect(Collectors.toList())
                                                                )
                                                )
                                                .flatMapMany(fetchJsonValue ->
                                                        Flux.fromStream(
                                                                protocolEntries.getValue().stream()
                                                                        .map(fetchItem -> {
                                                                                    String path = fetchItem.getPath();
                                                                                    if (fetchItem.getFetchField() == null) {
                                                                                        return jsonProvider.createObjectBuilder()
                                                                                                .add("op", "add")
                                                                                                .add("path", path)
                                                                                                .add("value", NULL)
                                                                                                .build();
                                                                                    } else if (fetchItem.getTarget() == null) {
                                                                                        JsonValue fieldJsonValue = fetchJsonValue.asJsonObject().get(fetchItem.getFetchField().getAlias());
                                                                                        return jsonProvider.createObjectBuilder()
                                                                                                .add("op", "add")
                                                                                                .add("path", path)
                                                                                                .add("value", fieldJsonValue)
                                                                                                .build();
                                                                                    } else {
                                                                                        JsonValue fieldJsonValue = fetchJsonValue.asJsonObject().get(fetchItem.getFetchField().getAlias());
                                                                                        if (fieldJsonValue.getValueType().equals(JsonValue.ValueType.ARRAY)) {
                                                                                            return jsonProvider.createObjectBuilder()
                                                                                                    .add("op", "add")
                                                                                                    .add("path", path)
                                                                                                    .add("value",
                                                                                                            fieldJsonValue.asJsonArray().stream()
                                                                                                                    .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                                                                                    .map(item -> item.asJsonObject().get(fetchItem.getTarget()))
                                                                                                                    .collect(JsonCollectors.toJsonArray())
                                                                                                    )
                                                                                                    .build();
                                                                                        } else if (fieldJsonValue.getValueType().equals(JsonValue.ValueType.OBJECT)) {
                                                                                            return jsonProvider.createObjectBuilder()
                                                                                                    .add("op", "add")
                                                                                                    .add("path", path)
                                                                                                    .add("value", fieldJsonValue.asJsonObject().get(fetchItem.getTarget()))
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
                )
                .collectList()
                .map(patchList ->
                        jsonProvider.createPatchBuilder(patchList.stream().collect(JsonCollectors.toJsonArray()))
                                .build()
                                .apply(jsonValue.asJsonObject())
                );
    }

    public Stream<FetchItem> buildFetchItems(ObjectType objectType, String path, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (documentManager.isOperationType(objectType) && !packageManager.isLocalPackage(fieldDefinition)) {
            String packageName = fieldDefinition.getPackageNameOrError();
            String protocol = fieldDefinition.getFetchProtocolOrError();
            return Stream.of(new FetchItem(packageName, protocol, path, field.setAlias(getAliasFromPath(path)), null));
        } else if (fieldDefinition.isFetchField()) {
            String fetchFrom = fieldDefinition.getFetchFromOrError();
            String protocol = fieldDefinition.getFetchProtocolOrError();
            Field fetchField = new Field();
            if (fieldDefinition.hasFetchWith()) {
                ObjectType fetchWithType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithTypeOrError());
                String packageName = fetchWithType.getPackageNameOrError();
                if (jsonValue.asJsonObject().isNull(fetchFrom)) {
                    return Stream.of(new FetchItem(packageName, protocol, path, null, null));
                }
                String fetchWithFrom = fieldDefinition.getFetchWithFromOrError();
                String fetchWithTo = fieldDefinition.getFetchWithToOrError();
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

                return Stream.of(new FetchItem(packageName, protocol, path, fetchField, target));
            } else {
                String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                if (jsonValue.asJsonObject().isNull(fetchFrom)) {
                    return Stream.of(new FetchItem(packageName, protocol, path, null, null));
                }
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
                        .setName(typeNameToFieldName(fieldTypeDefinition.getName()) + (fieldDefinition.getType().hasList() ? SUFFIX_LIST : ""));

                return Stream.of(new FetchItem(packageName, protocol, path, fetchField, null));
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
                                                    return buildFetchItems(fieldTypeDefinition.asObject(), path + "/" + index + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonObject().get(selectionName).asJsonArray().get(index));
                                                }
                                        )
                        )
                        .flatMap(stream -> stream);
            } else {
                return Stream.ofNullable(field.getFields())
                        .flatMap(Collection::stream)
                        .flatMap(subField -> {
                                    String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                    return buildFetchItems(fieldTypeDefinition.asObject(), path + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonObject().get(selectionName));
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
}
