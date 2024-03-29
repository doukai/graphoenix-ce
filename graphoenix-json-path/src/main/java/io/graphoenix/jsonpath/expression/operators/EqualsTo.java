package io.graphoenix.jsonpath.expression.operators;

import io.graphoenix.jsonpath.expression.Expression;

public class EqualsTo extends ComparisonOperator {
    public EqualsTo(String element, Expression expression) {
        super(element, "==", expression);
    }

    public EqualsTo(Expression elementExpression, Expression expression) {
        this(elementExpression.toString(), expression);
    }
}
