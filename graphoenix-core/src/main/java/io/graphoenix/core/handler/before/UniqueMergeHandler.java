package io.graphoenix.core.handler.before;

import com.google.common.collect.Streams;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.*;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.graphoenix.spi.handler.OperationHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple5;
import reactor.util.function.Tuples;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.FragmentHandler.FRAGMENT_HANDLER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
@Priority(UniqueMergeHandler.UNIQUE_MERGE_HANDLER_PRIORITY)
public class UniqueMergeHandler implements OperationBeforeHandler {

    public static final int UNIQUE_MERGE_HANDLER_PRIORITY = FRAGMENT_HANDLER_PRIORITY + 25;

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final Provider<OperationHandler> operationHandlerProvider;

    @Inject
    public UniqueMergeHandler(DocumentManager documentManager, JsonProvider jsonProvider, Provider<OperationHandler> operationHandlerProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
        this.operationHandlerProvider = operationHandlerProvider;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .fromStream(
                        operation.getFields().stream()
                                .filter(Field::isUniqueMerge)
                                .flatMap(field -> buildUniqueItems(operationType.getFieldOrError(field.getName()), field))
                                .collect(
                                        Collectors.groupingBy(
                                                Tuple5::getT1,
                                                Collectors.toList()
                                        )
                                )
                                .entrySet().stream()
                                .flatMap(typeEntry ->
                                        typeEntry.getValue().stream()
                                                .collect(
                                                        Collectors.groupingBy(
                                                                Tuple5::getT2,
                                                                Collectors.toList()
                                                        )
                                                )
                                                .entrySet().stream()
                                                .map(fieldEntry ->
                                                        new AbstractMap.SimpleEntry<>(
                                                                new Field(typeNameToFieldName(typeEntry.getKey().getName()) + SUFFIX_LIST)
                                                                        .setAlias(typeNameToFieldName(typeEntry.getKey().getName()) + "_" + fieldEntry.getKey())
                                                                        .setArguments(
                                                                                jsonProvider.createObjectBuilder()
                                                                                        .add(fieldEntry.getKey(),
                                                                                                jsonProvider.createObjectBuilder()
                                                                                                        .add(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME, new EnumValue(INPUT_OPERATOR_INPUT_VALUE_IN))
                                                                                                        .add(
                                                                                                                INPUT_OPERATOR_INPUT_VALUE_ARR_NAME,
                                                                                                                fieldEntry.getValue().stream()
                                                                                                                        .map(Tuple5::getT3)
                                                                                                                        .collect(JsonCollectors.toJsonArray())
                                                                                                        )
                                                                                        )
                                                                                        .build()
                                                                        )
                                                                        .addSelection(new Field(typeEntry.getKey().getIDFieldOrError().getName()))
                                                                        .addSelection(new Field(fieldEntry.getKey())),
                                                                fieldEntry.getValue()
                                                        )
                                                )
                                )
                )
                .collectList()
                .flatMapMany(fieldEntryList ->
                        Mono
                                .just(

                                        fieldEntryList.stream()
                                                .map(AbstractMap.SimpleEntry::getKey)
                                                .collect(Collectors.toList())
                                )
                                .filter(fieldList -> !fieldList.isEmpty())
                                .flatMap(fieldList ->
                                        Mono
                                                .from(
                                                        operationHandlerProvider.get().handle(
                                                                new Operation()
                                                                        .setOperationType(OPERATION_QUERY_NAME)
                                                                        .setSelections(fieldList)
                                                        )
                                                )
                                )
                                .flatMapMany(jsonValue ->
                                        Flux
                                                .fromStream(
                                                        fieldEntryList.stream()
                                                                .filter(fieldEntry -> !jsonValue.asJsonObject().isNull(fieldEntry.getKey().getAlias()))
                                                                .flatMap(fieldEntry ->
                                                                        fieldEntry.getValue().stream()
                                                                                .flatMap(tuple5 ->
                                                                                        jsonValue.asJsonObject().getJsonArray(fieldEntry.getKey().getAlias()).stream()
                                                                                                .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                                                                .filter(item -> item.asJsonObject().get(tuple5.getT2()).toString().equals(tuple5.getT3().toString()))
                                                                                                .map(item ->
                                                                                                        new AbstractMap.SimpleEntry<>(
                                                                                                                tuple5.getT4(),
                                                                                                                jsonProvider.createObjectBuilder()
                                                                                                                        .add("op", "add")
                                                                                                                        .add("path", tuple5.getT5() + "/" + INPUT_VALUE_WHERE_NAME)
                                                                                                                        .add("value",
                                                                                                                                jsonProvider.createObjectBuilder()
                                                                                                                                        .add(
                                                                                                                                                tuple5.getT1().getIDFieldOrError().getName(),
                                                                                                                                                jsonProvider.createObjectBuilder()
                                                                                                                                                        .add(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME, item.asJsonObject().get(tuple5.getT1().getIDFieldOrError().getName()))
                                                                                                                                        )
                                                                                                                                        .build()
                                                                                                                        )
                                                                                                                        .build()
                                                                                                        )
                                                                                                )
                                                                                )
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
                                                                .entrySet()
                                                                .stream()
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
                .then()
                .thenReturn(operation);
    }

    public Stream<Tuple5<ObjectType, String, ValueWithVariable, Field, String>> buildUniqueItems(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            return Streams
                    .concat(
                            Stream.ofNullable(fieldDefinition.getArguments())
                                    .flatMap(Collection::stream)
                                    .flatMap(inputValue ->
                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(inputValue.getName()))
                                                    .flatMap(subFieldDefinition ->
                                                            Stream.ofNullable(field.getArguments())
                                                                    .filter(arguments -> !hasIDValueWithVariable(idField.getName(), field.getArguments()))
                                                                    .flatMap(arguments ->
                                                                            arguments.getArgumentOrEmpty(inputValue.getName())
                                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                    )
                                                                    .flatMap(valueWithVariable ->
                                                                            buildUniqueItems(
                                                                                    field,
                                                                                    fieldTypeDefinition.asObject(),
                                                                                    "",
                                                                                    subFieldDefinition,
                                                                                    inputValue,
                                                                                    valueWithVariable
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
                                                    .filter((objectValueWithVariable -> !hasIDValueWithVariable(idField.getName(), objectValueWithVariable)))
                                                    .flatMap(objectValueWithVariable ->
                                                            documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                                                                    .flatMap(subInputValue ->
                                                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                    .flatMap(subFieldDefinition ->
                                                                                            objectValueWithVariable.getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                    .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                    .flatMap(subValueWithVariable ->
                                                                                                            buildUniqueItems(
                                                                                                                    field,
                                                                                                                    fieldTypeDefinition.asObject(),
                                                                                                                    "/" + INPUT_VALUE_INPUT_NAME,
                                                                                                                    subFieldDefinition,
                                                                                                                    subInputValue,
                                                                                                                    subValueWithVariable
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
                                                                    .filter(index -> !hasIDValueWithVariable(idField.getName(), arrayValueWithVariable.getValueWithVariable(index).asObject()))
                                                                    .mapToObj(index ->
                                                                            documentManager.getInputValueTypeDefinition(listInputValue).asInputObject().getInputValues().stream()
                                                                                    .flatMap(subInputValue ->
                                                                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                                    .flatMap(subFieldDefinition ->
                                                                                                            arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                                    .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                                    .flatMap(subValueWithVariable ->
                                                                                                                            buildUniqueItems(
                                                                                                                                    field,
                                                                                                                                    fieldTypeDefinition.asObject(),
                                                                                                                                    "/" + INPUT_VALUE_LIST_NAME + "/" + index,
                                                                                                                                    subFieldDefinition,
                                                                                                                                    subInputValue,
                                                                                                                                    subValueWithVariable
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

    public Stream<Tuple5<ObjectType, String, ValueWithVariable, Field, String>> buildUniqueItems(Field field, ObjectType objectType, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isNull()) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isUnique()) {
            return Stream.of(Tuples.of(objectType, fieldDefinition.getName(), valueWithVariable, field, path));
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            return inputValueTypeDefinition.asInputObject().getInputValues().stream()
                    .flatMap(subInputValue ->
                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                    .flatMap(subFieldDefinition -> {
                                                if (fieldDefinition.getType().hasList()) {
                                                    return IntStream.range(0, valueWithVariable.asArray().size())
                                                            .filter(index -> !hasIDValueWithVariable(idField.getName(), valueWithVariable.asArray().getValueWithVariable(index).asObject()))
                                                            .mapToObj(index ->
                                                                    Stream.ofNullable(valueWithVariable.asArray().getValueWithVariable(index).asObject().getObjectValueWithVariable())
                                                                            .flatMap(objectValue ->
                                                                                    Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                                            .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                            )
                                                                            .flatMap(subValueWithVariable ->
                                                                                    buildUniqueItems(
                                                                                            field,
                                                                                            fieldTypeDefinition.asObject(),
                                                                                            path + "/" + fieldDefinition.getName() + "/" + index,
                                                                                            subFieldDefinition,
                                                                                            subInputValue,
                                                                                            subValueWithVariable
                                                                                    )
                                                                            )
                                                            )
                                                            .flatMap(stream -> stream);
                                                } else {
                                                    return Stream.ofNullable(valueWithVariable.asObject())
                                                            .filter(objectValue -> !hasIDValueWithVariable(idField.getName(), objectValue))
                                                            .flatMap(objectValue ->
                                                                    Optional.ofNullable(objectValue.getValueWithVariable(subInputValue.getName()))
                                                                            .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                            )
                                                            .flatMap(subValueWithVariable ->
                                                                    buildUniqueItems(
                                                                            field,
                                                                            fieldTypeDefinition.asObject(),
                                                                            path + "/" + fieldDefinition.getName(),
                                                                            subFieldDefinition,
                                                                            subInputValue,
                                                                            subValueWithVariable
                                                                    )
                                                            );
                                                }
                                            }
                                    )
                    );
        }
        return Stream.empty();
    }

    private boolean hasIDValueWithVariable(String idFieldName, ObjectValueWithVariable objectValueWithVariable) {
        return objectValueWithVariable.containsKey(idFieldName) ||
                objectValueWithVariable.containsKey(INPUT_VALUE_WHERE_NAME) &&
                        objectValueWithVariable.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject().containsKey(idFieldName) &&
                        objectValueWithVariable.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject().getValueWithVariable(idFieldName).asObject().containsKey(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME);
    }

    private boolean hasIDValueWithVariable(String idFieldName, Arguments arguments) {
        return arguments.containsKey(idFieldName) ||
                arguments.containsKey(INPUT_VALUE_WHERE_NAME) &&
                        arguments.getArgument(INPUT_VALUE_WHERE_NAME).asObject().containsKey(idFieldName) &&
                        arguments.getArgument(INPUT_VALUE_WHERE_NAME).asObject().getValueWithVariable(idFieldName).asObject().containsKey(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME);

    }
}
