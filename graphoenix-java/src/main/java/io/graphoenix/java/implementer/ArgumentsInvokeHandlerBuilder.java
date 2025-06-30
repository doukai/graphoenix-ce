package io.graphoenix.java.implementer;

import com.google.common.collect.Streams;
import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.OperationBuilder;
import io.graphoenix.core.handler.before.FragmentHandler;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.spi.JsonProvider;
import org.tinylog.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.java.utils.TypeNameUtil.toClassName;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
public class ArgumentsInvokeHandlerBuilder {

    private final DocumentManager documentManager;
    private final PackageConfig packageConfig;

    @Inject
    public ArgumentsInvokeHandlerBuilder(DocumentManager documentManager, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageConfig = packageConfig;
    }

    public void writeToFiler(Filer filer) throws IOException {
        this.buildClass().writeTo(filer);
        Logger.info("ArgumentsInvokeHandler build success");
    }

    private JavaFile buildClass() {
        TypeSpec typeSpec = buildInvokeHandler();
        return JavaFile.builder(packageConfig.getHandlerPackageName(), typeSpec).build();
    }

    private TypeSpec buildInvokeHandler() {
        return TypeSpec.classBuilder("ArgumentsInvokeHandler")
                .addSuperinterface(ClassName.get(OperationBeforeHandler.class))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ApplicationScoped.class)
                .addAnnotation(AnnotationSpec.builder(Priority.class).addMember("value", "$T.FRAGMENT_HANDLER_PRIORITY + 100", ClassName.get(FragmentHandler.class)).build())
                .addField(
                        FieldSpec.builder(
                                ClassName.get(OperationBuilder.class),
                                "operationBuilder",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addField(
                        FieldSpec.builder(
                                ClassName.get(Jsonb.class),
                                "jsonb",
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
                .addField(
                        FieldSpec.builder(
                                ClassName.get(packageConfig.getHandlerPackageName(), "QueryArgumentsInvokeHandler"),
                                "queryArgumentsInvokeHandler",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addField(
                        FieldSpec.builder(
                                ClassName.get(packageConfig.getHandlerPackageName(), "ListQueryArgumentsInvokeHandler"),
                                "listQueryArgumentsInvokeHandler",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addField(
                        FieldSpec.builder(
                                ClassName.get(packageConfig.getHandlerPackageName(), "ConnectionQueryArgumentsInvokeHandler"),
                                "connectionQueryArgumentsInvokeHandler",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addField(
                        FieldSpec.builder(
                                ClassName.get(packageConfig.getHandlerPackageName(), "MutationArgumentsInvokeHandler"),
                                "mutationArgumentsInvokeHandler",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addField(
                        FieldSpec.builder(
                                ClassName.get(packageConfig.getHandlerPackageName(), "ListMutationArgumentsInvokeHandler"),
                                "listMutationArgumentsInvokeHandler",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addMethod(buildConstructor())
                .addMethods(buildTypeInvokeMethods())
                .build();
    }

    private MethodSpec buildConstructor() {

        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Inject.class)
                .addParameter(ClassName.get(OperationBuilder.class), "operationBuilder")
                .addParameter(ClassName.get(Jsonb.class), "jsonb")
                .addParameter(ClassName.get(JsonProvider.class), "jsonProvider")
                .addParameter(ClassName.get(packageConfig.getHandlerPackageName(), "QueryArgumentsInvokeHandler"), "queryArgumentsInvokeHandler")
                .addParameter(ClassName.get(packageConfig.getHandlerPackageName(), "ListQueryArgumentsInvokeHandler"), "listQueryArgumentsInvokeHandler")
                .addParameter(ClassName.get(packageConfig.getHandlerPackageName(), "ConnectionQueryArgumentsInvokeHandler"), "connectionQueryArgumentsInvokeHandler")
                .addParameter(ClassName.get(packageConfig.getHandlerPackageName(), "MutationArgumentsInvokeHandler"), "mutationArgumentsInvokeHandler")
                .addParameter(ClassName.get(packageConfig.getHandlerPackageName(), "ListMutationArgumentsInvokeHandler"), "listMutationArgumentsInvokeHandler")
                .addStatement("this.queryArgumentsInvokeHandler = queryArgumentsInvokeHandler")
                .addStatement("this.listQueryArgumentsInvokeHandler = listQueryArgumentsInvokeHandler")
                .addStatement("this.connectionQueryArgumentsInvokeHandler = connectionQueryArgumentsInvokeHandler")
                .addStatement("this.mutationArgumentsInvokeHandler = mutationArgumentsInvokeHandler")
                .addStatement("this.listMutationArgumentsInvokeHandler = listMutationArgumentsInvokeHandler")
                .addStatement("this.operationBuilder = operationBuilder")
                .addStatement("this.jsonb = jsonb")
                .addStatement("this.jsonProvider = jsonProvider");

        return builder.build();
    }

    private List<MethodSpec> buildTypeInvokeMethods() {
        return documentManager.getDocument().getObjectTypes()
                .map(this::buildTypeInvokeMethod)
                .collect(Collectors.toList());
    }

    private MethodSpec buildTypeInvokeMethod(ObjectType objectType) {
        String typeParameterName = typeNameToFieldName(objectType.getName());
        String operationTypeName;
        boolean isOperationType = documentManager.isOperationType(objectType);
        if (documentManager.isQueryOperationType(objectType)) {
            operationTypeName = documentManager.getDocument().getQueryOperationTypeOrError().getName();
            typeParameterName = OPERATION_QUERY_NAME;
        } else if (documentManager.isMutationOperationType(objectType)) {
            operationTypeName = documentManager.getDocument().getMutationOperationTypeOrError().getName();
            typeParameterName = OPERATION_MUTATION_NAME;
        } else if (documentManager.isSubscriptionOperationType(objectType)) {
            operationTypeName = documentManager.getDocument().getQueryOperationTypeOrError().getName();
            typeParameterName = OPERATION_SUBSCRIPTION_NAME;
        } else {
            operationTypeName = documentManager.getDocument().getQueryOperationTypeOrError().getName();
        }
        MethodSpec.Builder builder = MethodSpec.methodBuilder(typeParameterName)
                .addModifiers(Modifier.PUBLIC);

        if (isOperationType) {
            builder.returns(ParameterizedTypeName.get(ClassName.get(Mono.class), ClassName.get(Operation.class)))
                    .addAnnotation(Override.class)
                    .addParameter(ClassName.get(Operation.class), "operation")
                    .addParameter(ParameterizedTypeName.get(Map.class, String.class, JsonValue.class), "variables");
        } else {
            builder.returns(ParameterizedTypeName.get(ClassName.get(Mono.class), ClassName.get(Void.class)))
                    .addParameter(ClassName.get(Field.class), "parentField");
        }

        return builder
                .addStatement(
                        CodeBlock.builder()
                                .add("return $T.justOrEmpty($L)\n",
                                        ClassName.get(Mono.class),
                                        isOperationType ? "operation.getFields()" : "parentField.getFields()"
                                )
                                .add(".flatMapMany($T::fromIterable)\n",
                                        ClassName.get(Flux.class)
                                )
                                .add(".flatMap(field -> {\n")
                                .indent()
                                .add(
                                        CodeBlock.builder()
                                                .beginControlFlow("switch (field.getName())")
                                                .add(
                                                        CodeBlock.join(
                                                                Streams
                                                                        .concat(
                                                                                objectType.getFields().stream()
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                                                                                        .filter(fieldDefinition -> fieldDefinition.getArguments() != null)
                                                                                        .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.getType().hasList())
                                                                                        .map(fieldDefinition -> {
                                                                                                    String fieldTypeName = documentManager.getFieldTypeDefinition(fieldDefinition).getName();
                                                                                                    String methodName = typeNameToFieldName(fieldTypeName) + operationTypeName + SUFFIX_ARGUMENTS;
                                                                                                    String argumentInputName = fieldTypeName + operationTypeName + SUFFIX_ARGUMENTS;
                                                                                                    ClassName className = toClassName(documentManager.getDocument().getInputObjectTypeOrError(argumentInputName).getClassNameOrError());
                                                                                                    String handlerName;
                                                                                                    if (documentManager.isMutationOperationType(objectType)) {
                                                                                                        handlerName = "mutationArgumentsInvokeHandler";
                                                                                                    } else {
                                                                                                        if (fieldDefinition.isConnectionField()) {
                                                                                                            handlerName = "connectionQueryArgumentsInvokeHandler";
                                                                                                        } else {
                                                                                                            handlerName = "queryArgumentsInvokeHandler";
                                                                                                        }
                                                                                                    }
                                                                                                    return CodeBlock.builder().add("case $S:\n", fieldDefinition.getName())
                                                                                                            .indent()
                                                                                                            .add("return $T.justOrEmpty(field.getArguments())\n", ClassName.get(Mono.class))
                                                                                                            .indent()
                                                                                                            .add(".map(arguments -> jsonb.fromJson(arguments.toJson(), $T.class))\n",
                                                                                                                    className
                                                                                                            )
                                                                                                            .add(".flatMap(arguments -> $L.$L(arguments, field.getArguments()))\n",
                                                                                                                    handlerName,
                                                                                                                    methodName
                                                                                                            )
                                                                                                            .add(".switchIfEmpty($T.defer(() -> $L.$L(new $T(), field.getArguments())))\n",
                                                                                                                    ClassName.get(Mono.class),
                                                                                                                    handlerName,
                                                                                                                    methodName,
                                                                                                                    className
                                                                                                            )
                                                                                                            .add(".doOnNext($L -> field.setArguments(operationBuilder.updateJsonObject(field.getArguments(), jsonProvider.createReader(new $T(jsonb.toJson($L))).readObject())))\n",
                                                                                                                    methodName,
                                                                                                                    ClassName.get(StringReader.class),
                                                                                                                    methodName
                                                                                                            )
                                                                                                            .add(".then($L(field));",
                                                                                                                    typeNameToFieldName(fieldTypeName)
                                                                                                            )
                                                                                                            .unindent()
                                                                                                            .unindent()
                                                                                                            .build();
                                                                                                }
                                                                                        ),
                                                                                objectType.getFields().stream()
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                                                                                        .filter(fieldDefinition -> fieldDefinition.getArguments() != null)
                                                                                        .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                                                                        .filter(fieldDefinition -> fieldDefinition.getType().hasList())
                                                                                        .map(fieldDefinition -> {
                                                                                                    String fieldTypeName = documentManager.getFieldTypeDefinition(fieldDefinition).getName();
                                                                                                    String methodName = typeNameToFieldName(fieldTypeName) + SUFFIX_LIST + operationTypeName + SUFFIX_ARGUMENTS;
                                                                                                    String argumentInputName = fieldTypeName + SUFFIX_LIST + operationTypeName + SUFFIX_ARGUMENTS;
                                                                                                    ClassName className = toClassName(documentManager.getDocument().getInputObjectTypeOrError(argumentInputName).getClassNameOrError());
                                                                                                    String handlerName;
                                                                                                    if (documentManager.isMutationOperationType(objectType)) {
                                                                                                        handlerName = "listMutationArgumentsInvokeHandler";
                                                                                                    } else {
                                                                                                        handlerName = "listQueryArgumentsInvokeHandler";
                                                                                                    }
                                                                                                    return CodeBlock.builder().add("case $S:\n", fieldDefinition.getName())
                                                                                                            .indent()
                                                                                                            .add("return $T.justOrEmpty(field.getArguments())\n", ClassName.get(Mono.class))
                                                                                                            .indent()
                                                                                                            .add(".map(arguments -> jsonb.fromJson(arguments.toJson(), $T.class))\n",
                                                                                                                    className
                                                                                                            )
                                                                                                            .add(".flatMap(arguments -> $L.$L(arguments, field.getArguments()))\n",
                                                                                                                    handlerName,
                                                                                                                    methodName
                                                                                                            )
                                                                                                            .add(".switchIfEmpty($T.defer(() -> $L.$L(new $T(), field.getArguments())))\n",
                                                                                                                    ClassName.get(Mono.class),
                                                                                                                    handlerName,
                                                                                                                    methodName,
                                                                                                                    className
                                                                                                            )
                                                                                                            .add(".doOnNext($L -> field.setArguments(operationBuilder.updateJsonObject(field.getArguments(), jsonProvider.createReader(new $T(jsonb.toJson($L))).readObject())))\n",
                                                                                                                    methodName,
                                                                                                                    ClassName.get(StringReader.class),
                                                                                                                    methodName
                                                                                                            )
                                                                                                            .add(".then($L(field));",
                                                                                                                    typeNameToFieldName(fieldTypeName)
                                                                                                            )
                                                                                                            .unindent()
                                                                                                            .unindent()
                                                                                                            .build();
                                                                                                }
                                                                                        ),
                                                                                Stream.of(CodeBlock.builder().add(CodeBlock.of("default:\n")).indent().add(CodeBlock.of("return $T.empty();\n", ClassName.get(Flux.class))).unindent().build())
                                                                        )
                                                                        .collect(Collectors.toList()),
                                                                System.lineSeparator()
                                                        )
                                                )
                                                .endControlFlow()
                                                .build()
                                )
                                .unindent()
                                .add("})\n")
                                .add(isOperationType ? ".then()\n" : ".then()")
                                .add(isOperationType ? ".thenReturn(operation)" : "")
                                .build()
                )
                .build();
    }
}
