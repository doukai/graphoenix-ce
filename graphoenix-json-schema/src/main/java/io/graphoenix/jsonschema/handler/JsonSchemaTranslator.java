package io.graphoenix.jsonschema.handler;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ArrayValueWithVariable;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputObjectType;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.graphql.type.Type;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;

import java.io.StringWriter;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.jsonschema.utils.ValidationUtil.*;
import static io.graphoenix.spi.constant.Hammurabi.*;
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

    public Stream<Map.Entry<String, String>> objectToJsonSchemaStringStream(InputObjectType inputObjectType) {
        return inputObjectToJsonSchema(inputObjectType)
                .map(jsonValue -> {
                            StringWriter stringWriter = new StringWriter();
                            jsonProvider.createWriter(stringWriter).write(jsonValue);
                            return new AbstractMap.SimpleEntry<>(jsonValue.asJsonObject().getString("$id").substring(1), stringWriter.toString());
                        }
                );
    }

    public Map.Entry<String, String> operationObjectToJsonSchemaString(GraphqlParser.OperationTypeDefinitionContext operationTypeDefinitionContext) {
        JsonValue jsonValue = operationObjectToJsonSchema(operationTypeDefinitionContext);
        StringWriter stringWriter = new StringWriter();
        jsonProvider.createWriter(stringWriter).write(jsonValue);
        return new AbstractMap.SimpleEntry<>(jsonValue.asJsonObject().getString("$id").substring(1), stringWriter.toString());
    }

    public Stream<JsonValue> inputObjectToJsonSchema(InputObjectType inputObjectType) {
        if (inputObjectType.getName().endsWith(SUFFIX_INPUT)) {
            String objectTypeName = inputObjectType.getName().substring(0, inputObjectType.getName().lastIndexOf(SUFFIX_INPUT));
            Definition definition = documentManager.getDocument().getDefinition(objectTypeName);
            if (definition.isObject()) {
                JsonObjectBuilder updateBuilder = getJsonSchemaDirective(inputObjectType)
                        .map(this::buildValidation)
                        .orElseGet(jsonProvider::createObjectBuilder)
                        .add("$id", jsonProvider.createValue("#" + inputObjectType.getName() + "_update"))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToUpdateProperties(inputObjectType, definition.isObject()))
                        .add("additionalProperties", TRUE)
                        .add("required", jsonProvider.createArrayBuilder().add(INPUT_VALUE_WHERE_NAME));

                JsonObjectBuilder builder = getJsonSchemaDirective(inputObjectType)
                        .map(this::buildValidation)
                        .orElseGet(jsonProvider::createObjectBuilder)
                        .add("$id", jsonProvider.createValue("#" + inputObjectType.name().getText()))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToInsertProperties(inputObjectType, definition.isObject()))
                        .add("additionalProperties", TRUE)
                        .add("required", buildRequired(definition.isObject()));
                return Stream.of(updateBuilder.build(), builder.build());
            }
        } else if (inputObjectType.name().getText().endsWith(LIST_SUFFIX + mutationTypeName + ARGUMENTS_SUFFIX)) {
            String objectTypeName = inputObjectType.name().getText().substring(0, inputObjectType.name().getText().lastIndexOf(LIST_SUFFIX + mutationTypeName + ARGUMENTS_SUFFIX));
            if (documentManager.isObject(objectTypeName)) {
                GraphqlParser.ObjectTypeDefinitionContext objectTypeDefinitionContext = documentManager.getObject(objectTypeName).orElseThrow(() -> new GraphQLErrors(TYPE_NOT_EXIST.bind(objectTypeName)));
                String schemaName = mutationTypeName + "_" + NAME_UTIL.getSchemaFieldName(objectTypeName) + LIST_SUFFIX + "_" + ARGUMENTS_SUFFIX;
                JsonObjectBuilder updateBuilder = getJsonSchemaDirective(inputObjectType.directives())
                        .map(this::buildValidation)
                        .orElseGet(jsonProvider::createObjectBuilder)
                        .add("$id", jsonProvider.createValue("#" + schemaName + "_update"))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToUpdateProperties(inputObjectType, objectTypeDefinitionContext))
                        .add("additionalProperties", TRUE)
                        .add("required", jsonProvider.createArrayBuilder().add(WHERE_INPUT_NAME));

                JsonObjectBuilder builder = getJsonSchemaDirective(inputObjectType.directives())
                        .map(this::buildValidation)
                        .orElseGet(jsonProvider::createObjectBuilder)
                        .add("$id", jsonProvider.createValue("#" + schemaName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToListProperties(inputObjectType))
                        .add("additionalProperties", TRUE)
                        .add("required", jsonProvider.createArrayBuilder().add(LIST_INPUT_NAME));
                return Stream.of(updateBuilder.build(), builder.build());
            }
        } else if (inputObjectType.name().getText().endsWith(mutationTypeName + ARGUMENTS_SUFFIX)) {
            String objectTypeName = inputObjectType.name().getText().substring(0, inputObjectType.name().getText().lastIndexOf(mutationTypeName + ARGUMENTS_SUFFIX));
            if (documentManager.isObject(objectTypeName)) {
                GraphqlParser.ObjectTypeDefinitionContext objectTypeDefinitionContext = documentManager.getObject(objectTypeName).orElseThrow(() -> new GraphQLErrors(TYPE_NOT_EXIST.bind(objectTypeName)));
                String schemaName = mutationTypeName + "_" + NAME_UTIL.getSchemaFieldName(objectTypeName) + "_" + ARGUMENTS_SUFFIX;
                JsonObjectBuilder updateBuilder = getJsonSchemaDirective(inputObjectType.directives())
                        .map(this::buildValidation)
                        .orElseGet(jsonProvider::createObjectBuilder)
                        .add("$id", jsonProvider.createValue("#" + schemaName + "_update"))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToUpdateProperties(inputObjectType, objectTypeDefinitionContext))
                        .add("additionalProperties", TRUE)
                        .add("required", jsonProvider.createArrayBuilder().add(WHERE_INPUT_NAME));

                JsonObjectBuilder builder = getJsonSchemaDirective(inputObjectType.directives())
                        .map(this::buildValidation)
                        .orElseGet(jsonProvider::createObjectBuilder)
                        .add("$id", jsonProvider.createValue("#" + schemaName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToInsertProperties(inputObjectType, objectTypeDefinitionContext))
                        .add("additionalProperties", TRUE)
                        .add("required", buildRequired(objectTypeDefinitionContext));
                return Stream.of(updateBuilder.build(), builder.build());
            }
        } else if (inputObjectType.name().getText().endsWith(LIST_SUFFIX + queryTypeName + ARGUMENTS_SUFFIX)) {
            String objectTypeName = inputObjectType.name().getText().substring(0, inputObjectType.name().getText().lastIndexOf(LIST_SUFFIX + queryTypeName + ARGUMENTS_SUFFIX));
            if (documentManager.isObject(objectTypeName)) {
                String schemaName = queryTypeName + "_" + NAME_UTIL.getSchemaFieldName(objectTypeName) + LIST_SUFFIX + "_" + ARGUMENTS_SUFFIX;
                JsonObjectBuilder builder = getJsonSchemaDirective(inputObjectType.directives())
                        .map(this::buildValidation)
                        .orElseGet(jsonProvider::createObjectBuilder)
                        .add("$id", jsonProvider.createValue("#" + schemaName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToProperties(inputObjectType))
                        .add("additionalProperties", TRUE)
                        .add("required", buildRequired(inputObjectType));
                return Stream.of(builder.build());
            }
        } else if (inputObjectType.name().getText().endsWith(queryTypeName + ARGUMENTS_SUFFIX)) {
            String objectTypeName = inputObjectType.name().getText().substring(0, inputObjectType.name().getText().lastIndexOf(queryTypeName + ARGUMENTS_SUFFIX));
            if (documentManager.isObject(objectTypeName)) {
                String schemaName = queryTypeName + "_" + NAME_UTIL.getSchemaFieldName(objectTypeName) + "_" + ARGUMENTS_SUFFIX;
                JsonObjectBuilder builder = getJsonSchemaDirective(inputObjectType.directives())
                        .map(this::buildValidation)
                        .orElseGet(jsonProvider::createObjectBuilder)
                        .add("$id", jsonProvider.createValue("#" + schemaName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToProperties(inputObjectType))
                        .add("additionalProperties", TRUE)
                        .add("required", buildRequired(inputObjectType));
                return Stream.of(builder.build());
            }
        } else if (inputObjectType.name().getText().endsWith(LIST_SUFFIX + subscriptionTypeName + ARGUMENTS_SUFFIX)) {
            String objectTypeName = inputObjectType.name().getText().substring(0, inputObjectType.name().getText().lastIndexOf(LIST_SUFFIX + subscriptionTypeName + ARGUMENTS_SUFFIX));
            if (documentManager.isObject(objectTypeName)) {
                String schemaName = subscriptionTypeName + "_" + NAME_UTIL.getSchemaFieldName(objectTypeName) + LIST_SUFFIX + "_" + ARGUMENTS_SUFFIX;
                JsonObjectBuilder builder = getJsonSchemaDirective(inputObjectType.directives())
                        .map(this::buildValidation)
                        .orElseGet(jsonProvider::createObjectBuilder)
                        .add("$id", jsonProvider.createValue("#" + schemaName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToProperties(inputObjectType))
                        .add("additionalProperties", TRUE)
                        .add("required", buildRequired(inputObjectType));
                return Stream.of(builder.build());
            }
        } else if (inputObjectType.name().getText().endsWith(subscriptionTypeName + ARGUMENTS_SUFFIX)) {
            String objectTypeName = inputObjectType.name().getText().substring(0, inputObjectType.name().getText().lastIndexOf(subscriptionTypeName + ARGUMENTS_SUFFIX));
            if (documentManager.isObject(objectTypeName)) {
                String schemaName = subscriptionTypeName + "_" + NAME_UTIL.getSchemaFieldName(objectTypeName) + "_" + ARGUMENTS_SUFFIX;
                JsonObjectBuilder builder = getJsonSchemaDirective(inputObjectType.directives())
                        .map(this::buildValidation)
                        .orElseGet(jsonProvider::createObjectBuilder)
                        .add("$id", jsonProvider.createValue("#" + schemaName))
                        .add("type", jsonProvider.createValue("object"))
                        .add("properties", inputObjectToProperties(inputObjectType))
                        .add("additionalProperties", TRUE)
                        .add("required", buildRequired(inputObjectType));
                return Stream.of(builder.build());
            }
        }
        JsonObjectBuilder builder = getJsonSchemaDirective(inputObjectType.directives())
                .map(this::buildValidation)
                .orElseGet(jsonProvider::createObjectBuilder)
                .add("$id", jsonProvider.createValue("#" + inputObjectType.name().getText()))
                .add("type", jsonProvider.createValue("object"))
                .add("properties", inputObjectToProperties(inputObjectType))
                .add("additionalProperties", TRUE)
                .add("required", buildRequired(inputObjectType));
        return Stream.of(builder.build());
    }

    public JsonValue operationObjectToJsonSchema(GraphqlParser.OperationTypeDefinitionContext operationTypeDefinitionContext) {
        String operationTypeName = operationTypeDefinitionContext.typeName().name().getText();
        GraphqlParser.ObjectTypeDefinitionContext objectTypeDefinitionContext = documentManager.getObject(operationTypeName)
                .orElseThrow(() -> new GraphQLErrors(GraphQLErrorType.TYPE_NOT_EXIST.bind(operationTypeDefinitionContext.typeName().name().getText())));
        JsonObjectBuilder jsonSchemaBuilder = jsonProvider.createObjectBuilder();

        JsonObjectBuilder builder = jsonSchemaBuilder.add("$id", jsonProvider.createValue("#" + objectTypeDefinitionContext.name().getText()))
                .add("type", jsonProvider.createValue("object"))
                .add("additionalProperties", TRUE);

        JsonObjectBuilder propertiesBuilder = jsonProvider.createObjectBuilder();
        objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                .filter(fieldDefinitionContext -> fieldDefinitionContext.argumentsDefinition() != null)
                .forEach(fieldDefinitionContext -> {
                            if (documentManager.isInvokeField(fieldDefinitionContext)) {
                                propertiesBuilder.add(
                                        fieldDefinitionContext.name().getText(),
                                        buildNullableType(
                                                jsonProvider.createObjectBuilder()
                                                        .add("$ref", operationTypeName + "_" + fieldDefinitionContext.name().getText() + "_" + ARGUMENTS_SUFFIX)
                                        )
                                );
                            } else {
                                if (operationTypeDefinitionContext.operationType().MUTATION() != null) {
                                    JsonArrayBuilder jsonArrayBuilder = jsonProvider.createArrayBuilder()
                                            .add(jsonProvider.createObjectBuilder()
                                                    .add("$ref", operationTypeName + "_" + fieldDefinitionContext.name().getText() + "_" + ARGUMENTS_SUFFIX)
                                            )
                                            .add(jsonProvider.createObjectBuilder()
                                                    .add("$ref", operationTypeName + "_" + fieldDefinitionContext.name().getText() + "_" + ARGUMENTS_SUFFIX + "_update")
                                            );
                                    propertiesBuilder.add(
                                            fieldDefinitionContext.name().getText(),
                                            buildNullableType(
                                                    jsonProvider.createObjectBuilder().add("anyOf", jsonArrayBuilder)
                                            )
                                    );
                                } else {
                                    propertiesBuilder.add(
                                            fieldDefinitionContext.name().getText(),
                                            buildNullableType(
                                                    jsonProvider.createObjectBuilder()
                                                            .add("$ref", operationTypeName + "_" + fieldDefinitionContext.name().getText() + "_" + ARGUMENTS_SUFFIX)
                                            )
                                    );
                                }
                            }
                        }
                );
        builder.add("properties", propertiesBuilder);
        return builder.build();
    }

    protected JsonValue buildRequired(FieldDefinition fieldDefinition) {
        return fieldDefinition.getArguments().stream()
                .filter(inputValue -> inputValue.getType().isNonNull())
                .map(inputValue -> jsonProvider.createValue(inputValue.getName()))
                .collect(JsonCollectors.toJsonArray());
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

    protected JsonObjectBuilder inputObjectToProperties(GraphqlParser.InputObjectTypeDefinitionContext inputObjectTypeDefinitionContext) {
        JsonObjectBuilder propertiesBuilder = jsonProvider.createObjectBuilder();
        inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition()
                .forEach(inputValueDefinitionContext ->
                        propertiesBuilder.add(inputValueDefinitionContext.name().getText(), fieldToProperty(inputValueDefinitionContext.type(), getJsonSchemaDirective(inputValueDefinitionContext.directives()).orElse(null)))
                );
        return propertiesBuilder;
    }

    protected JsonObjectBuilder inputObjectToUpdateProperties(InputObjectType inputObjectType, ObjectType objectType) {
        JsonObjectBuilder propertiesBuilder = jsonProvider.createObjectBuilder();
        inputObjectType.getInputValues().stream()
                .filter(inputValue -> !inputValue.getName().equals(INPUT_VALUE_LIST_NAME))
                .forEach(inputValue -> {
                            if (INPUT_VALUE_WHERE_NAME.equals(inputValue.getName())) {
                                JsonObjectBuilder propertyBuilder = getJsonSchemaDirective(inputValue)
                                        .map(this::buildValidation)
                                        .orElseGet(jsonProvider::createObjectBuilder);
                                propertiesBuilder.add(inputValue.getName(), buildType(inputValue.getType(), propertyBuilder));
                            } else {
                                Optional.ofNullable(objectType.getField(inputValue.getName()))
                                        .ifPresentOrElse(
                                                fieldDefinition ->
                                                        propertiesBuilder.add(fieldDefinition.getName(), fieldToProperty(fieldDefinitionFromInputValueDefinition.get().type(), getJsonSchemaDirective(fieldDefinitionFromInputValueDefinition.get().directives()).orElse(null)));

                                        );
                                Optional<GraphqlParser.FieldDefinitionContext> fieldDefinitionFromInputValueDefinition = documentManager.getFieldDefinitionFromInputValueDefinition(objectTypeDefinitionContext, inputValueDefinitionContext);
                                if (fieldDefinitionFromInputValueDefinition.isPresent()) {
                                    propertiesBuilder.add(fieldDefinitionFromInputValueDefinition.get().name().getText(), fieldToProperty(fieldDefinitionFromInputValueDefinition.get().type(), getJsonSchemaDirective(fieldDefinitionFromInputValueDefinition.get().directives()).orElse(null)));
                                } else {
                                    propertiesBuilder.add(inputValueDefinitionContext.name().getText(), fieldToProperty(inputValueDefinitionContext.type(), getJsonSchemaDirective(inputValueDefinitionContext.directives()).orElse(null)));
                                }
                            }
                        }
                );
        return propertiesBuilder;
    }

    protected JsonObjectBuilder inputObjectToListProperties(GraphqlParser.InputObjectTypeDefinitionContext inputObjectTypeDefinitionContext) {
        JsonObjectBuilder propertiesBuilder = jsonProvider.createObjectBuilder();
        inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().stream()
                .filter(inputValueDefinitionContext -> inputValueDefinitionContext.name().getText().equals(LIST_INPUT_NAME))
                .forEach(inputValueDefinitionContext ->
                        propertiesBuilder.add(inputValueDefinitionContext.name().getText(), fieldToProperty(inputValueDefinitionContext.type(), getJsonSchemaDirective(inputValueDefinitionContext.directives()).orElse(null)))
                );
        return propertiesBuilder;
    }

    protected JsonObjectBuilder inputObjectToInsertProperties(GraphqlParser.InputObjectTypeDefinitionContext inputObjectTypeDefinitionContext, GraphqlParser.ObjectTypeDefinitionContext objectTypeDefinitionContext) {
        JsonObjectBuilder propertiesBuilder = jsonProvider.createObjectBuilder();
        inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().stream()
                .filter(inputValueDefinitionContext -> !inputValueDefinitionContext.name().getText().equals(WHERE_INPUT_NAME))
                .filter(inputValueDefinitionContext -> !inputValueDefinitionContext.name().getText().equals(LIST_INPUT_NAME))
                .forEach(inputValueDefinitionContext -> {
                            Optional<GraphqlParser.FieldDefinitionContext> fieldDefinitionFromInputValueDefinition = documentManager.getFieldDefinitionFromInputValueDefinition(objectTypeDefinitionContext, inputValueDefinitionContext);
                            if (fieldDefinitionFromInputValueDefinition.isPresent()) {
                                propertiesBuilder.add(fieldDefinitionFromInputValueDefinition.get().name().getText(), fieldToProperty(fieldDefinitionFromInputValueDefinition.get().type(), getJsonSchemaDirective(fieldDefinitionFromInputValueDefinition.get().directives()).orElse(null)));
                            } else {
                                propertiesBuilder.add(inputValueDefinitionContext.name().getText(), fieldToProperty(inputValueDefinitionContext.type(), getJsonSchemaDirective(inputValueDefinitionContext.directives()).orElse(null)));
                            }
                        }
                );
        return propertiesBuilder;
    }

    protected JsonObjectBuilder fieldToProperty(Type type, Directive directive) {
        JsonObjectBuilder propertyBuilder = Optional.ofNullable(directive)
                .map(this::buildValidation)
                .orElseGet(jsonProvider::createObjectBuilder);
        if (type.isList()) {
            propertyBuilder.add("type", jsonProvider.createValue("array"));
            GraphqlParser.ObjectValueWithVariableContext objectValueWithVariableContext = Optional.ofNullable(directive)
                    .flatMap(directive1 -> getJsonSchemaObjectArgument(directive1, "items"))
                    .orElse(null);
            propertyBuilder.add("items", fieldToProperty(type.asListType().getType(), objectValueWithVariableContext));
            return propertyBuilder;
        } else if (typeContext.nonNullType() != null) {
            if (typeContext.nonNullType().listType() != null) {
                propertyBuilder.add("type", jsonProvider.createValue("array"));
                GraphqlParser.ObjectValueWithVariableContext objectValueWithVariableContext = Optional.ofNullable(directiveContext)
                        .flatMap(arrayDirectiveContext -> getJsonSchemaObjectArgument(arrayDirectiveContext, "items"))
                        .orElse(null);
                propertyBuilder.add("items", fieldToProperty(typeContext.nonNullType().listType().type(), objectValueWithVariableContext));
                return propertyBuilder;
            } else {
                return buildType(typeContext.nonNullType().typeName(), propertyBuilder);
            }
        } else {
            return buildNullableType(buildType(typeContext.typeName(), propertyBuilder));
        }
    }

    protected JsonObjectBuilder fieldToProperty(GraphqlParser.TypeContext typeContext, ObjectValueWithVariable objectValueWithVariable) {
        JsonObjectBuilder propertyBuilder = Optional.ofNullable(objectValueWithVariable)
                .map(this::buildValidation)
                .orElseGet(jsonProvider::createObjectBuilder);
        if (typeContext.listType() != null) {
            propertyBuilder.add("type", jsonProvider.createValue("array"));
            GraphqlParser.ObjectValueWithVariableContext subObjectValueWithVariableContext = Optional.ofNullable(objectValueWithVariableContext)
                    .flatMap(arrayObjectValueWithVariableContext -> getJsonSchemaObjectArgument(arrayObjectValueWithVariableContext, "items"))
                    .orElse(null);
            propertyBuilder.add("items", fieldToProperty(typeContext.listType().type(), subObjectValueWithVariableContext));
            return propertyBuilder;
        } else if (typeContext.nonNullType() != null) {
            if (typeContext.nonNullType().listType() != null) {
                propertyBuilder.add("type", jsonProvider.createValue("array"));
                GraphqlParser.ObjectValueWithVariableContext subObjectValueWithVariableContext = Optional.ofNullable(objectValueWithVariableContext)
                        .flatMap(arrayObjectValueWithVariableContext -> getJsonSchemaObjectArgument(arrayObjectValueWithVariableContext, "items"))
                        .orElse(null);
                propertyBuilder.add("items", fieldToProperty(typeContext.nonNullType().listType().type(), subObjectValueWithVariableContext));
                return propertyBuilder;
            } else {
                return buildType(typeContext.nonNullType().typeName(), propertyBuilder);
            }
        } else {
            return buildNullableType(buildType(typeContext.typeName(), propertyBuilder));
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
            if (fieldTypeName.endsWith(SUFFIX_INPUT)) {
                jsonObjectBuilder
                        .add("anyOf",
                                jsonProvider.createArrayBuilder()
                                        .add(jsonProvider.createObjectBuilder().add("$ref", jsonProvider.createValue(fieldTypeName)))
                                        .add(jsonProvider.createObjectBuilder().add("$ref", jsonProvider.createValue(fieldTypeName + "_update")))
                        );
            } else {
                jsonObjectBuilder.add("$ref", jsonProvider.createValue(fieldTypeName));
            }
        } else if (definition.isObject()) {
            jsonObjectBuilder
                    .add("anyOf",
                            jsonProvider.createArrayBuilder()
                                    .add(jsonProvider.createObjectBuilder().add("$ref", jsonProvider.createValue(fieldTypeName + SUFFIX_INPUT)))
                                    .add(jsonProvider.createObjectBuilder().add("$ref", jsonProvider.createValue(fieldTypeName + SUFFIX_INPUT + "_update")))
                    );
        }
        return jsonObjectBuilder;
    }

    protected JsonObjectBuilder buildNullableType(JsonObjectBuilder typeBuilder) {
        return jsonProvider.createObjectBuilder()
                .add("anyOf",
                        jsonProvider.createArrayBuilder()
                                .add(typeBuilder)
                                .add(jsonProvider.createObjectBuilder().add("type", jsonProvider.createValue("null")))
                );
    }

    protected JsonObjectBuilder buildValidation(Directive directive) {
        JsonObjectBuilder validationBuilder = jsonProvider.createObjectBuilder();

        getJsonSchemaIntArgument(directive, "minLength")
                .ifPresent(minLength -> validationBuilder.add("minLength", minLength));
        getJsonSchemaIntArgument(directive, "maxLength")
                .ifPresent(maxLength -> validationBuilder.add("maxLength", maxLength));
        getJsonSchemaStringArgument(directive, "pattern")
                .ifPresent(pattern -> validationBuilder.add("pattern", pattern));
        getJsonSchemaStringArgument(directive, "format")
                .ifPresent(format -> validationBuilder.add("format", format));
        getJsonSchemaStringArgument(directive, "contentMediaType")
                .ifPresent(contentMediaType -> validationBuilder.add("contentMediaType", contentMediaType));
        getJsonSchemaStringArgument(directive, "contentEncoding")
                .ifPresent(contentEncoding -> validationBuilder.add("contentEncoding", contentEncoding));

        getJsonSchemaFloatArgument(directive, "minimum")
                .ifPresent(minimum -> validationBuilder.add("minimum", minimum));
        getJsonSchemaFloatArgument(directive, "exclusiveMinimum")
                .ifPresent(exclusiveMinimum -> validationBuilder.add("exclusiveMinimum", exclusiveMinimum));
        getJsonSchemaFloatArgument(directive, "maximum")
                .ifPresent(maximum -> validationBuilder.add("maximum", maximum));
        getJsonSchemaFloatArgument(directive, "exclusiveMaximum")
                .ifPresent(exclusiveMaximum -> validationBuilder.add("exclusiveMaximum", exclusiveMaximum));
        getJsonSchemaFloatArgument(directive, "multipleOf")
                .ifPresent(multipleOf -> validationBuilder.add("multipleOf", multipleOf));

        getJsonSchemaStringArgument(directive, "const")
                .ifPresent(constValue -> validationBuilder.add("const", constValue));

        getJsonSchemaArrayArgument(directive, "allOf")
                .ifPresent(arrayValueWithVariableContext -> {
                            JsonArrayBuilder allOfBuilder = jsonProvider.createArrayBuilder();
                            arrayValueWithVariableContext.valueWithVariable().stream()
                                    .filter(valueWithVariableContext -> valueWithVariableContext.objectValueWithVariable() != null)
                                    .forEach(valueWithVariableContext -> allOfBuilder.add(buildValidation(valueWithVariableContext.objectValueWithVariable())));
                            validationBuilder.add("allOf", allOfBuilder);
                        }
                );

        getJsonSchemaArrayArgument(directive, "anyOf")
                .ifPresent(arrayValueWithVariableContext -> {
                            JsonArrayBuilder anyOfBuilder = jsonProvider.createArrayBuilder();
                            arrayValueWithVariableContext.valueWithVariable().stream()
                                    .filter(valueWithVariableContext -> valueWithVariableContext.objectValueWithVariable() != null)
                                    .forEach(valueWithVariableContext -> anyOfBuilder.add(buildValidation(valueWithVariableContext.objectValueWithVariable())));
                            validationBuilder.add("anyOf", anyOfBuilder);
                        }
                );

        getJsonSchemaArrayArgument(directive, "anyOf")
                .ifPresent(arrayValueWithVariableContext -> {
                            JsonArrayBuilder oneOfBuilder = jsonProvider.createArrayBuilder();
                            arrayValueWithVariableContext.valueWithVariable().stream()
                                    .filter(valueWithVariableContext -> valueWithVariableContext.objectValueWithVariable() != null)
                                    .forEach(valueWithVariableContext -> oneOfBuilder.add(buildValidation(valueWithVariableContext.objectValueWithVariable())));
                            validationBuilder.add("anyOf", oneOfBuilder);
                        }
                );

        getJsonSchemaObjectArgument(directive, "not")
                .ifPresent(not -> validationBuilder.add("not", buildValidation(not)));

        getJsonSchemaArrayArgument(directive, "properties")
                .ifPresent(arrayValueWithVariableContext -> validationBuilder.add("properties", buildProperties(arrayValueWithVariableContext)));

        getJsonSchemaObjectArgument(directive, "if")
                .ifPresent(ifValidation -> validationBuilder.add("if", buildValidation(ifValidation)));

        getJsonSchemaObjectArgument(directive, "then")
                .ifPresent(thenValidation -> validationBuilder.add("then", buildValidation(thenValidation)));

        getJsonSchemaObjectArgument(directive, "else")
                .ifPresent(elseValidation -> validationBuilder.add("else", buildValidation(elseValidation)));

        getJsonSchemaArrayArgument(directive, "dependentRequired")
                .ifPresent(arrayValueWithVariableContext -> validationBuilder.add("dependentRequired", buildDependentRequired(arrayValueWithVariableContext)));

        return validationBuilder;
    }

    protected JsonObjectBuilder buildValidation(GraphqlParser.ObjectValueWithVariableContext objectValueWithVariableContext) {
        JsonObjectBuilder validationBuilder = jsonProvider.createObjectBuilder();

        getJsonSchemaIntArgument(objectValueWithVariableContext, "minLength")
                .ifPresent(minLength -> validationBuilder.add("minLength", minLength));
        getJsonSchemaIntArgument(objectValueWithVariableContext, "maxLength")
                .ifPresent(maxLength -> validationBuilder.add("maxLength", maxLength));
        getJsonSchemaStringArgument(objectValueWithVariableContext, "pattern")
                .ifPresent(pattern -> validationBuilder.add("pattern", pattern));
        getJsonSchemaStringArgument(objectValueWithVariableContext, "format")
                .ifPresent(format -> validationBuilder.add("format", format));
        getJsonSchemaStringArgument(objectValueWithVariableContext, "contentMediaType")
                .ifPresent(contentMediaType -> validationBuilder.add("contentMediaType", contentMediaType));
        getJsonSchemaStringArgument(objectValueWithVariableContext, "contentEncoding")
                .ifPresent(contentEncoding -> validationBuilder.add("contentEncoding", contentEncoding));

        getJsonSchemaFloatArgument(objectValueWithVariableContext, "minimum")
                .ifPresent(minimum -> validationBuilder.add("minimum", minimum));
        getJsonSchemaFloatArgument(objectValueWithVariableContext, "exclusiveMinimum")
                .ifPresent(exclusiveMinimum -> validationBuilder.add("exclusiveMinimum", exclusiveMinimum));
        getJsonSchemaFloatArgument(objectValueWithVariableContext, "maximum")
                .ifPresent(maximum -> validationBuilder.add("maximum", maximum));
        getJsonSchemaFloatArgument(objectValueWithVariableContext, "exclusiveMaximum")
                .ifPresent(exclusiveMaximum -> validationBuilder.add("exclusiveMaximum", exclusiveMaximum));
        getJsonSchemaFloatArgument(objectValueWithVariableContext, "multipleOf")
                .ifPresent(multipleOf -> validationBuilder.add("multipleOf", multipleOf));

        getJsonSchemaStringArgument(objectValueWithVariableContext, "const")
                .ifPresent(constValue -> validationBuilder.add("const", constValue));

        getJsonSchemaArrayArgument(objectValueWithVariableContext, "allOf")
                .ifPresent(arrayValueWithVariableContext -> {
                            JsonArrayBuilder allOfBuilder = jsonProvider.createArrayBuilder();
                            arrayValueWithVariableContext.valueWithVariable().stream()
                                    .filter(valueWithVariableContext -> valueWithVariableContext.objectValueWithVariable() != null)
                                    .forEach(valueWithVariableContext -> allOfBuilder.add(buildValidation(valueWithVariableContext.objectValueWithVariable())));
                            validationBuilder.add("allOf", allOfBuilder);
                        }
                );

        getJsonSchemaArrayArgument(objectValueWithVariableContext, "anyOf")
                .ifPresent(arrayValueWithVariableContext -> {
                            JsonArrayBuilder anyOfBuilder = jsonProvider.createArrayBuilder();
                            arrayValueWithVariableContext.valueWithVariable().stream()
                                    .filter(valueWithVariableContext -> valueWithVariableContext.objectValueWithVariable() != null)
                                    .forEach(valueWithVariableContext -> anyOfBuilder.add(buildValidation(valueWithVariableContext.objectValueWithVariable())));
                            validationBuilder.add("anyOf", anyOfBuilder);
                        }
                );

        getJsonSchemaArrayArgument(objectValueWithVariableContext, "anyOf")
                .ifPresent(arrayValueWithVariableContext -> {
                            JsonArrayBuilder oneOfBuilder = jsonProvider.createArrayBuilder();
                            arrayValueWithVariableContext.valueWithVariable().stream()
                                    .filter(valueWithVariableContext -> valueWithVariableContext.objectValueWithVariable() != null)
                                    .forEach(valueWithVariableContext -> oneOfBuilder.add(buildValidation(valueWithVariableContext.objectValueWithVariable())));
                            validationBuilder.add("anyOf", oneOfBuilder);
                        }
                );

        getJsonSchemaObjectArgument(objectValueWithVariableContext, "not")
                .ifPresent(not -> validationBuilder.add("not", buildValidation(not)));

        getJsonSchemaArrayArgument(objectValueWithVariableContext, "properties")
                .ifPresent(arrayValueWithVariableContext -> validationBuilder.add("properties", buildProperties(arrayValueWithVariableContext)));

        getJsonSchemaObjectArgument(objectValueWithVariableContext, "if")
                .ifPresent(ifValidation -> validationBuilder.add("if", buildValidation(ifValidation)));

        getJsonSchemaObjectArgument(objectValueWithVariableContext, "then")
                .ifPresent(thenValidation -> validationBuilder.add("then", buildValidation(thenValidation)));

        getJsonSchemaObjectArgument(objectValueWithVariableContext, "else")
                .ifPresent(elseValidation -> validationBuilder.add("else", buildValidation(elseValidation)));

        getJsonSchemaArrayArgument(objectValueWithVariableContext, "dependentRequired")
                .ifPresent(arrayValueWithVariableContext -> validationBuilder.add("dependentRequired", buildDependentRequired(arrayValueWithVariableContext)));

        return validationBuilder;
    }

    protected JsonObjectBuilder buildProperties(GraphqlParser.ArrayValueWithVariableContext arrayValueWithVariableContext) {
        JsonObjectBuilder propertiesBuilder = jsonProvider.createObjectBuilder();
        arrayValueWithVariableContext.valueWithVariable().stream()
                .filter(property -> property.objectValueWithVariable() != null)
                .forEach(property ->
                        getJsonSchemaStringArgument(property.objectValueWithVariable(), "name")
                                .ifPresent(name ->
                                        getJsonSchemaObjectArgument(property.objectValueWithVariable(), "validation")
                                                .ifPresent(validation ->
                                                        propertiesBuilder.add(name, buildValidation(validation))
                                                )
                                )
                );
        return propertiesBuilder;
    }

    protected JsonObjectBuilder buildDependentRequired(ArrayValueWithVariable arrayValueWithVariable) {
        JsonObjectBuilder dependentRequiredBuilder = jsonProvider.createObjectBuilder();
        arrayValueWithVariable.getValueWithVariables().stream()
                .filter(ValueWithVariable::isObject)
                .forEach(property ->
                        getJsonSchemaStringArgument(property.asObject(), "name")
                                .map(name ->
                                        getJsonSchemaArrayArgument(property.asObject(), "required")
                                                .map(required ->
                                                        required.getValueWithVariables().stream()
                                                                .filter(ValueWithVariable::isString)
                                                                .map(item -> item.asString().getValue())
                                                )
                                )
                );
        return dependentRequiredBuilder;
    }
}
