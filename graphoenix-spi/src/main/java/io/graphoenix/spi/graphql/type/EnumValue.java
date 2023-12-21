package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;

public class EnumValue extends AbstractDefinition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/EnumValue.stg");

    public EnumValue() {
        super();
    }

    public EnumValue(String name) {
        super(name);
    }

    public EnumValue(GraphqlParser.EnumValueDefinitionContext enumValueDefinitionContext) {
        super(enumValueDefinitionContext.description(), enumValueDefinitionContext.directives());
        setName(enumValueDefinitionContext.enumValue().enumValueName().getText());
    }


    public EnumValue(VariableElement variableElement) {
        super(variableElement);
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("enumValueDefinition");
        st.add("enumValue", this);
        return st.render();
    }
}
