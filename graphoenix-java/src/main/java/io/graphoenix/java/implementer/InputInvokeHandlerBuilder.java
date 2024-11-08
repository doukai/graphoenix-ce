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
import reactor.util.function.Tuple4;
import reactor.util.function.Tuple8;

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
                                .map(Tuple4::getT1),
                        documentManager.getDocument().getObjectTypes()
                                .filter(packageManager::isLocalPackage)
                                .flatMap(objectType -> objectType.getFields().stream())
                                .flatMap(fieldDefinition -> fieldDefinition.getInvokes().stream())
                                .map(Tuple8::getT1)
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
        List<Tuple4<String, String, String, Boolean>> tuple4List = getInvokes(inputObjectType).collect(Collectors.toList());
        if (!tuple4List.isEmpty()) {
            int index = 0;
            for (Tuple4<String, String, String, Boolean> tuple4 : tuple4List) {
                String apiVariableName = typeNameToFieldName(toClassName(tuple4.getT1()).simpleName());
                String methodName = tuple4.getT2();
                ClassName returnClassName = toClassName(getClassName(tuple4.getT3()));
                Boolean async = tuple4.getT4();
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
                                                                .filter(inputValue -> !documentManager.getInputValueTypeDefinition(inputValue).isInputObject())
                                                                .filter(inputValue -> objectType.getField(inputValue.getName()) != null)
                                                                .filter(inputValue -> !objectType.getField(inputValue.getName()).getInvokes().isEmpty())
                                                                .filter(inputValue -> objectType.getField(inputValue.getName()).getInvokes().stream().anyMatch(Tuple8::getT8))
                                                                .map(inputValue -> {
                                                                            FieldDefinition fieldDefinition = objectType.getField(inputValue.getName());
                                                                            CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", fieldDefinition.getName());
                                                                            CodeBlock getFieldCodeBlock = CodeBlock.of("$L.$L()", resultParameterName, getFieldGetterMethodName(fieldDefinition.getName()));
                                                                            CodeBlock invokesCodeBlock = fieldDefinition.getInvokes().stream()
                                                                                    .filter(Tuple8::getT8)
                                                                                    .reduce(
                                                                                            CodeBlock.builder().build(),
                                                                                            (codeBlock, tuple6) -> {
                                                                                                String apiVariableName = typeNameToFieldName(toClassName(tuple6.getT1()).simpleName());
                                                                                                String methodName = tuple6.getT2();
                                                                                                ClassName returnClassName = toClassName(getClassName(tuple6.getT3()));
                                                                                                Boolean async = tuple6.getT4();
                                                                                                int parametersCount = tuple6.getT5().size();
                                                                                                String directiveName = tuple6.getT6();
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
                                                                                                } else {
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
                                                                            CodeBlock invokeCodeBlock = CodeBlock.of("return $L;", invokesCodeBlock);
                                                                            return CodeBlock.builder().add(caseCodeBlock).indent().add(invokeCodeBlock).unindent().build();
                                                                        }
                                                                ),
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

    public Stream<Tuple4<String, String, String, Boolean>> getInvokes(InputObjectType inputObjectType) {
        return Stream
                .concat(
                        inputObjectType.getInputInvokes().stream(),
                        inputObjectType.getInterfaces().stream()
                                .flatMap(interfaceObjectName -> documentManager.getDocument().getInputObjectType(interfaceObjectName).stream())
                                .flatMap(this::getInvokes)
                )
                .filter(StreamUtil.distinctByKey(tuple4 -> tuple4.getT1() + "." + tuple4.getT2()));
    }
}
