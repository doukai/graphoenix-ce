package io.graphoenix.grpc.server.implementer;

import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.graphoenix.spi.constant.Hammurabi.TYPE_MUTATION_NAME;
import static io.graphoenix.spi.constant.Hammurabi.TYPE_QUERY_NAME;
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
        String reactorClassName = "ReactorGrpc" + operationTypeName + "ServiceImpl";
        String superClassName = operationTypeName + "ServiceGrpc";
        String reactorSuperClassName = "Reactor" + operationTypeName + "ServiceGrpc";
        String serviceName = operationTypeName + "ServiceImplBase";

        return TypeSpec.classBuilder(className)
                .superclass(ClassName.get(grpcPackageName, superClassName, serviceName))
                .addModifiers(Modifier.PUBLIC)
                .addField(
                        FieldSpec
                                .builder(
                                        ClassName.get(grpcPackageName, reactorSuperClassName, serviceName),
                                        "reactorService",
                                        Modifier.PRIVATE,
                                        Modifier.FINAL
                                )
                                .initializer("new $T()", ClassName.get(grpcPackageName, reactorClassName))
                                .build()
                )
                .addMethods(buildTypeMethods(packageName, operationTypeName, fieldDefinitionList))
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

        ClassName requestClassName = ClassName.get(grpcPackageName, grpcRequestClassName);
        ParameterizedTypeName responseClassName = ParameterizedTypeName.get(ClassName.get("io.grpc.stub", "StreamObserver"), ClassName.get(grpcPackageName, grpcResponseClassName));
        return MethodSpec.methodBuilder(grpcHandlerMethodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(requestClassName, requestParameterName)
                .addParameter(responseClassName, responseObserverParameterName)
                .addStatement("reactorService.$L($T.just($L)).subscribe($L::onNext, $L::onError, $L::onCompleted)", grpcHandlerMethodName, ClassName.get(Mono.class), requestParameterName, responseObserverParameterName, responseObserverParameterName, responseObserverParameterName)
                .build();
    }

    private JavaFile buildGraphQLServiceImplClass(String packageName) {
        TypeSpec typeSpec = buildGraphQLServiceImpl(packageName);
        return JavaFile.builder(packageName + ".grpc", typeSpec).build();
    }

    private TypeSpec buildGraphQLServiceImpl(String packageName) {
        String grpcPackageName = packageName + ".grpc";
        return TypeSpec.classBuilder("GrpcGraphQLServiceImpl")
                .superclass(ClassName.get(grpcPackageName, "GraphQLServiceGrpc", "GraphQLServiceImplBase"))
                .addModifiers(Modifier.PUBLIC)
                .addField(
                        FieldSpec
                                .builder(
                                        ClassName.get(grpcPackageName, "ReactorGraphQLServiceGrpc", "GraphQLServiceImplBase"),
                                        "reactorService",
                                        Modifier.PRIVATE,
                                        Modifier.FINAL
                                )
                                .initializer("new $T()", ClassName.get(grpcPackageName, "ReactorGrpcGraphQLServiceImpl"))
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
                .addStatement("reactorService.$L($T.just($L)).subscribe($L::onNext, $L::onError, $L::onCompleted)", "request", ClassName.get(Mono.class), requestParameterName, responseObserverParameterName, responseObserverParameterName, responseObserverParameterName)
                .build();
    }
}
