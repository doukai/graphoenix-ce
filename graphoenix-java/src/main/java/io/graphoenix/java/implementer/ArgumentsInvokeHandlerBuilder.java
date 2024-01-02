package io.graphoenix.java.implementer;

import com.google.common.collect.Streams;
import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.java.utils.TypeNameUtil;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.bind.Jsonb;
import jakarta.json.spi.JsonProvider;
import org.tinylog.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.java.utils.TypeNameUtil.toClassName;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
public class ArgumentsInvokeHandlerBuilder {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;
    private Map<String, Map<String, List<Map.Entry<String, String>>>> invokeMethods;

    @Inject
    public ArgumentsInvokeHandlerBuilder(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
    }

    public void writeToFiler(Filer filer) throws IOException {
        this.invokeMethods = documentManager.getDocument().getObjectTypes()
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .collect(
                        Collectors.toMap(
                                AbstractDefinition::getName,
                                objectType ->
                                        objectType.getFields().stream()
                                                .filter(FieldDefinition::isInvokeField)
                                                .filter(packageManager::isLocalPackage)
                                                .map(fieldDefinition ->
                                                        new AbstractMap.SimpleEntry<>(
                                                                fieldDefinition.getInvokeClassNameOrError(),
                                                                new AbstractMap.SimpleEntry<>(
                                                                        fieldDefinition.getInvokeMethodNameOrError(),
                                                                        fieldDefinition.getInvokeReturnClassNameOrError()
                                                                )
                                                        )
                                                )
                                                .collect(
                                                        Collectors.groupingBy(
                                                                AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, String>>::getKey,
                                                                Collectors.mapping(
                                                                        AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, String>>::getValue,
                                                                        Collectors.toList()
                                                                )
                                                        )
                                                )
                        )
                );
        this.buildClass().writeTo(filer);
        Logger.info("ArgumentsInvokeHandler build success");
    }

    private JavaFile buildClass() {
        TypeSpec typeSpec = buildInvokeHandler();
        return JavaFile.builder(packageConfig.getHandlerPackageName(), typeSpec).build();
    }

    private TypeSpec buildInvokeHandler() {
        return TypeSpec.classBuilder("ArgumentsInvokeHandler")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ApplicationScoped.class)
                .addField(
                        FieldSpec.builder(
                                ParameterizedTypeName.get(ClassName.get(Provider.class), ClassName.get(Jsonb.class)),
                                "jsonb",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addField(
                        FieldSpec.builder(
                                ParameterizedTypeName.get(ClassName.get(Provider.class), ClassName.get(JsonProvider.class)),
                                "jsonProviderProvider",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addField(
                        FieldSpec.builder(
                                ParameterizedTypeName.get(ClassName.get(Provider.class), ClassName.get(packageConfig.getHandlerPackageName(), "InputInvokeHandler")),
                                "inputInvokeHandlerProvider",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addFields(buildFields())
                .addMethod(buildConstructor())
                .addMethods(buildTypeInvokeMethods())
                .build();
    }

    private Set<FieldSpec> buildFields() {
        return this.invokeMethods.values().stream()
                .flatMap(classMap ->
                        classMap.keySet().stream()
                                .map(TypeNameUtil::toClassName)
                                .map(className ->
                                        FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(Provider.class), className), typeNameToFieldName(className.simpleName()))
                                                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                                                .build()
                                )
                )
                .collect(Collectors.toSet());
    }

    private MethodSpec buildConstructor() {
        Set<String> classNameSet = invokeMethods.values().stream()
                .flatMap(value -> value.keySet().stream())
                .collect(Collectors.toSet());

        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Inject.class)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Provider.class), ClassName.get(Jsonb.class)), "jsonb")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Provider.class), ClassName.get(JsonProvider.class)), "jsonProviderProvider")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Provider.class), ClassName.get(packageConfig.getHandlerPackageName(), "InputInvokeHandler")), "inputInvokeHandlerProvider")
                .addParameters(
                        classNameSet.stream()
                                .map(TypeNameUtil::toClassName)
                                .map(className ->
                                        ParameterSpec.builder(
                                                ParameterizedTypeName.get(ClassName.get(Provider.class), className),
                                                typeNameToFieldName(className.simpleName())
                                        ).build()
                                )
                                .collect(Collectors.toList())
                )
                .addStatement("this.jsonb = jsonb")
                .addStatement("this.jsonProviderProvider = jsonProviderProvider")
                .addStatement("this.inputInvokeHandlerProvider = inputInvokeHandlerProvider");

        classNameSet.stream()
                .map(TypeNameUtil::toClassName)
                .forEach(className ->
                        builder.addStatement("this.$L = $L",
                                typeNameToFieldName(className.simpleName()),
                                typeNameToFieldName(className.simpleName())
                        )
                );

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
            operationTypeName = documentManager.getDocument().getSubscriptionOperationTypeOrError().getName();
            typeParameterName = OPERATION_SUBSCRIPTION_NAME;
        } else {
            operationTypeName = documentManager.getDocument().getSubscriptionOperationTypeOrError().getName();
        }
        MethodSpec.Builder builder = MethodSpec.methodBuilder(typeParameterName)
                .addModifiers(Modifier.PUBLIC);

        if (isOperationType) {
            builder.returns(ParameterizedTypeName.get(ClassName.get(Mono.class), ClassName.get(Operation.class)))
                    .addParameter(ClassName.get(Operation.class), "operation");
        } else {
            builder.returns(ParameterizedTypeName.get(ClassName.get(Mono.class), ClassName.get(Void.class)))
                    .addParameter(ClassName.get(Field.class), "parentField");
        }

        return builder.addStatement(
                CodeBlock.join(
                        Stream.of(
                                CodeBlock.of(
                                        "return $T.fromStream($T.ofNullable($L).flatMap($T::stream))",
                                        ClassName.get(Flux.class),
                                        ClassName.get(Stream.class),
                                        isOperationType ? "operation.getFields()" : "parentField.getFields()",
                                        ClassName.get(Collection.class)
                                ),
                                CodeBlock.builder()
                                        .add(".flatMap(field -> {\n")
                                        .indent()
                                        .add(CodeBlock.builder()
                                                .beginControlFlow("switch (field.getName())")
                                                .indent()
                                                .add(CodeBlock.join(
                                                        Streams.concat(
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
                                                                                    return CodeBlock.builder().add("case $S:\n", fieldDefinition.getName())
                                                                                            .indent()
                                                                                            .add("return $T.justOrEmpty(field.getArguments())\n", ClassName.get(Mono.class))
                                                                                            .indent()
                                                                                            .add(".flatMap(arguments -> inputInvokeHandlerProvider.get().$L(jsonb.get().fromJson(arguments.toJson(), $T.class), field.getArguments()))\n",
                                                                                                    methodName,
                                                                                                    toClassName(documentManager.getDocument().getInputObjectTypeOrError(argumentInputName).getClassNameOrError())
                                                                                            )
                                                                                            .add(".doOnNext($L -> field.updateArguments(jsonProviderProvider.get().createReader(new $T(jsonb.get().toJson($L).toString())).readObject()))\n",
                                                                                                    methodName,
                                                                                                    ClassName.get(StringReader.class),
                                                                                                    methodName
                                                                                            )
                                                                                            .add(".then($L(field))\n",
                                                                                                    typeNameToFieldName(fieldTypeName)
                                                                                            )
                                                                                            .add(".switchIfEmpty($L(field));",
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
                                                                                    return CodeBlock.builder().add("case $S:\n", fieldDefinition.getName())
                                                                                            .indent()
                                                                                            .add("return $T.justOrEmpty(field.getArguments())\n", ClassName.get(Mono.class))
                                                                                            .indent()
                                                                                            .add(".flatMap(arguments -> inputInvokeHandlerProvider.get().$L(jsonb.get().fromJson(arguments.toJson(), $T.class), field.getArguments()))\n",
                                                                                                    methodName,
                                                                                                    toClassName(documentManager.getDocument().getInputObjectTypeOrError(argumentInputName).getClassNameOrError())
                                                                                            )
                                                                                            .add(".doOnNext($L -> field.updateArguments(jsonProviderProvider.get().createReader(new $T(jsonb.get().toJson($L).toString())).readObject()))\n",
                                                                                                    methodName,
                                                                                                    ClassName.get(StringReader.class),
                                                                                                    methodName
                                                                                            )
                                                                                            .add(".then($L(field))\n",
                                                                                                    typeNameToFieldName(fieldTypeName)
                                                                                            )
                                                                                            .add(".switchIfEmpty($L(field));",
                                                                                                    typeNameToFieldName(fieldTypeName)
                                                                                            )
                                                                                            .unindent()
                                                                                            .unindent()
                                                                                            .build();
                                                                                }
                                                                        ),
                                                                Stream.of(CodeBlock.builder().add(CodeBlock.of("default:\n")).indent().add(CodeBlock.of("return $T.empty();\n", ClassName.get(Flux.class))).unindent().build())
                                                        ).collect(Collectors.toList()),
                                                        System.lineSeparator()
                                                        )
                                                )
                                                .unindent()
                                                .endControlFlow()
                                                .build()
                                        )
                                        .unindent()
                                        .add("})")
                                        .build(),
                                CodeBlock.of(isOperationType ? ".then().thenReturn(operation)" : ".then()")
                        ).collect(Collectors.toList()),
                        System.lineSeparator()
                )
        ).build();
    }
}
