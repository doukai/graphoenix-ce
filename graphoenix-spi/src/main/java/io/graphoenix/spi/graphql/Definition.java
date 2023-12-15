package io.graphoenix.spi.graphql;

public interface Definition {

    default boolean isSchema() {
        return false;
    }

    default boolean isScalar() {
        return false;
    }

    default boolean isEnum() {
        return false;
    }

    default boolean isObject() {
        return false;
    }

    default boolean isInterface() {
        return false;
    }

    default boolean isInputObject() {
        return false;
    }

    default boolean isDirective() {
        return false;
    }

    default boolean isOperation() {
        return false;
    }

    default boolean isFragment() {
        return false;
    }

    String toString();
}
