package io.graphoenix.core.handler.before;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.EXCLUDE_INPUT;

@ApplicationScoped
public class SubscriptionMutationFieldsMergeHandler implements OperationBeforeHandler {

    @Inject
    private final DocumentManager documentManager;

    public SubscriptionMutationFieldsMergeHandler(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return OperationBeforeHandler.super.mutation(operation, variables);
    }

    private List<Field> argumentsToFields(ObjectType objectType, FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
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
                                                .flatMap(valueWithVariable -> {
                                                            Field argumentField = new Field(subFieldDefinition.getName());
                                                            Definition subFieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
                                                            if (subFieldTypeDefinition.isObject()) {
                                                                argumentField.setSelections(objectValueToFields(subFieldTypeDefinition.asObject(), subFieldDefinition, inputValue, valueWithVariable))
                                                            }
                                                            return argumentField;
                                                        }
                                                )
                                )
                )
                .collect(Collectors.toList());
    }


    private List<Field> objectValueToFields(ObjectType objectType, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
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
                                                                        .flatMap(subValueWithVariable ->


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


                                                        );
                                            }
                                        }
                                )
                )
                .collect(Collectors.toList());
    }
}
