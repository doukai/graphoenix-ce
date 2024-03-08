package io.graphoenix.jsonpath.expression.operators;

import io.graphoenix.jsonpath.expression.ArrayValue;
import io.graphoenix.jsonpath.expression.Expression;

import java.util.List;

public class NotInExpression extends ComparisonOperator {
    public NotInExpression(String element, Expression expression) {
        super(element, "nin", expression);
    }

    public NotInExpression(String element, Expression... expressions) {
        super(element, "nin", new ArrayValue(expressions));
    }

    public NotInExpression(String element, List<Expression> expressionList) {
        super(element, "nin", new ArrayValue(expressionList));
    }
}
