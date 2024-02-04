package io.graphoenix.java.implementer;

import com.dslplatform.json.runtime.TypeDefinition;
import com.google.common.collect.Streams;
import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.OperationBuilder;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.java.utils.TypeNameUtil;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationAfterHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
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

import static io.graphoenix.core.utils.TypeNameUtil.getClassName;
import static io.graphoenix.java.utils.NameUtil.*;
import static io.graphoenix.java.utils.TypeNameUtil.toClassName;
import static io.graphoenix.java.utils.TypeNameUtil.toTypeName;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
public class InvokeHandlerBuilder {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;
    private Map<String, Map<String, List<Map.Entry<String, String>>>> invokeMethods;

    @Inject
    public InvokeHandlerBuilder(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
    }

    public void writeToFiler(Filer filer) throws IOException {
        this.invokeMethods = documentManager.getDocument().getObjectTypes()
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
        Logger.info("InvokeHandler build success");
    }

    private JavaFile buildClass() {
        TypeSpec typeSpec = buildInvokeHandler();
        return JavaFile.builder(packageConfig.getHandlerPackageName(), typeSpec).build();
    }

    private TypeSpec buildInvokeHandler() {
        return TypeSpec.classBuilder("InvokeHandler")
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
                .addParameter(ClassName.get(DocumentManager.class), "documentManager")
                .addParameter(ClassName.get(OperationBuilder.class), "operationBuilder")
                .addParameter(ClassName.get(Jsonb.class), "jsonb")
                .addParameter(ClassName.get(JsonProvider.class), "jsonProvider")
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
                .addStatement("this.documentManager = documentManager")
                .addStatement("this.operationBuilder = operationBuilder")
                .addStatement("this.jsonb = jsonb")
                .addStatement("this.jsonProvider = jsonProvider");

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
        return Stream
                .concat(
                        documentManager.getDocument().getObjectTypes()
                                .filter(objectType -> !documentManager.isOperationType(objectType))
                                .map(this::buildTypeInvokeMethod),
                        documentManager.getDocument().getObjectTypes()
                                .filter(documentManager::isOperationType)
                                .map(this::buildOperationTypeInvokeMethod)
                )
                .collect(Collectors.toList());
    }

    private MethodSpec buildTypeInvokeMethod(ObjectType objectType) {
        String typeParameterName = typeNameToFieldName(objectType.getName());
        ClassName typeClassName = toClassName(objectType.getClassNameOrError());
        String resultParameterName = "result";

        return MethodSpec.methodBuilder(typeParameterName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Mono.class), typeClassName))
                .addParameter(typeClassName, typeParameterName)
                .addParameter(ClassName.get(Field.class), "parentField")
                .addStatement(
                        CodeBlock.builder()
                                .add("return $T.justOrEmpty($L)\n",
                                        ClassName.get(Mono.class),
                                        typeParameterName
                                )
                                .add(".flatMap(result ->\n")
                                .indent()
                                .add("$T.justOrEmpty(parentField.getFields())\n",
                                        ClassName.get(Mono.class)
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
                                                                                        .filter(FieldDefinition::isInvokeField)
                                                                                        .map(fieldDefinition -> {
                                                                                                    String invokeClassName = fieldDefinition.getInvokeClassNameOrError();
                                                                                                    String methodName = fieldDefinition.getInvokeMethodNameOrError();
                                                                                                    ClassName returnClassName = toClassName(getClassName(fieldDefinition.getInvokeReturnClassNameOrError()));

                                                                                                    String apiVariableName = typeNameToFieldName(toClassName(invokeClassName).simpleName());
                                                                                                    String fieldSetterMethodName = getFieldSetterMethodName(fieldDefinition.getName());
                                                                                                    CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", fieldDefinition.getName());
                                                                                                    CodeBlock invokeCodeBlock;

                                                                                                    if (returnClassName.canonicalName().equals(Mono.class.getCanonicalName())) {
                                                                                                        invokeCodeBlock = CodeBlock.of("return $L.get().$L($L).doOnNext($L -> $L.$L($L));",
                                                                                                                apiVariableName,
                                                                                                                methodName,
                                                                                                                resultParameterName,
                                                                                                                getFieldName(fieldDefinition.getName()),
                                                                                                                resultParameterName,
                                                                                                                fieldSetterMethodName,
                                                                                                                getFieldName(fieldDefinition.getName())
                                                                                                        );
                                                                                                    } else if (returnClassName.canonicalName().equals(Flux.class.getCanonicalName())) {
                                                                                                        invokeCodeBlock = CodeBlock.of("return $L.get().$L($L).collectList().doOnNext($L -> $L.$L($L));",
                                                                                                                apiVariableName,
                                                                                                                methodName,
                                                                                                                resultParameterName,
                                                                                                                getFieldName(fieldDefinition.getName()),
                                                                                                                resultParameterName,
                                                                                                                fieldSetterMethodName,
                                                                                                                getFieldName(fieldDefinition.getName())
                                                                                                        );
                                                                                                    } else {
                                                                                                        invokeCodeBlock = CodeBlock.of("return $T.justOrEmpty($L.get().$L($L)).doOnNext($L -> $L.$L($L));",
                                                                                                                ClassName.get(Mono.class),
                                                                                                                apiVariableName,
                                                                                                                methodName,
                                                                                                                resultParameterName,
                                                                                                                getFieldName(fieldDefinition.getName()),
                                                                                                                resultParameterName,
                                                                                                                fieldSetterMethodName,
                                                                                                                getFieldName(fieldDefinition.getName())
                                                                                                        );
                                                                                                    }
                                                                                                    return CodeBlock.builder().add(caseCodeBlock).indent().add(invokeCodeBlock).unindent().build();
                                                                                                }
                                                                                        ),
                                                                                objectType.getFields().stream()
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                                                                                        .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.getType().hasList())
                                                                                        .map(fieldDefinition -> {
                                                                                                    ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
                                                                                                    CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", fieldDefinition.getName());
                                                                                                    CodeBlock invokeCodeBlock = CodeBlock.of("return $T.justOrEmpty($L.$L()).flatMap(item -> $L(item, field)).doOnNext($L -> $L.$L($L));",
                                                                                                            ClassName.get(Mono.class),
                                                                                                            resultParameterName,
                                                                                                            getFieldGetterMethodName(fieldDefinition.getName()),
                                                                                                            typeNameToFieldName(fieldTypeDefinition.getName()),
                                                                                                            getFieldName(fieldDefinition.getName()),
                                                                                                            resultParameterName,
                                                                                                            getFieldSetterMethodName(fieldDefinition.getName()),
                                                                                                            getFieldName(fieldDefinition.getName())
                                                                                                    );
                                                                                                    return CodeBlock.builder().add(caseCodeBlock).indent().add(invokeCodeBlock).unindent().build();
                                                                                                }
                                                                                        ),
                                                                                objectType.getFields().stream()
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                                                                                        .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                                                                        .filter(fieldDefinition -> fieldDefinition.getType().hasList())
                                                                                        .map(fieldDefinition -> {
                                                                                                    ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
                                                                                                    CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", fieldDefinition.getName());
                                                                                                    CodeBlock invokeCodeBlock = CodeBlock.of("return $T.justOrEmpty($L.$L()).flatMapMany($T::fromIterable).flatMap(item-> $L(item, field)).collectList().doOnNext($L -> $L.$L($L));",
                                                                                                            ClassName.get(Mono.class),
                                                                                                            resultParameterName,
                                                                                                            getFieldGetterMethodName(fieldDefinition.getName()),
                                                                                                            ClassName.get(Flux.class),
                                                                                                            typeNameToFieldName(fieldTypeDefinition.getName()),
                                                                                                            getFieldName(fieldDefinition.getName()),
                                                                                                            resultParameterName,
                                                                                                            getFieldSetterMethodName(fieldDefinition.getName()),
                                                                                                            getFieldName(fieldDefinition.getName())
                                                                                                    );
                                                                                                    return CodeBlock.builder().add(caseCodeBlock).indent().add(invokeCodeBlock).unindent().build();
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
                                .add(".then()\n")
                                .add(".thenReturn($L)\n", typeParameterName)
                                .unindent()
                                .add(")")
                                .build()
                )
                .build();
    }

    private MethodSpec buildOperationTypeInvokeMethod(ObjectType objectType) {
        String typeParameterName = typeNameToFieldName(objectType.getName());
        MethodSpec.Builder builder = MethodSpec.methodBuilder(typeParameterName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Mono.class), ClassName.get(JsonValue.class)))
                .addAnnotation(Override.class)
                .addParameter(ClassName.get(Operation.class), "operation")
                .addParameter(ClassName.get(JsonValue.class), "jsonValue");

        return builder
                .addStatement("$T operationType = documentManager.getOperationTypeOrError(operation)",
                        ClassName.get(ObjectType.class)
                )
                .addStatement(
                        CodeBlock.builder()
                                .add("return $T.justOrEmpty(operation.getFields())\n",
                                        ClassName.get(Mono.class)
                                )
                                .add(".flatMapMany($T::fromIterable)\n",
                                        ClassName.get(Flux.class)
                                )
                                .add(".flatMap(field -> {\n")
                                .indent()
                                .add(
                                        CodeBlock.builder()
                                                .add("String selectionName = $T.ofNullable(field.getAlias()).orElse(field.getName());\n", ClassName.get(Optional.class))
                                                .beginControlFlow("switch (field.getName())")
                                                .add(
                                                        CodeBlock.join(
                                                                Streams
                                                                        .concat(
                                                                                objectType.getFields().stream()
                                                                                        .filter(packageManager::isLocalPackage)
                                                                                        .filter(FieldDefinition::isInvokeField)
                                                                                        .map(fieldDefinition -> {
                                                                                                    String invokeClassName = fieldDefinition.getInvokeClassNameOrError();
                                                                                                    String methodName = fieldDefinition.getInvokeMethodNameOrError();
                                                                                                    List<Map.Entry<String, String>> parameters = fieldDefinition.getInvokeParametersList();
                                                                                                    ClassName returnClassName = toClassName(getClassName(fieldDefinition.getInvokeReturnClassNameOrError()));
                                                                                                    String apiVariableName = typeNameToFieldName(toClassName(invokeClassName).simpleName());

                                                                                                    CodeBlock parametersCodeBlock = CodeBlock.join(
                                                                                                            parameters.stream()
                                                                                                                    .map(parameter ->
                                                                                                                            CodeBlock.of("jsonb.fromJson(field.getArguments().get($S).toString(), $T.class)",
                                                                                                                                    parameter.getKey(),
                                                                                                                                    toTypeName(parameter.getValue())
                                                                                                                            )
                                                                                                                    )
                                                                                                                    .collect(Collectors.toList()), ", ");

                                                                                                    CodeBlock.Builder codeBlockBuilder = CodeBlock.builder().add("case $S:\n", fieldDefinition.getName())
                                                                                                            .indent();

                                                                                                    if (returnClassName.canonicalName().equals(Mono.class.getCanonicalName())) {
                                                                                                        codeBlockBuilder
                                                                                                                .add("return $L.get().$L($L)\n",
                                                                                                                        apiVariableName,
                                                                                                                        methodName,
                                                                                                                        parametersCodeBlock
                                                                                                                )
                                                                                                                .indent();
                                                                                                    } else if (returnClassName.canonicalName().equals(Flux.class.getCanonicalName())) {
                                                                                                        codeBlockBuilder
                                                                                                                .add("return $L.get().$L($L)\n",
                                                                                                                        apiVariableName,
                                                                                                                        methodName,
                                                                                                                        parametersCodeBlock
                                                                                                                )
                                                                                                                .indent()
                                                                                                                .add(".collectList()\n");
                                                                                                    } else {
                                                                                                        codeBlockBuilder
                                                                                                                .add("return $T.just($L.get().$L($L))\n",
                                                                                                                        ClassName.get(Mono.class),
                                                                                                                        apiVariableName,
                                                                                                                        methodName,
                                                                                                                        parametersCodeBlock
                                                                                                                )
                                                                                                                .indent();
                                                                                                    }
                                                                                                    return codeBlockBuilder
                                                                                                            .add(".map($L -> new $T<>(selectionName, operationBuilder.updateJsonValue(field, operationType.getField(field.getName()), jsonProvider.createReader(new $T(jsonb.toJson($L))).readValue())));",
                                                                                                                    methodName,
                                                                                                                    ClassName.get(AbstractMap.SimpleEntry.class),
                                                                                                                    ClassName.get(StringReader.class),
                                                                                                                    methodName
                                                                                                            )
                                                                                                            .unindent()
                                                                                                            .unindent()
                                                                                                            .build();
                                                                                                }
                                                                                        ),
                                                                                objectType.getFields().stream()
                                                                                        .filter(packageManager::isLocalPackage)
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                                                                                        .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.getType().hasList())
                                                                                        .map(fieldDefinition -> {
                                                                                                    ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
                                                                                                    String methodName = typeNameToFieldName(fieldTypeDefinition.getName());
                                                                                                    return CodeBlock.builder().add("case $S:\n", fieldDefinition.getName())
                                                                                                            .indent()
                                                                                                            .add("return $L(jsonb.fromJson(jsonValue.asJsonObject().get(selectionName).toString(), $T.class), field)\n",
                                                                                                                    methodName,
                                                                                                                    toClassName(fieldTypeDefinition.getClassNameOrError())
                                                                                                            )
                                                                                                            .indent()
                                                                                                            .add(".map($L -> new $T<>(selectionName, operationBuilder.updateJsonValue(field, operationType.getField(field.getName()), jsonProvider.createReader(new $T(jsonb.toJson($L))).readValue())));",
                                                                                                                    methodName,
                                                                                                                    ClassName.get(AbstractMap.SimpleEntry.class),
                                                                                                                    ClassName.get(StringReader.class),
                                                                                                                    methodName
                                                                                                            )
                                                                                                            .unindent()
                                                                                                            .unindent()
                                                                                                            .build();
                                                                                                }
                                                                                        ),
                                                                                objectType.getFields().stream()
                                                                                        .filter(packageManager::isLocalPackage)
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                                                                                        .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                                                                                        .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                                                                        .filter(fieldDefinition -> fieldDefinition.getType().hasList())
                                                                                        .map(fieldDefinition -> {
                                                                                                    ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
                                                                                                    String methodName = typeNameToFieldName(fieldTypeDefinition.getName());
                                                                                                    return CodeBlock.builder().add("case $S:\n", fieldDefinition.getName())
                                                                                                            .indent()
                                                                                                            .add("return $T.justOrEmpty(($T<$T>) jsonb.fromJson(jsonValue.asJsonObject().get(selectionName).toString(), new $T<$T<$T>>() {}.type))\n",
                                                                                                                    ClassName.get(Mono.class),
                                                                                                                    ClassName.get(List.class),
                                                                                                                    toClassName(fieldTypeDefinition.getClassNameOrError()),
                                                                                                                    ClassName.get(TypeDefinition.class),
                                                                                                                    ClassName.get(List.class),
                                                                                                                    toClassName(fieldTypeDefinition.getClassNameOrError())
                                                                                                            )
                                                                                                            .indent()
                                                                                                            .add(".flatMapMany($T::fromIterable)\n",
                                                                                                                    ClassName.get(Flux.class)
                                                                                                            )
                                                                                                            .add(".flatMap(item -> $L(item, field))\n",
                                                                                                                    methodName
                                                                                                            )
                                                                                                            .add(".collectList()\n")
                                                                                                            .add(".map($L ->  jsonProvider.createReader(new $T(jsonb.toJson($L))).readValue())\n",
                                                                                                                    methodName,
                                                                                                                    ClassName.get(StringReader.class),
                                                                                                                    methodName
                                                                                                            )
                                                                                                            .add(".defaultIfEmpty($T.NULL)\n",
                                                                                                                    ClassName.get(JsonValue.class)
                                                                                                            )
                                                                                                            .add(".map(jsonArray -> new $T<>(selectionName, operationBuilder.updateJsonValue(field, operationType.getField(field.getName()), jsonArray)));",
                                                                                                                    ClassName.get(AbstractMap.SimpleEntry.class)
                                                                                                            )
                                                                                                            .unindent()
                                                                                                            .unindent()
                                                                                                            .build();
                                                                                                }
                                                                                        ),
                                                                                Stream.of(CodeBlock.builder().add(CodeBlock.of("default:\n")).indent().add(CodeBlock.of("return $T.just(new $T<>(selectionName, jsonValue.asJsonObject().get(selectionName)));\n", ClassName.get(Mono.class), ClassName.get(AbstractMap.SimpleEntry.class))).unindent().build())
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
                                .add(".collectList()\n")
                                .add(".map(list -> list.stream().collect($T.toJsonObject()))",
                                        ClassName.get(JsonCollectors.class)
                                )
                                .build()
                )
                .build();
    }
}
