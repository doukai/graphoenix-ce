package io.graphoenix.jsonpath.expression;

public class BooleanValue implements Expression {
    private final String value;

    public BooleanValue(String value) {
        this.value = value;
    }

    public BooleanValue(Boolean value) {
        this.value = String.valueOf(value);
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
