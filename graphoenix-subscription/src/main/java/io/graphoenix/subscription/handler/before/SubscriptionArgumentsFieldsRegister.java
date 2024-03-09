package io.graphoenix.subscription.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.graphoenix.subscription.handler.SubscriptionFilterFieldsManager;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.DIRECTIVE_HIDE_NAME;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_EXS_NAME;

@ApplicationScoped
@Priority(675)
public class SubscriptionArgumentsFieldsRegister implements OperationBeforeHandler {

    private final DocumentManager documentManager;

    private final SubscriptionFilterFieldsManager subscriptionFilterFieldsManager;

    @Inject
    public SubscriptionArgumentsFieldsRegister(DocumentManager documentManager, SubscriptionFilterFieldsManager subscriptionFilterFieldsManager) {
        this.documentManager = documentManager;
        this.subscriptionFilterFieldsManager = subscriptionFilterFieldsManager;
    }

    @Override
    public Mono<Operation> subscription(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux.fromIterable(operation.getFields())
                .filter(field -> documentManager.getFieldTypeDefinition(operationType.getField(field.getName())).isObject())
                .map(field -> {
                            FieldDefinition fieldDefinition = operationType.getField(field.getName());
                            String typeName = documentManager.getFieldTypeDefinition(fieldDefinition).getName();
                            return new AbstractMap.SimpleEntry<>(
                                    typeName,
                                    argumentsToFields(fieldDefinition, field)
                            );
                        }
                )
                .doOnNext(entry ->
                        subscriptionFilterFieldsManager.merge(
                                entry.getKey(),
                                entry.getValue(),
                                Field::mergeFields
                        )
                )
                .then()
                .thenReturn(operation);
    }

    private List<Field> argumentsToFields(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        return Field.mergeFields(
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
                                                        .map(valueWithVariable -> {
                                                                    Field argumentField = new Field(subFieldDefinition.getName()).addDirective(new Directive(DIRECTIVE_HIDE_NAME));
                                                                    Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                    if (subFieldTypeDefinition.isObject() && valueWithVariable.isObject()) {
                                                                        argumentField.setSelections(objectValueToFields(subFieldDefinition, inputValue, valueWithVariable));
                                                                    }
                                                                    return argumentField;
                                                                }
                                                        )
                                        )
                        )
                        .collect(Collectors.toList()),
                fieldDefinition.getArgument(INPUT_VALUE_EXS_NAME).stream()
                        .flatMap(exsInputValue ->
                                Stream.ofNullable(field.getArguments())
                                        .flatMap(arguments ->
                                                arguments.getArgument(exsInputValue.getName())
                                                        .or(() -> Optional.ofNullable(exsInputValue.getDefaultValue())).stream()
                                        )
                                        .filter(ValueWithVariable::isArray)
                                        .map(ValueWithVariable::asArray)
                                        .flatMap(arrayValueWithVariable ->
                                                IntStream.range(0, arrayValueWithVariable.size())
                                                        .mapToObj(index ->
                                                                documentManager.getInputValueTypeDefinition(exsInputValue).asInputObject().getInputValues().stream()
                                                                        .flatMap(subInputValue ->
                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                        .flatMap(subFieldDefinition ->
                                                                                                arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariable(subInputValue.getName())
                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                        .map(subValueWithVariable -> {
                                                                                                                    Field argumentField = new Field(subFieldDefinition.getName()).addDirective(new Directive(DIRECTIVE_HIDE_NAME));
                                                                                                                    Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                                                                    if (subFieldTypeDefinition.isObject() && subValueWithVariable.isObject()) {
                                                                                                                        argumentField.setSelections(objectValueToFields(subFieldDefinition, subInputValue, subValueWithVariable));
                                                                                                                    }
                                                                                                                    return argumentField;
                                                                                                                }
                                                                                                        )
                                                                                        )
                                                                        )
                                                        )
                                                        .flatMap(stream -> stream)
                                        )
                        )
                        .collect(Collectors.toList())
        );
    }

    private List<Field> objectValueToFields(FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        return Field.mergeFields(
                inputValueTypeDefinition.asInputObject().getInputValues().stream()
                        .flatMap(subInputValue ->
                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                        .flatMap(subFieldDefinition ->
                                                Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                        .flatMap(objectValue ->
                                                                Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                        )
                                                        .map(subValueWithVariable -> {
                                                                    Field inputValueField = new Field(subFieldDefinition.getName()).addDirective(new Directive(DIRECTIVE_HIDE_NAME));
                                                                    Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                    if (subFieldTypeDefinition.isObject() && subValueWithVariable.isObject()) {
                                                                        inputValueField.setSelections(objectValueToFields(subFieldDefinition, subInputValue, subValueWithVariable));
                                                                    }
                                                                    return inputValueField;
                                                                }
                                                        )
                                        )
                        )
                        .collect(Collectors.toList()),
                inputValueTypeDefinition.asInputObject().getInputValue(INPUT_VALUE_EXS_NAME).stream()
                        .flatMap(exsInputValue ->
                                valueWithVariable.asObject().getValueWithVariable(exsInputValue.getName())
                                        .or(() -> Optional.ofNullable(exsInputValue.getDefaultValue())).stream()
                                        .filter(ValueWithVariable::isArray)
                                        .map(ValueWithVariable::asArray)
                                        .flatMap(arrayValueWithVariable ->
                                                IntStream.range(0, arrayValueWithVariable.size())
                                                        .mapToObj(index ->
                                                                documentManager.getInputValueTypeDefinition(exsInputValue).asInputObject().getInputValues().stream()
                                                                        .flatMap(subInputValue ->
                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                        .flatMap(subFieldDefinition ->
                                                                                                arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariable(subInputValue.getName())
                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                        .map(subValueWithVariable -> {
                                                                                                                    Field inputValueField = new Field(subFieldDefinition.getName()).addDirective(new Directive(DIRECTIVE_HIDE_NAME));
                                                                                                                    Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                                                                                    if (subFieldTypeDefinition.isObject() && subValueWithVariable.isObject()) {
                                                                                                                        inputValueField.setSelections(objectValueToFields(subFieldDefinition, subInputValue, subValueWithVariable));
                                                                                                                    }
                                                                                                                    return inputValueField;
                                                                                                                }
                                                                                                        )
                                                                                        )
                                                                        )
                                                        )
                                                        .flatMap(stream -> stream)
                                        )
                        )
                        .collect(Collectors.toList())
        );
    }
}
