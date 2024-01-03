package io.graphoenix.java.implementer;

import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.ConnectionBuilder;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationAfterHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
public class ConnectionHandlerBuilder {

    private final DocumentManager documentManager;
    private final PackageConfig packageConfig;

    @Inject
    public ConnectionHandlerBuilder(DocumentManager documentManager, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageConfig = packageConfig;
    }

    public void writeToFiler(Filer filer) throws IOException {
        this.buildClass().writeTo(filer);
        Logger.info("ConnectionHandler build success");
    }

    private JavaFile buildClass() {
        TypeSpec typeSpec = buildConnectionHandler();
        return JavaFile.builder(packageConfig.getHandlerPackageName(), typeSpec).build();
    }

    private TypeSpec buildConnectionHandler() {
        return TypeSpec.classBuilder("ConnectionHandler")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(OperationAfterHandler.class)
                .addAnnotation(ApplicationScoped.class)
                .addAnnotation(AnnotationSpec.builder(Priority.class).addMember("value", "$T.MAX_VALUE - 200", ClassName.get(Integer.class)).build())
                .addField(
                        FieldSpec.builder(
                                ClassName.get(DocumentManager.class),
                                "documentManager",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addField(
                        FieldSpec.builder(
                                ClassName.get(ConnectionBuilder.class),
                                "connectionBuilder",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addField(
                        FieldSpec.builder(
                                ClassName.get(JsonProvider.class),
                                "jsonProvider",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addMethod(buildConstructor())
                .addMethods(buildTypeConnectionMethods())
                .build();
    }

    private MethodSpec buildConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Inject.class)
                .addParameter(ClassName.get(DocumentManager.class), "documentManager")
                .addStatement("this.documentManager = documentManager")
                .addParameter(ClassName.get(ConnectionBuilder.class), "connectionBuilder")
                .addStatement("this.connectionBuilder = connectionBuilder")
                .addParameter(ClassName.get(JsonProvider.class), "jsonProvider")
                .addStatement("this.jsonProvider = jsonProvider");
        return builder.build();
    }

    private List<MethodSpec> buildTypeConnectionMethods() {
        return documentManager.getDocument().getObjectTypes()
                .filter(objectType -> !objectType.getName().equals(TYPE_PAGE_INFO_NAME))
                .map(this::buildTypeConnectionMethod)
                .collect(Collectors.toList());
    }

    private MethodSpec buildTypeConnectionMethod(ObjectType objectType) {
        String fieldName = typeNameToFieldName(objectType.getName());
        MethodSpec.Builder builder = MethodSpec.methodBuilder(fieldName)
                .addModifiers(Modifier.PUBLIC);

        if (documentManager.isOperationType(objectType)) {
            builder.returns(ParameterizedTypeName.get(Mono.class, JsonValue.class))
                    .addParameter(ClassName.get(Operation.class), "operation")
                    .addParameter(ClassName.get(JsonValue.class), "jsonValue")
                    .addStatement("$T jsonPatchBuilder = jsonProvider.createPatchBuilder()", ClassName.get(JsonPatchBuilder.class))
                    .addStatement("$T path = \"\"", ClassName.get(String.class));
        } else {
            builder.addParameter(ClassName.get(JsonPatchBuilder.class), "jsonPatchBuilder")
                    .addParameter(ClassName.get(String.class), "path")
                    .addParameter(ClassName.get(String.class), "typeName")
                    .addParameter(ClassName.get(Field.class), "parentField")
                    .addParameter(ClassName.get(JsonValue.class), "jsonValue");
        }

        if (objectType.getName().endsWith(SUFFIX_CONNECTION)) {
            String typeName = objectType.getName().substring(0, objectType.getName().length() - SUFFIX_CONNECTION.length());
            builder.beginControlFlow("if (jsonValue != null && !jsonValue.getValueType().equals($T.ValueType.NULL) && parentField.getFields() != null && !parentField.getFields().isEmpty())", ClassName.get(JsonValue.class))
                    .addStatement("$T connection = connectionBuilder.build(typeName, parentField, jsonValue)", ClassName.get(JsonValue.class))
                    .addStatement("jsonPatchBuilder.add(path, connection)")
                    .beginControlFlow("for ($T field : parentField.getFields())",
                            ClassName.get(Field.class)
                    )
                    .beginControlFlow("if (field.getName().equals($S))", FIELD_EDGES_NAME)
                    .addStatement("String selectionName = $T.ofNullable(field.getAlias()).orElse(field.getName())", ClassName.get(Optional.class))
                    .addStatement("$T jsonArray = connection.asJsonObject().get(selectionName).asJsonArray()", ClassName.get(JsonArray.class))
                    .addStatement("$T.range(0, jsonArray.size()).forEach(index -> $L(jsonPatchBuilder, path + \"/\" + selectionName + \"/\" + index, $S, field, jsonArray.get(index)))",
                            ClassName.get(IntStream.class),
                            typeNameToFieldName(typeName + SUFFIX_EDGE),
                            objectType.getName()
                    )
                    .endControlFlow()
                    .endControlFlow()
                    .endControlFlow();
        } else {
            if (documentManager.isOperationType(objectType)) {
                builder.addAnnotation(Override.class)
                        .beginControlFlow("if (operation.getFields() != null && !operation.getFields().isEmpty())")
                        .beginControlFlow("for ($T field : operation.getFields())",
                                ClassName.get(Field.class)
                        );
            } else {
                builder.beginControlFlow("if (jsonValue != null && !jsonValue.getValueType().equals($T.ValueType.NULL) && parentField.getFields() != null && !parentField.getFields().isEmpty())", ClassName.get(JsonValue.class))
                        .beginControlFlow("for ($T field : parentField.getFields())",
                                ClassName.get(Field.class)
                        );
            }
            builder.addStatement("String selectionName = $T.ofNullable(field.getAlias()).orElse(field.getName())", ClassName.get(Optional.class));
            List<FieldDefinition> fieldDefinitions = objectType.getFields().stream()
                    .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                    .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                    .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                    .collect(Collectors.toList());

            int index = 0;
            for (FieldDefinition fieldDefinition : fieldDefinitions) {
                if (index == 0) {
                    builder.beginControlFlow("if (field.getName().equals($S))", fieldDefinition.getName());
                } else {
                    builder.nextControlFlow("else if (field.getName().equals($S))", fieldDefinition.getName());
                }
                ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
                if (fieldDefinition.isConnectionField()) {
                    builder.addStatement("$L(jsonPatchBuilder, path + \"/\" + selectionName, $S, field, jsonValue)",
                            typeNameToFieldName(fieldTypeDefinition.getName()),
                            objectType.getName()
                    );
                } else if (!fieldDefinition.getType().hasList()) {
                    builder.addStatement("$L(jsonPatchBuilder, path + \"/\" + selectionName, $S, field, jsonValue.asJsonObject().get(selectionName))",
                            typeNameToFieldName(fieldTypeDefinition.getName()),
                            objectType.getName()
                    );
                } else {
                    builder.addStatement("$T jsonArray = jsonValue.asJsonObject().get(selectionName).asJsonArray()", ClassName.get(JsonArray.class))
                            .addStatement("$T.range(0, jsonArray.size()).forEach(index -> $L(jsonPatchBuilder, path + \"/\" + selectionName + \"/\" + index, $S, field, jsonArray.get(index)))",
                                    ClassName.get(IntStream.class),
                                    typeNameToFieldName(fieldTypeDefinition.getName()),
                                    objectType.getName()
                            );
                }
                if (index == fieldDefinitions.size() - 1) {
                    builder.endControlFlow();
                }
                index++;
            }
            builder.endControlFlow()
                    .endControlFlow();
        }
        if (documentManager.isOperationType(objectType)) {
            builder.addStatement("return $T.just(jsonPatchBuilder.build().apply(jsonValue.asJsonObject()))", ClassName.get(Mono.class));
        }
        return builder.build();
    }
}
