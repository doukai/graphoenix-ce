package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class FragmentDefinition extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/FragmentDefinition.stg");
    private String typeName;
    private Collection<Selection> selections;

    public FragmentDefinition(GraphqlParser.FragmentDefinitionContext fragmentDefinitionContext) {
        super(null, fragmentDefinitionContext.directives());
        setName(fragmentDefinitionContext.fragmentName().getText());
        this.typeName = fragmentDefinitionContext.typeCondition().typeName().name().getText();
        this.selections = fragmentDefinitionContext.selectionSet().selection().stream()
                .map(Selection::of)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public String getTypeName() {
        return typeName;
    }

    public FragmentDefinition setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public Collection<Selection> getSelections() {
        return selections;
    }

    public FragmentDefinition setSelections(Collection<Selection> selections) {
        this.selections = selections;
        return this;
    }

    @Override
    public boolean isFragment() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("fragmentDefinitionDefinition");
        st.add("fragmentDefinition", this);
        return st.render();
    }
}
