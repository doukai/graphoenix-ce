package io.graphoenix.core.handler.after;

import com.google.common.collect.Streams;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
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
import io.graphoenix.spi.handler.FetchAfterHandler;
import io.graphoenix.spi.handler.OperationAfterHandler;
import io.graphoenix.spi.handler.PackageFetchHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.after.ConnectionBuilder.CONNECTION_BUILDER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST;

@ApplicationScoped
@Priority(MutationAfterFetchHandler.MUTATION_AFTER_FETCH_HANDLER_PRIORITY)
public class MutationAfterFetchHandler implements OperationAfterHandler, FetchAfterHandler {

    public static final int MUTATION_AFTER_FETCH_HANDLER_PRIORITY = CONNECTION_BUILDER_PRIORITY - 150;

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;
    private final JsonProvider jsonProvider;

    @Inject
    public MutationAfterFetchHandler(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .fromIterable(
                        operation.getFields().stream()
                                .flatMap(field -> {
                                            String selectionName = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
                                            return buildFetchItems(
                                                    operationType,
                                                    operationType.getField(field.getName()),
                                                    field,
                                                    jsonValue.asJsonObject().get(selectionName)
                                            );
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
                                        CDI.current().select(PackageFetchHandler.class, NamedLiteral.of(protocolEntries.getKey())).get()
                                                .request(
                                                        packageEntries.getKey(),
                                                        new Operation()
                                                                .setOperationType(OPERATION_MUTATION_NAME)
                                                                .setSelections(
                                                                        FetchItem.buildMutationFields(protocolEntries.getValue())
                                                                )
                                                )
                                                .flatMapMany(fetchJsonValue ->
                                                        Flux.fromStream(
                                                                protocolEntries.getValue().stream()
                                                                        .filter(fetchItem -> fetchItem.getFetchField() != null)
                                                                        .map(fetchItem -> {
                                                                                    String alias = Optional.ofNullable(fetchItem.getFetchField().getAlias()).orElseGet(fetchItem.getFetchField()::getName);
                                                                                    JsonValue fieldJsonValue = fetchJsonValue.asJsonObject().get(alias);
                                                                                    return jsonProvider.createObjectBuilder()
                                                                                            .add("op", "add")
                                                                                            .add("path", "/" + alias)
                                                                                            .add("value", fieldJsonValue)
                                                                                            .build();
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

    public Stream<FetchItem> buildFetchItems(ObjectType objectType, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (documentManager.isMutationOperationType(objectType) && !packageManager.isLocalPackage(fieldDefinition)) {
            String protocol = fieldDefinition.getFetchProtocol().map(EnumValue::getValue)
                    .orElse(packageConfig.getDefaultFetchProtocol());
            String packageName = fieldDefinition.getPackageNameOrError();
            return Stream.of(new FetchItem(packageName, protocol, field));
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            String idName = fieldTypeDefinition.asObject().getIDFieldOrError().getName();
            if (fieldDefinition.getType().hasList()) {
                return Streams
                        .concat(
                                fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_INPUT_NAME).stream()
                                        .flatMap(inputValue ->
                                                Stream.ofNullable(field.getArguments())
                                                        .flatMap(arguments ->
                                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                        )
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(objectValueWithVariable ->
                                                                documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                                                                        .flatMap(subInputValue ->
                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                        .flatMap(subFieldDefinition ->
                                                                                                objectValueWithVariable.getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                        .flatMap(subValueWithVariable ->
                                                                                                                buildFetchItems(
                                                                                                                        fieldTypeDefinition.asObject(),
                                                                                                                        field,
                                                                                                                        "/" + INPUT_VALUE_INPUT_NAME,
                                                                                                                        subFieldDefinition,
                                                                                                                        subInputValue,
                                                                                                                        subValueWithVariable,
                                                                                                                        jsonValue.asJsonObject()
                                                                                                                )
                                                                                                        )
                                                                                        )
                                                                        )
                                                        )
                                        ),
                                fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_LIST_NAME).stream()
                                        .flatMap(listInputValue ->
                                                Stream.ofNullable(field.getArguments())
                                                        .flatMap(arguments ->
                                                                arguments.getArgumentOrEmpty(listInputValue.getName())
                                                                        .or(() -> Optional.ofNullable(listInputValue.getDefaultValue())).stream()
                                                        )
                                                        .filter(ValueWithVariable::isArray)
                                                        .map(ValueWithVariable::asArray)
                                                        .map(arrayValueWithVariable ->
                                                                arrayValueWithVariable.getValueWithVariables().stream()
                                                                        .filter(valueWithVariable -> !valueWithVariable.isNull())
                                                                        .filter(item -> !item.asObject().containsKey(INPUT_VALUE_WHERE_NAME) || item.asObject().getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject().containsKey(idName))
                                                                        .filter(valueWithVariable ->
                                                                                !valueWithVariable.asObject().containsKey(FIELD_DEPRECATED_NAME) ||
                                                                                        !valueWithVariable.asObject().asJsonObject().getBoolean(FIELD_DEPRECATED_NAME)
                                                                        )
                                                                        .collect(Collectors.toList())
                                                        )
                                                        .flatMap(valueWithVariableList ->
                                                                IntStream.range(0, valueWithVariableList.size())
                                                                        .filter(index -> !jsonValue.asJsonArray().get(index).getValueType().equals(JsonValue.ValueType.NULL))
                                                                        .filter(index ->
                                                                                !jsonValue.asJsonArray().get(index).asJsonObject().containsKey(FIELD_DEPRECATED_NAME) ||
                                                                                        !jsonValue.asJsonArray().get(index).asJsonObject().getBoolean(FIELD_DEPRECATED_NAME)
                                                                        )
                                                                        .mapToObj(index ->
                                                                                documentManager.getInputValueTypeDefinition(listInputValue).asInputObject().getInputValues().stream()
                                                                                        .flatMap(subInputValue ->
                                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                                        .flatMap(subFieldDefinition ->
                                                                                                                valueWithVariableList.get(index).asObject().getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                                        .flatMap(subValueWithVariable ->
                                                                                                                                buildFetchItems(
                                                                                                                                        fieldTypeDefinition.asObject(),
                                                                                                                                        field,
                                                                                                                                        "/" + INPUT_VALUE_LIST_NAME + "/" + index,
                                                                                                                                        subFieldDefinition,
                                                                                                                                        subInputValue,
                                                                                                                                        subValueWithVariable,
                                                                                                                                        jsonValue.asJsonArray().get(index)
                                                                                                                                )
                                                                                                                        )
                                                                                                        )
                                                                                        )
                                                                        )
                                                                        .flatMap(stream -> stream)
                                                        )
                                        ),
                                jsonValue.asJsonArray().stream()
                                        .flatMap(item ->
                                                Stream.ofNullable(fieldDefinition.getArguments())
                                                        .flatMap(Collection::stream)
                                                        .flatMap(inputValue ->
                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(inputValue.getName()))
                                                                        .flatMap(subFieldDefinition ->
                                                                                Stream.ofNullable(field.getArguments())
                                                                                        .flatMap(arguments ->
                                                                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                                        )
                                                                                        .flatMap(valueWithVariable ->
                                                                                                buildFetchItems(
                                                                                                        fieldTypeDefinition.asObject(),
                                                                                                        field,
                                                                                                        "",
                                                                                                        subFieldDefinition,
                                                                                                        inputValue,
                                                                                                        valueWithVariable,
                                                                                                        item
                                                                                                )
                                                                                        )
                                                                        )
                                                        )
                                        )
                        );
            } else {
                return Stream.ofNullable(fieldDefinition.getArguments())
                        .flatMap(Collection::stream)
                        .flatMap(inputValue ->
                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(inputValue.getName()))
                                        .flatMap(subFieldDefinition ->
                                                Stream.ofNullable(field.getArguments())
                                                        .flatMap(arguments ->
                                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                        )
                                                        .flatMap(valueWithVariable ->
                                                                buildFetchItems(
                                                                        fieldTypeDefinition.asObject(),
                                                                        field,
                                                                        "",
                                                                        subFieldDefinition,
                                                                        inputValue,
                                                                        valueWithVariable,
                                                                        jsonValue
                                                                )
                                                        )
                                        )
                        );
            }
        }
        return Stream.empty();
    }

    public Stream<FetchItem> buildFetchItems(ObjectType objectType, Field field, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, JsonValue jsonValue) {
        if (valueWithVariable.isNull() || jsonValue == null || jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isFetchField()) {
            String fetchFrom = fieldDefinition.getFetchFromOrError();
            if (fieldTypeDefinition.isObject()) {
                FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
                if (fieldDefinition.hasFetchWith()) {
                    ObjectType fetchWithType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithTypeOrError());
                    String packageName = fetchWithType.getPackageNameOrError();
                    String fetchWithFrom = fieldDefinition.getFetchWithFromOrError();
                    String fetchWithTo = fieldDefinition.getFetchWithToOrError();
                    if (fieldDefinition.getType().hasList()) {
                        return valueWithVariable.asArray().getValueWithVariables().stream()
                                .map(item ->
                                        item.asObject().containsKey(INPUT_VALUE_WHERE_NAME) && item.asObject().keySet().size() == 1 ?
                                                jsonProvider.createObjectBuilder()
                                                        .add(fetchWithFrom, getFetchFrom(jsonValue.asJsonObject().get(fetchFrom), fetchWithType.getField(fetchWithFrom)))
                                                        .add(fetchWithTo, item.asObject().getJsonObject(INPUT_VALUE_WHERE_NAME).getJsonObject(idField.getName()).get(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
                                                        .build() :
                                                jsonProvider.createObjectBuilder()
                                                        .add(fetchWithFrom, getFetchFrom(jsonValue.asJsonObject().get(fetchFrom), fetchWithType.getField(fetchWithFrom)))
                                                        .add(
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
                                                                item
                                                        )
                                                        .build()
                                )
                                .map(item -> {
                                            String id;
                                            if (item.asJsonObject().containsKey(idField.getName())) {
                                                id = getId(item.asJsonObject().get(idField.getName()));
                                            } else {
                                                id = UUID.randomUUID().toString();
                                            }
                                            return new FetchItem(packageName, packageManager.isLocalPackage(fetchWithType) ? ENUM_PROTOCOL_ENUM_VALUE_LOCAL : packageConfig.getDefaultFetchProtocol(), path, fetchWithType.getName(), item, id, fetchWithType.getIDFieldOrError().getName());
                                        }
                                );
                    } else {
                        String id;
                        if (valueWithVariable.asJsonObject().containsKey(idField.getName())) {
                            id = getId(valueWithVariable.asJsonObject().get(idField.getName()));
                        } else {
                            id = UUID.randomUUID().toString();
                        }
                        JsonValue mutationJsonValue =
                                valueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME) && valueWithVariable.asObject().keySet().size() == 1 ?
                                        jsonProvider.createObjectBuilder()
                                                .add(fetchWithFrom, getFetchFrom(jsonValue.asJsonObject().get(fetchFrom), fetchWithType.getField(fetchWithFrom)))
                                                .add(fetchWithTo, valueWithVariable.asObject().getJsonObject(INPUT_VALUE_WHERE_NAME).getJsonObject(idField.getName()).get(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
                                                .build() :
                                        jsonProvider.createObjectBuilder()
                                                .add(fetchWithFrom, getFetchFrom(jsonValue.asJsonObject().get(fetchFrom), fetchWithType.getField(fetchWithFrom)))
                                                .add(
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
                                                        valueWithVariable
                                                )
                                                .build();
                        return Stream.of(new FetchItem(packageName, packageManager.isLocalPackage(fetchWithType) ? ENUM_PROTOCOL_ENUM_VALUE_LOCAL : packageConfig.getDefaultFetchProtocol(), path, fetchWithType.getName(), mutationJsonValue, id, fetchWithType.getIDFieldOrError().getName()));
                    }
                } else {
                    String protocol = fieldDefinition.getFetchProtocolOrError().getValue();
                    String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                    String fetchTo = fieldDefinition.getFetchToOrError();
                    if (fieldDefinition.getType().hasList()) {
                        return valueWithVariable.asArray().getValueWithVariables().stream()
                                .map(item ->
                                        jsonProvider.createObjectBuilder(item.asJsonObject())
                                                .add(fetchTo, getFetchFrom(jsonValue.asJsonObject().get(fetchFrom), fieldTypeDefinition.asObject().getField(fetchTo)))
                                                .build()
                                )
                                .map(item -> {
                                            String id;
                                            if (item.asJsonObject().containsKey(idField.getName())) {
                                                id = getId(item.asJsonObject().get(idField.getName()));
                                            } else {
                                                id = UUID.randomUUID().toString();
                                            }
                                            return new FetchItem(packageName, protocol, path, fieldTypeDefinition.getName(), item, id, idField.getName());
                                        }
                                );
                    } else if (!documentManager.isFetchAnchor(objectType, fieldDefinition)) {
                        String id;
                        if (valueWithVariable.asJsonObject().containsKey(idField.getName())) {
                            id = getId(valueWithVariable.asJsonObject().get(idField.getName()));
                        } else {
                            id = UUID.randomUUID().toString();
                        }
                        JsonValue mutationJsonValue = jsonProvider.createObjectBuilder(valueWithVariable.asObject())
                                .add(fetchTo, getFetchFrom(jsonValue.asJsonObject().get(fetchFrom), fieldTypeDefinition.asObject().getField(fetchTo)))
                                .build();
                        return Stream.of(new FetchItem(packageName, protocol, path, fieldTypeDefinition.getName(), mutationJsonValue, id, idField.getName()));
                    }
                }
            } else {
                if (fieldDefinition.hasFetchWith()) {
                    ObjectType fetchWithType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithTypeOrError());
                    String packageName = fetchWithType.getPackageNameOrError();
                    String fetchWithFrom = fieldDefinition.getFetchWithFromOrError();
                    String fetchWithTo = fieldDefinition.getFetchWithToOrError();

                    if (fieldDefinition.getType().hasList()) {
                        return valueWithVariable.asArray().stream()
                                .map(item ->
                                        jsonProvider.createObjectBuilder()
                                                .add(fetchWithFrom, getFetchFrom(jsonValue.asJsonObject().get(fetchFrom), fetchWithType.getField(fetchWithFrom)))
                                                .add(fetchWithTo, item)
                                                .build()
                                )
                                .map(item -> {
                                            String id = UUID.randomUUID().toString();
                                            return new FetchItem(packageName, packageManager.isLocalPackage(fetchWithType) ? ENUM_PROTOCOL_ENUM_VALUE_LOCAL : packageConfig.getDefaultFetchProtocol(), path, fetchWithType.getName(), item, id, fetchWithType.getIDFieldOrError().getName());
                                        }
                                );
                    }
                }
            }
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            JsonValue fieldJsonValue = jsonValue.asJsonObject().get(fieldDefinition.getName());
            if (fieldJsonValue == null) {
                return Stream.empty();
            }
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            return inputValueTypeDefinition.asInputObject().getInputValues().stream()
                    .filter(subInputValue -> !documentManager.getInputValueTypeDefinition(subInputValue).isLeaf())
                    .flatMap(subInputValue ->
                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                    .flatMap(subFieldDefinition -> {
                                                if (fieldDefinition.getType().hasList()) {
                                                    return IntStream.range(0, valueWithVariable.asArray().size())
                                                            .mapToObj(index ->
                                                                    fieldJsonValue.asJsonArray().stream()
                                                                            .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                                            .map(JsonValue::asJsonObject)
                                                                            .flatMap(item ->
                                                                                    Stream.ofNullable(valueWithVariable.asArray().getValueWithVariable(index).asObject().getObjectValueWithVariable())
                                                                                            .flatMap(objectValue ->
                                                                                                    Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                                                            .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                            )
                                                                                            .flatMap(subValueWithVariable ->
                                                                                                    buildFetchItems(
                                                                                                            fieldTypeDefinition.asObject(),
                                                                                                            field,
                                                                                                            path + "/" + fieldDefinition.getName() + "/" + index,
                                                                                                            subFieldDefinition,
                                                                                                            subInputValue,
                                                                                                            subValueWithVariable,
                                                                                                            item
                                                                                                    )
                                                                                            )
                                                                            )
                                                            )
                                                            .flatMap(stream -> stream);
                                                } else {
                                                    return Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                            .flatMap(objectValue ->
                                                                    Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                            .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                            )
                                                            .flatMap(subValueWithVariable ->
                                                                    buildFetchItems(
                                                                            fieldTypeDefinition.asObject(),
                                                                            field,
                                                                            path + "/" + fieldDefinition.getName(),
                                                                            subFieldDefinition,
                                                                            subInputValue,
                                                                            subValueWithVariable,
                                                                            fieldJsonValue
                                                                    )
                                                            );
                                                }
                                            }
                                    )
                    );
        }
        return Stream.empty();
    }

    private JsonValue getFetchFrom(JsonValue jsonValue, FieldDefinition fieldDefinition) {
        String typeName = documentManager.getFieldTypeDefinition(fieldDefinition).getName();
        if (typeName.equals(SCALA_INT_NAME) ||
                typeName.equals(SCALA_BIG_INTEGER_NAME) ||
                typeName.equals(SCALA_FLOAT_NAME) ||
                typeName.equals(SCALA_BIG_DECIMAL_NAME)) {
            if (jsonValue.getValueType().equals(JsonValue.ValueType.STRING)) {
                return jsonProvider.createValue(Integer.valueOf(((JsonString) jsonValue).getString()));
            }
        }
        return jsonValue;
    }

    private String getId(JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.STRING)) {
            return ((JsonString) jsonValue).getString();
        } else {
            return jsonValue.toString();
        }
    }
}
