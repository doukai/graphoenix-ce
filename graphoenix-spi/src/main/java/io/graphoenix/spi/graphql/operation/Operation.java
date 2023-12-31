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

import static io.graphoenix.spi.constant.Hammurabi.OPERATION_QUERY_NAME;

public class Operation extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/Operation.stg");
    private String operationType;
    private final Map<String, VariableDefinition> variableDefinitionMap = new LinkedHashMap<>();
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
        this.variableDefinitionMap.put(variableDefinition.getVariable().getName(), variableDefinition);
        return this;
    }

    public Collection<Selection> getSelections() {
        return selections;
    }

    public Collection<Field> getFields() {
        return selections.stream()
                .filter(Selection::isField)
                .map(Selection::asField)
                .collect(Collectors.toList());
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
