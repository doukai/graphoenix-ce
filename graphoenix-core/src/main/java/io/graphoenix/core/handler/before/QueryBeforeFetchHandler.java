package io.graphoenix.core.handler.before;

import com.google.common.collect.Streams;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchHandler;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.nozdormu.spi.context.BeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST;
import static io.graphoenix.spi.utils.NameUtil.getAliasFromPath;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
@Priority(700)
public class QueryBeforeFetchHandler implements OperationBeforeHandler {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final Map<String, FetchHandler> fetchHandlerMap = BeanContext.getMap(FetchHandler.class);

    @Inject
    public QueryBeforeFetchHandler(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<Operation> query(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    @Override
    public Mono<Operation> subscription(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .fromIterable(
                        operation.getFields().stream()
                                .flatMap(field -> {
                                            String selectionName = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
                                            return buildFetchItems("/" + selectionName, operationType.getField(field.getName()), field);
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
                        Flux
                                .fromIterable(packageEntries.getValue().entrySet())
                                .flatMap(protocolEntries ->
                                        fetchHandlerMap.get(protocolEntries.getKey())
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
                                                        Flux
                                                                .fromIterable(
                                                                        protocolEntries.getValue().stream()
                                                                                .collect(
                                                                                        Collectors.groupingBy(
                                                                                                FetchItem::getField,
                                                                                                Collectors.mapping(
                                                                                                        fetchItem -> fetchItem,
                                                                                                        Collectors.groupingBy(
                                                                                                                FetchItem::getPath,
                                                                                                                Collectors.mapping(
                                                                                                                        fetchItem -> {
                                                                                                                            JsonValue fieldJsonValue = fetchJsonValue.asJsonObject().get(fetchItem.getFetchField().getAlias());
                                                                                                                            return jsonProvider.createObjectBuilder()
                                                                                                                                    .add(
                                                                                                                                            fetchItem.getFetchFrom(),
                                                                                                                                            jsonProvider.createObjectBuilder()
                                                                                                                                                    .add(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME, new EnumValue(INPUT_OPERATOR_INPUT_VALUE_IN))
                                                                                                                                                    .add(
                                                                                                                                                            INPUT_OPERATOR_INPUT_VALUE_ARR_NAME,
                                                                                                                                                            fieldJsonValue.asJsonArray().stream()
                                                                                                                                                                    .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                                                                                                                                    .map(item -> item.asJsonObject().get(fetchItem.getTarget()))
                                                                                                                                                                    .collect(JsonCollectors.toJsonArray())
                                                                                                                                                    )
                                                                                                                                    )
                                                                                                                                    .build();
                                                                                                                        },
                                                                                                                        Collectors.toList()
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                                .entrySet()
                                                                )
                                                                .map(entry ->
                                                                        entry.getKey()
                                                                                .setArguments(
                                                                                        (JsonObject) jsonProvider
                                                                                                .createPatchBuilder(
                                                                                                        entry.getValue().entrySet().stream()
                                                                                                                .map(pathEntry ->
                                                                                                                        jsonProvider.createObjectBuilder()
                                                                                                                                .add("op", "add")
                                                                                                                                .add("path", pathEntry.getKey() + "/" + INPUT_VALUE_EXS_NAME)
                                                                                                                                .add(
                                                                                                                                        "value",
                                                                                                                                        Stream.of(jsonProvider.createPointer(pathEntry.getKey() + "/" + INPUT_VALUE_EXS_NAME))
                                                                                                                                                .filter(jsonPointer -> jsonPointer.containsValue(entry.getKey().getArguments()))
                                                                                                                                                .map(jsonPointer -> jsonPointer.getValue(entry.getKey().getArguments()))
                                                                                                                                                .filter(jsonValue -> jsonValue.getValueType().equals(JsonValue.ValueType.ARRAY))
                                                                                                                                                .findFirst()
                                                                                                                                                .map(jsonValue ->
                                                                                                                                                        Stream
                                                                                                                                                                .concat(
                                                                                                                                                                        jsonValue.asJsonArray().stream(),
                                                                                                                                                                        pathEntry.getValue().stream()
                                                                                                                                                                )
                                                                                                                                                                .collect(JsonCollectors.toJsonArray())
                                                                                                                                                )
                                                                                                                                                .orElseGet(() ->
                                                                                                                                                        pathEntry.getValue().stream()
                                                                                                                                                                .collect(JsonCollectors.toJsonArray())
                                                                                                                                                )
                                                                                                                                )
                                                                                                                                .build()
                                                                                                                )
                                                                                                                .collect(JsonCollectors.toJsonArray())
                                                                                                )
                                                                                                .build()
                                                                                                .apply(entry.getKey().getArguments())
                                                                                )
                                                                )
                                                )
                                )
                )
                .then()
                .thenReturn(operation);
    }

    public Stream<FetchItem> buildFetchItems(String path, FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            return Streams
                    .concat(
                            Stream.ofNullable(field.getFields())
                                    .flatMap(Collection::stream)
                                    .flatMap(subField -> {
                                                String selectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                                return buildFetchItems(path + "/" + selectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField);
                                            }
                                    ),
                            Stream.ofNullable(fieldDefinition.getArguments())
                                    .flatMap(Collection::stream)
                                    .filter(inputValue -> inputValue.getName().endsWith(SUFFIX_EXPRESSION))
                                    .flatMap(inputValue ->
                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(inputValue.getName()))
                                                    .flatMap(subFieldDefinition ->
                                                            Stream.ofNullable(field.getArguments())
                                                                    .flatMap(arguments ->
                                                                            arguments.getArgument(inputValue.getName())
                                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                    )
                                                                    .flatMap(valueWithVariable ->
                                                                            buildFetchItems(
                                                                                    path,
                                                                                    field,
                                                                                    "",
                                                                                    subFieldDefinition,
                                                                                    inputValue,
                                                                                    valueWithVariable
                                                                            )
                                                                    )
                                                    )

                                    ),
                            fieldDefinition.getArgument(INPUT_VALUE_WHERE_NAME).stream()
                                    .flatMap(whereInputValue ->
                                            Stream.ofNullable(field.getArguments())
                                                    .flatMap(arguments ->
                                                            arguments.getArgument(whereInputValue.getName())
                                                                    .or(() -> Optional.ofNullable(whereInputValue.getDefaultValue())).stream()
                                                    )
                                                    .filter(ValueWithVariable::isObject)
                                                    .map(ValueWithVariable::asObject)
                                                    .flatMap(objectValueWithVariable ->
                                                            documentManager.getInputValueTypeDefinition(whereInputValue).asInputObject().getInputValues().stream()
                                                                    .filter(subInputValue -> subInputValue.getName().endsWith(SUFFIX_EXPRESSION))
                                                                    .flatMap(subInputValue ->
                                                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                    .flatMap(subFieldDefinition ->
                                                                                            objectValueWithVariable.getValueWithVariable(subInputValue.getName())
                                                                                                    .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                    .flatMap(valueWithVariable ->
                                                                                                            buildFetchItems(
                                                                                                                    path,
                                                                                                                    field,
                                                                                                                    "/" + INPUT_VALUE_WHERE_NAME,
                                                                                                                    subFieldDefinition,
                                                                                                                    subInputValue,
                                                                                                                    valueWithVariable
                                                                                                            )
                                                                                                    )
                                                                                    )
                                                                    )
                                                    )
                                    )
                    );
        }
        return Stream.empty();
    }

    public Stream<FetchItem> buildFetchItems(String fieldPath, Field field, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isFetchField()) {
            String protocol = fieldDefinition.getFetchProtocolOrError().toLowerCase();
            String fetchFrom = fieldDefinition.getFetchFromOrError();
            Field fetchField = new Field();
            if (fieldDefinition.hasFetchWith()) {
                ObjectType fetchWithType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithTypeOrError());
                String packageName = fetchWithType.getPackageNameOrError();
                String fetchWithFrom = fieldDefinition.getFetchWithFromOrError();
                String fetchWithTo = fieldDefinition.getFetchWithToOrError();
                fetchField
                        .setAlias(getAliasFromPath(fieldPath) + "__" + getAliasFromPath(path + "/" + fetchFrom))
                        .setArguments(
                                Map.of(
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
                                                .orElseThrow(() -> new GraphQLErrors(FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST.bind(fetchWithTo))),
                                        valueWithVariable,
                                        INPUT_VALUE_GROUP_BY_NAME,
                                        Collections.singletonList(fetchWithFrom)
                                )
                        )
                        .addSelection(new Field(fetchWithFrom))
                        .setName(typeNameToFieldName(fetchWithType.getName()) + SUFFIX_LIST);

                return Stream.of(new FetchItem(packageName, protocol, path, fetchField, fetchWithFrom, field, fetchFrom));
            } else {
                String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                String fetchTo = fieldDefinition.getFetchToOrError();
                valueWithVariable.asObject().put(INPUT_VALUE_GROUP_BY_NAME, Collections.singletonList(fetchTo));
                fetchField
                        .setAlias(getAliasFromPath(fieldPath) + "__" + getAliasFromPath(path + "/" + fetchFrom))
                        .setArguments(valueWithVariable.asObject())
                        .addSelection(new Field(fetchTo))
                        .setName(typeNameToFieldName(fieldTypeDefinition.getName()) + SUFFIX_LIST);

                return Stream.of(new FetchItem(packageName, protocol, path, fetchField, fetchTo, field, fetchFrom));
            }
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            return Stream
                    .concat(
                            inputValueTypeDefinition.asInputObject().getInputValues().stream()
                                    .filter(subInputValue -> subInputValue.getName().endsWith(SUFFIX_EXPRESSION))
                                    .flatMap(subInputValue ->
                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                    .flatMap(subFieldDefinition ->
                                                            Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                                    .flatMap(objectValue ->
                                                                            Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                                    .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                    )
                                                                    .flatMap(subValueWithVariable ->
                                                                            buildFetchItems(
                                                                                    fieldPath,
                                                                                    field,
                                                                                    path + "/" + fieldDefinition.getName(),
                                                                                    subFieldDefinition,
                                                                                    subInputValue,
                                                                                    subValueWithVariable
                                                                            )
                                                                    )
                                                    )
                                    ),
                            inputValueTypeDefinition.asInputObject().getInputValue(INPUT_VALUE_WHERE_NAME).stream()
                                    .flatMap(whereInputValue ->
                                            valueWithVariable.asObject().getValueWithVariable(whereInputValue.getName())
                                                    .or(() -> Optional.ofNullable(whereInputValue.getDefaultValue())).stream()
                                                    .filter(ValueWithVariable::isObject)
                                                    .map(ValueWithVariable::asObject)
                                                    .flatMap(objectValueWithVariable ->
                                                            documentManager.getInputValueTypeDefinition(whereInputValue).asInputObject().getInputValues().stream()
                                                                    .filter(subInputValue -> subInputValue.getName().endsWith(SUFFIX_EXPRESSION))
                                                                    .flatMap(subInputValue ->
                                                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                    .flatMap(subFieldDefinition ->
                                                                                            objectValueWithVariable.getValueWithVariable(subInputValue.getName())
                                                                                                    .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                    .flatMap(subValueWithVariable ->
                                                                                                            buildFetchItems(
                                                                                                                    fieldPath,
                                                                                                                    field,
                                                                                                                    path + "/" + INPUT_VALUE_WHERE_NAME + "/" + fieldDefinition.getName(),
                                                                                                                    subFieldDefinition,
                                                                                                                    subInputValue,
                                                                                                                    subValueWithVariable
                                                                                                            )
                                                                                                    )
                                                                                    )

                                                                    )
                                                    )
                                    )
                    );
        }
        return Stream.empty();
    }
}