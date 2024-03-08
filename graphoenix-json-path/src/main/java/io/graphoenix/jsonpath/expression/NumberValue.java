package io.graphoenix.jsonpath.expression;

public class NumberValue implements Expression {
    private final String value;

    public NumberValue(String value) {
        this.value = value;
    }

    public NumberValue(Number value) {
        this.value = String.valueOf(value);
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
