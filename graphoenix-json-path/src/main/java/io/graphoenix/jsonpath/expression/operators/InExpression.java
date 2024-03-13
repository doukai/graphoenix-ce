package io.graphoenix.jsonpath.expression.operators;

import io.graphoenix.jsonpath.expression.ArrayValue;
import io.graphoenix.jsonpath.expression.Expression;

import java.util.List;

public class InExpression extends ComparisonOperator {
    public InExpression(String element, Expression expression) {
        super(element, "in", expression);
    }

    public InExpression(String element, Expression... expressions) {
        super(element, "in", new ArrayValue(expressions));
    }

    public InExpression(String element, List<Expression> expressionList) {
        super(element, "in", new ArrayValue(expressionList));
    }

    public InExpression(Expression elementExpression, Expression expression) {
        this(elementExpression.toString(), expression);
    }

    public InExpression(Expression elementExpression, Expression... expressions) {
        this(elementExpression.toString(), expressions);
    }

    public InExpression(Expression elementExpression, List<Expression> expressionList) {
        this(elementExpression.toString(), expressionList);
    }
}
