package io.graphoenix.jsonpath.expression.operators;

import io.graphoenix.jsonpath.expression.Expression;
import io.graphoenix.jsonpath.expression.NullValue;

public class IsNullExpression extends ComparisonOperator {
    public IsNullExpression(String element) {
        super(element, "==", new NullValue());
    }

    public IsNullExpression(Expression elementExpression) {
        this(elementExpression.toString());
    }
}
