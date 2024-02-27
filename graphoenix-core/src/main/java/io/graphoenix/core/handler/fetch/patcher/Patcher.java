package io.graphoenix.core.handler.fetch.patcher;

public interface Patcher {

    default boolean isFetchPatcher() {
        return false;
    }

    default boolean isValuePatcher() {
        return false;
    }

    default boolean isFieldArgumentsPatcher() {
        return false;
    }
}
