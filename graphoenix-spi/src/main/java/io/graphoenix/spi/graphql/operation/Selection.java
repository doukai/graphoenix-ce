package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;

public interface Selection {

    static Selection of(GraphqlParser.SelectionContext selectionContext) {
        if (selectionContext.field() != null) {
            return new Field(selectionContext);
        } else if (selectionContext.fragmentSpread() != null) {
            return new Fragment(selectionContext);
        }
        throw new RuntimeException("unsupported selection: " + selectionContext.getText());
    }

    default boolean isField() {
        return false;
    }

    default boolean isFragment() {
        return false;
    }

    String toString();
}
