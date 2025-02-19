package io.graphoenix.java.implementer;

import com.squareup.javapoet.*;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import java.util.*;

import static io.graphoenix.core.utils.TypeNameUtil.*;
import static io.graphoenix.java.utils.NameUtil.getFieldGetterMethodName;
import static io.graphoenix.spi.constant.Hammurabi.ENUM_PROTOCOL_ENUM_VALUE_GRPC;

@ApplicationScoped
public class PackageOperationInterfaceImplementer {

    private final DocumentManager documentManager;

    @Inject
    public PackageOperationInterfaceImplementer(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public CodeBlock getCodeBlock(Operation operation, CodeBlock parameterMapCodeBlock) {
        String packageName = operation.getPackageNameOrError();
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        Field field = operation.getSelection(0).asField();
        FieldDefinition fieldDefinition = operationType.getField(field.getName());
        String protocol = fieldDefinition.getFetchProtocol().orElse(new EnumValue(ENUM_PROTOCOL_ENUM_VALUE_GRPC)).getValue();
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);

        ClassName operationTypeClassName = ClassName.get(fieldTypeDefinition.getPackageNameOrError() + ".dto.objectType", operationType.getName());
        String fieldGetterMethodName = getFieldGetterMethodName(field.getName());
        String returnTypeName = operation.getInvokeReturnClassNameOrError();
        String returnClassName = getClassName(returnTypeName);
        String[] returnTypeArgumentTypeNames = getArgumentTypeNames(returnTypeName);

        CodeBlock getOperation = CodeBlock.of("documentManager.getDocument().getDefinition($S).toString()", operation.getName());

        if (returnTypeArgumentTypeNames.length > 0) {
            String argumentTypeName0 = getArgumentTypeName0(returnTypeName);
            if (returnClassName.equals(Mono.class.getCanonicalName())) {
                String[] monoArgumentTypeArgumentNames = getArgumentTypeNames(argumentTypeName0);
                if (monoArgumentTypeArgumentNames.length > 0) {
                    Optional<ClassName> collectionImplementationClassName = getCollectionImplementationClassName(argumentTypeName0);
                    if (collectionImplementationClassName.isPresent()) {
                        return CodeBlock.of(
                                "packageOperationDAO.fetchAsync($S, $S, $L, $L, $T.class).mapNotNull($T::$L).mapNotNull($T::new)",
                                packageName,
                                protocol,
                                getOperation,
                                parameterMapCodeBlock,
                                operationTypeClassName,
                                operationTypeClassName,
                                fieldGetterMethodName,
                                collectionImplementationClassName.get()
                        );
                    }
                }
                return CodeBlock.of(
                        "packageOperationDAO.fetchAsync($S, $S, $L, $L, $T.class).mapNotNull($T::$L)",
                        packageName,
                        protocol,
                        getOperation,
                        parameterMapCodeBlock,
                        operationTypeClassName,
                        operationTypeClassName,
                        fieldGetterMethodName
                );
            } else {
                Optional<ClassName> collectionImplementationClassName = getCollectionImplementationClassName(returnTypeName);
                return collectionImplementationClassName
                        .map(collectionClassName ->
                                CodeBlock.of(
                                        "packageOperationDAO.fetchAsync($S, $S, $L, $L, $T.class).mapNotNull($T::$L).mapNotNull($T::new)",
                                        packageName,
                                        protocol,
                                        getOperation,
                                        parameterMapCodeBlock,
                                        operationTypeClassName,
                                        operationTypeClassName,
                                        fieldGetterMethodName,
                                        collectionClassName
                                )
                        )
                        .orElseGet(() ->
                                CodeBlock.of(
                                        "packageOperationDAO.fetchAsync($S, $S, $L, $L, $T.class).mapNotNull($T::$L)",
                                        packageName,
                                        protocol,
                                        getOperation,
                                        parameterMapCodeBlock,
                                        operationTypeClassName,
                                        operationTypeClassName,
                                        fieldGetterMethodName
                                )
                        );
            }
        } else {
            return CodeBlock.of(
                    "packageOperationDAO.fetchAsync($S, $S, $L, $L, $T.class).mapNotNull($T::$L)",
                    packageName,
                    protocol,
                    getOperation,
                    parameterMapCodeBlock,
                    operationTypeClassName,
                    operationTypeClassName,
                    fieldGetterMethodName
            );
        }
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
}
