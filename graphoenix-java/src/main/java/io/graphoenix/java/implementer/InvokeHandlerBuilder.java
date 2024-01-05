package io.graphoenix.java.implementer;

import com.google.common.collect.Streams;
import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.java.utils.TypeNameUtil;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.bind.Jsonb;
import org.tinylog.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.java.utils.NameUtil.*;
import static io.graphoenix.java.utils.TypeNameUtil.toClassName;
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
        Logger.info("InvokeHandler build success");
    }

    private JavaFile buildClass() {
        TypeSpec typeSpec = buildInvokeHandler();
        return JavaFile.builder(packageConfig.getHandlerPackageName(), typeSpec).build();
    }

    private TypeSpec buildInvokeHandler() {
        return TypeSpec.classBuilder("InvokeHandler")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ApplicationScoped.class)
                .addField(
                        FieldSpec.builder(
                                ClassName.get(Jsonb.class),
                                "jsonb",
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
                .addParameter(ClassName.get(Jsonb.class), "jsonb")
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
                .addStatement("this.jsonb = jsonb");

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
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .map(this::buildTypeInvokeMethod)
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
                        CodeBlock.join(
                                Stream
                                        .of(
                                                CodeBlock.of(
                                                        "return $T.justOrEmpty($L).flatMap(result -> $T.justOrEmpty(parentField.getFields()).flatMapMany($T::fromIterable)",
                                                        ClassName.get(Mono.class),
                                                        typeParameterName,
                                                        ClassName.get(Mono.class),
                                                        ClassName.get(Flux.class)
                                                ),
                                                CodeBlock.builder()
                                                        .add(".flatMap(field -> {\n")
                                                        .indent()
                                                        .indent()
                                                        .add(CodeBlock.builder()
                                                                .beginControlFlow("switch (field.getName())")
                                                                .indent()
                                                                .add(CodeBlock.join(
                                                                                Streams.concat(
                                                                                        Stream.ofNullable(invokeMethods.get(objectType.getName()))
                                                                                                .flatMap(map -> map.entrySet().stream())
                                                                                                .flatMap(entry ->
                                                                                                        entry.getValue().stream()
                                                                                                                .map(methodEntry -> {
                                                                                                                            String apiVariableName = typeNameToFieldName(toClassName(entry.getKey()).simpleName());
                                                                                                                            String invokeFieldName = getFieldName(methodEntry.getKey());
                                                                                                                            String fieldSetterMethodName = getFieldSetterMethodName(invokeFieldName);
                                                                                                                            CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", invokeFieldName);
                                                                                                                            CodeBlock invokeCodeBlock;

                                                                                                                            if (toClassName(methodEntry.getValue()).canonicalName().equals(Mono.class.getCanonicalName())) {
                                                                                                                                invokeCodeBlock = CodeBlock.of("return $L.get().$L($L).doOnNext(result -> $L.$L(result));",
                                                                                                                                        apiVariableName,
                                                                                                                                        methodEntry.getKey(),
                                                                                                                                        resultParameterName,
                                                                                                                                        resultParameterName,
                                                                                                                                        fieldSetterMethodName
                                                                                                                                );
                                                                                                                            } else if (toClassName(methodEntry.getValue()).canonicalName().equals(Flux.class.getCanonicalName())) {
                                                                                                                                invokeCodeBlock = CodeBlock.of("return $L.get().$L($L).collectList().doOnNext(result -> $L.$L(result));",
                                                                                                                                        apiVariableName,
                                                                                                                                        methodEntry.getKey(),
                                                                                                                                        resultParameterName,
                                                                                                                                        resultParameterName,
                                                                                                                                        fieldSetterMethodName
                                                                                                                                );
                                                                                                                            } else {
                                                                                                                                invokeCodeBlock = CodeBlock.of("return $T.justOrEmpty($L.get().$L($L)).doOnNext(result -> $L.$L(result));",
                                                                                                                                        ClassName.get(Mono.class),
                                                                                                                                        apiVariableName,
                                                                                                                                        methodEntry.getKey(),
                                                                                                                                        resultParameterName,
                                                                                                                                        resultParameterName,
                                                                                                                                        fieldSetterMethodName
                                                                                                                                );
                                                                                                                            }
                                                                                                                            return CodeBlock.builder().add(caseCodeBlock).indent().add(invokeCodeBlock).unindent().build();
                                                                                                                        }
                                                                                                                )
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
                                                                                                            CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", fieldTypeDefinition.getName());
                                                                                                            CodeBlock invokeCodeBlock = CodeBlock.of("return $T.from($T.justOrEmpty($L.$L())).flatMap($T::fromIterable).flatMap(item-> $L(item, field)).collectList().doOnNext($L -> $L.$L($L));",
                                                                                                                    ClassName.get(Flux.class),
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
                                                                                ).collect(Collectors.toList()),
                                                                                System.lineSeparator()
                                                                        )
                                                                )
                                                                .unindent()
                                                                .endControlFlow()
                                                                .build()
                                                        )
                                                        .unindent()
                                                        .add("})\n")
                                                        .add(".then()\n")
                                                        .add(".thenReturn($L)\n", typeParameterName)
                                                        .unindent()
                                                        .add(")\n")
                                                        .add(".defaultIfEmpty(null)")
                                                        .build()
                                        )
                                        .collect(Collectors.toList()),
                                System.lineSeparator()
                        )
                )
                .build();
    }
}
