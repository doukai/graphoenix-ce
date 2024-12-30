package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;

import java.util.Optional;

import static io.graphoenix.spi.constant.Hammurabi.*;

public interface Selection extends Definition {

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

    default Field asField() {
        return (Field) this;
    }

    default Fragment asFragment() {
        return (Fragment) this;
    }

    default boolean isInclude() {
        return !hasDirective(DIRECTIVE_INCLUDE_NAME) && !hasDirective(DIRECTIVE_HIDE_NAME) ||
                hasDirective(DIRECTIVE_INCLUDE_NAME) && Optional.ofNullable(getDirective(DIRECTIVE_INCLUDE_NAME))
                        .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_INCLUDE_ARGUMENT_IF_NAME))
                        .filter(ValueWithVariable::isBoolean)
                        .map(valueWithVariable -> valueWithVariable.asBoolean().getValue())
                        .orElse(true) ||
                hasDirective(DIRECTIVE_HIDE_NAME) && !Optional.ofNullable(getDirective(DIRECTIVE_SKIP_NAME))
                        .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_SKIP_ARGUMENT_IF_NAME))
                        .filter(ValueWithVariable::isBoolean)
                        .map(valueWithVariable -> valueWithVariable.asBoolean().getValue())
                        .orElse(true);
    }

    String toString();
}
