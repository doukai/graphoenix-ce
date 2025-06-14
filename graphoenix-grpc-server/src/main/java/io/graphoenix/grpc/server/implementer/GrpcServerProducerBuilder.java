package io.graphoenix.grpc.server.implementer;

import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.tinylog.Logger;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.graphoenix.spi.constant.Hammurabi.TYPE_MUTATION_NAME;
import static io.graphoenix.spi.constant.Hammurabi.TYPE_QUERY_NAME;

@ApplicationScoped
public class GrpcServerProducerBuilder {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;

    @Inject
    public GrpcServerProducerBuilder(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
    }

    public void writeToFiler(Filer filer) throws IOException {
        try {
            List<String> packageNameList = documentManager.getDocument().getObjectTypes()
                    .filter(objectType -> !documentManager.isOperationType(objectType))
                    .filter(packageManager::isLocalPackage)
                    .map(AbstractDefinition::getPackageNameOrError)
                    .distinct()
                    .collect(Collectors.toList());
            this.buildGrpcServerProducerClass(packageConfig.getGrpcPackageName(), packageNameList).writeTo(filer);
            Logger.info("{}.GrpcServerProducer build success", packageConfig.getGrpcPackageName());
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    private JavaFile buildGrpcServerProducerClass(String packageName, List<String> packageNameList) {
        TypeSpec typeSpec = buildGrpcServerBuilder(packageNameList);
        return JavaFile.builder(packageName, typeSpec).build();
    }

    private TypeSpec buildGrpcServerBuilder(List<String> packageNameList) {
        TypeSpec.Builder builder = TypeSpec.classBuilder("GrpcServerProducer")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ApplicationScoped.class)
                .addField(
                        FieldSpec
                                .builder(
                                        ClassName.get("io.graphoenix.grpc.server.config", "GrpcServerConfig"),
                                        "grpcServerConfig",
                                        Modifier.PRIVATE,
                                        Modifier.FINAL
                                )
                                .build()
                )
                .addMethod(
                        MethodSpec.constructorBuilder()
                                .addModifiers(Modifier.PUBLIC)
                                .addAnnotation(Inject.class)
                                .addParameter(ClassName.get("io.graphoenix.grpc.server.config", "GrpcServerConfig"), "grpcServerConfig")
                                .addStatement("this.grpcServerConfig = grpcServerConfig")
                                .build()
                );

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("server")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ApplicationScoped.class)
                .addAnnotation(Produces.class)
                .returns(Server.class)
                .addStatement("ServerBuilder<?> serverBuilder = $T.forPort(grpcServerConfig.getPort())", ClassName.get(ServerBuilder.class));

        packageNameList
                .forEach(packageName -> {
                            if (documentManager.getDocument().getObjectTypes()
                                    .anyMatch(objectType ->
                                            !documentManager.isOperationType(objectType) &&
                                                    !objectType.isContainer() &&
                                                    objectType.getPackageName().orElseGet(packageConfig::getPackageName).equals(packageName))
                            ) {
                                methodBuilder
                                        .addStatement(
                                                "serverBuilder.addService(new $T()).addService(new $T()).addService(new $T())",
                                                ClassName.get(packageName + ".grpc", "Grpc" + TYPE_QUERY_NAME + "ServiceImpl"),
                                                ClassName.get(packageName + ".grpc", "Grpc" + TYPE_MUTATION_NAME + "ServiceImpl"),
                                                ClassName.get(packageName + ".grpc", "GrpcGraphQLServiceImpl")
                                        );
                            } else {
                                methodBuilder
                                        .addStatement(
                                                "serverBuilder.addService(new $T())",
                                                ClassName.get(packageName + ".grpc", "GrpcGraphQLServiceImpl")
                                        );
                            }
                        }
                );
        methodBuilder.addStatement("return serverBuilder.build()");
        return builder.addMethod(methodBuilder.build()).build();
    }
}
