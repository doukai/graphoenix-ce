package io.graphoenix.core.handler.after;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchBeforeHandler;
import io.graphoenix.spi.handler.OperationAfterHandler;
import io.graphoenix.spi.handler.PackageFetchHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
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

import static io.graphoenix.core.handler.after.ConnectionBuilder.CONNECTION_BUILDER_PRIORITY;
import static io.graphoenix.core.handler.fetch.LocalPackageFetchHandler.LOCAL_FETCH_NAME;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST;
import static io.graphoenix.spi.utils.NameUtil.getAliasFromPath;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;
import static io.nozdormu.spi.utils.CDIUtil.getNamedInstanceMap;
import static jakarta.json.JsonValue.NULL;

@ApplicationScoped
@Priority(QueryAfterFetchHandler.QUERY_AFTER_FETCH_HANDLER_PRIORITY)
public class QueryAfterFetchHandler implements OperationAfterHandler, FetchBeforeHandler {

    public static final int QUERY_AFTER_FETCH_HANDLER_PRIORITY = CONNECTION_BUILDER_PRIORITY - 100;

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final JsonProvider jsonProvider;
    private final Map<String, PackageFetchHandler> packageFetchHandlerMap;

    @Inject
    public QueryAfterFetchHandler(DocumentManager documentManager, PackageManager packageManager, JsonProvider jsonProvider, Instance<PackageFetchHandler> fetchHandlerInstance) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.jsonProvider = jsonProvider;
        this.packageFetchHandlerMap = getNamedInstanceMap(fetchHandlerInstance);
    }

    @Override
    public Mono<JsonValue> handle(Operation operation, JsonValue jsonValue) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .fromIterable(
                        operation.getFields().stream()
                                .flatMap(field -> {
                                            String selectionName = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
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
                                        packageFetchHandlerMap.get(protocolEntries.getKey())
                                                .request(
                                                        packageEntries.getKey(),
                                                        new Operation()
                                                                .setOperationType(OPERATION_QUERY_NAME)
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
                        (JsonValue) jsonProvider.createPatchBuilder(patchList.stream().collect(JsonCollectors.toJsonArray()))
                                .build()
                                .apply(jsonValue.asJsonObject())
                )
                .defaultIfEmpty(jsonValue);
    }

    public Stream<FetchItem> buildFetchItems(ObjectType objectType, String path, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (documentManager.isQueryOperationType(objectType) && !packageManager.isLocalPackage(fieldDefinition)) {
            String protocol = fieldDefinition.getFetchProtocol().orElse(new EnumValue(ENUM_PROTOCOL_ENUM_VALUE_GRPC)).getValue().toLowerCase();
            String packageName = fieldDefinition.getPackageNameOrError();
            return Stream.of(new FetchItem(packageName, protocol, path, field, null));
        } else if (fieldDefinition.isFetchField()) {
            String fetchFrom = fieldDefinition.getFetchFromOrError();
            Field fetchField = new Field();
            if (fieldDefinition.hasFetchWith()) {
                ObjectType fetchWithType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithTypeOrError());
                String packageName = fetchWithType.getPackageNameOrError();
                if (jsonValue.asJsonObject().isNull(fetchFrom)) {
                    return Stream.of(new FetchItem(packageName, LOCAL_FETCH_NAME, path));
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
                                                getId(jsonValue.asJsonObject().get(fetchFrom))
                                        )
                                )
                        )
                        .addSelection(
                                fieldDefinition.getFetchTo()
                                        .map(fetchTo ->
                                                fetchWithType.getFields().stream()
                                                        .filter(withTypeFieldDefinition ->
                                                                Stream
                                                                        .concat(
                                                                                withTypeFieldDefinition.getMapFrom().stream(),
                                                                                withTypeFieldDefinition.getFetchFrom().stream()
                                                                        )
                                                                        .anyMatch(name -> name.equals(fetchWithTo))
                                                        )
                                                        .findFirst()
                                                        .map(withTypeFieldDefinition ->
                                                                new Field(withTypeFieldDefinition.getName())
                                                                        .setSelections(field.getSelections())
                                                        )
                                                        .orElseThrow(() -> new GraphQLErrors(FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST.bind(fetchWithTo)))
                                        )
                                        .orElseGet(() -> new Field(fetchWithTo))
                                        .setArguments(
                                                fieldDefinition.getArguments().stream()
                                                        .flatMap(inputValue ->
                                                                Stream.ofNullable(field.getArguments())
                                                                        .flatMap(arguments ->
                                                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                        )
                                                                        .map(valueWithVariable -> new AbstractMap.SimpleEntry<>(inputValue.getName(), valueWithVariable))
                                                        )
                                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y))
                                        )
                        )
                        .setName(typeNameToFieldName(fetchWithType.getName()) + (fieldDefinition.getType().hasList() ? SUFFIX_LIST : ""));

                String target = fieldDefinition.getFetchTo()
                        .map(fetchTo ->
                                fetchWithType.getFields().stream()
                                        .filter(withTypeFieldDefinition ->
                                                Stream
                                                        .concat(
                                                                withTypeFieldDefinition.getMapFrom().stream(),
                                                                withTypeFieldDefinition.getFetchFrom().stream()
                                                        )
                                                        .anyMatch(name -> name.equals(fetchWithTo))
                                        )
                                        .findFirst()
                                        .map(AbstractDefinition::getName)
                                        .orElseThrow(() -> new GraphQLErrors(FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST.bind(fetchWithTo)))
                        )
                        .orElse(fetchWithTo);

                return Stream.of(new FetchItem(packageName, LOCAL_FETCH_NAME, path, fetchField, target));
            } else {
                String protocol = fieldDefinition.getFetchProtocolOrError().getValue().toLowerCase();
                String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                if (jsonValue.asJsonObject().isNull(fetchFrom)) {
                    return Stream.of(new FetchItem(packageName, protocol, path));
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
                                                                        getId(jsonValue.asJsonObject().get(fetchFrom))
                                                                )
                                                        )
                                                ),
                                                fieldDefinition.getArguments().stream()
                                                        .flatMap(inputValue ->
                                                                Stream.ofNullable(field.getArguments())
                                                                        .flatMap(arguments ->
                                                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                        )
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
            String selectionName = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
            if (!jsonValue.asJsonObject().containsKey(selectionName) || jsonValue.asJsonObject().isNull(selectionName)) {
                return Stream.empty();
            }
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

    private String getId(JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.STRING)) {
            return ((JsonString) jsonValue).getString();
        } else {
            return jsonValue.toString();
        }
    }
}
