package io.graphoenix.jsonschema.utils;

import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ArrayValueWithVariable;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public final class ValidationUtil {

    public static String DIRECTIVE_JSON_SCHEMA_NAME = "jsonSchema";
    public static String INPUT_JSON_SCHEMA_NAME = "JsonSchema";
    public static String INPUT_PROPERTY_NAME = "Property";

    public static Optional<Directive> getJsonSchemaDirective(Definition definition) {
        return Stream.ofNullable(definition.getDirectives())
                .flatMap(Collection::stream)
                .filter(directive -> directive.getName().equals(DIRECTIVE_JSON_SCHEMA_NAME))
                .findFirst();
    }

    public static Optional<String> getJsonSchemaStringArgument(Directive directive, String argumentName) {
        return directive.getArguments().getArguments().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isString())
                .findFirst()
                .map(entry -> entry.getValue().asString().getValue());
    }

    public static Optional<Float> getJsonSchemaFloatArgument(Directive directive, String argumentName) {
        return directive.getArguments().getArguments().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isFloat())
                .findFirst()
                .map(entry -> entry.getValue().asFloat().getValue().floatValue());
    }

    public static Optional<Integer> getJsonSchemaIntArgument(Directive directive, String argumentName) {
        return directive.getArguments().getArguments().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isInt())
                .findFirst()
                .map(entry -> entry.getValue().asInt().getValue().intValue());
    }

    public static Optional<Boolean> getJsonSchemaBooleanArgument(Directive directive, String argumentName) {
        return directive.getArguments().getArguments().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isBoolean())
                .findFirst()
                .map(entry -> entry.getValue().asBoolean().getValue());
    }

    public static Optional<ObjectValueWithVariable> getJsonSchemaObjectArgument(Directive directive, String argumentName) {
        return directive.getArguments().getArguments().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isObject())
                .findFirst()
                .map(entry -> entry.getValue().asObject());
    }

    public static Optional<ArrayValueWithVariable> getJsonSchemaArrayArgument(Directive directive, String argumentName) {
        return directive.getArguments().getArguments().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isArray())
                .findFirst()
                .map(entry -> entry.getValue().asArray());
    }

    public static Optional<String> getJsonSchemaStringArgument(ObjectValueWithVariable objectValueWithVariable, String argumentName) {
        return objectValueWithVariable.getObjectValueWithVariable().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isString())
                .findFirst()
                .map(entry -> entry.getValue().asString().getValue());
    }

    public static Optional<Float> getJsonSchemaFloatArgument(ObjectValueWithVariable objectValueWithVariable, String argumentName) {
        return objectValueWithVariable.getObjectValueWithVariable().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isFloat())
                .findFirst()
                .map(entry -> entry.getValue().asFloat().getValue().floatValue());
    }

    public static Optional<Integer> getJsonSchemaIntArgument(ObjectValueWithVariable objectValueWithVariable, String argumentName) {
        return objectValueWithVariable.getObjectValueWithVariable().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isInt())
                .findFirst()
                .map(entry -> entry.getValue().asInt().getValue().intValue());
    }

    public static Optional<Boolean> getJsonSchemaBooleanArgument(ObjectValueWithVariable objectValueWithVariable, String argumentName) {
        return objectValueWithVariable.getObjectValueWithVariable().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isBoolean())
                .findFirst()
                .map(entry -> entry.getValue().asBoolean().getValue());
    }

    public static Optional<ObjectValueWithVariable> getJsonSchemaObjectArgument(ObjectValueWithVariable objectValueWithVariable, String argumentName) {
        return objectValueWithVariable.getObjectValueWithVariable().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isObject())
                .findFirst()
                .map(entry -> entry.getValue().asObject());
    }

    public static Optional<ArrayValueWithVariable> getJsonSchemaArrayArgument(ObjectValueWithVariable objectValueWithVariable, String argumentName) {
        return objectValueWithVariable.getObjectValueWithVariable().entrySet().stream()
                .filter(entry -> entry.getKey().equals(argumentName))
                .filter(entry -> entry.getValue().isArray())
                .findFirst()
                .map(entry -> entry.getValue().asArray());
    }
}
