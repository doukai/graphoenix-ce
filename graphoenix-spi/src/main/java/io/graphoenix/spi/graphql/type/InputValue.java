package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;

import static io.graphoenix.spi.constant.Hammurabi.SCALA_STRING_NAME;
import static io.graphoenix.spi.utils.ElementUtil.getDefaultValueFromElement;
import static io.graphoenix.spi.utils.ElementUtil.variableElementToTypeName;

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

    public InputValue(VariableElement variableElement, Types typeUtils) {
        super(variableElement);
        this.type = variableElementToTypeName(variableElement, typeUtils);
        this.defaultValue = getDefaultValueFromElement(variableElement);
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
            if (this.type.getTypeName().getName().equals(SCALA_STRING_NAME)) {
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
