package io.graphoenix.core.handler.before;

import io.graphoenix.spi.graphql.common.Arguments;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
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
@Priority(100)
public class DefaultValueHandler implements OperationBeforeHandler {

    @Override
    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        Map<String, JsonValue> mergedVariables = mergeDefaultValues(operation.getVariableDefinitions(), variables);
        return Mono.just(
                operation
                        .setSelections(
                                replaceFieldsVariables(operation.getFields(), mergedVariables)
                                        .map(field -> (Field) field.setDirectives(replaceDirectivesVariables(field.getDirectives(), mergedVariables).collect(Collectors.toList())))
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
                .flatMap(field ->
                        Stream
                                .concat(
                                        Stream.of(
                                                field.setArguments(
                                                        Stream.ofNullable(field.getArguments())
                                                                .map(Arguments::getArguments)
                                                                .flatMap(jsonValues -> jsonValues.entrySet().stream())
                                                                .peek(valueWithVariableEntry -> {
                                                                            if (valueWithVariableEntry.getValue().isVariable()) {
                                                                                valueWithVariableEntry.setValue(ValueWithVariable.of(variables.get(valueWithVariableEntry.getKey())));
                                                                            }
                                                                        }
                                                                )
                                                                .collect(
                                                                        Collectors.toMap(
                                                                                Map.Entry::getKey,
                                                                                Map.Entry::getValue
                                                                        )
                                                                )
                                                )
                                        ),
                                        replaceFieldsVariables(field.getFields(), variables)
                                )
                );
    }

    public Stream<Directive> replaceDirectivesVariables(Collection<Directive> directives, Map<String, JsonValue> variables) {
        return Stream.ofNullable(directives)
                .flatMap(Collection::stream)
                .map(directive ->
                        directive.setArguments(
                                Stream.ofNullable(directive.getArguments())
                                        .map(Arguments::getArguments)
                                        .flatMap(jsonValues -> jsonValues.entrySet().stream())
                                        .peek(valueWithVariableEntry -> {
                                                    if (valueWithVariableEntry.getValue().isVariable()) {
                                                        valueWithVariableEntry.setValue(ValueWithVariable.of(variables.get(valueWithVariableEntry.getKey())));
                                                    }
                                                }
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

    public Map<String, JsonValue> mergeDefaultValues(Collection<VariableDefinition> variableDefinitions, Map<String, JsonValue> variables) {
        return Stream
                .concat(
                        Stream.ofNullable(variables)
                                .map(Map::entrySet)
                                .flatMap(Collection::stream),
                        Stream.ofNullable(variableDefinitions)
                                .flatMap(Collection::stream)
                                .filter(variableDefinition -> variableDefinition.getDefaultValue() != null && variables.get(variableDefinition.getVariable().getName()) == null)
                                .map(variableDefinition -> new AbstractMap.SimpleEntry<>(variableDefinition.getVariable().getName(), variableDefinition.getDefaultValue()))
                )
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (v1, v2) -> v2)
                );
    }
}
