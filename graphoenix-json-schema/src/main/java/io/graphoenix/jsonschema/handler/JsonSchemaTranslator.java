package io.graphoenix.jsonschema.handler;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.type.InputObjectType;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.graphql.type.Type;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import org.tinylog.Logger;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;
import static jakarta.json.JsonValue.TRUE;

@ApplicationScoped
public class JsonSchemaTranslator {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;

    @Inject
    public JsonSchemaTranslator(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    public void writeToFiler(Filer filer) {
        documentManager.getDocument().getInputObjectTypes()
                .filter(inputObjectType -> !inputObjectType.isInputInterface())
                .map(this::inputObjectToJsonSchema)
                .forEach(entry -> {
                            try {
                                FileObject schema = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/schema/" + entry.getKey());
                                Writer writer = schema.openWriter();
                                writer.write(entry.getValue().toString());
                                writer.close();
                            } catch (IOException e) {
                                Logger.error(e);
                            }
                        }
                );
    }

    public Map.Entry<String, JsonValue> inputObjectToJsonSchema(InputObjectType inputObjectType) {
        ObjectType objectType = documentManager.getInputObjectBelong(inputObjectType);
        if (objectType != null) {
            String mutationTypeName = documentManager.getDocument().getMutationOperationTypeOrError().getName();
            String queryTypeName = documentManager.getDocument().getQueryOperationTypeOrError().getName();
            String subscriptionTypeName = documentManager.getDocument().getSubscriptionOperationTypeOrError().getName();
            if (inputObjectType.getName().endsWith(SUFFIX_INPUT)) {
                JsonArrayBuilder jsonArrayBuilder = jsonProvider.createArrayBuilder()
                        .add(
                                jsonProvider.createObjectBuilder()
                                        .add("properties", inputObjectToUpdateProperties(inputObjectType, objectType))
                                        .add("required", jsonProvider.createArrayBuilder().add(INPUT_VALUE_WHERE_NAME))
                        )
                        .add(
                                jsonProvider.createObjectBuilder()
                                        .add("properties", inputObjectToInsertProperties(inputObjectType, objectType))
                                        .add("required", buildRequired(objectType))
                        );

                JsonObjectBuilder builder = buildJsonSchemaBuilder(inputObjectType)
                        .add("$id", jsonProvider.createValue("#" + inputObjectType.getName()))
                        .add("type", jsonProvider.createValue("object"))
                        .add("anyOf", jsonArrayBuilder)
                        .add("additionalProperties", TRUE);

                return new AbstractMap.SimpleEntry<>(inputObjectType.getName(), builder.build());
            } else if (inputObjectType.getName().endsWith(SUFFIX_LIST + mutationTypeName + SUFFIX_ARGUMENTS)) {
                String operationFieldName = mutationTypeName + "_" + typeNameToFieldName(objectType.getName()) + SUFFIX_LIST + "_" + SUFFIX_ARGUMENTS;
                JsonArrayBuilder jsonArrayBuilder = jsonProvider.createArrayBuilder()
                        .add(
                                jsonProvider.createObjectBuilder()
                                        .add("properties", inputObjectToListProperties(inputObjectType))
                                        .add("required", jsonProvider.createArrayBuilder().add(INPUT_VALUE_LIST_NAME))
                        )
                        .add(
                                jsonProvider.createObjectBuilder()
                                        .add("properties", inputObjectToUpdateProperties(inputObjectType, objectType))
                                        .add("required", jsonProvider.createArrayBuilder().add(INPUT_VALUE_WHERE_NAME))
                        );

                JsonObjectBuilder builder = buildJsonSchemaBuilder(inputObjectType)
                        .add("$id", jsonProvider.createValue("#" + operationFieldName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("anyOf", jsonArrayBuilder)
                        .add("additionalProperties", TRUE);

                return new AbstractMap.SimpleEntry<>(operationFieldName, builder.build());
            } else if (inputObjectType.getName().endsWith(mutationTypeName + SUFFIX_ARGUMENTS)) {
                String operationFieldName = mutationTypeName + "_" + typeNameToFieldName(objectType.getName()) + "_" + SUFFIX_ARGUMENTS;
                JsonArrayBuilder jsonArrayBuilder = jsonProvider.createArrayBuilder()
                        .add(
                                jsonProvider.createObjectBuilder()
                                        .add("properties", inputObjectToInsertProperties(inputObjectType, objectType))
                                        .add("required", buildRequired(objectType))
                        )
                        .add(
                                jsonProvider.createObjectBuilder()
                                        .add("properties", inputObjectToInputProperties(inputObjectType))
                                        .add("required", jsonProvider.createArrayBuilder().add(INPUT_VALUE_INPUT_NAME))
                        )
                        .add(
                                jsonProvider.createObjectBuilder()
                                        .add("properties", inputObjectToUpdateProperties(inputObjectType, objectType))
                                        .add("required", jsonProvider.createArrayBuilder().add(INPUT_VALUE_WHERE_NAME))
                        );

                JsonObjectBuilder builder = buildJsonSchemaBuilder(inputObjectType)
                        .add("$id", jsonProvider.createValue("#" + operationFieldName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("anyOf", jsonArrayBuilder)
                        .add("additionalProperties", TRUE);

                return new AbstractMap.SimpleEntry<>(operationFieldName, builder.build());
            } else if (inputObjectType.getName().endsWith(SUFFIX_LIST + queryTypeName + SUFFIX_ARGUMENTS)) {
                String operationFieldName = queryTypeName + "_" + typeNameToFieldName(objectType.getName()) + SUFFIX_LIST + "_" + SUFFIX_ARGUMENTS;
                JsonObjectBuilder builder = buildJsonSchemaBuilder(inputObjectType)
                        .add("$id", jsonProvider.createValue("#" + operationFieldName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToProperties(inputObjectType))
                        .add("additionalProperties", TRUE)
                        .add("required", buildRequired(inputObjectType));

                return new AbstractMap.SimpleEntry<>(operationFieldName, builder.build());
            } else if (inputObjectType.getName().endsWith(queryTypeName + SUFFIX_ARGUMENTS)) {
                String operationFieldName = queryTypeName + "_" + typeNameToFieldName(objectType.getName()) + "_" + SUFFIX_ARGUMENTS;
                JsonObjectBuilder builder = buildJsonSchemaBuilder(inputObjectType)
                        .add("$id", jsonProvider.createValue("#" + operationFieldName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToProperties(inputObjectType))
                        .add("additionalProperties", TRUE)
                        .add("required", buildRequired(inputObjectType));

                return new AbstractMap.SimpleEntry<>(operationFieldName, builder.build());
            } else if (inputObjectType.getName().endsWith(SUFFIX_LIST + subscriptionTypeName + SUFFIX_ARGUMENTS)) {
                String operationFieldName = subscriptionTypeName + "_" + typeNameToFieldName(objectType.getName()) + SUFFIX_LIST + "_" + SUFFIX_ARGUMENTS;
                JsonObjectBuilder builder = buildJsonSchemaBuilder(inputObjectType)
                        .add("$id", jsonProvider.createValue("#" + operationFieldName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToProperties(inputObjectType))
                        .add("additionalProperties", TRUE)
                        .add("required", buildRequired(inputObjectType));

                return new AbstractMap.SimpleEntry<>(operationFieldName, builder.build());
            } else if (inputObjectType.getName().endsWith(subscriptionTypeName + SUFFIX_ARGUMENTS)) {
                String operationFieldName = subscriptionTypeName + "_" + typeNameToFieldName(objectType.getName()) + "_" + SUFFIX_ARGUMENTS;
                JsonObjectBuilder builder = buildJsonSchemaBuilder(inputObjectType)
                        .add("$id", jsonProvider.createValue("#" + operationFieldName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToProperties(inputObjectType))
                        .add("additionalProperties", TRUE)
                        .add("required", buildRequired(inputObjectType));

                return new AbstractMap.SimpleEntry<>(operationFieldName, builder.build());
            }
        }
        JsonObjectBuilder builder = buildJsonSchemaBuilder(inputObjectType)
                .add("$id", jsonProvider.createValue("#" + inputObjectType.getName()))
                .add("type", jsonProvider.createValue("object"))
                .add("properties", inputObjectToProperties(inputObjectType))
                .add("additionalProperties", TRUE)
                .add("required", buildRequired(inputObjectType));

        return new AbstractMap.SimpleEntry<>(inputObjectType.getName(), builder.build());
    }

    protected JsonValue buildRequired(InputObjectType inputObjectType) {
        return inputObjectType.getInputValues().stream()
                .filter(inputValue -> inputValue.getType().isNonNull())
                .map(inputValue -> jsonProvider.createValue(inputValue.getName()))
                .collect(JsonCollectors.toJsonArray());
    }

    protected JsonValue buildRequired(ObjectType objectType) {
        return objectType.getFields().stream()
                .filter(fieldDefinition -> fieldDefinition.getType().isNonNull())
                .map(fieldDefinition -> jsonProvider.createValue(fieldDefinition.getName()))
                .collect(JsonCollectors.toJsonArray());
    }

    protected JsonObject inputObjectToProperties(InputObjectType inputObjectType) {
        return inputObjectType.getInputValues().stream()
                .map(inputValue ->
                        new AbstractMap.SimpleEntry<>(inputValue.getName(), (JsonValue) fieldToProperty(inputValue.getType(), inputValue.getDirective(DIRECTIVE_JSON_SCHEMA_NAME)).build())
                )
                .collect(JsonCollectors.toJsonObject());
    }

    protected JsonObject inputObjectToUpdateProperties(InputObjectType inputObjectType, ObjectType objectType) {
        return inputObjectType.getInputValues().stream()
                .filter(inputValue -> !inputValue.getName().equals(INPUT_VALUE_LIST_NAME))
                .filter(inputValue -> !inputValue.getName().equals(INPUT_VALUE_INPUT_NAME))
                .map(inputValue -> {
                            if (INPUT_VALUE_WHERE_NAME.equals(inputValue.getName())) {
                                return new AbstractMap.SimpleEntry<>(inputValue.getName(), (JsonValue) buildType(inputValue.getType(), buildJsonSchemaBuilder(inputValue)).build());
                            } else {
                                return Optional.ofNullable(objectType.getField(inputValue.getName()))
                                        .map(fieldDefinition -> new AbstractMap.SimpleEntry<>(fieldDefinition.getName(), (JsonValue) fieldToProperty(fieldDefinition.getType(), fieldDefinition.getDirective(DIRECTIVE_JSON_SCHEMA_NAME)).build()))
                                        .orElseGet(() ->
                                                new AbstractMap.SimpleEntry<>(inputValue.getName(), fieldToProperty(inputValue.getType(), inputValue.getDirective(DIRECTIVE_JSON_SCHEMA_NAME)).build())
                                        );
                            }
                        }
                )
                .collect(JsonCollectors.toJsonObject());
    }

    protected JsonObject inputObjectToListProperties(InputObjectType inputObjectType) {
        return inputObjectType.getInputValues().stream()
                .filter(inputValue -> inputValue.getName().equals(INPUT_VALUE_LIST_NAME))
                .map(inputValue ->
                        new AbstractMap.SimpleEntry<>(inputValue.getName(), (JsonValue) fieldToProperty(inputValue.getType(), inputValue.getDirective(DIRECTIVE_JSON_SCHEMA_NAME)).build())
                )
                .collect(JsonCollectors.toJsonObject());
    }

    protected JsonObject inputObjectToInputProperties(InputObjectType inputObjectType) {
        return inputObjectType.getInputValues().stream()
                .filter(inputValue -> inputValue.getName().equals(INPUT_VALUE_INPUT_NAME))
                .map(inputValue ->
                        new AbstractMap.SimpleEntry<>(inputValue.getName(), (JsonValue) fieldToProperty(inputValue.getType(), inputValue.getDirective(DIRECTIVE_JSON_SCHEMA_NAME)).build())
                )
                .collect(JsonCollectors.toJsonObject());
    }

    protected JsonObject inputObjectToInsertProperties(InputObjectType inputObjectType, ObjectType objectType) {
        return inputObjectType.getInputValues().stream()
                .filter(inputValue -> !inputValue.getName().equals(INPUT_VALUE_WHERE_NAME))
                .filter(inputValue -> !inputValue.getName().equals(INPUT_VALUE_LIST_NAME))
                .filter(inputValue -> !inputValue.getName().equals(INPUT_VALUE_INPUT_NAME))
                .map(inputValue ->
                        Optional.ofNullable(objectType.getField(inputValue.getName()))
                                .map(fieldDefinition -> new AbstractMap.SimpleEntry<>(fieldDefinition.getName(), (JsonValue) fieldToProperty(fieldDefinition.getType(), fieldDefinition.getDirective(DIRECTIVE_JSON_SCHEMA_NAME)).build()))
                                .orElseGet(() ->
                                        new AbstractMap.SimpleEntry<>(inputValue.getName(), fieldToProperty(inputValue.getType(), inputValue.getDirective(DIRECTIVE_JSON_SCHEMA_NAME)).build())
                                )
                )
                .collect(JsonCollectors.toJsonObject());
    }

    protected JsonObjectBuilder fieldToProperty(Type type, Directive directive) {
        JsonObjectBuilder propertyBuilder = buildJsonSchemaBuilder(directive);
        if (type.isNonNull()) {
            if (type.asNonNullType().getType().isList()) {
                return propertyBuilder
                        .add("type", jsonProvider.createValue("array"))
                        .add(
                                "items",
                                fieldToProperty(
                                        type.asNonNullType().getType().asListType().getType(),
                                        directive != null ? directive.getArgument("items") : null
                                )
                        );
            } else {
                return buildType(type.asNonNullType().getType().asTypeName(), propertyBuilder);
            }
        } else {
            if (type.isList()) {
                return buildNullableType(
                        propertyBuilder
                                .add("type", jsonProvider.createValue("array"))
                                .add(
                                        "items",
                                        fieldToProperty(
                                                type.asListType().getType(),
                                                directive != null ? directive.getArgument("items") : null
                                        )
                                )
                );
            } else {
                return buildNullableType(buildType(type.asTypeName(), propertyBuilder));
            }
        }
    }

    protected JsonObjectBuilder fieldToProperty(Type type, ValueWithVariable valueWithVariable) {
        JsonObjectBuilder propertyBuilder = buildJsonSchemaBuilder(valueWithVariable);
        if (type.isNonNull()) {
            if (type.asNonNullType().getType().isList()) {
                return propertyBuilder
                        .add("type", jsonProvider.createValue("array"))
                        .add(
                                "items",
                                fieldToProperty(
                                        type.asNonNullType().getType().asListType().getType(),
                                        valueWithVariable != null ? valueWithVariable.asObject().getValueWithVariable("items") : null
                                )
                        );
            } else {
                return buildType(type.asNonNullType().getType().asTypeName(), propertyBuilder);
            }
        } else {
            if (type.isList()) {
                return buildNullableType(
                        propertyBuilder
                                .add("type", jsonProvider.createValue("array"))
                                .add(
                                        "items",
                                        fieldToProperty(
                                                type.asListType().getType(),
                                                valueWithVariable != null ? valueWithVariable.asObject().getValueWithVariable("items") : null
                                        )
                                )
                );
            } else {
                return buildNullableType(buildType(type.asTypeName(), propertyBuilder));
            }
        }
    }

    protected JsonObjectBuilder buildType(Type type, JsonObjectBuilder jsonObjectBuilder) {
        String fieldTypeName = type.getTypeName().getName();
        Definition definition = documentManager.getDocument().getDefinition(fieldTypeName);
        if (definition.isScalar()) {
            switch (fieldTypeName) {
                case "ID":
                case "String":
                case "Date":
                case "Time":
                case "DateTime":
                case "Timestamp":
                    jsonObjectBuilder.add("type", jsonProvider.createValue("string"));
                    break;
                case "Boolean":
                    jsonObjectBuilder.add("type", jsonProvider.createValue("boolean"));
                    break;
                case "Int":
                case "BigInteger":
                    jsonObjectBuilder.add("type", jsonProvider.createValue("integer"));
                    break;
                case "Float":
                case "BigDecimal":
                    jsonObjectBuilder.add("type", jsonProvider.createValue("number"));
                    break;
            }
        } else if (definition.isEnum()) {
            jsonObjectBuilder.add(
                    "enum",
                    definition.asEnum().getEnumValues().stream()
                            .map(enumValueDefinition -> jsonProvider.createValue(enumValueDefinition.getName()))
                            .collect(JsonCollectors.toJsonArray())
            );
        } else if (definition.isInputObject()) {
            jsonObjectBuilder.add("$ref", jsonProvider.createValue(fieldTypeName));
        } else if (definition.isObject()) {
            jsonObjectBuilder.add("$ref", jsonProvider.createValue(fieldTypeName + SUFFIX_INPUT));
        }
        return jsonObjectBuilder;
    }

    protected JsonObjectBuilder buildNullableType(JsonObjectBuilder typeBuilder) {
        return jsonProvider.createObjectBuilder()
                .add(
                        "anyOf",
                        jsonProvider
                                .createArrayBuilder()
                                .add(typeBuilder)
                                .add(jsonProvider.createObjectBuilder().add("type", jsonProvider.createValue("null")))
                );
    }

    protected JsonObjectBuilder buildJsonSchemaBuilder(Definition definition) {
        return Optional.ofNullable(definition.getDirective(DIRECTIVE_JSON_SCHEMA_NAME))
                .map(this::buildJsonSchemaBuilder)
                .orElseGet(jsonProvider::createObjectBuilder);
    }

    protected JsonObjectBuilder buildJsonSchemaBuilder(Directive directive) {
        return Optional.ofNullable(directive)
                .map(jsonSchema -> jsonProvider.createObjectBuilder(jsonSchema.getArguments().asJsonObject()))
                .orElseGet(jsonProvider::createObjectBuilder);
    }

    protected JsonObjectBuilder buildJsonSchemaBuilder(ValueWithVariable valueWithVariable) {
        return Optional.ofNullable(valueWithVariable)
                .map(jsonSchema -> jsonProvider.createObjectBuilder(jsonSchema.asJsonObject()))
                .orElseGet(jsonProvider::createObjectBuilder);
    }
}
