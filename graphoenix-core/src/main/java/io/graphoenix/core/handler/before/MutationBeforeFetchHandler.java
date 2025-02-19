package io.graphoenix.core.handler.before;

import com.google.common.collect.Streams;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchBeforeHandler;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.graphoenix.spi.handler.PackageFetchHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
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

import static io.graphoenix.core.handler.before.ConnectionSplitter.CONNECTION_SPLITTER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
@Priority(MutationBeforeFetchHandler.MUTATION_BEFORE_FETCH_HANDLER_PRIORITY)
public class MutationBeforeFetchHandler implements OperationBeforeHandler, FetchBeforeHandler {

    public static final int MUTATION_BEFORE_FETCH_HANDLER_PRIORITY = CONNECTION_SPLITTER_PRIORITY + 450;

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;

    @Inject
    public MutationBeforeFetchHandler(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .fromIterable(
                        operation.getFields().stream()
                                .flatMap(field -> buildFetchItems(operationType.getField(field.getName()), field))
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
                                        Mono
                                                .just(
                                                        FetchItem.buildMutationFields(protocolEntries.getValue())
                                                )
                                                .filter(fieldList -> !fieldList.isEmpty())
                                                .flatMap(fieldList ->
                                                        CDI.current().select(PackageFetchHandler.class, NamedLiteral.of(protocolEntries.getKey())).get()
                                                                .request(
                                                                        packageEntries.getKey(),
                                                                        new Operation()
                                                                                .setOperationType(OPERATION_MUTATION_NAME)
                                                                                .setSelections(fieldList)
                                                                )
                                                )
                                                .flatMapMany(fetchJsonValue ->
                                                        Flux
                                                                .fromIterable(
                                                                        FetchItem
                                                                                .buildMutationItems(protocolEntries.getValue())
                                                                                .collect(
                                                                                        Collectors.groupingBy(
                                                                                                FetchItem::getField,
                                                                                                Collectors.mapping(
                                                                                                        fetchItem -> {
                                                                                                            JsonValue fieldJsonValue = fetchJsonValue.asJsonObject().get(typeNameToFieldName(fetchItem.getTypeName()) + SUFFIX_LIST).asJsonArray().get(fetchItem.getIndex());
                                                                                                            return jsonProvider.createObjectBuilder()
                                                                                                                    .add("op", "add")
                                                                                                                    .add("path", fetchItem.getPath() + "/" + fetchItem.getFetchFrom())
                                                                                                                    .add("value", fieldJsonValue.asJsonObject().get(fetchItem.getTarget()))
                                                                                                                    .build();
                                                                                                        },
                                                                                                        Collectors.toList()
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
                                                                                                        entry.getValue().stream()
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

    public Stream<FetchItem> buildFetchItems(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            return Streams
                    .concat(
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
                                                                                    field.getArguments()
                                                                            )
                                                                    )
                                                    )
                                    ),
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
                                                                                                                    objectValueWithVariable
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
                                                    .flatMap(arrayValueWithVariable ->
                                                            IntStream.range(0, arrayValueWithVariable.size())
                                                                    .mapToObj(index ->
                                                                            documentManager.getInputValueTypeDefinition(listInputValue).asInputObject().getInputValues().stream()
                                                                                    .flatMap(subInputValue ->
                                                                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                                    .flatMap(subFieldDefinition ->
                                                                                                            arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                                    .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                                    .flatMap(subValueWithVariable ->
                                                                                                                            buildFetchItems(
                                                                                                                                    fieldTypeDefinition.asObject(),
                                                                                                                                    field,
                                                                                                                                    "/" + INPUT_VALUE_LIST_NAME + "/" + index,
                                                                                                                                    subFieldDefinition,
                                                                                                                                    subInputValue,
                                                                                                                                    subValueWithVariable,
                                                                                                                                    arrayValueWithVariable.getValueWithVariable(index).asObject()
                                                                                                                            )
                                                                                                                    )
                                                                                                    )
                                                                                    )
                                                                    )
                                                                    .flatMap(stream -> stream)
                                                    )
                                    )
                    );
        }
        return Stream.empty();
    }

    public Stream<FetchItem> buildFetchItems(ObjectType objectType, Field field, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, AbstractMap<String, JsonValue> arguments) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isFetchField()) {
            if (!fieldDefinition.getType().hasList() && !fieldDefinition.hasFetchWith() && documentManager.isFetchAnchor(objectType, fieldDefinition)) {
                String protocol = fieldDefinition.getFetchProtocolOrError().getValue();
                String fetchFrom = fieldDefinition.getFetchFromOrError();
                String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                String fetchTo = fieldDefinition.getFetchToOrError();
                FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
                if (valueWithVariable.isNull()) {
                    arguments.put(fetchFrom, JsonValue.NULL);
                    return Stream.empty();
                }
                if (valueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME) && valueWithVariable.asObject().keySet().size() == 1) {
                    return Stream.empty();
                }
                String id;
                if (valueWithVariable.asJsonObject().containsKey(idField.getName())) {
                    id = getId(valueWithVariable.asJsonObject().get(idField.getName()));
                } else {
                    id = UUID.randomUUID().toString();
                }

                return Stream.of(
                        new FetchItem(
                                packageName,
                                protocol,
                                path,
                                fieldTypeDefinition.getName(),
                                valueWithVariable.asJsonObject(),
                                id,
                                fetchTo,
                                field,
                                fetchFrom
                        )
                );
            }
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            if (valueWithVariable.isNull()) {
                return Stream.empty();
            }
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            return inputValueTypeDefinition.asInputObject().getInputValues().stream()
                    .filter(subInputValue -> !documentManager.getInputValueTypeDefinition(subInputValue).isLeaf())
                    .flatMap(subInputValue ->
                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                    .flatMap(subFieldDefinition -> {
                                                if (fieldDefinition.getType().hasList()) {
                                                    return IntStream.range(0, valueWithVariable.asArray().size())
                                                            .mapToObj(index ->
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
                                                                                            valueWithVariable.asArray().getValueWithVariable(index).asObject()
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
                                                                            valueWithVariable.asObject()
                                                                    )
                                                            );
                                                }
                                            }
                                    )
                    );
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
