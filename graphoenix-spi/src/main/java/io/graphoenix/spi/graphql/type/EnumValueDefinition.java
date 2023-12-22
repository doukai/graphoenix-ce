package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.VariableElement;

public class EnumValueDefinition extends AbstractDefinition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/EnumValueDefinition.stg");

    public EnumValueDefinition() {
        super();
    }

    public EnumValueDefinition(String name) {
        super(name);
    }

    public EnumValueDefinition(GraphqlParser.EnumValueDefinitionContext enumValueDefinitionContext) {
        super(enumValueDefinitionContext.description(), enumValueDefinitionContext.directives());
        setName(enumValueDefinitionContext.enumValue().enumValueName().getText());
    }


    public EnumValueDefinition(VariableElement variableElement) {
        super(variableElement);
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("enumValueDefinitionDefinition");
        st.add("enumValueDefinition", this);
        return st.render();
    }
}
