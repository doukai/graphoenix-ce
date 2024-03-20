package io.graphoenix.spi.utils;

import io.graphoenix.spi.graphql.common.Arguments;
import io.graphoenix.spi.graphql.common.ValueWithVariable;

import java.util.Optional;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_OPERATOR_INPUT_VALUE_SKIP_NULL_NAME;

public final class ValueWithVariableUtil {

    public static boolean isOr(Arguments arguments) {
        return Optional.ofNullable(arguments)
                .flatMap(result -> result.getArgumentOrEmpty(INPUT_VALUE_COND_NAME))
                .filter(ValueWithVariable::isEnum)
                .map(field -> field.asEnum().getValue().equals(INPUT_CONDITIONAL_INPUT_VALUE_OR))
                .orElse(false);
    }

    public static boolean isOr(ValueWithVariable valueWithVariable) {
        return Optional.ofNullable(valueWithVariable)
                .filter(ValueWithVariable::isObject)
                .map(ValueWithVariable::asObject)
                .flatMap(objectValueWithVariable -> objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_VALUE_COND_NAME))
                .filter(ValueWithVariable::isEnum)
                .map(field -> field.asEnum().getValue().equals(INPUT_CONDITIONAL_INPUT_VALUE_OR))
                .orElse(false);
    }

    public static boolean isNot(Arguments arguments) {
        return Optional.ofNullable(arguments)
                .flatMap(result -> result.getArgumentOrEmpty(INPUT_VALUE_NOT_NAME))
                .filter(ValueWithVariable::isBoolean)
                .map(field -> field.asBoolean().getValue())
                .orElse(false);
    }

    public static boolean isNot(ValueWithVariable valueWithVariable) {
        return Optional.ofNullable(valueWithVariable)
                .filter(ValueWithVariable::isObject)
                .map(ValueWithVariable::asObject)
                .flatMap(objectValueWithVariable -> objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_VALUE_NOT_NAME))
                .filter(ValueWithVariable::isBoolean)
                .map(field -> field.asBoolean().getValue())
                .orElse(false);
    }

    public static boolean skipNull(Arguments arguments) {
        return Optional.ofNullable(arguments)
                .flatMap(result -> result.getArgumentOrEmpty(INPUT_OPERATOR_INPUT_VALUE_SKIP_NULL_NAME))
                .filter(ValueWithVariable::isBoolean)
                .map(field -> field.asBoolean().getValue())
                .orElse(false);
    }

    public static boolean skipNull(ValueWithVariable valueWithVariable) {
        return Optional.ofNullable(valueWithVariable)
                .filter(ValueWithVariable::isObject)
                .map(ValueWithVariable::asObject)
                .flatMap(objectValueWithVariable -> objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_SKIP_NULL_NAME))
                .filter(ValueWithVariable::isBoolean)
                .map(field -> field.asBoolean().getValue())
                .orElse(false);
    }
}
