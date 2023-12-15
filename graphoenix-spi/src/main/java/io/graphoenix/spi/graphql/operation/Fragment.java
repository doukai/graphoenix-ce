package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.common.Directive;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class Fragment implements Selection {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/Fragment.stg");
    private String fragmentName;
    private Collection<Directive> directives;

    public Fragment(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public Fragment(GraphqlParser.SelectionContext selectionContext) {
        this.fragmentName = selectionContext.fragmentSpread().fragmentName().getText();
        if (selectionContext.fragmentSpread().directives() != null) {
            this.directives = selectionContext.fragmentSpread().directives().directive().stream()
                    .map(Directive::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public Fragment setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
        return this;
    }

    public Collection<Directive> getDirectives() {
        return directives;
    }

    public Fragment setDirectives(Collection<Directive> directives) {
        this.directives = directives;
        return this;
    }

    @Override
    public boolean isFragment() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("fragmentDefinition");
        st.add("fragment", this);
        return st.render();
    }
}
