package io.graphoenix.spi.error;

public enum GraphQLErrorType {
    SYNTAX_ERROR(0, "graphql syntax error: %s line: %s column %s"),
    QUERY_TYPE_DEFINITION_NOT_EXIST(-1, "query type definition not exist"),
    MUTATION_TYPE_DEFINITION_NOT_EXIST(-2, "mutation type definition not exist"),
    SUBSCRIBE_TYPE_DEFINITION_NOT_EXIST(-3, "subscribe type definition not exist"),
    TYPE_DEFINITION_NOT_EXIST(-4, "type definition not exist: %s"),
    INPUT_OBJECT_DEFINITION_NOT_EXIST(-5, "input object definition not exist: %s"),
    FIELD_DEFINITION_NOT_EXIST(-6, "field definition not exist in type %s: %s"),
    ID_FIELD_DEFINITION_NOT_EXIST(-7, "id field definition not exist: %s"),
    INPUT_VALUE_NOT_EXIST(-8, "input value not exist in input object %s: %s"),
    ENUM_VALUE_NOT_EXIST(-9, "enum value not exist in enum %s: %s"),

    OPERATION_NOT_EXIST(-10, "operation not exist"),
    QUERY_NOT_EXIST(-11, "query not exist in operation"),
    MUTATION_NOT_EXIST(-12, "mutation not exist in operation"),
    SUBSCRIBE_NOT_EXIST(-13, "subscribe not exist in operation"),
    FRAGMENT_NOT_EXIST(-14, "fragment not exist: %s"),
    SELECTION_NOT_EXIST(-15, "selection definition not exist: %s"),
    OBJECT_SELECTION_NOT_EXIST(-16, "selection definition not exist in object type field: %s"),

    OPERATION_VARIABLE_NOT_EXIST(-20, "variable: %s not exist in operation: %s"),
    ARGUMENT_NOT_EXIST(-21, "argument not exist: %s"),
    MAP_FROM_OBJECT_FIELD_NOT_EXIST(-22, "map from object field definition not exist in type %s: %s"),
    MAP_TO_OBJECT_FIELD_NOT_EXIST(-23, "map to object field definition not exist in type %s: %s"),
    FETCH_FROM_OBJECT_FIELD_NOT_EXIST(-24, "fetch from object field definition not exist in type %s: %s"),
    FETCH_TO_OBJECT_FIELD_NOT_EXIST(-25, "fetch to object field definition not exist in type %s: %s"),

    UNSUPPORTED_OPERATION_TYPE(-30, "unsupported operation type"),
    UNSUPPORTED_FIELD_TYPE(-31, "unsupported field type: %s"),
    UNSUPPORTED_VALUE(-32, "unsupported value: %s"),
    UNSUPPORTED_DEFAULT_VALUE(-33, "unsupported default value: %s"),
    UNSUPPORTED_OPERATOR_VALUE(-34, "unsupported operator value: %s"),
    UNSUPPORTED_FUNCTION_NAME(-35, "unsupported function name: %s"),
    UNSUPPORTED_LOCATION_NAME(-36, "unsupported location name: %s"),
    UNSUPPORTED_SELECTION(-37, "unsupported selection: %s"),

    MAP_FROM_ARGUMENT_NOT_EXIST(-41, "from argument not exist in @map directive: %s"),
    MAP_TO_ARGUMENT_NOT_EXIST(-42, "to argument not exist in @map directive: %s"),
    MAP_WITH_TYPE_ARGUMENT_NOT_EXIST(-43, "map with type to field not exist: %s"),
    MAP_WITH_FROM_ARGUMENT_NOT_EXIST(-44, "map with type from field not exist: %s"),
    MAP_WITH_TO_ARGUMENT_NOT_EXIST(-45, "map with type to field not exist: %s"),

    FETCH_FROM_ARGUMENT_NOT_EXIST(-51, "from argument not exist in @fetch directive: %s"),
    FETCH_TO_ARGUMENT_NOT_EXIST(-52, "to argument not exist in @fetch directive: %s"),
    FETCH_PROTOCOL_ARGUMENT_NOT_EXIST(-53, "protocol argument not exist in @fetch directive: %s"),
    FETCH_WITH_TYPE_ARGUMENT_NOT_EXIST(-54, "fetch with type to field not exist: %s"),
    FETCH_WITH_FROM_ARGUMENT_NOT_EXIST(-55, "fetch with type from field not exist: %s"),
    FETCH_WITH_TO_ARGUMENT_NOT_EXIST(-56, "fetch with type to field not exist: %s"),
    FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST(-57, "fetch with type to object field not exist: %s"),

    PACKAGE_NAME_ARGUMENT_NOT_EXIST(-71, "name not exist in @package directive: %s"),
    CLASS_NAME_DIRECTIVE_NOT_EXIST(-72, "name not exist in @class directive: %s"),
    ANNOTATION_NAME_ARGUMENT_NOT_EXIST(-73, "name not exist in @annotation directive: %s"),
    GRPC_CLASS_NAME_ARGUMENT_NOT_EXIST(-74, "name not exist in @grpc directive: %s"),
    METHOD_NAME_ARGUMENT_NOT_EXIST(-75, "methodName not exist in @invoke directive: %s"),
    RETURN_CLASS_NAME_ARGUMENT_NOT_EXIST(-76, "returnClassName not exist in @invoke directive: %s"),

    FUNC_NAME_NOT_EXIST(-81, "@func name argument not exist in function field: %s"),
    FUNC_FIELD_NOT_EXIST(-82, "@func field argument not exist in function field: %s"),
    CONNECTION_FIELD_NOT_EXIST(-83, "@connection field argument not exist in connection field: %s"),
    CONNECTION_AGG_FIELD_NOT_EXIST(-84, "@connection agg argument not exist in connection field: %s"),

    RESOURCE_HAS_BEEN_UPDATED(-40900, "resource has been updated"),
    EXISTED_UNIQUE_VALUES(-40901, "existed unique values"),

    UNKNOWN(-99999, "unknown error");

    private final int code;
    private final String description;
    private Object[] variables;

    GraphQLErrorType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    public GraphQLErrorType bind(Object... variables) {
        this.variables = variables;
        return this;
    }

    @Override
    public String toString() {
        return code + ": " + String.format(description, variables);
    }
}
