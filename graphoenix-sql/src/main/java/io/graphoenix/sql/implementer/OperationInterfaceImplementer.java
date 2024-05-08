package io.graphoenix.sql.implementer;

import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.core.utils.FileUtil;
import io.graphoenix.spi.dao.OperationDAO;
import io.graphoenix.spi.error.GraphQLErrorType;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ArrayValueWithVariable;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputObjectType;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.sql.handler.SQLFormatHandler;
import io.graphoenix.sql.translator.MutationTranslator;
import io.graphoenix.sql.translator.QueryTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import org.tinylog.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.utils.TypeNameUtil.*;
import static io.graphoenix.java.utils.NameUtil.getFieldGetterMethodName;
import static io.graphoenix.java.utils.TypeNameUtil.toClassName;
import static io.graphoenix.java.utils.TypeNameUtil.toTypeName;
import static io.graphoenix.spi.constant.Hammurabi.OPERATION_MUTATION_NAME;
import static io.graphoenix.spi.constant.Hammurabi.OPERATION_QUERY_NAME;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
public class OperationInterfaceImplementer {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final QueryTranslator queryTranslator;
    private final MutationTranslator mutationTranslator;
    private final SQLFormatHandler sqlFormatHandler;
    private final PackageConfig packageConfig;

    @Inject
    public OperationInterfaceImplementer(DocumentManager documentManager, PackageManager packageManager, QueryTranslator queryTranslator, MutationTranslator mutationTranslator, SQLFormatHandler sqlFormatHandler, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.queryTranslator = queryTranslator;
        this.mutationTranslator = mutationTranslator;
        this.sqlFormatHandler = sqlFormatHandler;
        this.packageConfig = packageConfig;
    }

    public void writeToFiler(Filer filer) throws IOException {
        documentManager.getDocument().getOperations()
                .filter(packageManager::isLocalPackage)
                .map(operation ->
                        new AbstractMap.SimpleEntry<>(
                                operation.getInvokeClassNameOrError(),
                                operation
                        )
                )
                .collect(
                        Collectors.groupingBy(
                                Map.Entry<String, Operation>::getKey,
                                Collectors.mapping(
                                        Map.Entry<String, Operation>::getValue,
                                        Collectors.toList()
                                )
                        )
                )
                .forEach((interfaceName, operationList) -> {
                            int index = interfaceName.lastIndexOf(".");
                            String packageName = interfaceName.substring(0, index);
                            String simpleName = interfaceName.substring(index + 1);
                            try {
                                buildImplementClass(packageName, simpleName, operationList).writeTo(filer);
                                for (Operation operation : operationList) {
                                    Map.Entry<String, String> sqlFileEntry = buildSQLFile(simpleName, operation);
                                    FileObject fileObject = filer.createResource(
                                            StandardLocation.CLASS_OUTPUT,
                                            packageName,
                                            sqlFileEntry.getKey()
                                    );
                                    Writer writer = fileObject.openWriter();
                                    writer.write(sqlFileEntry.getValue());
                                    writer.close();
                                    Logger.info("{} build success", sqlFileEntry.getKey());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    private Map.Entry<String, String> buildSQLFile(String simpleName, Operation operation) {
        String methodName = operation.getInvokeMethodNameOrError();
        int methodIndex = operation.getInvokeMethodIndexOrError();
        String sqlFileName = "SQL" + simpleName + "Impl_" + methodName + "_" + methodIndex + ".sql";
        if (operation.getOperationType() == null || operation.getOperationType().equals(OPERATION_QUERY_NAME)) {
            return new AbstractMap.SimpleEntry<>(sqlFileName, sqlFormatHandler.query(queryTranslator.operationToSelectSQL(operation).orElse("")));
        } else if (operation.getOperationType().equals(OPERATION_MUTATION_NAME)) {
            return new AbstractMap.SimpleEntry<>(sqlFileName, sqlFormatHandler.mutation(mutationTranslator.operationToStatementSQLStream(operation)));
        } else {
            throw new GraphQLErrors(GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE);
        }
    }

    public JavaFile buildImplementClass(String packageName, String simpleName, List<Operation> operationList) {

        TypeSpec.Builder builder = TypeSpec.classBuilder("SQL" + simpleName + "Impl")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ApplicationScoped.class)
                .addSuperinterface(ClassName.get(packageName, simpleName))
                .addField(
                        FieldSpec
                                .builder(
                                        ClassName.get(OperationDAO.class),
                                        "operationDAO",
                                        Modifier.PRIVATE,
                                        Modifier.FINAL
                                )
                                .build()
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
                                ClassName.get(packageConfig.getHandlerPackageName(), "InputInvokeHandler"),
                                "inputInvokeHandler",
                                Modifier.PRIVATE,
                                Modifier.FINAL
                        ).build()
                )
                .addFields(buildSQLFields(operationList))
                .addStaticBlock(buildSQLFieldInitializeCodeBlock(packageName, simpleName, operationList))
                .addMethod(buildConstructor())
                .addMethods(
                        operationList.stream()
                                .sorted(Comparator.comparingInt(Operation::getInvokeMethodIndexOrError))
                                .map(this::executableElementToMethodSpec)
                                .collect(Collectors.toList())
                );
        return JavaFile.builder(packageName, builder.build()).build();
    }

    private List<FieldSpec> buildSQLFields(List<Operation> operationList) {
        return operationList.stream()
                .sorted(Comparator.comparingInt(Operation::getInvokeMethodIndexOrError))
                .map(this::buildSQLField)
                .collect(Collectors.toList());
    }

    private FieldSpec buildSQLField(Operation operation) {
        return FieldSpec
                .builder(
                        TypeName.get(String.class),
                        operation.getInvokeMethodNameOrError() + "_" + operation.getInvokeMethodIndexOrError(),
                        Modifier.PRIVATE,
                        Modifier.STATIC,
                        Modifier.FINAL
                )
                .build();
    }

    private CodeBlock buildSQLFieldInitializeCodeBlock(String packageName, String simpleName, List<Operation> operationList) {
        ClassName typeClassName = ClassName.get(packageName, "SQL" + simpleName + "Impl");
        CodeBlock.Builder builder = CodeBlock.builder();
        operationList.stream()
                .sorted(Comparator.comparingInt(Operation::getInvokeMethodIndexOrError))
                .forEach(operation ->
                        builder.addStatement(
                                "$L = $T.fileToString($T.class, $S)",
                                operation.getInvokeMethodNameOrError() + "_" + operation.getInvokeMethodIndexOrError(),
                                ClassName.get(FileUtil.class),
                                typeClassName,
                                "SQL" + simpleName + "Impl_" + operation.getInvokeMethodNameOrError() + "_" + operation.getInvokeMethodIndexOrError() + ".sql"
                        )
                );
        return builder.build();
    }

    private MethodSpec buildConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Inject.class)
                .addParameter(ParameterSpec.builder(ClassName.get(OperationDAO.class), "operationDAO").build())
                .addParameter(ClassName.get(Jsonb.class), "jsonb")
                .addParameter(ClassName.get(packageConfig.getHandlerPackageName(), "InputInvokeHandler"), "inputInvokeHandler")
                .addStatement("this.operationDAO = operationDAO")
                .addStatement("this.jsonb = jsonb")
                .addStatement("this.inputInvokeHandler = inputInvokeHandler");

        return builder.build();
    }

    private MethodSpec executableElementToMethodSpec(Operation operation) {
        TypeName typeName = toTypeName(operation.getInvokeReturnClassNameOrError());
        List<Map.Entry<String, String>> parameters = operation.getInvokeParametersList();
        Field field = operation.getSelection(0).asField();
        FieldDefinition fieldDefinition = documentManager.getOperationTypeOrError(operation).getField(field.getName());

        MethodSpec.Builder builder = MethodSpec.methodBuilder(operation.getInvokeMethodNameOrError())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameters(
                        parameters.stream()
                                .map(entry -> ParameterSpec.builder(toTypeName(entry.getValue()), entry.getKey()).build())
                                .collect(Collectors.toList())
                )
                .returns(typeName);

        CodeBlock parameterMapCodeBlock;
        if (parameters.isEmpty()) {
            parameterMapCodeBlock = CodeBlock.of("$T.of()", ClassName.get(Map.class));
        } else {
            parameterMapCodeBlock = CodeBlock.of(
                    "$T.of($L)",
                    ClassName.get(Map.class),
                    CodeBlock
                            .join(
                                    parameters.stream()
                                            .map(entry -> {
                                                        InputValue variableInputValue = getVariableInputValue(entry.getKey(), field, fieldDefinition).orElseThrow(() -> new RuntimeException("variable inputValue not found: " + entry.getKey()));
                                                        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(variableInputValue);
                                                        if (inputValueTypeDefinition.isInputObject()) {
                                                            return CodeBlock.of("$S, (Object)$L", entry.getKey(), entry.getKey() + "Invoked");
                                                        } else {
                                                            return CodeBlock.of("$S, (Object)$L", entry.getKey(), entry.getKey());
                                                        }
                                                    }
                                            )
                                            .collect(Collectors.toList()),
                                    ", "
                            )
            );
        }

        CodeBlock codeBlock = parameters.stream()
                .map(entry ->
                        new AbstractMap.SimpleEntry<>(entry.getKey(), getVariableInputValue(entry.getKey(), field, fieldDefinition).orElseThrow(() -> new RuntimeException("variable inputValue not found: " + entry.getKey())))


                )
                .filter(entry -> documentManager.getInputValueTypeDefinition(entry.getValue()).isInputObject())
                .reduce(getCodeBlock(operation, parameterMapCodeBlock),
                        (pre, cur) -> {
                            InputValue variableInputValue = cur.getValue();
                            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(variableInputValue);
                            String methodName = typeNameToFieldName(inputValueTypeDefinition.getName());
                            CodeBlock invokeCodeBlock;
                            if (variableInputValue.getType().hasList()) {
                                invokeCodeBlock = CodeBlock.of("$T.fromIterable(new $T($L).getValueWithVariables()).flatMap(valueWithVariable -> inputInvokeHandler.$L(jsonb.fromJson(valueWithVariable.toString(), $T.class), valueWithVariable.asObject())).collectList().flatMap($LInvoked -> $L)",
                                        ClassName.get(Flux.class),
                                        ClassName.get(ArrayValueWithVariable.class),
                                        cur.getKey(),
                                        methodName,
                                        toClassName(documentManager.getDocument().getInputObjectTypeOrError(inputValueTypeDefinition.getName()).getClassNameOrError()),
                                        cur.getKey(),
                                        pre
                                );
                            } else {
                                invokeCodeBlock = CodeBlock.of("$T.just(new $T($L)).flatMap(objectValueWithVariable -> inputInvokeHandler.$L(jsonb.fromJson(objectValueWithVariable.toString(), $T.class), objectValueWithVariable)).flatMap($LInvoked -> $L)",
                                        ClassName.get(Mono.class),
                                        ClassName.get(ObjectValueWithVariable.class),
                                        cur.getKey(),
                                        methodName,
                                        toClassName(documentManager.getDocument().getInputObjectTypeOrError(inputValueTypeDefinition.getName()).getClassNameOrError()),
                                        cur.getKey(),
                                        pre
                                );
                            }
                            return invokeCodeBlock;
                        },
                        (x, y) -> y
                );

        List<String> thrownTypes = operation.getInvokeThrownTypes().collect(Collectors.toList());
        if (thrownTypes.isEmpty()) {
            builder.beginControlFlow("try")
                    .addStatement("return $L", codeBlock)
                    .nextControlFlow("catch($T e)", Exception.class)
                    .addStatement("throw new $T(e)", GraphQLErrors.class)
                    .endControlFlow();
        } else {
            builder.addStatement("return $L", codeBlock);
        }
        return builder.build();
    }

    private CodeBlock getCodeBlock(Operation operation, CodeBlock parameterMapCodeBlock) {
        String fieldName = operation.getSelection(0).asField().getName();
        String fieldGetterMethodName = getFieldGetterMethodName(fieldName);
        String methodName = operation.getInvokeMethodNameOrError();
        int methodIndex = operation.getInvokeMethodIndexOrError();
        String sqlFieldName = methodName + "_" + methodIndex;
        String returnTypeName = operation.getInvokeReturnClassNameOrError();
        String returnClassName = getClassName(returnTypeName);
        String[] returnTypeArgumentTypeNames = getArgumentTypeNames(returnTypeName);
        String queryTypeName = documentManager.getDocument().getQueryOperationTypeOrError().getName();
        String mutationTypeName = documentManager.getDocument().getMutationOperationTypeOrError().getName();
        ClassName queryClassName = ClassName.get(packageConfig.getObjectTypePackageName(), queryTypeName);
        ClassName mutationClassName = ClassName.get(packageConfig.getObjectTypePackageName(), mutationTypeName);

        if (returnTypeArgumentTypeNames.length > 0) {
            String argumentTypeName0 = getArgumentTypeName0(returnTypeName);
            if (returnClassName.equals(Mono.class.getCanonicalName())) {
                String[] monoArgumentTypeArgumentNames = getArgumentTypeNames(argumentTypeName0);
                if (operation.getOperationType() == null || operation.getOperationType().equals(OPERATION_QUERY_NAME)) {
                    if (monoArgumentTypeArgumentNames.length > 0) {
                        Optional<ClassName> collectionImplementationClassName = getCollectionImplementationClassName(argumentTypeName0);
                        if (collectionImplementationClassName.isPresent()) {
                            return CodeBlock.of(
                                    "operationDAO.findAsync($L, $L, $T.class).mapNotNull($T::$L).mapNotNull($T::new)",
                                    sqlFieldName,
                                    parameterMapCodeBlock,
                                    queryClassName,
                                    queryClassName,
                                    fieldGetterMethodName,
                                    collectionImplementationClassName.get()
                            );
                        }
                    }
                    return CodeBlock.of(
                            "operationDAO.findAsync($L, $L, $T.class).mapNotNull($T::$L)",
                            sqlFieldName,
                            parameterMapCodeBlock,
                            queryClassName,
                            queryClassName,
                            fieldGetterMethodName
                    );
                } else if (operation.getOperationType().equals(OPERATION_MUTATION_NAME)) {
                    if (monoArgumentTypeArgumentNames.length > 0) {
                        Optional<ClassName> collectionImplementationClassName = getCollectionImplementationClassName(argumentTypeName0);
                        if (collectionImplementationClassName.isPresent()) {
                            return CodeBlock.of(
                                    "operationDAO.saveAsync($L, $L, $T.class).mapNotNull($T::$L).mapNotNull($T::new)",
                                    sqlFieldName,
                                    parameterMapCodeBlock,
                                    mutationClassName,
                                    mutationClassName,
                                    fieldGetterMethodName,
                                    collectionImplementationClassName.get()
                            );
                        }
                    }
                    return CodeBlock.of(
                            "operationDAO.saveAsync($L, $L, $T.class).mapNotNull($T::$L)",
                            sqlFieldName,
                            parameterMapCodeBlock,
                            mutationClassName,
                            mutationClassName,
                            fieldGetterMethodName
                    );
                }
            } else {
                if (operation.getOperationType() == null || operation.getOperationType().equals(OPERATION_QUERY_NAME)) {
                    Optional<ClassName> collectionImplementationClassName = getCollectionImplementationClassName(returnTypeName);
                    return collectionImplementationClassName
                            .map(collectionClassName ->
                                    CodeBlock.of(
                                            "new $T(operationDAO.find($L, $L, $T.class).$L())",
                                            collectionClassName,
                                            sqlFieldName,
                                            parameterMapCodeBlock,
                                            queryClassName,
                                            fieldGetterMethodName
                                    )
                            )
                            .orElseGet(() ->
                                    CodeBlock.of(
                                            "operationDAO.find($L, $L, $T.class).$L()",
                                            sqlFieldName,
                                            parameterMapCodeBlock,
                                            queryClassName,
                                            fieldGetterMethodName
                                    )
                            );
                } else if (operation.getOperationType().equals(OPERATION_MUTATION_NAME)) {
                    Optional<ClassName> collectionImplementationClassName = getCollectionImplementationClassName(returnTypeName);
                    return collectionImplementationClassName
                            .map(collectionTypeName ->
                                    CodeBlock.of(
                                            "new $T(operationDAO.save($L, $L, $T.class).$L())",
                                            collectionTypeName,
                                            sqlFieldName,
                                            parameterMapCodeBlock,
                                            mutationClassName,
                                            fieldGetterMethodName
                                    )
                            )
                            .orElseGet(() ->
                                    CodeBlock.of(
                                            "operationDAO.save($L, $L, $T.class).$L()",
                                            sqlFieldName,
                                            parameterMapCodeBlock,
                                            mutationClassName,
                                            fieldGetterMethodName
                                    )
                            );
                }
            }
        } else {
            if (operation.getOperationType() == null || operation.getOperationType().equals(OPERATION_QUERY_NAME)) {
                return CodeBlock.of(
                        "operationDAO.find($L, $L, $T.class).$L()",
                        sqlFieldName,
                        parameterMapCodeBlock,
                        queryClassName,
                        fieldGetterMethodName
                );
            } else if (operation.getOperationType().equals(OPERATION_MUTATION_NAME)) {
                return CodeBlock.of(
                        "operationDAO.save($L, $L, $T.class).$L()",
                        sqlFieldName,
                        parameterMapCodeBlock,
                        mutationClassName,
                        fieldGetterMethodName
                );
            }
        }
        throw new RuntimeException("unsupported return class: " + returnTypeName);
    }

    private Optional<ClassName> getCollectionImplementationClassName(String typeName) {
        String className = getClassName(typeName);
        if (className.equals(List.class.getCanonicalName())) {
            return Optional.of(ClassName.get(ArrayList.class));
        } else if (className.equals(Set.class.getCanonicalName())) {
            return Optional.of(ClassName.get(LinkedHashSet.class));
        } else {
            return Optional.empty();
        }
    }

    private Optional<InputValue> getVariableInputValue(String name, Field field, FieldDefinition fieldDefinition) {
        return Stream.ofNullable(field.getArguments())
                .flatMap(arguments -> arguments.getArguments().entrySet().stream())
                .filter(entry -> entry.getValue().isVariable())
                .filter(entry -> entry.getValue().asVariable().getName().equals(name))
                .findFirst()
                .map(entry -> fieldDefinition.getArgument(entry.getKey()))
                .or(() ->
                        Stream.ofNullable(field.getArguments())
                                .flatMap(arguments -> arguments.getArguments().entrySet().stream())
                                .filter(entry -> entry.getValue().isObject())
                                .flatMap(entry ->
                                        getVariableInputValue(
                                                name,
                                                entry.getValue().asObject(),
                                                documentManager.getInputValueTypeDefinition(fieldDefinition.getArgument(entry.getKey())).asInputObject()
                                        ).stream()
                                )
                                .findFirst()
                                .or(() ->
                                        Stream.ofNullable(field.getArguments())
                                                .flatMap(arguments -> arguments.getArguments().entrySet().stream())
                                                .filter(entry -> entry.getValue().isArray())
                                                .filter(entry ->
                                                        entry.getValue().asArray().getValueWithVariables().stream()
                                                                .filter(ValueWithVariable::isVariable)
                                                                .anyMatch(valueWithVariable -> valueWithVariable.asVariable().getName().equals(name))
                                                )
                                                .findFirst()
                                                .map(entry -> fieldDefinition.getArgument(entry.getKey()))
                                                .or(() ->
                                                        Stream.ofNullable(field.getArguments())
                                                                .flatMap(arguments -> arguments.getArguments().entrySet().stream())
                                                                .filter(entry -> entry.getValue().isArray())
                                                                .flatMap(entry ->
                                                                        entry.getValue().asArray().getValueWithVariables().stream()
                                                                                .flatMap(valueWithVariable ->
                                                                                        getVariableInputValue(
                                                                                                name,
                                                                                                valueWithVariable.asObject(),
                                                                                                documentManager.getInputValueTypeDefinition(fieldDefinition.getArgument(entry.getKey())).asInputObject()
                                                                                        ).stream()
                                                                                )
                                                                )
                                                                .findFirst()
                                                )
                                )
                );
    }

    private Optional<InputValue> getVariableInputValue(String name, ObjectValueWithVariable objectValueWithVariable, InputObjectType inputObjectType) {
        return Stream.ofNullable(objectValueWithVariable.getObjectValueWithVariable())
                .flatMap(stringValueWithVariableMap -> stringValueWithVariableMap.entrySet().stream())
                .filter(entry -> entry.getValue().isVariable())
                .filter(entry -> entry.getValue().asVariable().getName().equals(name))
                .findFirst()
                .map(entry -> inputObjectType.getInputValue(entry.getKey()))
                .or(() ->
                        Stream.ofNullable(objectValueWithVariable.getObjectValueWithVariable())
                                .flatMap(stringValueWithVariableMap -> stringValueWithVariableMap.entrySet().stream())
                                .filter(entry -> entry.getValue().isObject())
                                .flatMap(entry ->
                                        getVariableInputValue(
                                                name,
                                                entry.getValue().asObject(),
                                                documentManager.getInputValueTypeDefinition(inputObjectType.getInputValue(entry.getKey())).asInputObject()
                                        ).stream()
                                )
                                .findFirst()
                                .or(() ->
                                        Stream.ofNullable(objectValueWithVariable.getObjectValueWithVariable())
                                                .flatMap(stringValueWithVariableMap -> stringValueWithVariableMap.entrySet().stream())
                                                .filter(entry -> entry.getValue().isArray())
                                                .filter(entry ->
                                                        entry.getValue().asArray().getValueWithVariables().stream()
                                                                .filter(ValueWithVariable::isVariable)
                                                                .anyMatch(valueWithVariable -> valueWithVariable.asVariable().getName().equals(name))
                                                )
                                                .findFirst()
                                                .map(entry -> inputObjectType.getInputValue(entry.getKey()))
                                                .or(() ->
                                                        Stream.ofNullable(objectValueWithVariable.getObjectValueWithVariable())
                                                                .flatMap(stringValueWithVariableMap -> stringValueWithVariableMap.entrySet().stream())
                                                                .filter(entry -> entry.getValue().isArray())
                                                                .flatMap(entry ->
                                                                        entry.getValue().asArray().getValueWithVariables().stream()
                                                                                .flatMap(valueWithVariable ->
                                                                                        getVariableInputValue(
                                                                                                name,
                                                                                                valueWithVariable.asObject(),
                                                                                                documentManager.getInputValueTypeDefinition(inputObjectType.getInputValue(entry.getKey())).asInputObject()
                                                                                        ).stream()
                                                                                )
                                                                )
                                                                .findFirst()
                                                )
                                )
                );
    }
}
