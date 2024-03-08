package io.graphoenix.jsonpath.expression.operators;

import io.graphoenix.jsonpath.expression.Expression;

public abstract class ComparisonOperator implements Expression {

    private final String path;

    private final String operator;

    private final Expression expression;

    private final boolean not;

    public ComparisonOperator(String path, String operator, Expression expression) {
        this.path = path;
        this.operator = operator;
        this.expression = expression;
        this.not = false;
    }

    public ComparisonOperator(String path, String operator, Expression expression, boolean not) {
        this.path = path;
        this.operator = operator;
        this.expression = expression;
        this.not = not;
    }

    @Override
    public String toString() {
        String comparisonOperator = path + " " + operator + " " + expression;
        if (not) {
            return "!(" + comparisonOperator + ")";
        } else {
            return comparisonOperator;
        }
    }
}
