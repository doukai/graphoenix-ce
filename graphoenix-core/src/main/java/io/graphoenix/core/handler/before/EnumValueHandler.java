package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.*;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Priority(400)
public class EnumValueHandler implements OperationBeforeHandler {

    private final DocumentManager documentManager;

    @Inject
    public EnumValueHandler(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Override
    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        return Mono.just(
                operation
                        .setSelections(
                                replaceEnumValue(documentManager.getOperationTypeOrError(operation), operation.getFields())
                                        .collect(Collectors.toList())

                        )
        );
    }

    public Stream<Field> replaceEnumValue(ObjectType objectType, Collection<Field> fields) {
        return Stream.ofNullable(fields)
                .flatMap(Collection::stream)
                .flatMap(field ->
                        Stream.of(
                                field.setArguments(
                                        Stream.ofNullable(field.getArguments())
                                                .map(Arguments::getArguments)
                                                .flatMap(jsonValues -> jsonValues.entrySet().stream())
                                                .peek(valueWithVariableEntry -> {
                                                            InputValue inputValue = objectType.getField(field.getName()).getArgument(valueWithVariableEntry.getKey());
                                                            valueWithVariableEntry.setValue(replaceEnumValue(inputValue, valueWithVariableEntry.getValue()));
                                                        }
                                                )
                                                .collect(
                                                        Collectors.toMap(
                                                                Map.Entry::getKey,
                                                                Map.Entry::getValue
                                                        )
                                                )
                                )
                        )
                );
    }

    public ValueWithVariable replaceEnumValue(InputValue inputValue, ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isArray()) {
            return new ArrayValueWithVariable(
                    valueWithVariable.asArray().getValueWithVariables().stream()
                            .map(item -> replaceEnumValue(inputValue, item))
                            .collect(Collectors.toList())
            );
        } else if (valueWithVariable.isObject()) {
            return new ObjectValueWithVariable(
                    valueWithVariable.asObject().getObjectValueWithVariable().entrySet().stream()
                            .peek(valueWithVariableEntry -> {
                                        InputValue fieldInputValue = documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValue(valueWithVariableEntry.getKey());
                                        valueWithVariableEntry.setValue(replaceEnumValue(fieldInputValue, valueWithVariableEntry.getValue()));
                                    }
                            )
            );
        } else {
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            if (inputValueTypeDefinition.isEnum() && (valueWithVariable.isString() || valueWithVariable.getValueType().equals(JsonValue.ValueType.STRING))) {
                return new EnumValue(valueWithVariable.asString().getValue());
            } else {
                return valueWithVariable;
            }
        }
    }
}
