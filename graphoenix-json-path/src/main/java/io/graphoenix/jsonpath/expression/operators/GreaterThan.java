package io.graphoenix.jsonpath.expression.operators;

import io.graphoenix.jsonpath.expression.Expression;

public class GreaterThan extends ComparisonOperator {
    public GreaterThan(String element, Expression expression) {
        super(element, ">", expression);
    }

    public GreaterThan(Expression elementExpression, Expression expression) {
        this(elementExpression.toString(), expression);
    }
}
