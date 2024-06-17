package io.graphoenix.core.handler;

import com.google.common.collect.Streams;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.*;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonCollectors;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;

@ApplicationScoped
public class OperationBuilder {

    private final DocumentManager documentManager;

    @Inject
    public OperationBuilder(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public JsonObject updateJsonObject(JsonObject original, JsonObject jsonObject) {
        return jsonObject.entrySet().stream()
                .filter(entry ->
                        !entry.getValue().getValueType().equals(JsonValue.ValueType.NULL) ||
                                entry.getValue().getValueType().equals(JsonValue.ValueType.NULL) && original != null && original.containsKey(entry.getKey())
                )
                .map(entry ->
                        new AbstractMap.SimpleEntry<>(
                                entry.getKey(),
                                updateJsonValue(original != null ? original.get(entry.getKey()) : null, entry.getValue())
                        )
                )
                .collect(JsonCollectors.toJsonObject());
    }

    public JsonValue updateJsonValue(JsonValue original, JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.OBJECT)) {
            return updateJsonObject(original != null ? original.asJsonObject() : null, jsonValue.asJsonObject());
        } else if (jsonValue.getValueType().equals(JsonValue.ValueType.ARRAY)) {
            return IntStream.range(0, jsonValue.asJsonArray().size())
                    .mapToObj(index -> updateJsonValue(original != null ? original.asJsonArray().get(index) : null, jsonValue.asJsonArray().get(index)))
                    .collect(JsonCollectors.toJsonArray());
        } else {
            return jsonValue;
        }
    }

    public JsonObject updateJsonObject(Field field, FieldDefinition fieldDefinition, JsonObject jsonObject) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        return Stream.ofNullable(field.getFields())
                .flatMap(Collection::stream)
                .map(subField -> {
                            String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                            return new AbstractMap.SimpleEntry<>(
                                    subSelectionName,
                                    updateJsonValue(subField, fieldTypeDefinition.asObject().getField(subField.getName()), jsonObject.get(subSelectionName))
                            );
                        }
                )
                .collect(JsonCollectors.toJsonObject());
    }

    public JsonValue updateJsonValue(Field field, FieldDefinition fieldDefinition, JsonValue jsonValue) {
        if (jsonValue == null) {
            return JsonValue.NULL;
        }
        if (jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return jsonValue;
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            if (fieldDefinition.getType().hasList()) {
                return jsonValue.asJsonArray().stream()
                        .map(value -> updateJsonObject(field, fieldDefinition, value.asJsonObject()))
                        .collect(JsonCollectors.toJsonArray());
            } else {
                return updateJsonObject(field, fieldDefinition, jsonValue.asJsonObject());
            }
        } else {
            return jsonValue;
        }
    }

    public Operation toBackupOperation(Operation operation, TransactionCompensator transactionCompensator) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return new Operation(OPERATION_QUERY_NAME)
                .setSelections(
                        operation.getFields().stream()
                                .flatMap(field -> toBackupField(operationType.getField(field.getName()), field, transactionCompensator))
                                .collect(Collectors.toList())
                );
    }

    public Stream<Field> toBackupField(FieldDefinition fieldDefinition, Field field, TransactionCompensator transactionCompensator) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            if (field.hasArgument(INPUT_VALUE_WHERE_NAME)) {
                return Stream.ofNullable(fieldDefinition.getArguments())
                        .flatMap(Collection::stream)
                        .filter(inputValue -> inputValue.getName().equals(INPUT_VALUE_WHERE_NAME))
                        .findFirst()
                        .flatMap(inputValue ->
                                Stream.ofNullable(field.getArguments())
                                        .flatMap(arguments ->
                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                        )
                                        .filter(ValueWithVariable::isObject)
                                        .map(valueWithVariable ->
                                                new Field(field.getName())
                                                        .setAlias(field.getAlias())
                                                        .setArguments(valueWithVariable.asObject())
                                                        .addSelections(argumentsToFields(fieldDefinition, field, transactionCompensator).collect(Collectors.toList()))
                                        )
                                        .findFirst()
                        )
                        .stream();
            } else if (field.hasArgument(idField.getName())) {
                return Stream.ofNullable(fieldDefinition.getArguments())
                        .flatMap(Collection::stream)
                        .filter(inputValue -> inputValue.getName().equals(idField.getName()))
                        .findFirst()
                        .flatMap(inputValue ->
                                Stream.ofNullable(field.getArguments())
                                        .flatMap(arguments ->
                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                        )
                                        .filter(ValueWithVariable::isObject)
                                        .map(valueWithVariable ->
                                                new Field(field.getName())
                                                        .setAlias(field.getAlias())
                                                        .setArguments(
                                                                Map.of(
                                                                        idField.getName(),
                                                                        Map.of(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME, valueWithVariable)
                                                                )
                                                        )
                                                        .addSelections(argumentsToFields(fieldDefinition, field, transactionCompensator).collect(Collectors.toList()))
                                        )
                                        .findFirst()
                        )
                        .stream();
            } else if (field.hasArgument(INPUT_VALUE_INPUT_NAME)) {
                return fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_INPUT_NAME).stream()
                        .flatMap(inputValue ->
                                Stream.ofNullable(field.getArguments())
                                        .flatMap(arguments ->
                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                        )
                                        .filter(ValueWithVariable::isObject)
                                        .map(ValueWithVariable::asObject)
                                        .flatMap(objectValueWithVariable ->
                                                inputObjectValueToFields(fieldDefinition.getName(), "/" + INPUT_VALUE_INPUT_NAME, fieldDefinition, inputValue, objectValueWithVariable, transactionCompensator)
                                        )
                        );
            } else if (field.hasArgument(INPUT_VALUE_LIST_NAME)) {
                return fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_LIST_NAME).stream()
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
                                                        .filter(index -> arrayValueWithVariable.getValueWithVariable(index).isObject())
                                                        .mapToObj(index ->
                                                                inputObjectValueToFields(fieldDefinition.getName(), "/" + INPUT_VALUE_LIST_NAME + "/" + index, fieldDefinition, listInputValue, arrayValueWithVariable.getValueWithVariable(index), transactionCompensator)
                                                        )
                                                        .flatMap(fieldStream -> fieldStream)
                                        )
                        );
            } else {
                transactionCompensator.addNewTypePath(fieldTypeDefinition.getName(), fieldDefinition.getName(), "/" + idField.getName());
                return argumentsToFields(fieldDefinition, field, transactionCompensator);
            }
        }
        return Stream.empty();
    }

    private Stream<Field> argumentsToFields(FieldDefinition fieldDefinition, Field field, TransactionCompensator transactionCompensator) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
        String alias = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
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
                                                                .map(valueWithVariable -> {
                                                                            Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                            if (subFieldTypeDefinition.isObject() && valueWithVariable.isObject()) {
                                                                                if (valueWithVariable.asObject().containsKey(idField.getName()) || valueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME)) {
                                                                                    Field argumentField = new Field(subFieldDefinition.getName())
                                                                                            .setAlias(alias + "_" + subFieldDefinition.getName())
                                                                                            .setSelections(inputObjectValueToFields(fieldDefinition.getName(), "", subFieldDefinition, inputValue, valueWithVariable, transactionCompensator).collect(Collectors.toList()))
                                                                                            .mergeSelection(new Field(fieldTypeDefinition.asObject().getIDFieldOrError().getName()));
                                                                                    return Stream.of(argumentField);
                                                                                } else {
                                                                                    return inputObjectValueToFields(fieldDefinition.getName(), "", subFieldDefinition, inputValue, valueWithVariable, transactionCompensator);
                                                                                }
                                                                            }
                                                                            return Stream.empty();
                                                                        }
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
                                                                                                .flatMap(subValueWithVariable -> {
                                                                                                            Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                                                            if (subFieldTypeDefinition.isObject() && subValueWithVariable.isObject()) {
                                                                                                                if (subValueWithVariable.asObject().containsKey(idField.getName()) || subValueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME)) {
                                                                                                                    Field inputValueField = new Field(subFieldDefinition.getName())
                                                                                                                            .setAlias(alias + "_" + INPUT_VALUE_INPUT_NAME + "_" + subFieldDefinition.getName())
                                                                                                                            .setSelections(inputObjectValueToFields(fieldDefinition.getName(), "", subFieldDefinition, subInputValue, subValueWithVariable, transactionCompensator).collect(Collectors.toList()))
                                                                                                                            .mergeSelection(new Field(fieldTypeDefinition.asObject().getIDFieldOrError().getName()));
                                                                                                                    return Stream.of(inputValueField);
                                                                                                                } else {
                                                                                                                    return inputObjectValueToFields(fieldDefinition.getName(), "", subFieldDefinition, subInputValue, subValueWithVariable, transactionCompensator);
                                                                                                                }
                                                                                                            }
                                                                                                            return Stream.empty();
                                                                                                        }
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
                                                                                                                .flatMap(subValueWithVariable -> {
                                                                                                                            Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                                                                            if (subFieldTypeDefinition.isObject() && subValueWithVariable.isObject()) {
                                                                                                                                if (subValueWithVariable.asObject().containsKey(idField.getName()) || subValueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME)) {
                                                                                                                                    Field inputValueField = new Field(subFieldDefinition.getName())
                                                                                                                                            .setAlias(alias + "_" + INPUT_VALUE_LIST_NAME + "_" + index + "_" + subFieldDefinition.getName())
                                                                                                                                            .setSelections(inputObjectValueToFields(alias, "/" + INPUT_VALUE_LIST_NAME + "/" + index, subFieldDefinition, subInputValue, subValueWithVariable, transactionCompensator).collect(Collectors.toList()))
                                                                                                                                            .mergeSelection(new Field(fieldTypeDefinition.asObject().getIDFieldOrError().getName()));
                                                                                                                                    return Stream.of(inputValueField);
                                                                                                                                } else {
                                                                                                                                    return inputObjectValueToFields(alias, "/" + INPUT_VALUE_LIST_NAME + "/" + index, subFieldDefinition, subInputValue, subValueWithVariable, transactionCompensator);

                                                                                                                                }
                                                                                                                            }
                                                                                                                            return Stream.empty();
                                                                                                                        }
                                                                                                                )
                                                                                                )
                                                                                )
                                                                )
                                                                .flatMap(fieldStream ->
                                                                        fieldStream
                                                                                .reduce((pre, cur) -> {
                                                                                            if (pre.getFields() != null && cur.getFields() != null) {
                                                                                                pre.mergeSelection(cur.getFields());
                                                                                            }
                                                                                            return pre;
                                                                                        }
                                                                                )
                                                                                .stream()
                                                                )
                                                )
                                )
                );
    }

    private Stream<Field> inputObjectValueToFields(String fieldName, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, TransactionCompensator transactionCompensator) {
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
        return inputValueTypeDefinition.asInputObject().getInputValues().stream()
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
                                                                        .flatMap(subValueWithVariable -> {
                                                                                    Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                                    if (subFieldTypeDefinition.isObject() && subValueWithVariable.isObject()) {
                                                                                        Stream<Field> fieldStream = inputObjectValueToFields(fieldName, path + "/" + index + "/" + subFieldDefinition.getName(), subFieldDefinition, subInputValue, subValueWithVariable, transactionCompensator);
                                                                                        if (subValueWithVariable.asObject().containsKey(idField.getName()) || subValueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME)) {
                                                                                            Field inputValueField = new Field(subFieldDefinition.getName())
                                                                                                    .setAlias(fieldName + "_" + path.replaceAll("/", "_") + "_" + index + "_" + subFieldDefinition.getName())
                                                                                                    .setSelections(fieldStream.collect(Collectors.toList()))
                                                                                                    .mergeSelection(new Field(fieldTypeDefinition.asObject().getIDFieldOrError().getName()));
                                                                                            return Stream.of(inputValueField);
                                                                                        } else {
                                                                                            transactionCompensator.addNewTypePath(fieldTypeDefinition.getName(), fieldName, path + "/" + index + "/" + subFieldTypeDefinition.getName());
                                                                                            return fieldStream;
                                                                                        }
                                                                                    }
                                                                                    return Stream.empty();
                                                                                }
                                                                        )
                                                        )
                                                        .flatMap(fieldStream ->
                                                                fieldStream
                                                                        .reduce((pre, cur) -> {
                                                                                    if (pre.getFields() != null && cur.getFields() != null) {
                                                                                        //TODO merge arguments
                                                                                        pre.mergeSelection(cur.getFields());
                                                                                    }
                                                                                    return pre;
                                                                                }
                                                                        )
                                                                        .stream()
                                                        );
                                            } else {
                                                return Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                        .flatMap(objectValue ->
                                                                Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                        )
                                                        .flatMap(subValueWithVariable -> {
                                                                    Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                    if (subFieldTypeDefinition.isObject() && subValueWithVariable.isObject()) {
                                                                        Stream<Field> fieldStream = inputObjectValueToFields(fieldName, path + "/" + subFieldDefinition.getName(), subFieldDefinition, subInputValue, subValueWithVariable, transactionCompensator);
                                                                        if (subValueWithVariable.asObject().containsKey(idField.getName()) || subValueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME)) {
                                                                            Field inputValueField = new Field(subFieldDefinition.getName())
                                                                                    .setAlias(fieldName + "_" + path.replaceAll("/", "_") + "_" + subFieldDefinition.getName())
                                                                                    .setSelections(fieldStream.collect(Collectors.toList()))
                                                                                    .mergeSelection(new Field(fieldTypeDefinition.asObject().getIDFieldOrError().getName()));
                                                                            return Stream.of(inputValueField);
                                                                        } else {
                                                                            transactionCompensator.addNewTypePath(fieldTypeDefinition.getName(), fieldName, path + "/" + subFieldTypeDefinition.getName());
                                                                            return fieldStream;
                                                                        }
                                                                    }
                                                                    return Stream.empty();
                                                                }
                                                        );
                                            }
                                        }
                                )
                );
    }

    public boolean hasFetchArguments(Operation operation) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return operation.getFields().stream()
                .anyMatch(field -> {
                            FieldDefinition fieldDefinition = operationType.getField(field.getName());
                            return fieldDefinition.isFetchField() || hasFetchArguments(fieldDefinition, field);
                        }
                );
    }

    private boolean hasFetchArguments(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        return Stream.ofNullable(fieldDefinition.getArguments())
                .flatMap(Collection::stream)
                .allMatch(inputValue ->
                        Stream.ofNullable(fieldTypeDefinition.asObject().getField(inputValue.getName()))
                                .anyMatch(subFieldDefinition ->
                                        Stream.ofNullable(field.getArguments())
                                                .flatMap(arguments ->
                                                        arguments.getArgumentOrEmpty(inputValue.getName())
                                                                .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                )
                                                .anyMatch(valueWithVariable ->
                                                        subFieldDefinition.isFetchField() ||
                                                                documentManager.getInputValueTypeDefinition(inputValue).isInputObject() &&
                                                                        hasFetchArguments(subFieldDefinition, inputValue, valueWithVariable)
                                                )
                                )
                ) ||
                fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_INPUT_NAME).stream()
                        .allMatch(inputValue ->
                                Stream.ofNullable(field.getArguments())
                                        .flatMap(arguments ->
                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                        )
                                        .filter(ValueWithVariable::isObject)
                                        .map(ValueWithVariable::asObject)
                                        .allMatch(objectValueWithVariable ->
                                                documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                                                        .allMatch(subInputValue ->
                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                        .anyMatch(subFieldDefinition ->
                                                                                objectValueWithVariable.getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                        .anyMatch(subValueWithVariable ->
                                                                                                subFieldDefinition.isFetchField() ||
                                                                                                        documentManager.getInputValueTypeDefinition(subInputValue).isInputObject() &&
                                                                                                                hasFetchArguments(subFieldDefinition, subInputValue, subValueWithVariable)
                                                                                        )
                                                                        )
                                                        )
                                        )
                        ) ||
                fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_LIST_NAME).stream()
                        .anyMatch(listInputValue ->
                                Stream.ofNullable(field.getArguments())
                                        .flatMap(arguments ->
                                                arguments.getArgumentOrEmpty(listInputValue.getName())
                                                        .or(() -> Optional.ofNullable(listInputValue.getDefaultValue())).stream()
                                        )
                                        .filter(ValueWithVariable::isArray)
                                        .map(ValueWithVariable::asArray)
                                        .anyMatch(arrayValueWithVariable ->
                                                IntStream.range(0, arrayValueWithVariable.size())
                                                        .mapToObj(index ->
                                                                documentManager.getInputValueTypeDefinition(listInputValue).asInputObject().getInputValues().stream()
                                                                        .anyMatch(subInputValue ->
                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                        .anyMatch(subFieldDefinition ->
                                                                                                arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                        .anyMatch(subValueWithVariable ->
                                                                                                                subFieldDefinition.isFetchField() ||
                                                                                                                        documentManager.getInputValueTypeDefinition(subInputValue).isInputObject() &&
                                                                                                                                hasFetchArguments(subFieldDefinition, subInputValue, subValueWithVariable)
                                                                                                        )
                                                                                        )
                                                                        )
                                                        )
                                                        .reduce(false, (pre, cur) -> pre || cur)
                                        )
                        );
    }

    private boolean hasFetchArguments(FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        return inputValueTypeDefinition.asInputObject().getInputValues().stream()
                .anyMatch(subInputValue ->
                        Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                .anyMatch(subFieldDefinition -> {
                                            if (fieldDefinition.getType().hasList()) {
                                                return IntStream.range(0, valueWithVariable.asArray().size())
                                                        .mapToObj(index ->
                                                                Stream.ofNullable(valueWithVariable.asArray().getValueWithVariable(index).asObject().getObjectValueWithVariable())
                                                                        .flatMap(objectValue ->
                                                                                Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                        )
                                                                        .anyMatch(subValueWithVariable ->
                                                                                subFieldDefinition.isFetchField() ||
                                                                                        documentManager.getInputValueTypeDefinition(subInputValue).isInputObject() &&
                                                                                                hasFetchArguments(subFieldDefinition, subInputValue, subValueWithVariable)
                                                                        )
                                                        )
                                                        .reduce(false, (pre, cur) -> pre || cur);
                                            } else {
                                                return Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                        .flatMap(objectValue ->
                                                                Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                        )
                                                        .anyMatch(subValueWithVariable ->
                                                                subFieldDefinition.isFetchField() ||
                                                                        documentManager.getInputValueTypeDefinition(subInputValue).isInputObject() &&
                                                                                hasFetchArguments(subFieldDefinition, subInputValue, subValueWithVariable)
                                                        );
                                            }
                                        }
                                )
                );
    }
}
