package io.graphoenix.core.handler;

import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonCollectors;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.DIRECTIVE_HIDE_NAME;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_LIST_NAME;

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

    private List<Field> mutationArgumentsToFields(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        return Stream
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
                                                                            Field argumentField = new Field(subFieldDefinition.getName()).addDirective(new Directive(DIRECTIVE_HIDE_NAME));
                                                                            Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                            if (subFieldTypeDefinition.isObject() && valueWithVariable.isObject()) {
                                                                                argumentField.setSelections(inputObjectValueToFields(subFieldDefinition, inputValue, valueWithVariable));
                                                                            }
                                                                            return argumentField;
                                                                        }
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
                                                                                                                .map(subValueWithVariable -> {
                                                                                                                            Field inputValueField = new Field(subFieldDefinition.getName()).addDirective(new Directive(DIRECTIVE_HIDE_NAME));
                                                                                                                            Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                                                                            if (subFieldTypeDefinition.isObject() && subValueWithVariable.isObject()) {
                                                                                                                                inputValueField.setSelections(inputObjectValueToFields(subFieldDefinition, subInputValue, subValueWithVariable));
                                                                                                                            }
                                                                                                                            return inputValueField;
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
                )
                .collect(Collectors.toList());
    }

    private List<Field> inputObjectValueToFields(FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
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
                                                                        .map(subValueWithVariable -> {
                                                                                    Field inputValueField = new Field(subFieldDefinition.getName()).addDirective(new Directive(DIRECTIVE_HIDE_NAME));
                                                                                    Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                                    if (subFieldTypeDefinition.isObject() && subValueWithVariable.isObject()) {
                                                                                        inputValueField.setSelections(inputObjectValueToFields(subFieldDefinition, subInputValue, subValueWithVariable));
                                                                                    }
                                                                                    return inputValueField;
                                                                                }
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
                                                        );
                                            } else {
                                                return Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                        .flatMap(objectValue ->
                                                                Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                        )
                                                        .map(subValueWithVariable -> {
                                                                    Field inputValueField = new Field(subFieldDefinition.getName()).addDirective(new Directive(DIRECTIVE_HIDE_NAME));
                                                                    Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                    if (subFieldTypeDefinition.isObject() && subValueWithVariable.isObject()) {
                                                                        inputValueField.setSelections(inputObjectValueToFields(subFieldDefinition, subInputValue, subValueWithVariable));
                                                                    }
                                                                    return inputValueField;
                                                                }
                                                        );
                                            }
                                        }
                                )
                )
                .collect(Collectors.toList());
    }
}
