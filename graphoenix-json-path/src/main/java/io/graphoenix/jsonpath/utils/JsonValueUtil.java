package io.graphoenix.jsonpath.utils;

import io.graphoenix.jsonpath.expression.*;
import io.graphoenix.spi.error.GraphQLErrorType;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.common.ValueWithVariable;

import java.util.stream.Collectors;

public final class JsonValueUtil {

    public static Expression valueToJsonValue(ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isNull()) {
            return new NullValue();
        } else if (valueWithVariable.isEnum()) {
            return enumValueToJsonValue(valueWithVariable.asEnum());
        } else if (valueWithVariable.isArray()) {
            return new ArrayValue(valueWithVariable.asArray().getValueWithVariables().stream().map(JsonValueUtil::valueToJsonValue).collect(Collectors.toList()));
        } else {
            return scalarValueToJsonValue(valueWithVariable);
        }
    }

    public static Expression enumValueToJsonValue(EnumValue enumValue) {
        return new StringValue(enumValue.getValue());
    }

    public static Expression scalarValueToJsonValue(ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isString()) {
            return new StringValue(valueWithVariable.asString().getValue());
        } else if (valueWithVariable.isInt()) {
            return new NumberValue(valueWithVariable.asInt().getValue());
        } else if (valueWithVariable.isFloat()) {
            return new NumberValue(valueWithVariable.asFloat().getValue());
        } else if (valueWithVariable.isBoolean()) {
            return new BooleanValue(valueWithVariable.asBoolean().getValue());
        }
        throw new GraphQLErrors(GraphQLErrorType.UNSUPPORTED_VALUE.bind(valueWithVariable.toString()));
    }
}
