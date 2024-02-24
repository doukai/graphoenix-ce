package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.type.VariableDefinition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.OPERATION_QUERY_NAME;

public class Operation extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/Operation.stg");
    private String operationType;
    private Map<String, VariableDefinition> variableDefinitionMap;
    private final Collection<Selection> selections = new LinkedHashSet<>();

    public Operation() {
        super();
    }

    public Operation(String name) {
        super(name);
    }

    public Operation(GraphqlParser.OperationDefinitionContext operationDefinitionContext) {
        super(operationDefinitionContext.name(), null, operationDefinitionContext.directives());
        if (operationDefinitionContext.operationType() != null) {
            this.operationType = operationDefinitionContext.operationType().getText();
        } else {
            this.operationType = OPERATION_QUERY_NAME;
        }
        if (operationDefinitionContext.variableDefinitions() != null) {
            setVariableDefinitions(
                    operationDefinitionContext.variableDefinitions().variableDefinition().stream()
                            .map(VariableDefinition::new)
                            .collect(Collectors.toList())
            );
        }
        setSelections(
                operationDefinitionContext.selectionSet().selection().stream()
                        .map(Selection::of)
                        .collect(Collectors.toList())
        );
    }

    public String getOperationType() {
        return operationType;
    }

    public Operation setOperationType(String operationType) {
        this.operationType = operationType;
        return this;
    }

    public VariableDefinition getVariableDefinition(String name) {
        return Optional.ofNullable(variableDefinitionMap).map(stringVariableDefinitionMap -> stringVariableDefinitionMap.get(name)).orElse(null);
    }

    public Collection<VariableDefinition> getVariableDefinitions() {
        return Optional.ofNullable(variableDefinitionMap).map(Map::values).orElse(null);
    }

    public Operation setVariableDefinitions(Collection<VariableDefinition> variableDefinitions) {
        if (variableDefinitions != null && variableDefinitions.size() > 0) {
            this.variableDefinitionMap = variableDefinitions.stream()
                    .collect(
                            Collectors.toMap(
                                    variableDefinition -> variableDefinition.getVariable().getName(),
                                    variableDefinition -> variableDefinition,
                                    (x, y) -> y,
                                    LinkedHashMap::new
                            )
                    );
        }
        return this;
    }

    public Operation addVariableDefinitions(Collection<VariableDefinition> variableDefinitions) {
        if (this.variableDefinitionMap == null) {
            this.variableDefinitionMap = new LinkedHashMap<>();
        }
        this.variableDefinitionMap.putAll(
                (Map<? extends String, ? extends VariableDefinition>) variableDefinitions.stream()
                        .collect(
                                Collectors.toMap(
                                        variableDefinition -> variableDefinition.getVariable().getName(),
                                        variableDefinition -> variableDefinition,
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                        )
        );
        return this;
    }

    public Operation addVariableDefinition(VariableDefinition variableDefinition) {
        if (this.variableDefinitionMap == null) {
            this.variableDefinitionMap = new LinkedHashMap<>();
        }
        this.variableDefinitionMap.put(variableDefinition.getVariable().getName(), variableDefinition);
        return this;
    }

    public Collection<Selection> getSelections() {
        return selections;
    }

    public Collection<Field> getFields() {
        return Optional.ofNullable(selections)
                .map(fields ->
                        fields.stream()
                                .filter(Selection::isField)
                                .map(Selection::asField)
                                .collect(Collectors.toList())
                )
                .orElse(null);
    }

    public Collection<Fragment> getFragments() {
        return Optional.ofNullable(selections)
                .map(fragments ->
                        fragments.stream()
                                .filter(Selection::isFragment)
                                .map(Selection::asFragment)
                                .collect(Collectors.toList())
                )
                .orElse(null);
    }

    public Operation setSelections(Collection<? extends Selection> selections) {
        this.selections.clear();
        this.selections.addAll(selections);
        return this;
    }

    public Operation addSelections(Collection<? extends Selection> selections) {
        this.selections.addAll(selections);
        return this;
    }

    public Field getField(String name) {
        return this.selections.stream()
                .filter(Selection::isField)
                .map(selection -> (Field) selection)
                .filter(field -> field.getAlias() != null && field.getAlias().equals(name) || field.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Operation addSelection(Selection selection) {
        this.selections.add(selection);
        return this;
    }

    public Operation mergeSelection(Collection<Field> fields) {
        setSelections(
                Stream.ofNullable(fields)
                        .flatMap(Collection::stream)
                        .reduce(
                                this.selections,
                                (pre, cur) ->
                                        Stream
                                                .concat(
                                                        Stream.ofNullable(pre)
                                                                .flatMap(Collection::stream)
                                                                .filter(Selection::isField)
                                                                .map(selection -> (Field) selection)
                                                                .map(original -> {
                                                                            if (Optional.ofNullable(original.getAlias()).orElseGet(original::getName)
                                                                                    .equals(Optional.ofNullable(cur.getAlias()).orElseGet(cur::getName)))
                                                                                if (cur.getFields() != null) {
                                                                                    return original.mergeSelection(cur.getFields());
                                                                                }
                                                                            return original;
                                                                        }
                                                                ),
                                                        Stream.ofNullable(cur)
                                                                .filter(field ->
                                                                        Stream.ofNullable(pre)
                                                                                .flatMap(Collection::stream)
                                                                                .filter(Selection::isField)
                                                                                .map(selection -> (Field) selection)
                                                                                .noneMatch(original ->
                                                                                        Optional.ofNullable(original.getAlias()).orElseGet(original::getName)
                                                                                                .equals(Optional.ofNullable(field.getAlias()).orElseGet(field::getName))
                                                                                )
                                                                )
                                                )
                                                .collect(Collectors.toCollection(LinkedHashSet::new)),
                                (x, y) -> y
                        )
        );
        return this;
    }

    @Override
    public boolean isOperation() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("operationDefinition");
        st.add("operation", this);
        return st.render();
    }
}
