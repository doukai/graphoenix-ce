package io.graphoenix.core.handler.after;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST;

@ApplicationScoped
@Priority(Integer.MAX_VALUE - 500)
public class MutationAfterFetchHandler implements OperationAfterHandler {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final Map<String, FetchHandler> fetchHandlerMap = BeanContext.getMap(FetchHandler.class);

    @Inject
    public MutationAfterFetchHandler(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
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
                                            return buildFetchItems(operationType.getField(field.getName()), field, jsonValue.asJsonObject().get(selectionName));
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
                                                                .setOperationType(OPERATION_MUTATION_NAME)
                                                                .setSelections(
                                                                        FetchItem.buildMutationFields(protocolEntries.getValue())
                                                                )
                                                )
                                )
                )
                .then()
                .thenReturn(jsonValue);
    }

    public Stream<FetchItem> buildFetchItems(FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            if (fieldDefinition.getType().hasList()) {
                return Stream
                        .concat(
                                fieldDefinition.getArgument(INPUT_VALUE_LIST_NAME).stream()
                                        .flatMap(listInputValue ->
                                                Stream.ofNullable(field.getArguments())
                                                        .flatMap(arguments ->
                                                                arguments.getArgument(listInputValue.getName())
                                                                        .or(() -> Optional.ofNullable(listInputValue.getDefaultValue())).stream()
                                                        )
                                                        .filter(ValueWithVariable::isArray)
                                                        .map(ValueWithVariable::asArray)
                                                        .flatMap(arrayValueWithVariable ->
                                                                IntStream.range(0, arrayValueWithVariable.size())
                                                                        .mapToObj(index ->
                                                                                documentManager.getInputValueTypeDefinition(listInputValue).asInputObject().getInputValues().stream()
                                                                                        .flatMap(subInputValue ->
                                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                                        .flatMap(subFieldDefinition ->
                                                                                                                arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariable(subInputValue.getName())
                                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                                        .flatMap(subValueWithVariable ->
                                                                                                                                buildFetchItems(
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
                                        .map(item ->
                                                Stream.ofNullable(fieldDefinition.getArguments())
                                                        .flatMap(Collection::stream)
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
                                                                                                        "",
                                                                                                        subFieldDefinition,
                                                                                                        inputValue,
                                                                                                        valueWithVariable,
                                                                                                        item
                                                                                                )
                                                                                        )
                                                                        )
                                                        ))
                                        .flatMap(stream -> stream)
                        );
            } else {
                return Stream.ofNullable(fieldDefinition.getArguments())
                        .flatMap(Collection::stream)
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

    public Stream<FetchItem> buildFetchItems(String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, JsonValue jsonValue) {
        if (valueWithVariable.isNull() || jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isFetchField()) {
            String protocol = fieldDefinition.getFetchProtocolOrError().getValue().toLowerCase();
            String fetchFrom = fieldDefinition.getFetchFromOrError();
            if (fieldTypeDefinition.isObject()) {
                FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
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
                                            return new FetchItem(packageName, protocol, path, fetchWithType.getName(), item, id, fetchWithType.getIDFieldOrError().getName());
                                        }
                                );
                    } else {
                        String id;
                        if (valueWithVariable.asJsonObject().containsKey(idField.getName())) {
                            id = getId(valueWithVariable.asJsonObject().get(idField.getName()));
                        } else {
                            id = UUID.randomUUID().toString();
                        }
                        JsonValue mutationJsonValue = jsonProvider.createObjectBuilder()
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
                        return Stream.of(new FetchItem(packageName, protocol, path, fetchWithType.getName(), mutationJsonValue, id, fetchWithType.getIDFieldOrError().getName()));
                    }
                } else {
                    String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                    String fetchTo = fieldDefinition.getFetchToOrError();
                    if (fieldDefinition.getType().hasList()) {
                        return valueWithVariable.asArray().stream()
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
                    } else if (!fieldDefinition.isFetchAnchor()) {
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
                                            return new FetchItem(packageName, protocol, path, fetchWithType.getName(), item, id, fetchWithType.getIDFieldOrError().getName());
                                        }
                                );
                    }
                }
            }
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            if (jsonValue.asJsonObject().isNull(fieldDefinition.getName())) {
                return Stream.empty();
            }
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            return inputValueTypeDefinition.asInputObject().getInputValues().stream()
                    .flatMap(subInputValue ->
                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                    .flatMap(subFieldDefinition -> {
                                                JsonValue fieldJsonValue = jsonValue.asJsonObject().get(fieldDefinition.getName());
                                                ValueWithVariable fieldValueWithVariable = valueWithVariable.asObject().getValueWithVariableOrNull(fieldDefinition.getName());
                                                if (subFieldDefinition.getType().hasList()) {
                                                    return IntStream.range(0, fieldJsonValue.asJsonArray().size())
                                                            .mapToObj(index ->
                                                                    Stream.ofNullable(fieldValueWithVariable.asArray().getValueWithVariable(index).asObject().getObjectValueWithVariable())
                                                                            .flatMap(objectValue ->
                                                                                    Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                                            .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                            )
                                                                            .flatMap(subValueWithVariable ->
                                                                                    buildFetchItems(
                                                                                            path + "/" + fieldDefinition.getName() + "/" + index,
                                                                                            subFieldDefinition,
                                                                                            subInputValue,
                                                                                            subValueWithVariable,
                                                                                            fieldJsonValue.asJsonArray().get(index)
                                                                                    )
                                                                            )
                                                            )
                                                            .flatMap(stream -> stream);
                                                } else {
                                                    return Stream.ofNullable(fieldValueWithVariable.asObject().getObjectValueWithVariable())
                                                            .flatMap(objectValue ->
                                                                    Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                            .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                            )
                                                            .flatMap(subValueWithVariable ->
                                                                    buildFetchItems(
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
