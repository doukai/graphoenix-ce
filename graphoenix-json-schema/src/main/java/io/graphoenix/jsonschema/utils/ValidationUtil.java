package io.graphoenix.jsonschema.utils;

import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.common.ValueWithVariable;

import java.util.Optional;

public final class ValidationUtil {

    public static String DIRECTIVE_JSON_SCHEMA_NAME = "jsonSchema";
    public static String INPUT_JSON_SCHEMA_NAME = "JsonSchema";
    public static String INPUT_PROPERTY_NAME = "Property";

    public static Optional<Directive> getJsonSchemaDirective(Definition definition) {
        return Optional.ofNullable(definition.getDirective(DIRECTIVE_JSON_SCHEMA_NAME));
    }

    public static Optional<ObjectValueWithVariable> getJsonSchemaObjectArgument(Directive directive, String argumentName) {
        return Optional.ofNullable(directive)
                .map(jsonSchemaDirective -> jsonSchemaDirective.getArgument(argumentName))
                .map(ValueWithVariable::asObject);
    }

    public static Optional<ObjectValueWithVariable> getJsonSchemaObjectArgument(ObjectValueWithVariable objectValueWithVariable, String argumentName) {
        return Optional.ofNullable(objectValueWithVariable)
                .map(jsonSchemaObjectValueWithVariable -> jsonSchemaObjectValueWithVariable.getValueWithVariable(argumentName))
                .map(ValueWithVariable::asObject);
    }
}
