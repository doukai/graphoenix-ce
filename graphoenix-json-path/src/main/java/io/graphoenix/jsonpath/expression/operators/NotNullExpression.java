package io.graphoenix.jsonpath.expression.operators;

import io.graphoenix.jsonpath.expression.Expression;
import io.graphoenix.jsonpath.expression.NullValue;

public class NotNullExpression extends ComparisonOperator {
    public NotNullExpression(String element) {
        super(element, "!=", new NullValue());
    }

    public NotNullExpression(Expression elementExpression) {
        this(elementExpression.toString());
    }
}
