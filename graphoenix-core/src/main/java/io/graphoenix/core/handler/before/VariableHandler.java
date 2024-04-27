package io.graphoenix.core.handler.before;

import io.graphoenix.spi.graphql.common.*;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Fragment;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.VariableDefinition;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Priority(VariableHandler.VARIABLE_HANDLER_PRIORITY)
public class VariableHandler implements OperationBeforeHandler {

    public static final int VARIABLE_HANDLER_PRIORITY = 0;

    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        Map<String, JsonValue> mergedVariables = mergeDefaultValues(operation.getVariableDefinitions(), variables);
        return Mono.just(
                operation
                        .setSelections(
                                Stream
                                        .concat(
                                                replaceFieldsVariables(operation.getFields(), mergedVariables)
                                                        .map(field -> (Field) field.setDirectives(replaceDirectivesVariables(field.getDirectives(), mergedVariables).collect(Collectors.toList()))),
                                                operation.getFragments().stream()
                                                        .map(fragment -> (Fragment) fragment.setDirectives(replaceDirectivesVariables(fragment.getDirectives(), mergedVariables).collect(Collectors.toList())))
                                        )
                                        .collect(Collectors.toList())

                        )
                        .setDirectives(
                                replaceDirectivesVariables(operation.getDirectives(), mergedVariables)
                                        .collect(Collectors.toList())
                        )
        );
    }

    public Stream<Field> replaceFieldsVariables(Collection<Field> fields, Map<String, JsonValue> variables) {
        return Stream.ofNullable(fields)
                .flatMap(Collection::stream)
                .map(field ->
                        field
                                .setArguments(
                                        Stream.ofNullable(field.getArguments())
                                                .map(Arguments::getArguments)
                                                .flatMap(jsonValues -> jsonValues.entrySet().stream())
                                                .filter(valueWithVariableEntry -> !valueWithVariableEntry.getValue().isVariable() || variables.containsKey(valueWithVariableEntry.getValue().asVariable().getName()))
                                                .map(valueWithVariableEntry ->
                                                        new AbstractMap.SimpleEntry<>(valueWithVariableEntry.getKey(), replaceVariables(valueWithVariableEntry.getValue(), variables))
                                                )
                                                .collect(
                                                        Collectors.toMap(
                                                                Map.Entry::getKey,
                                                                Map.Entry::getValue
                                                        )
                                                )
                                )
                                .setSelections(
                                        Stream
                                                .concat(
                                                        replaceFieldsVariables(field.getFields(), variables)
                                                                .map(subField -> (Field) subField.setDirectives(replaceDirectivesVariables(subField.getDirectives(), variables).collect(Collectors.toList()))),
                                                        Stream.ofNullable(field.getFragments())
                                                                .flatMap(Collection::stream)
                                                                .map(fragment -> (Fragment) fragment.setDirectives(replaceDirectivesVariables(fragment.getDirectives(), variables).collect(Collectors.toList())))
                                                )
                                                .collect(Collectors.toList())
                                )
                );
    }

    public Stream<Directive> replaceDirectivesVariables(Collection<Directive> directives, Map<String, JsonValue> variables) {
        return Stream.ofNullable(directives)
                .flatMap(Collection::stream)
                .map(directive ->
                        directive
                                .setArguments(
                                        Stream.ofNullable(directive.getArguments())
                                                .map(Arguments::getArguments)
                                                .flatMap(jsonValues -> jsonValues.entrySet().stream())
                                                .filter(valueWithVariableEntry -> !valueWithVariableEntry.getValue().isVariable() || variables.containsKey(valueWithVariableEntry.getValue().asVariable().getName()))
                                                .map(valueWithVariableEntry ->
                                                        new AbstractMap.SimpleEntry<>(valueWithVariableEntry.getKey(), replaceVariables(valueWithVariableEntry.getValue(), variables))
                                                )
                                                .collect(
                                                        Collectors.toMap(
                                                                Map.Entry::getKey,
                                                                Map.Entry::getValue
                                                        )
                                                )
                                )
                );
    }

    public ValueWithVariable replaceVariables(ValueWithVariable valueWithVariable, Map<String, JsonValue> variables) {
        if (valueWithVariable.isVariable()) {
            return ValueWithVariable.of(variables.get(valueWithVariable.asVariable().getName()));
        } else if (valueWithVariable.isObject()) {
            return new ObjectValueWithVariable(
                    replaceObjectVariables(valueWithVariable.asObject(), variables)
            );
        } else if (valueWithVariable.isArray()) {
            return new ArrayValueWithVariable(
                    valueWithVariable.asArray().getValueWithVariables().stream()
                            .map(item -> replaceVariables(item, variables))
                            .collect(Collectors.toList())
            );
        }
        return valueWithVariable;
    }

    public Map<String, ValueWithVariable> replaceObjectVariables(ObjectValueWithVariable objectValueWithVariable, Map<String, JsonValue> variables) {
        return objectValueWithVariable.getObjectValueWithVariable().entrySet().stream()
                .filter(valueWithVariableEntry -> !valueWithVariableEntry.getValue().isVariable() || variables.containsKey(valueWithVariableEntry.getValue().asVariable().getName()))
                .map(valueWithVariableEntry ->
                        new AbstractMap.SimpleEntry<>(valueWithVariableEntry.getKey(), replaceVariables(valueWithVariableEntry.getValue(), variables))
                )
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        )
                );
    }

    public Map<String, JsonValue> mergeDefaultValues(Collection<VariableDefinition> variableDefinitions, Map<String, JsonValue> variables) {
        return Stream
                .concat(
                        Stream.ofNullable(variables)
                                .map(Map::entrySet)
                                .flatMap(Collection::stream),
                        Stream.ofNullable(variableDefinitions)
                                .flatMap(Collection::stream)
                                .filter(variableDefinition -> variableDefinition.getDefaultValue() != null && variables.containsKey(variableDefinition.getVariable().getName()))
                                .map(variableDefinition -> new AbstractMap.SimpleEntry<>(variableDefinition.getVariable().getName(), variableDefinition.getDefaultValue()))
                )
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (v1, v2) -> v2
                        )
                );
    }
}
