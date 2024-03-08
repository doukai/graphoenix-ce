package io.graphoenix.jsonpath.expression;

public class NotExpression implements Expression {

    private final Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "!(" + expression + ")";
    }
}
