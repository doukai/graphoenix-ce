package io.graphoenix.java.implementer;

import com.google.common.collect.Streams;
import com.squareup.javapoet.*;
import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.java.utils.TypeNameUtil;
import io.graphoenix.spi.graphql.AbstractDefinition;
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
                .map(this::buildTypeInvokeMethod)
                .collect(Collectors.toList());
    }

    private MethodSpec buildTypeInvokeMethod(ObjectType objectType) {
        ClassName typeClassName = TYPE_NAME_UTIL.toClassName(packageManager.getClassName(objectType));
        String typeParameterName = getParameterName(objectType);

        return MethodSpec.methodBuilder(typeParameterName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Mono.class), typeClassName))
                .addParameter(typeClassName, typeParameterName)
                .addParameter(ClassName.get(GraphqlParser.SelectionSetContext.class), "selectionSetContext")
                .addStatement(
                        CodeBlock.join(
                                Stream.of(
                                        CodeBlock.of(
                                                "return $T.fromStream($T.ofNullable(selectionSetContext).map($T.SelectionSetContext::selection).flatMap(selectionContexts -> selectionContexts.stream()))",
                                                ClassName.get(Flux.class),
                                                ClassName.get(Stream.class),
                                                ClassName.get(GraphqlParser.class)
                                        ),
                                        CodeBlock.of(".filter(selectionContext -> selectionContext.field() != null)"),
                                        CodeBlock.builder()
                                                .add(".flatMap(selectionContext -> {\n")
                                                .indent()
                                                .add("String fieldName = selectionContext.field().name().getText();\n")
                                                .add(CodeBlock.builder()
                                                        .beginControlFlow("switch (fieldName)")
                                                        .indent()
                                                        .add(CodeBlock.join(
                                                                        Streams.concat(
                                                                                Stream.ofNullable(invokeMethods.get(objectType.name().getText()))
                                                                                        .flatMap(map -> map.entrySet().stream())
                                                                                        .flatMap(entry ->
                                                                                                entry.getValue().stream()
                                                                                                        .map(methodEntry -> {
                                                                                                                    String apiVariableName = typeManager.typeToLowerCamelName(TYPE_NAME_UTIL.toClassName(entry.getKey()).simpleName());
                                                                                                                    String invokeFieldName = typeManager.getInvokeFieldName(methodEntry.getKey());
                                                                                                                    String fieldSetterMethodName = typeManager.getFieldSetterMethodName(invokeFieldName);
                                                                                                                    CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", invokeFieldName);
                                                                                                                    CodeBlock invokeCodeBlock;
                                                                                                                    if (TYPE_UTIL.getClassName(methodEntry.getValue()).canonicalName().equals(PublisherBuilder.class.getCanonicalName())) {
                                                                                                                        invokeCodeBlock = CodeBlock.of("return $T.from($L.get().$L($L).buildRs()).doOnNext(result -> $L.$L(result));",
                                                                                                                                ClassName.get(Mono.class),
                                                                                                                                apiVariableName,
                                                                                                                                methodEntry.getKey(),
                                                                                                                                typeParameterName,
                                                                                                                                typeParameterName,
                                                                                                                                fieldSetterMethodName
                                                                                                                        );
                                                                                                                    } else if (TYPE_UTIL.getClassName(methodEntry.getValue()).canonicalName().equals(Mono.class.getCanonicalName())) {
                                                                                                                        invokeCodeBlock = CodeBlock.of("return $L.get().$L($L).doOnNext(result -> $L.$L(result));",
                                                                                                                                apiVariableName,
                                                                                                                                methodEntry.getKey(),
                                                                                                                                typeParameterName,
                                                                                                                                typeParameterName,
                                                                                                                                fieldSetterMethodName
                                                                                                                        );
                                                                                                                    } else if (TYPE_UTIL.getClassName(methodEntry.getValue()).canonicalName().equals(Flux.class.getCanonicalName())) {
                                                                                                                        invokeCodeBlock = CodeBlock.of("return $L.get().$L($L).collectList().doOnNext(result -> $L.$L(result));",
                                                                                                                                apiVariableName,
                                                                                                                                methodEntry.getKey(),
                                                                                                                                typeParameterName,
                                                                                                                                typeParameterName,
                                                                                                                                fieldSetterMethodName
                                                                                                                        );
                                                                                                                    } else {
                                                                                                                        invokeCodeBlock = CodeBlock.of("return $T.justOrEmpty($L.get().$L($L)).doOnNext(result -> $L.$L(result));",
                                                                                                                                ClassName.get(Mono.class),
                                                                                                                                apiVariableName,
                                                                                                                                methodEntry.getKey(),
                                                                                                                                typeParameterName,
                                                                                                                                typeParameterName,
                                                                                                                                fieldSetterMethodName
                                                                                                                        );
                                                                                                                    }
                                                                                                                    return CodeBlock.builder().add(caseCodeBlock).indent().add(invokeCodeBlock).unindent().build();
                                                                                                                }
                                                                                                        )
                                                                                        ),
                                                                                documentManager.getFields(objectType.name().getText())
                                                                                        .filter(documentManager::isNotInvokeField)
                                                                                        .filter(documentManager::isNotFetchField)
                                                                                        .filter(documentManager::isNotFunctionField)
                                                                                        .filter(fieldDefinitionContext -> documentManager.isObject(documentManager.getFieldTypeName(fieldDefinitionContext.type())))
                                                                                        .filter(fieldDefinitionContext -> !documentManager.fieldTypeIsList(fieldDefinitionContext.type()))
                                                                                        .map(fieldDefinitionContext -> {
                                                                                                    CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", fieldDefinitionContext.name().getText());
                                                                                                    CodeBlock invokeCodeBlock = CodeBlock.of("return $T.justOrEmpty($L.$L()).flatMap(field -> $L(field, selectionContext.field().selectionSet())).doOnNext($L -> $L.$L($L));",
                                                                                                            ClassName.get(Mono.class),
                                                                                                            typeParameterName,
                                                                                                            typeManager.getFieldGetterMethodName(fieldDefinitionContext),
                                                                                                            getObjectMethodName(documentManager.getFieldTypeName(fieldDefinitionContext.type())),
                                                                                                            typeManager.getFieldName(fieldDefinitionContext.name().getText()),
                                                                                                            typeParameterName,
                                                                                                            typeManager.getFieldSetterMethodName(fieldDefinitionContext),
                                                                                                            typeManager.getFieldName(fieldDefinitionContext.name().getText())
                                                                                                    );
                                                                                                    return CodeBlock.builder().add(caseCodeBlock).indent().add(invokeCodeBlock).unindent().build();
                                                                                                }
                                                                                        ),
                                                                                documentManager.getFields(objectType.name().getText())
                                                                                        .filter(documentManager::isNotInvokeField)
                                                                                        .filter(documentManager::isNotFetchField)
                                                                                        .filter(documentManager::isNotFunctionField)
                                                                                        .filter(fieldDefinitionContext -> documentManager.isObject(documentManager.getFieldTypeName(fieldDefinitionContext.type())))
                                                                                        .filter(fieldDefinitionContext -> documentManager.fieldTypeIsList(fieldDefinitionContext.type()))
                                                                                        .map(fieldDefinitionContext -> {
                                                                                                    CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", fieldDefinitionContext.name().getText());
                                                                                                    CodeBlock invokeCodeBlock = CodeBlock.of("return $T.from($T.justOrEmpty($L.$L())).flatMap($T::fromIterable).flatMap(item-> $L(item, selectionContext.field().selectionSet())).collectList().doOnNext($L -> $L.$L($L));",
                                                                                                            ClassName.get(Flux.class),
                                                                                                            ClassName.get(Mono.class),
                                                                                                            typeParameterName,
                                                                                                            typeManager.getFieldGetterMethodName(fieldDefinitionContext),
                                                                                                            ClassName.get(Flux.class),
                                                                                                            getObjectMethodName(documentManager.getFieldTypeName(fieldDefinitionContext.type())),
                                                                                                            typeManager.getFieldName(fieldDefinitionContext.name().getText()),
                                                                                                            typeParameterName,
                                                                                                            typeManager.getFieldSetterMethodName(fieldDefinitionContext),
                                                                                                            typeManager.getFieldName(fieldDefinitionContext.name().getText())
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
                                                .add("})")
                                                .build(),
                                        CodeBlock.of(".then()"),
                                        CodeBlock.of(".thenReturn($L)", typeParameterName)
                                ).collect(Collectors.toList()),
                                System.lineSeparator()
                        )
                )
                .build();
    }

    private String getParameterName(GraphqlParser.ObjectTypeDefinitionContext objectTypeDefinitionContext) {
        return typeManager.typeToLowerCamelName(objectTypeDefinitionContext.name().getText());
    }

    private String getObjectMethodName(String objectName) {
        return typeManager.typeToLowerCamelName(objectName);
    }
}
