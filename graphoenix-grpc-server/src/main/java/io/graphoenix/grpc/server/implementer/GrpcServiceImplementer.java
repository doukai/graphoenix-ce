package io.graphoenix.grpc.server.implementer;

import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.grpc.server.handler.ProtobufConverter;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.handler.FetchHandler;
import io.graphoenix.spi.handler.OperationHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.json.spi.JsonProvider;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;
import static io.graphoenix.spi.utils.NameUtil.getGrpcFieldName;
import static io.graphoenix.spi.utils.NameUtil.getGrpcServiceRpcName;


@ApplicationScoped
public class GrpcServiceImplementer {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;

    @Inject
    public GrpcServiceImplementer(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig) {
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
                                Logger.info("{}.Grpc" + TYPE_QUERY_NAME + "ServiceImpl build success", packageName);
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
                                Logger.info("{}.Grpc" + TYPE_MUTATION_NAME + "ServiceImpl build success", packageName);
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
                                Logger.info("{}.GrpcGraphQLServiceImpl build success", packageName);
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
        String className = "Grpc" + operationTypeName + "ServiceImpl";
        String superClassName = operationTypeName + "ServiceGrpc";
        String serviceName = operationTypeName + "ServiceImplBase";

        return TypeSpec.classBuilder(className)
                .superclass(ClassName.get(grpcPackageName, superClassName, serviceName))
                .addModifiers(Modifier.PUBLIC)
                .addField(
                        FieldSpec
                                .builder(
                                        ClassName.get(DocumentManager.class),
                                        "documentManager",
                                        Modifier.PRIVATE,
                                        Modifier.FINAL
                                )
                                .build()
                )
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
                .addField(
                        FieldSpec
                                .builder(
                                        ClassName.get(ProtobufConverter.class),
                                        "protobufConverter",
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
                .addStatement("this.documentManager = $T.current().select($T.class).get()", ClassName.get(CDI.class), ClassName.get(DocumentManager.class))
                .addStatement("this.operationHandler = $T.current().select($T.class).get()", ClassName.get(CDI.class), ClassName.get(OperationHandler.class))
                .addStatement("this.jsonProvider = $T.current().select($T.class).get()", ClassName.get(CDI.class), ClassName.get(JsonProvider.class))
                .addStatement("this.protobufConverter = $T.current().select($T.class).get()", ClassName.get(CDI.class), ClassName.get(ProtobufConverter.class))
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
        String responseObserverParameterName = "responseObserver";
        String grpcHandlerMethodName = getGrpcFieldName(fieldDefinition.getName());
        String grpcRequestClassName = operationTypeName + getGrpcServiceRpcName(fieldDefinition.getName()) + "Request";
        String grpcResponseClassName = operationTypeName + getGrpcServiceRpcName(fieldDefinition.getName()) + "Response";
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);

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

        ClassName requestClassName = ClassName.get(grpcPackageName, grpcRequestClassName);
        ParameterizedTypeName responseClassName = ParameterizedTypeName.get(ClassName.get("io.grpc.stub", "StreamObserver"), ClassName.get(grpcPackageName, grpcResponseClassName));
        return MethodSpec.methodBuilder(grpcHandlerMethodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(requestClassName, requestParameterName)
                .addParameter(responseClassName, responseObserverParameterName)
                .addStatement("$T fieldDefinition = documentManager.getDocument().getObjectTypeOrError($S).getField($S)",
                        ClassName.get(FieldDefinition.class),
                        operationTypeName,
                        fieldDefinition.getName()
                )
                .addStatement(
                        CodeBlock.builder()
                                .add(
                                        fieldTypeDefinition.isObject() ?
                                                CodeBlock.of(
                                                        "$T.from(operationHandler.handle(new $T().setOperationType($S).addSelection(new $T($S).setArguments(protobufConverter.toJsonValue($L, fieldDefinition).asJsonObject()).setSelections($T.ofNullable($L.getSelectionSet()).orElse($S)))))\n",
                                                        ClassName.get(Mono.class),
                                                        ClassName.get(Operation.class),
                                                        operationType,
                                                        ClassName.get(Field.class),
                                                        fieldDefinition.getName(),
                                                        requestParameterName,
                                                        ClassName.get(Optional.class),
                                                        requestParameterName,
                                                        fieldDefinition.isConnectionField() ?
                                                                documentManager
                                                                        .getFieldTypeDefinition(
                                                                                documentManager
                                                                                        .getFieldTypeDefinition(
                                                                                                fieldTypeDefinition.asObject().getFieldOrError(FIELD_EDGES_NAME)
                                                                                        )
                                                                                        .asObject().getFieldOrError(FIELD_NODE_NAME)
                                                                        )
                                                                        .asObject().getFields().stream()
                                                                        .filter(subFieldDefinition -> !subFieldDefinition.isFunctionField())
                                                                        .filter(subFieldDefinition -> documentManager.getFieldTypeDefinition(subFieldDefinition).isLeaf())
                                                                        .map(AbstractDefinition::getName)
                                                                        .collect(Collectors.joining(" ", "{totalCount edges {node {", "}}}")) :
                                                                fieldTypeDefinition.asObject().getFields().stream()
                                                                        .filter(subFieldDefinition -> !subFieldDefinition.isFunctionField())
                                                                        .filter(subFieldDefinition -> documentManager.getFieldTypeDefinition(subFieldDefinition).isLeaf())
                                                                        .map(AbstractDefinition::getName)
                                                                        .collect(Collectors.joining(" ", "{", "}"))
                                                ) :
                                                CodeBlock.of(
                                                        "$T.from(operationHandler.handle(new $T().setOperationType($S).addSelection(new $T($S).setArguments(protobufConverter.toJsonValue($L, fieldDefinition).asJsonObject()))))\n",
                                                        ClassName.get(Mono.class),
                                                        ClassName.get(Operation.class),
                                                        operationType,
                                                        ClassName.get(Field.class),
                                                        fieldDefinition.getName(),
                                                        requestParameterName
                                                )
                                )
                                .indent()
                                .add(".map(jsonValue -> ($T) protobufConverter.fromJsonValue(jsonValue.asJsonObject().get($S), $T.newBuilder(), fieldDefinition))\n",
                                        ClassName.get(grpcPackageName, grpcResponseClassName),
                                        fieldDefinition.getName(),
                                        ClassName.get(grpcPackageName, grpcResponseClassName)
                                )
                                .add(".subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted)")
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
        return TypeSpec.classBuilder("GrpcGraphQLServiceImpl")
                .superclass(ClassName.get(packageName + ".grpc", "GraphQLServiceGrpc", "GraphQLServiceImplBase"))
                .addModifiers(Modifier.PUBLIC)
                .addField(
                        FieldSpec
                                .builder(
                                        ClassName.get(FetchHandler.class),
                                        "fetchHandler",
                                        Modifier.PRIVATE,
                                        Modifier.FINAL
                                )
                                .build()
                )
                .addMethod(
                        MethodSpec.constructorBuilder()
                                .addModifiers(Modifier.PUBLIC)
                                .addStatement("this.fetchHandler = $T.current().select($T.class).get()", ClassName.get(CDI.class), ClassName.get(FetchHandler.class))
                                .build()
                )
                .addMethod(buildOperationMethod(packageName))
                .build();
    }

    private MethodSpec buildOperationMethod(String packageName) {
        String grpcPackageName = packageName + ".grpc";
        String requestParameterName = "request";
        String responseObserverParameterName = "responseObserver";
        ClassName requestClassName = ClassName.get(grpcPackageName, "GraphQLRequest");
        ParameterizedTypeName responseClassName = ParameterizedTypeName.get(ClassName.get("io.grpc.stub", "StreamObserver"), ClassName.get(grpcPackageName, "GraphQLResponse"));

        return MethodSpec.methodBuilder("request")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(requestClassName, requestParameterName)
                .addParameter(responseClassName, responseObserverParameterName)
                .addStatement(
                        CodeBlock.builder()
                                .add("fetchHandler.request($T.fromString($L.getRequest()))\n",
                                        ClassName.get(Operation.class),
                                        requestParameterName
                                )
                                .indent()
                                .add(".map(jsonValue -> $T.newBuilder().setResponse(jsonValue.toString()).build())\n",
                                        ClassName.get(grpcPackageName, "GraphQLResponse")
                                )
                                .add(".subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted)")
                                .unindent()
                                .build()
                )
                .build();
    }
}
