package io.graphoenix.java.implementer;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.java.utils.TypeNameUtil;
import io.graphoenix.spi.graphql.common.Arguments;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.type.InputObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.bind.Jsonb;
import org.tinylog.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.java.utils.NameUtil.*;
import static io.graphoenix.java.utils.TypeNameUtil.toClassName;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
public class InputInvokeHandlerBuilder {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;
    private Map<String, List<Tuple3<String, String, String>>> invokeMethods;

    @Inject
    public InputInvokeHandlerBuilder(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
    }

    public void writeToFiler(Filer filer) throws IOException {
        this.invokeMethods = documentManager.getDocument().getInputObjectTypes()
                .filter(packageManager::isLocalPackage)
                .filter(InputObjectType::isInvokesInput)
                .map(inputObjectType -> new AbstractMap.SimpleEntry<>(inputObjectType.getName(), inputObjectType.getInputInvokes()))
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        )
                );
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
                                ParameterizedTypeName.get(ClassName.get(Provider.class), ClassName.get(Jsonb.class)),
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
                .flatMap(Collection::stream)
                .map(Tuple3::getT1)
                .collect(Collectors.toSet())
                .stream()
                .map(TypeNameUtil::toClassName)
                .map(className ->
                        FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(Provider.class), className), typeNameToFieldName(className.simpleName()))
                                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                                .build()
                )
                .collect(Collectors.toSet());
    }

    private MethodSpec buildConstructor() {
        Set<String> classNameSet = this.invokeMethods.values().stream()
                .flatMap(Collection::stream)
                .map(Tuple3::getT1)
                .collect(Collectors.toSet());

        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Inject.class)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Provider.class), ClassName.get(Jsonb.class)), "jsonb")
                .addParameters(
                        classNameSet.stream()
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
        return Stream.concat(
                documentManager.getDocument().getInputObjectTypes()
                        .filter(inputObjectType -> inputObjectType.getName().endsWith(SUFFIX_ARGUMENTS))
                        .map(inputObjectType -> buildInputTypeInvokeMethod(inputObjectType, true)),
                documentManager.getDocument().getInputObjectTypes()
                        .filter(inputObjectType ->
                                inputObjectType.getName().endsWith(SUFFIX_INPUT) && documentManager.getDocument().getDefinition(inputObjectType.getName().substring(0, inputObjectType.getName().lastIndexOf(SUFFIX_INPUT))).isObject() ||
                                        inputObjectType.getName().endsWith(SUFFIX_EXPRESSION) && documentManager.getDocument().getDefinition(inputObjectType.getName().substring(0, inputObjectType.getName().lastIndexOf(SUFFIX_EXPRESSION))).isObject()
                        )
                        .map(inputObjectType -> buildInputTypeInvokeMethod(inputObjectType, false))
        )
                .collect(Collectors.toList());
    }

    private MethodSpec buildInputTypeInvokeMethod(InputObjectType inputObjectType, boolean arguments) {
        ClassName typeClassName = toClassName(inputObjectType.getClassNameOrError());
        String typeParameterName = typeNameToFieldName(typeClassName.simpleName());
        String resultParameterName = "result";

        MethodSpec.Builder builder = MethodSpec.methodBuilder(typeParameterName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Mono.class), typeClassName))
                .addParameter(typeClassName, typeParameterName);

        if (arguments) {
            builder.addParameter(ClassName.get(Arguments.class), "arguments");
        } else {
            builder.addParameter(ClassName.get(ObjectValueWithVariable.class), "objectValueWithVariable");
        }

        List<Tuple3<String, String, String>> tuple3List = invokeMethods.get(inputObjectType.getName());
        if (tuple3List != null && tuple3List.size() > 0) {
            int index = 0;
            for (Tuple3<String, String, String> tuple3 : tuple3List) {
                String apiVariableName = typeNameToFieldName(toClassName(tuple3.getT1()).simpleName());
                String methodName = tuple3.getT2();
                ClassName returnClassName = toClassName(tuple3.getT3());
                CodeBlock invokeCodeBlock;
                if (returnClassName.canonicalName().equals(Mono.class.getCanonicalName())) {
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
                    builder.addCode("return $L.map(result -> (($T)result))\n", invokeCodeBlock, typeClassName);
                } else {
                    builder.addCode(".flatMap(result -> $L).map(result -> (($T)result))\n", invokeCodeBlock, typeClassName);
                }
                index++;
            }
        } else {
            builder.addCode("return $T.just($L)", ClassName.get(Mono.class), typeParameterName);
        }

        builder.addCode(
                CodeBlock.join(
                        Stream
                                .of(
                                        arguments ?
                                                CodeBlock.of(
                                                        ".flatMap(result -> $T.fromStream($T.ofNullable(arguments).map($T::getArguments).map($T::entrySet).flatMap($T::stream))",
                                                        ClassName.get(Flux.class),
                                                        ClassName.get(Stream.class),
                                                        ClassName.get(Arguments.class),
                                                        ClassName.get(Map.class),
                                                        ClassName.get(Set.class)
                                                ) :
                                                CodeBlock.of(
                                                        ".flatMap(result -> $T.fromStream($T.ofNullable(objectValueWithVariable).map($T::getObjectValueWithVariable).map($T::entrySet).flatMap($T::stream))",
                                                        ClassName.get(Flux.class),
                                                        ClassName.get(Stream.class),
                                                        ClassName.get(ObjectValueWithVariable.class),
                                                        ClassName.get(Map.class),
                                                        ClassName.get(Set.class)
                                                ),
                                        CodeBlock.builder()
                                                .indent()
                                                .add(".flatMap(entry -> {\n")
                                                .indent()
                                                .add("String fieldName = entry.getKey();\n")
                                                .add(CodeBlock.builder()
                                                        .beginControlFlow("switch (fieldName)")
                                                        .indent()
                                                        .add(CodeBlock.join(
                                                                Streams
                                                                        .concat(
                                                                                inputObjectType.getInputValues().stream()
                                                                                        .filter(inputValue -> documentManager.getInputValueTypeDefinition(inputValue).isInputObject())
                                                                                        .filter(inputValue -> {
                                                                                                    InputObjectType inputValueType = documentManager.getInputValueTypeDefinition(inputValue).asInputObject();
                                                                                                    return inputValueType.getName().endsWith(SUFFIX_INPUT) && documentManager.getDocument().getDefinition(inputValueType.getName().substring(0, inputValueType.getName().lastIndexOf(SUFFIX_INPUT))).isObject() ||
                                                                                                            inputValueType.getName().endsWith(SUFFIX_EXPRESSION) && documentManager.getDocument().getDefinition(inputValueType.getName().substring(0, inputValueType.getName().lastIndexOf(SUFFIX_EXPRESSION))).isObject();
                                                                                                }
                                                                                        )
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
                                                                                        .filter(inputValue -> {
                                                                                                    InputObjectType inputValueType = documentManager.getInputValueTypeDefinition(inputValue).asInputObject();
                                                                                                    return inputValueType.getName().endsWith(SUFFIX_INPUT) && documentManager.getDocument().getDefinition(inputValueType.getName().substring(0, inputValueType.getName().lastIndexOf(SUFFIX_INPUT))).isObject() ||
                                                                                                            inputValueType.getName().endsWith(SUFFIX_EXPRESSION) && documentManager.getDocument().getDefinition(inputValueType.getName().substring(0, inputValueType.getName().lastIndexOf(SUFFIX_EXPRESSION))).isObject();
                                                                                                }
                                                                                        )
                                                                                        .filter(inputValue -> !inputValue.getType().hasList())
                                                                                        .map(inputValue -> {
                                                                                                    InputObjectType inputValueType = documentManager.getInputValueTypeDefinition(inputValue).asInputObject();
                                                                                                    CodeBlock caseCodeBlock = CodeBlock.of("case $S:\n", inputValue.getName());
                                                                                                    CodeBlock invokeCodeBlock = CodeBlock.of("return $T.from($T.justOrEmpty($L.$L()).map($T::size).flatMapMany(size -> $T.range(0, size))).flatMap(index -> $L($T.get($L.$L(), index), entry.getValue().asArray().getValueWithVariables().get(index).asObject())).collectList().doOnNext($L -> $L.$L($L));",
                                                                                                            ClassName.get(Flux.class),
                                                                                                            ClassName.get(Mono.class),
                                                                                                            resultParameterName,
                                                                                                            getFieldGetterMethodName(inputValue.getName()),
                                                                                                            ClassName.get(Collection.class),
                                                                                                            ClassName.get(Flux.class),
                                                                                                            typeNameToFieldName(inputValueType.getName()),
                                                                                                            ClassName.get(Iterables.class),
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
                                                        .unindent()
                                                        .endControlFlow()
                                                        .build()
                                                )
                                                .unindent()
                                                .add("})\n")
                                                .add(".then()\n")
                                                .add(".thenReturn(result)\n")
                                                .unindent()
                                                .add(");")
                                                .build()
                                )
                                .collect(Collectors.toList()),
                        System.lineSeparator()
                )
        );
        return builder.build();
    }
}
