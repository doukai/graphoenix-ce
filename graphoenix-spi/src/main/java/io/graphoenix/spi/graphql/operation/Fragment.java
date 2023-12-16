package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public class Fragment extends AbstractDefinition implements Selection {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/Fragment.stg");
    private String fragmentName;

    public Fragment() {
        super();
    }

    public Fragment(String fragmentName) {
        super();
        this.fragmentName = fragmentName;
    }

    public Fragment(GraphqlParser.SelectionContext selectionContext) {
        super(null, selectionContext.fragmentSpread().directives());
        this.fragmentName = selectionContext.fragmentSpread().fragmentName().getText();
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public Fragment setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
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
