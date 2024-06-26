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

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.subscription.handler.before.SubscriptionIDFieldsMergeHandler.SUBSCRIPTION_ID_FIELDS_MERGE_HANDLER_PRIORITY;

@ApplicationScoped
@Priority(SubscriptionArgumentsFieldsRegister.SUBSCRIPTION_ARGUMENTS_FIELDS_REGISTER_PRIORITY)
public class SubscriptionArgumentsFieldsRegister implements OperationBeforeHandler {

    public static final int SUBSCRIPTION_ARGUMENTS_FIELDS_REGISTER_PRIORITY = SUBSCRIPTION_ID_FIELDS_MERGE_HANDLER_PRIORITY + 100;

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
                .flatMap(field -> {
                            FieldDefinition fieldDefinition = operationType.getField(field.getName());
                            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
                            if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
                                return Mono.just(
                                        new AbstractMap.SimpleEntry<>(
                                                fieldTypeDefinition.getName(),
                                                argumentsToFields(fieldDefinition, field)
                                        )
                                );
                            }
                            return Mono.empty();
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
                                Stream
                                        .concat(
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
                                                                                        argumentField.setSelections(objectValueToFields(subFieldDefinition, inputValue, valueWithVariable));
                                                                                    }
                                                                                    return argumentField;
                                                                                }
                                                                        )
                                                        ),
                                                field.getArguments().containsKey(INPUT_VALUE_INCLUDE_DEPRECATED_NAME) ?
                                                        Stream.empty() :
                                                        Stream.of(new Field(FIELD_DEPRECATED_NAME).addDirective(new Directive(DIRECTIVE_HIDE_NAME)))
                                        )
                        )
                        .collect(Collectors.toList()),
                fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_EXS_NAME).stream()
                        .flatMap(exsInputValue ->
                                Stream.ofNullable(field.getArguments())
                                        .flatMap(arguments ->
                                                arguments.getArgumentOrEmpty(exsInputValue.getName())
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
                                                                                                arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariableOrEmpty(subInputValue.getName())
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
                                Stream
                                        .concat(
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
                                                        ),
                                                valueWithVariable.asObject().containsKey(INPUT_VALUE_INCLUDE_DEPRECATED_NAME) ?
                                                        Stream.empty() :
                                                        Stream.of(new Field(FIELD_DEPRECATED_NAME).addDirective(new Directive(DIRECTIVE_HIDE_NAME)))
                                        )
                        )
                        .collect(Collectors.toList()),
                inputValueTypeDefinition.asInputObject().getInputValueOrEmpty(INPUT_VALUE_EXS_NAME).stream()
                        .flatMap(exsInputValue ->
                                valueWithVariable.asObject().getValueWithVariableOrEmpty(exsInputValue.getName())
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
                                                                                                arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariableOrEmpty(subInputValue.getName())
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
