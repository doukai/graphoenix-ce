package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public class InputValue extends AbstractDefinition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/InputValue.stg");
    private Type type;
    private String defaultValue;

    public InputValue() {
        super();
    }

    public InputValue(String name) {
        super(name);
    }

    public InputValue(GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext) {
        super(inputValueDefinitionContext.name(), inputValueDefinitionContext.description(), inputValueDefinitionContext.directives());
        this.type = Type.of(inputValueDefinitionContext.type());
        if (inputValueDefinitionContext.defaultValue() != null) {
            this.defaultValue = inputValueDefinitionContext.defaultValue().value().getText();
        }
    }

    public Type getType() {
        return type;
    }

    public InputValue setType(Type type) {
        this.type = type;
        return this;
    }

    public InputValue setType(String typeName) {
        this.type = Type.of(typeName);
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public InputValue setDefaultValue(Object defaultValue) {
        if (defaultValue != null) {
            if (this.type.getTypeName().getName().equals("String")) {
                this.defaultValue = "\"" + defaultValue + "\"";
            } else {
                this.defaultValue = String.valueOf(defaultValue);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("inputValueDefinition");
        st.add("inputValue", this);
        return st.render();
    }
}
