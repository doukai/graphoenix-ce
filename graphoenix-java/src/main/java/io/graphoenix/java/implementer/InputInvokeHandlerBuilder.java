package io.graphoenix.java.implementer;

import com.google.common.collect.Streams;
import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.java.utils.TypeNameUtil;
import io.graphoenix.spi.graphql.common.Arguments;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputObjectType;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.utils.StreamUtil;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.utils.TypeNameUtil.getClassName;
import static io.graphoenix.java.utils.NameUtil.*;
import static io.graphoenix.java.utils.TypeNameUtil.toClassName;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
public class InputInvokeHandlerBuilder {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;
    private Set<String> invokeClassSet;

    @Inject
    public InputInvokeHandlerBuilder(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
    }

    public void writeToFiler(Filer filer) throws IOException {
        this.invokeClassSet = Stream.concat(
                        documentManager.getDocument().getInputObjectTypes()
                                .filter(packageManager::isLocalPackage)
//                              .filter(InputObjectType::isInvokesInput)
                                .flatMap(this::getInvokes)
                                .map(objectValueWithVariable -> objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_CLASS_NAME_NAME)),
                        documentManager.getDocument().getObjectTypes()
                                .filter(packageManager::isLocalPackage)
                                .flatMap(objectType -> objectType.getFields().stream())
                                .flatMap(fieldDefinition -> documentManager.getDirectiveInvokes(fieldDefinition).stream())
                                .filter(objectValueWithVariable -> packageManager.isLocalPackage(objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_PACKAGE_NAME_NAME)))
                                .filter(objectValueWithVariable ->
                                        objectValueWithVariable.getBoolean(INPUT_INVOKE_INPUT_VALUE_ON_EXPRESSION_NAME, false) ||
                                                objectValueWithVariable.getBoolean(INPUT_INVOKE_INPUT_VALUE_ON_INPUT_VALUE_NAME, false)
                                )
                                .map(objectValueWithVariable -> objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_CLASS_NAME_NAME))
                )
                .collect(Collectors.toSet());
        this.buildClass().writeTo(filer);
        Logger.info("InputInvokeHandler build success");
    }

    private JavaFile buildClass() {
        TypeSpec typeSpec = buildInvokeHandler();
        return JavaFile.builder(packageConfig.getHandlerPackageName(), typeSpec).build();
    }

    private TypeSpec buildInvokeHandler() {
        return TypeSpec.classBuilder("InputInvokeHandler")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ApplicationScoped.class)
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
        return invokeClassSet.stream()
                .map(TypeNameUtil::toClassName)
                .map(className ->
                        FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(Provider.class), className), typeNameToFieldName(className.simpleName()))
                                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                                .build()
                )
                .collect(Collectors.toSet());
    }

    private MethodSpec buildConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Inject.class)
                .addParameter(ClassName.get(DocumentManager.class), "documentManager")
                .addParameter(ClassName.get(Jsonb.class), "jsonb")
                .addParameters(
                        invokeClassSet.stream()
                                .map(TypeNameUtil::toClassName)
                                .map(className ->
                                        ParameterSpec
                                                .builder(
                                                        ParameterizedTypeName.get(ClassName.get(Provider.class), className),
                                                        typeNameToFieldName(className.simpleName())
                                                )
                                                .build()
                                )
                                .collect(Collectors.toList())
                )
                .addStatement("this.documentManager = documentManager")
                .addStatement("this.jsonb = jsonb");

        invokeClassSet.stream()
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
                        documentManager.getDocument().getInputObjectTypes()
                                .filter(inputObjectType -> inputObjectType.getName().endsWith(SUFFIX_ARGUMENTS))
                                .filter(inputObjectType -> documentManager.getInputObjectBelong(inputObjectType) != null)
                                .map(inputObjectType -> buildInputTypeInvokeMethod(inputObjectType, true)),
                        documentManager.getDocument().getInputObjectTypes()
                                .filter(inputObjectType ->
                                        inputObjectType.getName().endsWith(SUFFIX_INPUT) ||
                                                inputObjectType.getName().endsWith(SUFFIX_EXPRESSION)
                                )
                                .filter(inputObjectType -> documentManager.getInputObjectBelong(inputObjectType) != null)
                                .map(inputObjectType -> buildInputTypeInvokeMethod(inputObjectType, false))
                )
                .collect(Collectors.toList());
    }

    private MethodSpec buildInputTypeInvokeMethod(InputObjectType inputObjectType, boolean arguments) {
        ClassName typeClassName = toClassName(inputObjectType.getClassNameOrError());
        String typeParameterName = typeNameToFieldName(typeClassName.simpleName());
        String resultParameterName = "result";
        ObjectType objectType = documentManager.getInputObjectBelong(inputObjectType);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(typeParameterName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Mono.class), typeClassName))
                .addParameter(typeClassName, typeParameterName);

        if (arguments) {
            builder.addParameter(ClassName.get(Arguments.class), "arguments");
        } else {
            builder.addParameter(ClassName.get(ObjectValueWithVariable.class), "objectValueWithVariable");
        }

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        List<ObjectValueWithVariable> invokes = getInvokes(inputObjectType).collect(Collectors.toList());
        if (!invokes.isEmpty()) {
            int index = 0;
            for (ObjectValueWithVariable invoke : invokes) {
                String apiVariableName = typeNameToFieldName(toClassName(invoke.getString(INPUT_INVOKE_INPUT_VALUE_CLASS_NAME_NAME)).simpleName());
                String methodName = invoke.getString(INPUT_INVOKE_INPUT_VALUE_METHOD_NAME_NAME);
                ClassName returnClassName = toClassName(getClassName(invoke.getString(INPUT_INVOKE_INPUT_VALUE_RETURN_CLASS_NAME_NAME)));
                boolean async = invoke.getBoolean(INPUT_INVOKE_INPUT_VALUE_ASYNC_NAME, false);
                CodeBlock invokeCodeBlock;
                if (async) {
                    invokeCodeBlock = CodeBlock.of("$L.get().async($S, $L)",
                            apiVariableName,
                            methodName,
                            index == 0 ? typeParameterName : resultParameterName
                    );
                } else if (returnClassName.canonicalName().equals(Mono.class.getCanonicalName())) {
                    invokeCodeBlock = CodeBlock.of("$L.get().$L($L)",
                            apiVariableName,
                            methodName,
                            index == 0 ? typeParameterName : resultParameterName
                    );
                } else if (returnClassName.canonicalName().equals(Flux.class.getCanonicalName())) {
                    invokeCodeBlock = CodeBlock.of("$L.get().$L($L).last()",
                            apiVariableName,
                            methodName,
                            index == 0 ? typeParameterName : resultParameterName
                    );
                } else {
                    invokeCodeBlock = CodeBlock.of("$T.justOrEmpty($L.get().$L($L))",
                            ClassName.get(Mono.class),
                            apiVariableName,
                            methodName,
                            index == 0 ? typeParameterName : resultParameterName
                    );
                }
                if (index == 0) {
                    codeBlockBuilder.add("return $L.map(result -> (($T)result))\n", invokeCodeBlock, typeClassName);
                } else {
                    codeBlockBuilder.add(".flatMap(result -> $L).map(result -> (($T)result))\n", invokeCodeBlock, typeClassName);
                }
                index++;
            }
        } else {
            codeBlockBuilder.add("return $T.justOrEmpty($L)\n", ClassName.get(Mono.class), typeParameterName);
        }

        inputObjectType.getInputValues().stream()
                .filter(inputValue ->
                        documentManager.getInputValueTypeDefinition(inputValue).isLeaf() ||
                                documentManager.getInputBelong(documentManager.getInputValueTypeDefinition(inputValue)) != null &&
                                        documentManager.getInputBelong(documentManager.getInputValueTypeDefinition(inputValue)).isLeaf()
                )
                .filter(inputValue -> objectType.getField(inputValue.getName()) != null)
                .filter(inputValue -> !documentManager.getDirectiveInvokes(objectType.getField(inputValue.getName())).isEmpty())
                .filter(inputValue ->
                        documentManager.getDirectiveInvokes(objectType.getField(inputValue.getName())).stream()
                                .filter(objectValueWithVariable -> packageManager.isLocalPackage(objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_PACKAGE_NAME_NAME)))
                                .anyMatch(objectValueWithVariable ->
                                        documentManager.getInputValueTypeDefinition(inputValue).getName().endsWith(SUFFIX_EXPRESSION) ?
                                                objectValueWithVariable.getBoolean(INPUT_INVOKE_INPUT_VALUE_ON_EXPRESSION_NAME, false) :
                                                objectValueWithVariable.getBoolean(INPUT_INVOKE_INPUT_VALUE_ON_INPUT_VALUE_NAME, false)
                                )
                )
                .forEach(inputValue -> {
                            FieldDefinition fieldDefinition = objectType.getField(inputValue.getName());
                            CodeBlock getFieldCodeBlock = CodeBlock.of("$L.$L()", resultParameterName, getFieldGetterMethodName(fieldDefinition.getName()));
                            CodeBlock invokesCodeBlock = documentManager.getDirectiveInvokes(fieldDefinition).stream()
                                    .filter(objectValueWithVariable -> packageManager.isLocalPackage(objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_PACKAGE_NAME_NAME)))
                                    .filter(objectValueWithVariable ->
                                            documentManager.getInputValueTypeDefinition(inputValue).getName().endsWith(SUFFIX_EXPRESSION) ?
                                                    objectValueWithVariable.getBoolean(INPUT_INVOKE_INPUT_VALUE_ON_EXPRESSION_NAME, false) :
                                                    objectValueWithVariable.getBoolean(INPUT_INVOKE_INPUT_VALUE_ON_INPUT_VALUE_NAME, false)
                                    )
                                    .reduce(
                                            CodeBlock.builder().build(),
                                            (codeBlock, objectValueWithVariable) -> {
                                                String apiVariableName = typeNameToFieldName(toClassName(objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_CLASS_NAME_NAME)).simpleName());
                                                String methodName = objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_METHOD_NAME_NAME);
                                                ClassName returnClassName = toClassName(getClassName(objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_RETURN_CLASS_NAME_NAME)));
                                                boolean async = objectValueWithVariable.getBoolean(INPUT_INVOKE_INPUT_VALUE_ASYNC_NAME, false);
                                                int parametersCount = objectValueWithVariable.getJsonArray(INPUT_INVOKE_INPUT_VALUE_PARAMETER_NAME).size();
                                                String directiveName = objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_DIRECTIVE_NAME_NAME);
                                                CodeBlock invokeCodeBlock;
                                                if (parametersCount == 1) {
                                                    if (async) {
                                                        invokeCodeBlock = CodeBlock.of("$L.get().async($S, $L)",
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock
                                                        );
                                                    } else if (returnClassName.canonicalName().equals(Mono.class.getCanonicalName())) {
                                                        invokeCodeBlock = CodeBlock.of("$L.get().$L($L)",
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock
                                                        );
                                                    } else if (returnClassName.canonicalName().equals(Flux.class.getCanonicalName())) {
                                                        invokeCodeBlock = CodeBlock.of("$L.get().$L($L).last()",
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock
                                                        );
                                                    } else {
                                                        invokeCodeBlock = CodeBlock.of("$T.justOrEmpty($L.get().$L($L))",
                                                                ClassName.get(Mono.class),
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock
                                                        );
                                                    }
                                                } else if (parametersCount == 2) {
                                                    CodeBlock argumentsBlock = CodeBlock.of("documentManager.getDocument().getObjectTypeOrError($S).getField($S).getDirective($S).getArguments()",
                                                            objectType.getName(),
                                                            fieldDefinition.getName(),
                                                            directiveName
                                                    );

                                                    if (async) {
                                                        invokeCodeBlock = CodeBlock.of("$L.get().async($S, $L, $L)",
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock,
                                                                argumentsBlock
                                                        );
                                                    } else if (returnClassName.canonicalName().equals(Mono.class.getCanonicalName())) {
                                                        invokeCodeBlock = CodeBlock.of("$L.get().$L($L, $L)",
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock,
                                                                argumentsBlock
                                                        );
                                                    } else if (returnClassName.canonicalName().equals(Flux.class.getCanonicalName())) {
                                                        invokeCodeBlock = CodeBlock.of("$L.get().$L($L, $L).last()",
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock,
                                                                argumentsBlock
                                                        );
                                                    } else {
                                                        invokeCodeBlock = CodeBlock.of("$T.justOrEmpty($L.get().$L($L, $L))",
                                                                ClassName.get(Mono.class),
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock,
                                                                argumentsBlock
                                                        );
                                                    }
                                                } else {
                                                    CodeBlock argumentsBlock = CodeBlock.of("documentManager.getDocument().getObjectTypeOrError($S).getField($S).getDirective($S).getArguments()",
                                                            objectType.getName(),
                                                            fieldDefinition.getName(),
                                                            directiveName
                                                    );

                                                    if (async) {
                                                        invokeCodeBlock = CodeBlock.of("$L.get().async($S, $L, $L, $L)",
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock,
                                                                argumentsBlock,
                                                                arguments ? "arguments" : "objectValueWithVariable"
                                                        );
                                                    } else if (returnClassName.canonicalName().equals(Mono.class.getCanonicalName())) {
                                                        invokeCodeBlock = CodeBlock.of("$L.get().$L($L, $L, $L)",
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock,
                                                                argumentsBlock,
                                                                arguments ? "arguments" : "objectValueWithVariable"
                                                        );
                                                    } else if (returnClassName.canonicalName().equals(Flux.class.getCanonicalName())) {
                                                        invokeCodeBlock = CodeBlock.of("$L.get().$L($L, $L, $L).last()",
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock,
                                                                argumentsBlock,
                                                                arguments ? "arguments" : "objectValueWithVariable"
                                                        );
                                                    } else {
                                                        invokeCodeBlock = CodeBlock.of("$T.justOrEmpty($L.get().$L($L, $L, $L))",
                                                                ClassName.get(Mono.class),
                                                                apiVariableName,
                                                                methodName,
                                                                getFieldCodeBlock,
                                                                argumentsBlock,
                                                                arguments ? "arguments" : "objectValueWithVariable"
                                                        );
                                                    }
                                                }
                                                if (codeBlock.isEmpty()) {
                                                    return CodeBlock.of("$L.doOnNext($L -> $L.$L($L))",
                                                            invokeCodeBlock,
                                                            getFieldName(fieldDefinition.getName()),
                                                            resultParameterName,
                                                            getFieldSetterMethodName(fieldDefinition.getName()),
                                                            getFieldName(fieldDefinition.getName())
                                                    );
                                                } else {
                                                    return CodeBlock.of("$L.then($L.doOnNext($L -> $L.$L($L)))",
                                                            codeBlock,
                                                            invokeCodeBlock,
                                                            getFieldName(fieldDefinition.getName()),
                                                            resultParameterName,
                                                            getFieldSetterMethodName(fieldDefinition.getName()),
                                                            getFieldName(fieldDefinition.getName())
                                                    );
                                                }
                                            },
                                            (x, y) -> y
                                    );

                            codeBlockBuilder
                                    .add(".flatMap(result -> \n")
                                    .indent()
                                    .add(invokesCodeBlock)
                                    .add(CodeBlock.of(".thenReturn(result)\n"))
                                    .unindent()
                                    .add(")\n");
                        }
                );

        codeBlockBuilder
                .add(".flatMap(result -> \n")
                .indent()
                .add(arguments ? "$T.justOrEmpty(arguments)\n" : "$T.justOrEmpty(objectValueWithVariable)\n",
                        ClassName.get(Mono.class)
                )
                .add(arguments ? ".map($T::getArguments)\n" : ".map($T::getObjectValueWithVariable)\n",
                        arguments ? ClassName.get(Arguments.class) : ClassName.get(ObjectValueWithVariable.class)
                )
                .add(".map($T::entrySet)\n",
                        ClassName.get(Map.class)
                )
                .add(".flatMapMany($T::fromIterable)\n",
                        ClassName.get(Flux.class)
                )
                .add(".flatMap(entry -> {\n")
                .indent()
                .add(CodeBlock.builder()
                        .beginControlFlow("switch (entry.getKey())")
                        .add(CodeBlock.join(
                                        Streams
                                                .concat(
                                                        inputObjectType.getInputValues().stream()
                                                                .filter(inputValue -> documentManager.getInputValueTypeDefinition(inputValue).isInputObject())
                                                                .filter(inputValue -> documentManager.getInputObjectBelong(documentManager.getInputValueTypeDefinition(inputValue).asInputObject()) != null)
                                                                .filter(inputValue -> !inputValue.getType().hasList())
                                                                .map(inputValue -> {
                                                                            InputObjectType inputValueType = documentManager.getInputValueTypeDefinition(inputValue).asInputObject();
                                                                            CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", inputValue.getName());
                                                                            CodeBlock invokeCodeBlock = CodeBlock.of("return $T.justOrEmpty($L.$L()).flatMap(field -> $L(field, entry.getValue().asObject())).doOnNext($L -> $L.$L($L));",
                                                                                    ClassName.get(Mono.class),
                                                                                    resultParameterName,
                                                                                    getFieldGetterMethodName(inputValue.getName()),
                                                                                    typeNameToFieldName(inputValueType.getName()),
                                                                                    getFieldName(inputValue.getName()),
                                                                                    resultParameterName,
                                                                                    getFieldSetterMethodName(inputValue.getName()),
                                                                                    getFieldName(inputValue.getName())
                                                                            );
                                                                            return CodeBlock.builder().add(caseCodeBlock).indent().add(invokeCodeBlock).unindent().build();
                                                                        }
                                                                ),
                                                        inputObjectType.getInputValues().stream()
                                                                .filter(inputValue -> documentManager.getInputValueTypeDefinition(inputValue).isInputObject())
                                                                .filter(inputValue -> documentManager.getInputObjectBelong(documentManager.getInputValueTypeDefinition(inputValue).asInputObject()) != null)
                                                                .filter(inputValue -> inputValue.getType().hasList())
                                                                .map(inputValue -> {
                                                                            InputObjectType inputValueType = documentManager.getInputValueTypeDefinition(inputValue).asInputObject();
                                                                            CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", inputValue.getName());
                                                                            CodeBlock invokeCodeBlock = CodeBlock.of("return $T.justOrEmpty($L.$L()).map($T::size).flatMapMany(size -> $T.range(0, size)).flatMap(index -> $L(new $T<>($L.$L()).get(index), entry.getValue().asArray().getValueWithVariables().get(index).asObject())).collectList().doOnNext($L -> $L.$L($L));",
                                                                                    ClassName.get(Mono.class),
                                                                                    resultParameterName,
                                                                                    getFieldGetterMethodName(inputValue.getName()),
                                                                                    ClassName.get(Collection.class),
                                                                                    ClassName.get(Flux.class),
                                                                                    typeNameToFieldName(inputValueType.getName()),
                                                                                    ClassName.get(ArrayList.class),
                                                                                    resultParameterName,
                                                                                    getFieldGetterMethodName(inputValue.getName()),
                                                                                    getFieldName(inputValue.getName()),
                                                                                    resultParameterName,
                                                                                    getFieldSetterMethodName(inputValue.getName()),
                                                                                    getFieldName(inputValue.getName())
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
                .add(".thenReturn($L)\n", resultParameterName)
                .unindent()
                .add(")");
        builder.addStatement(codeBlockBuilder.build());
        return builder.build();
    }

    public Stream<ObjectValueWithVariable> getInvokes(InputObjectType inputObjectType) {
        return Stream
                .concat(
                        inputObjectType.getInputInvokes().stream(),
                        inputObjectType.getInterfaces().stream()
                                .flatMap(interfaceObjectName -> documentManager.getDocument().getInputObjectType(interfaceObjectName).stream())
                                .flatMap(this::getInvokes)
                )
                .filter(objectValueWithVariable -> packageManager.isLocalPackage(objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_PACKAGE_NAME_NAME)))
                .filter(StreamUtil.distinctByKey(objectValueWithVariable -> objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_CLASS_NAME_NAME) + "." + objectValueWithVariable.getString(INPUT_INVOKE_INPUT_VALUE_METHOD_NAME_NAME)));
    }
}
