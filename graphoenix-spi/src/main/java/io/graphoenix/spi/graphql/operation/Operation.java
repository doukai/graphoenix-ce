package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.type.VariableDefinition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class Operation extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/Operation.stg");
    private String operationType;
    private Map<String, VariableDefinition> variableDefinitionMap;
    private Collection<Selection> selections;

    public Operation() {
        super();
        this.variableDefinitionMap = new LinkedHashMap<>();
        this.selections = new LinkedHashSet<>();
    }

    public Operation(String name) {
        super(name);
        this.variableDefinitionMap = new LinkedHashMap<>();
        this.selections = new LinkedHashSet<>();
    }

    public Operation(GraphqlParser.OperationDefinitionContext operationDefinitionContext) {
        super(operationDefinitionContext.name(), null, operationDefinitionContext.directives());
        if (operationDefinitionContext.operationType() != null) {
            this.operationType = operationDefinitionContext.operationType().getText();
        } else {
            this.operationType = "query";
        }
        if (operationDefinitionContext.variableDefinitions() != null) {
            this.variableDefinitionMap = operationDefinitionContext.variableDefinitions().variableDefinition().stream()
                    .map(VariableDefinition::new)
                    .collect(
                            Collectors.toMap(
                                    variableDefinition -> variableDefinition.getVariable().getName(),
                                    variableDefinition -> variableDefinition,
                                    (x, y) -> y,
                                    LinkedHashMap::new
                            )
                    );
        }
        this.selections = operationDefinitionContext.selectionSet().selection().stream()
                .map(Selection::of)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public String getOperationType() {
        return operationType;
    }

    public Operation setOperationType(String operationType) {
        this.operationType = operationType;
        return this;
    }

    public VariableDefinition getVariableDefinition(String name) {
        return variableDefinitionMap.get(name);
    }

    public Collection<VariableDefinition> getVariableDefinitions() {
        return variableDefinitionMap.values();
    }

    public Operation setVariableDefinitions(Collection<VariableDefinition> variableDefinitions) {
        this.variableDefinitionMap.clear();
        return addVariableDefinitions(variableDefinitions);
    }

    public Operation addVariableDefinitions(Collection<VariableDefinition> variableDefinitions) {
        this.variableDefinitionMap.putAll(
                variableDefinitions.stream()
                        .collect(
                                Collectors.toMap(
                                        variableDefinition -> variableDefinition.getVariable().getName(),
                                        variableDefinition -> variableDefinition
                                )
                        )
        );
        return this;
    }

    public Operation addVariableDefinition(VariableDefinition variableDefinition) {
        this.variableDefinitionMap.put(variableDefinition.getVariable().getName(), variableDefinition);
        return this;
    }

    public Collection<Selection> getSelections() {
        return selections;
    }

    public Operation setSelections(Collection<Selection> selections) {
        this.selections = selections;
        return this;
    }

    public Operation addSelections(Collection<Selection> selections) {
        if (this.selections == null) {
            this.selections = new LinkedHashSet<>();
        }
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
        if (this.selections == null) {
            this.selections = new LinkedHashSet<>();
        }
        this.selections.add(selection);
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
