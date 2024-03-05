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
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.DIRECTIVE_HIDE_NAME;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_LIST_NAME;

@ApplicationScoped
@Priority(750)
public class MutationSubscriptionFieldsMergeHandler implements OperationBeforeHandler {

    private final DocumentManager documentManager;

    @Inject
    public MutationSubscriptionFieldsMergeHandler(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.just(
                operation.setSelections(
                        operation.getFields().stream()
                                .map(field -> field.mergeSelection(argumentsToFields(operationType.getField(field.getName()), field)))
                                .collect(Collectors.toList())
                )
        );
    }

    private List<Field> argumentsToFields(FieldDefinition fieldDefinition, Field field) {
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
                                ),
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

    private List<Field> objectValueToFields(FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
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
                                                                                        inputValueField.setSelections(objectValueToFields(subFieldDefinition, subInputValue, subValueWithVariable));
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
                                                                        inputValueField.setSelections(objectValueToFields(subFieldDefinition, subInputValue, subValueWithVariable));
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
