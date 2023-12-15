package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.type.VariableDefinition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Operation {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/Operation.stg");
    private String operationType;
    private String name;
    private Collection<VariableDefinition> variableDefinitions;
    private Collection<Directive> directives;
    private Collection<Field> fields;

    public Operation() {
    }

    public Operation(GraphqlParser.OperationDefinitionContext operationDefinitionContext) {
        if (operationDefinitionContext.operationType() != null) {
            this.operationType = operationDefinitionContext.operationType().getText();
        } else {
            this.operationType = "query";
        }
        if (operationDefinitionContext.name() != null) {
            this.name = operationDefinitionContext.name().getText();
        }
        if (operationDefinitionContext.variableDefinitions() != null) {
            this.variableDefinitions = operationDefinitionContext.variableDefinitions().variableDefinition().stream()
                    .map(VariableDefinition::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (operationDefinitionContext.directives() != null) {
            this.directives = operationDefinitionContext.directives().directive().stream()
                    .map(Directive::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        this.fields = operationDefinitionContext.selectionSet().selection().stream()
                .map(Field::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public String getOperationType() {
        return operationType;
    }

    public Operation setOperationType(String operationType) {
        this.operationType = operationType;
        return this;
    }

    public String getName() {
        return name;
    }

    public Operation setName(String name) {
        this.name = name;
        return this;
    }

    public Collection<VariableDefinition> getVariableDefinitions() {
        return variableDefinitions;
    }

    public Operation setVariableDefinitions(Collection<VariableDefinition> variableDefinitions) {
        this.variableDefinitions = variableDefinitions;
        return this;
    }

    public Operation addVariableDefinition(VariableDefinition variableDefinition) {
        if (this.variableDefinitions == null) {
            this.variableDefinitions = new LinkedHashSet<>();
        }
        this.variableDefinitions.add(variableDefinition);
        return this;
    }

    public Operation addVariableDefinitions(Stream<VariableDefinition> variableDefinitionStream) {
        if (this.variableDefinitions == null) {
            this.variableDefinitions = new LinkedHashSet<>();
        }
        this.variableDefinitions.addAll(variableDefinitionStream.collect(Collectors.toList()));
        return this;
    }

    public Collection<Directive> getDirectives() {
        return directives;
    }

    public Operation setDirectives(Collection<Directive> directives) {
        this.directives = directives;
        return this;
    }

    public Operation addDirective(Directive directive) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.add(directive);
        return this;
    }

    public Operation addDirectives(Collection<Directive> directives) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.addAll(directives);
        return this;
    }

    public Collection<Field> getFields() {
        return fields;
    }

    public Operation setFields(Collection<Field> fields) {
        this.fields = fields;
        return this;
    }

    public Operation addFields(Collection<Field> fields) {
        if (this.fields == null) {
            this.fields = new LinkedHashSet<>();
        }
        this.fields.addAll(fields);
        return this;
    }

    public Field getField(String name) {
        return this.fields.stream()
                .filter(field -> field.getAlias() != null && field.getAlias().equals(name) || field.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Operation addField(Field field) {
        if (this.fields == null) {
            this.fields = new LinkedHashSet<>();
        }
        this.fields.add(field);
        return this;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("operationDefinition");
        st.add("operation", this);
        return st.render();
    }
}
