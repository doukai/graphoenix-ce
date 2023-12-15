package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class FragmentDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/FragmentDefinition.stg");
    private String fragmentName;
    private String typeName;
    private Collection<Selection> selections;
    private Collection<Directive> directives;

    public FragmentDefinition(GraphqlParser.FragmentDefinitionContext fragmentDefinitionContext) {
        this.fragmentName = fragmentDefinitionContext.fragmentName().getText();
        this.typeName = fragmentDefinitionContext.typeCondition().typeName().name().getText();
        if (fragmentDefinitionContext.directives() != null) {
            this.directives = fragmentDefinitionContext.directives().directive().stream()
                    .map(Directive::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        this.selections = fragmentDefinitionContext.selectionSet().selection().stream()
                .map(Selection::of)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public FragmentDefinition setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
        return this;
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

    public Collection<Directive> getDirectives() {
        return directives;
    }

    public FragmentDefinition setDirectives(Collection<Directive> directives) {
        this.directives = directives;
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
