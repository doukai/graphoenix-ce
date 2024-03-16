package io.graphoenix.grpc.server.implementer;

import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.grpc.server.utils.ProtobufUtil;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.handler.OperationHandler;
import io.nozdormu.spi.context.BeanContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.StringReader;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;
import static io.graphoenix.spi.utils.NameUtil.getGrpcFieldName;
import static io.graphoenix.spi.utils.NameUtil.getGrpcServiceRpcName;


@ApplicationScoped
public class ReactorGrpcServiceImplementer {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;

    @Inject
    public ReactorGrpcServiceImplementer(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
    }

    public void writeToFiler(Filer filer) throws IOException {
        documentManager.getDocument().getQueryOperationType().stream()
                .flatMap(objectType -> objectType.getFields().stream())
                .filter(packageManager::isLocalPackage)
                .map(fieldDefinition -> new AbstractMap.SimpleEntry<>(fieldDefinition.getPackageName().orElseGet(packageConfig::getPackageName), fieldDefinition))
                .collect(
                        Collectors.groupingBy(
                                Map.Entry<String, FieldDefinition>::getKey,
                                Collectors.mapping(
                                        Map.Entry<String, FieldDefinition>::getValue,
                                        Collectors.toList()
                                )
                        )
                )
                .forEach((packageName, fieldDefinitionList) -> {
                            try {
                                this.buildTypeServiceImplClass(packageName, TYPE_QUERY_NAME, fieldDefinitionList).writeTo(filer);
                                Logger.info("{}.ReactorGrpc" + TYPE_QUERY_NAME + "ServiceImpl build success", packageName);
                            } catch (IOException e) {
                                Logger.error(e);
                            }
                        }
                );

        documentManager.getDocument().getMutationOperationType().stream()
                .flatMap(objectType -> objectType.getFields().stream())
                .filter(packageManager::isLocalPackage)
                .map(fieldDefinition -> new AbstractMap.SimpleEntry<>(fieldDefinition.getPackageName().orElseGet(packageConfig::getPackageName), fieldDefinition))
                .collect(
                        Collectors.groupingBy(
                                Map.Entry<String, FieldDefinition>::getKey,
                                Collectors.mapping(
                                        Map.Entry<String, FieldDefinition>::getValue,
                                        Collectors.toList()
                                )
                        )
                )
                .forEach((packageName, fieldDefinitionList) -> {
                            try {
                                this.buildTypeServiceImplClass(packageName, TYPE_MUTATION_NAME, fieldDefinitionList).writeTo(filer);
                                Logger.info("{}.ReactorGrpc" + TYPE_MUTATION_NAME + "ServiceImpl build success", packageName);
                            } catch (IOException e) {
                                Logger.error(e);
                            }
                        }
                );

        documentManager.getDocument().getObjectTypes()
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .filter(packageManager::isLocalPackage)
                .map(AbstractDefinition::getPackageNameOrError)
                .distinct()
                .forEach(packageName -> {
                            try {
                                this.buildGraphQLServiceImplClass(packageName).writeTo(filer);
                                Logger.info("{}.ReactorGrpcGraphQLServiceImpl build success", packageName);
                            } catch (IOException e) {
                                Logger.error(e);
                            }
                        }
                );
    }

    private JavaFile buildTypeServiceImplClass(String packageName, String operationTypeName, List<FieldDefinition> fieldDefinitionList) {
        TypeSpec typeSpec = buildOperationTypeServiceImpl(packageName, operationTypeName, fieldDefinitionList);
        return JavaFile.builder(packageName + ".grpc", typeSpec).build();
    }

    private TypeSpec buildOperationTypeServiceImpl(String packageName, String operationTypeName, List<FieldDefinition> fieldDefinitionList) {
        String grpcPackageName = packageName + ".grpc";
        String className = "ReactorGrpc" + operationTypeName + "ServiceImpl";
        String superClassName = "Reactor" + operationTypeName + "ServiceGrpc";
        String serviceName = operationTypeName + "ServiceImplBase";

        return TypeSpec.classBuilder(className)
                .superclass(ClassName.get(grpcPackageName, superClassName, serviceName))
                .addModifiers(Modifier.PUBLIC)
                .addField(
                        FieldSpec
                                .builder(
                                        ClassName.get(OperationHandler.class),
                                        "operationHandler",
                                        Modifier.PRIVATE,
                                        Modifier.FINAL
                                )
                                .build()
                )
                .addField(
                        FieldSpec
                                .builder(
                                        ClassName.get(JsonProvider.class),
                                        "jsonProvider",
                                        Modifier.PRIVATE,
                                        Modifier.FINAL
                                )
                                .build()
                )
                .addMethod(buildConstructor())
                .addMethods(buildTypeMethods(packageName, operationTypeName, fieldDefinitionList))
                .build();
    }

    private MethodSpec buildConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.operationHandler = $T.get($T.class)", ClassName.get(BeanContext.class), ClassName.get(OperationHandler.class))
                .addStatement("this.jsonProvider = $T.get($T.class)", ClassName.get(BeanContext.class), ClassName.get(JsonProvider.class))
                .build();
    }

    private List<MethodSpec> buildTypeMethods(String packageName, String operationTypeName, List<FieldDefinition> fieldDefinitionList) {
        return fieldDefinitionList.stream()
                .map(fieldDefinition -> buildTypeMethod(packageName, operationTypeName, fieldDefinition))
                .collect(Collectors.toList());
    }

    private MethodSpec buildTypeMethod(String packageName, String operationTypeName, FieldDefinition fieldDefinition) {
        String grpcPackageName = packageName + ".grpc";
        String requestParameterName = "request";
        String grpcHandlerMethodName = getGrpcFieldName(fieldDefinition.getName());
        String grpcRequestClassName = operationTypeName + getGrpcServiceRpcName(fieldDefinition.getName()) + "Request";
        String grpcResponseClassName = operationTypeName + getGrpcServiceRpcName(fieldDefinition.getName()) + "Response";

        String operationType;
        switch (operationTypeName) {
            case TYPE_QUERY_NAME:
                operationType = OPERATION_QUERY_NAME;
                break;
            case TYPE_MUTATION_NAME:
                operationType = OPERATION_MUTATION_NAME;
                break;
            default:
                throw new GraphQLErrors(UNSUPPORTED_OPERATION_TYPE);
        }

        ParameterizedTypeName requestClassName = ParameterizedTypeName.get(ClassName.get(Mono.class), ClassName.get(grpcPackageName, grpcRequestClassName));
        ParameterizedTypeName responseClassName = ParameterizedTypeName.get(ClassName.get(Mono.class), ClassName.get(grpcPackageName, grpcResponseClassName));
        return MethodSpec.methodBuilder(grpcHandlerMethodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(requestClassName, requestParameterName)
                .returns(responseClassName)
                .addStatement(
                        CodeBlock.builder()
                                .add("return $L.map($T::toJson)\n",
                                        requestParameterName,
                                        ClassName.get(ProtobufUtil.class)
                                )
                                .indent()
                                .add(".map(json -> jsonProvider.createReader(new $T(json)).readObject())\n",
                                        ClassName.get(StringReader.class)
                                )
                                .add(".map(jsonObject -> new $T().setOperationType($S).addSelection(new $T($S).addSelections(jsonObject.getOrDefault(\"selectionSet\", $T.NULL)).setArguments(jsonObject)))\n",
                                        ClassName.get(Operation.class),
                                        operationType,
                                        ClassName.get(Field.class),
                                        fieldDefinition.getName(),
                                        ClassName.get(JsonValue.class)
                                )
                                .add(".flatMap(operation -> $T.from(operationHandler.handle(operation)))\n",
                                        ClassName.get(Mono.class)
                                )
                                .add(".map(jsonValue -> ($T) $T.fromJson(jsonValue.asJsonObject().get($S).toString(), $T.newBuilder()))",
                                        ClassName.get(grpcPackageName, grpcResponseClassName),
                                        ClassName.get(ProtobufUtil.class),
                                        fieldDefinition.getName(),
                                        ClassName.get(grpcPackageName, grpcResponseClassName)
                                )
                                .unindent()
                                .build()
                )
                .build();
    }

    private JavaFile buildGraphQLServiceImplClass(String packageName) {
        TypeSpec typeSpec = buildGraphQLServiceImpl(packageName);
        return JavaFile.builder(packageName + ".grpc", typeSpec).build();
    }

    private TypeSpec buildGraphQLServiceImpl(String packageName) {
        return TypeSpec.classBuilder("ReactorGrpcGraphQLServiceImpl")
                .superclass(ClassName.get(packageName + ".grpc", "ReactorGraphQLServiceGrpc", "GraphQLServiceImplBase"))
                .addModifiers(Modifier.PUBLIC)
                .addField(
                        FieldSpec
                                .builder(
                                        ClassName.get(OperationHandler.class),
                                        "operationHandler",
                                        Modifier.PRIVATE,
                                        Modifier.FINAL
                                )
                                .build()
                )
                .addMethod(
                        MethodSpec.constructorBuilder()
                                .addModifiers(Modifier.PUBLIC)
                                .addStatement("this.operationHandler = $T.get($T.class)", ClassName.get(BeanContext.class), ClassName.get(OperationHandler.class))
                                .build()
                )
                .addMethod(buildOperationMethod(packageName))
                .build();
    }

    private MethodSpec buildOperationMethod(String packageName) {
        String grpcPackageName = packageName + ".grpc";
        String requestParameterName = "request";
        ParameterizedTypeName requestClassName = ParameterizedTypeName.get(ClassName.get(Mono.class), ClassName.get(grpcPackageName, "GraphQLRequest"));
        ParameterizedTypeName responseClassName = ParameterizedTypeName.get(ClassName.get(Mono.class), ClassName.get(grpcPackageName, "GraphQLResponse"));
        return MethodSpec.methodBuilder("request")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(requestClassName, requestParameterName)
                .returns(responseClassName)
                .addStatement(
                        CodeBlock.builder()
                                .add("return request.map($T::getRequest)\n",
                                        ClassName.get(grpcPackageName, "GraphQLRequest")
                                )
                                .indent()
                                .add(".flatMap(operation -> $T.from(operationHandler.handle($T.fromString(operation))))\n",
                                        ClassName.get(Mono.class),
                                        ClassName.get(Operation.class)
                                )
                                .add(".map(jsonValue -> $T.newBuilder().setResponse(jsonValue.toString()).build())",
                                        ClassName.get(grpcPackageName, "GraphQLResponse")
                                )
                                .unindent()
                                .build()
                )
                .build();
    }
}
