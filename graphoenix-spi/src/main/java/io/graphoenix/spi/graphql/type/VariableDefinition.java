package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.Variable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class VariableDefinition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/VariableDefinition.stg");
    private Variable variable;
    private String typeName;
    private String defaultValue;
    private final Collection<Directive> directives = new LinkedHashSet<>();

    public VariableDefinition() {
    }

    public VariableDefinition(GraphqlParser.VariableDefinitionContext variableDefinitionContext) {
        if (variableDefinitionContext.variable() != null) {
            this.variable = new Variable(variableDefinitionContext.variable());
        }
        if (variableDefinitionContext.type() != null) {
            this.typeName = variableDefinitionContext.type().getText();
        }
        if (variableDefinitionContext.defaultValue() != null) {
            this.defaultValue = variableDefinitionContext.defaultValue().value().getText();
        }
        if (variableDefinitionContext.directives() != null) {
            setDirectives(
                    variableDefinitionContext.directives().directive().stream()
                            .map(Directive::new)
                            .collect(Collectors.toList())
            );
        }
    }

    public Variable getVariable() {
        return variable;
    }

    public VariableDefinition setVariable(Variable variable) {
        this.variable = variable;
        return this;
    }

    public VariableDefinition setVariable(String variableName) {
        this.variable = new Variable(variableName);
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public VariableDefinition setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public VariableDefinition setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public Collection<Directive> getDirectives() {
        return directives;
    }

    public VariableDefinition setDirectives(Collection<Directive> directives) {
        this.directives.clear();
        this.directives.addAll(directives);
        return this;
    }

    public VariableDefinition addDirectives(Collection<Directive> directives) {
        this.directives.addAll(directives);
        return this;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("variableDefinitionDefinition");
        st.add("variableDefinition", this);
        return st.render();
    }
}
