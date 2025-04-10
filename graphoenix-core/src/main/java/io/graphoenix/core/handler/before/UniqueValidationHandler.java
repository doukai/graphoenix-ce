package io.graphoenix.core.handler.before;

import com.google.common.collect.Streams;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.error.GraphQLError;
import io.graphoenix.spi.error.GraphQLErrors;
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
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.FragmentHandler.FRAGMENT_HANDLER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.EXISTED_UNIQUE_VALUES;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
@Priority(UniqueValidationHandler.UNIQUE_VALIDATION_HANDLER_PRIORITY)
public class UniqueValidationHandler implements OperationBeforeHandler {

    public static final int UNIQUE_VALIDATION_HANDLER_PRIORITY = FRAGMENT_HANDLER_PRIORITY + 75;

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final Provider<OperationHandler> operationHandlerProvider;

    @Inject
    public UniqueValidationHandler(DocumentManager documentManager, JsonProvider jsonProvider, Provider<OperationHandler> operationHandlerProvider) {
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
                                .flatMap(field -> buildUniqueItems(operationType.getField(field.getName()), field))
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
                                                                        Stream
                                                                                .concat(
                                                                                        fieldEntry.getValue().stream()
                                                                                                .filter(tuple5 -> tuple5.getT5().isNull())
                                                                                                .filter(tuple5 ->
                                                                                                        jsonValue.asJsonObject().getJsonArray(fieldEntry.getKey().getAlias()).stream()
                                                                                                                .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                                                                                .anyMatch(item -> item.asJsonObject().get(tuple5.getT2()).toString().equals(tuple5.getT3().toString()))
                                                                                                ),
                                                                                        fieldEntry.getValue().stream()
                                                                                                .filter(tuple5 -> !tuple5.getT5().isNull())
                                                                                                .filter(tuple5 ->
                                                                                                        jsonValue.asJsonObject().getJsonArray(fieldEntry.getKey().getAlias()).stream()
                                                                                                                .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                                                                                .anyMatch(item ->
                                                                                                                        item.asJsonObject().get(tuple5.getT2()).toString().equals(tuple5.getT3().toString()) &&
                                                                                                                                !item.asJsonObject().get(tuple5.getT1().getIDFieldOrError().getName()).toString().equals(tuple5.getT5().toString())
                                                                                                                )
                                                                                                )
                                                                                )
                                                                                .map(tuple5 -> new GraphQLError(EXISTED_UNIQUE_VALUES).setPath(tuple5.getT4()))
                                                                )
                                                )
                                )
                )
                .collectList()
                .flatMap(graphQLErrors -> {
                            if (!graphQLErrors.isEmpty()) {
                                return Mono.error(new GraphQLErrors().addAll(graphQLErrors));
                            }
                            return Mono.just(operation);
                        }
                );
    }

    public Stream<Tuple5<ObjectType, String, ValueWithVariable, String, ValueWithVariable>> buildUniqueItems(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            ValueWithVariable idValueWithVariable = getIDValueWithVariable(idField.getName(), field.getArguments()).orElse(new NullValue());
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
                                                                            buildUniqueItems(
                                                                                    fieldTypeDefinition.asObject(),
                                                                                    "",
                                                                                    idValueWithVariable,
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
                                                    .flatMap(objectValueWithVariable -> {
                                                                ValueWithVariable itemIdValueWithVariable = getIDValueWithVariable(idField.getName(), objectValueWithVariable).orElse(new NullValue());
                                                                return documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                                                                        .flatMap(subInputValue ->
                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                        .flatMap(subFieldDefinition ->
                                                                                                objectValueWithVariable.getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                        .flatMap(subValueWithVariable ->
                                                                                                                buildUniqueItems(
                                                                                                                        fieldTypeDefinition.asObject(),
                                                                                                                        "/" + INPUT_VALUE_INPUT_NAME,
                                                                                                                        itemIdValueWithVariable,
                                                                                                                        subFieldDefinition,
                                                                                                                        subInputValue,
                                                                                                                        subValueWithVariable
                                                                                                                )
                                                                                                        )
                                                                                        )
                                                                        );
                                                            }
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
                                                                    .mapToObj(index -> {
                                                                                ValueWithVariable itemIdValueWithVariable = getIDValueWithVariable(idField.getName(), arrayValueWithVariable.getValueWithVariable(index).asObject()).orElse(new NullValue());
                                                                                return documentManager.getInputValueTypeDefinition(listInputValue).asInputObject().getInputValues().stream()
                                                                                        .flatMap(subInputValue ->
                                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                                        .flatMap(subFieldDefinition ->
                                                                                                                arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                                        .flatMap(subValueWithVariable ->
                                                                                                                                buildUniqueItems(
                                                                                                                                        fieldTypeDefinition.asObject(),
                                                                                                                                        "/" + INPUT_VALUE_LIST_NAME + "/" + index,
                                                                                                                                        itemIdValueWithVariable,
                                                                                                                                        subFieldDefinition,
                                                                                                                                        subInputValue,
                                                                                                                                        subValueWithVariable
                                                                                                                                )
                                                                                                                        )
                                                                                                        )
                                                                                        );
                                                                            }
                                                                    )
                                                                    .flatMap(stream -> stream)
                                                    )
                                    )
                    );
        }
        return Stream.empty();
    }

    public Stream<Tuple5<ObjectType, String, ValueWithVariable, String, ValueWithVariable>> buildUniqueItems(ObjectType objectType, String path, ValueWithVariable parentIDValueWithVariable, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isNull()) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isUnique()) {
            return Stream.of(Tuples.of(objectType, fieldDefinition.getName(), valueWithVariable, path + "/" + fieldDefinition.getName(), parentIDValueWithVariable));
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            return inputValueTypeDefinition.asInputObject().getInputValues().stream()
                    .flatMap(subInputValue ->
                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                    .flatMap(subFieldDefinition -> {
                                                if (fieldDefinition.getType().hasList()) {
                                                    return IntStream.range(0, valueWithVariable.asArray().size())
                                                            .mapToObj(index -> {
                                                                        ValueWithVariable idValueWithVariable = getIDValueWithVariable(idField.getName(), valueWithVariable.asArray().getValueWithVariable(index).asObject()).orElse(new NullValue());
                                                                        return Stream.ofNullable(valueWithVariable.asArray().getValueWithVariable(index).asObject().getObjectValueWithVariable())
                                                                                .flatMap(objectValue ->
                                                                                        Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                                                .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                )
                                                                                .flatMap(subValueWithVariable ->
                                                                                        buildUniqueItems(
                                                                                                fieldTypeDefinition.asObject(),
                                                                                                path + "/" + fieldDefinition.getName() + "/" + index,
                                                                                                idValueWithVariable,
                                                                                                subFieldDefinition,
                                                                                                subInputValue,
                                                                                                subValueWithVariable
                                                                                        )
                                                                                );
                                                                    }
                                                            )
                                                            .flatMap(stream -> stream);
                                                } else {
                                                    ValueWithVariable idValueWithVariable = getIDValueWithVariable(idField.getName(), valueWithVariable.asObject()).orElse(new NullValue());
                                                    return Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                            .flatMap(objectValue ->
                                                                    Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                            .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                            )
                                                            .flatMap(subValueWithVariable ->
                                                                    buildUniqueItems(
                                                                            fieldTypeDefinition.asObject(),
                                                                            path + "/" + fieldDefinition.getName(),
                                                                            idValueWithVariable,
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

    private Optional<ValueWithVariable> getIDValueWithVariable(String idFieldName, ObjectValueWithVariable objectValueWithVariable) {
        if (objectValueWithVariable.containsKey(idFieldName)) {
            return Optional.of(objectValueWithVariable.getValueWithVariable(idFieldName));
        } else if (objectValueWithVariable.containsKey(INPUT_VALUE_WHERE_NAME) &&
                objectValueWithVariable.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject().containsKey(idFieldName) &&
                objectValueWithVariable.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject().getValueWithVariable(idFieldName).asObject().containsKey(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)) {
            return Optional.of(
                    objectValueWithVariable.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject()
                            .getValueWithVariable(idFieldName).asObject()
                            .getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
            );
        }
        return Optional.empty();
    }

    private Optional<ValueWithVariable> getIDValueWithVariable(String idFieldName, Arguments arguments) {
        if (arguments.containsKey(idFieldName)) {
            return Optional.of(arguments.getArgument(idFieldName));
        } else if (arguments.containsKey(INPUT_VALUE_WHERE_NAME) &&
                arguments.getArgument(INPUT_VALUE_WHERE_NAME).asObject().containsKey(idFieldName) &&
                arguments.getArgument(INPUT_VALUE_WHERE_NAME).asObject().getValueWithVariable(idFieldName).asObject().containsKey(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)) {
            return Optional.of(
                    arguments.getArgument(INPUT_VALUE_WHERE_NAME).asObject()
                            .getValueWithVariable(idFieldName).asObject()
                            .getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
            );
        }
        return Optional.empty();
    }
}
