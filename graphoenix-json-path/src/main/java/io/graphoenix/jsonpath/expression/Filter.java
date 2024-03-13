package io.graphoenix.jsonpath.expression;

public class Filter implements Expression {

    private final String element;

    private final Expression expression;

    public Filter(String element, Expression expression) {
        this.element = element;
        this.expression = expression;
    }

    public Filter(Expression expression) {
        this.element = "$";
        this.expression = expression;
    }

    @Override
    public String toString() {
        return element + "[?" + expression + "]";
    }
}
