package io.graphoenix.grpc.client.implementer;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.*;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.grpc.client.resolver.PackageNameResolverProvider;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.FetchHandler;
import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolverRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.stream.Collectors;

import static io.graphoenix.spi.utils.NameUtil.packageNameToUnderline;

@ApplicationScoped
public class GrpcFetchHandlerBuilder {

    private final PackageConfig packageConfig;
    private final Set<String> packageNameSet;

    @Inject
    public GrpcFetchHandlerBuilder(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
        this.packageNameSet = documentManager.getDocument().getObjectTypes()
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .filter(packageManager::isNotLocalPackage)
                .map(AbstractDefinition::getPackageNameOrError)
                .collect(Collectors.toSet());
    }

    public void writeToFiler(Filer filer) throws IOException {
        this.buildClass().writeTo(filer);
        Logger.info("GrpcFetchHandler build success");
    }

    private JavaFile buildClass() {
        return JavaFile.builder(packageConfig.getGrpcHandlerPackageName(), buildGrpcFetchHandler()).build();
    }

    private TypeSpec buildGrpcFetchHandler() {
        TypeSpec.Builder builder = TypeSpec.classBuilder("GrpcFetchHandler")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(FetchHandler.class))
                .addAnnotation(ApplicationScoped.class)
                .addAnnotation(AnnotationSpec.builder(Named.class).addMember("value", "$S", "grpc").build())
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
                .addMethod(buildOperationMethod());

        packageNameSet.forEach(packageName ->
                builder.addField(
                        FieldSpec
                                .builder(
                                        ClassName.get(packageName + ".grpc", "ReactorGraphQLServiceGrpc", "ReactorGraphQLServiceStub"),
                                        packageNameToUnderline(packageName) + "_GraphQLServiceStub",
                                        Modifier.PRIVATE,
                                        Modifier.FINAL
                                )
                                .build()
                )
        );
        return builder.build();
    }

    private MethodSpec buildConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Inject.class)
                .addParameter(ClassName.get(JsonProvider.class), "jsonProvider")
                .addParameter(ClassName.get(PackageNameResolverProvider.class), "packageNameResolverProvider")
                .addStatement("this.jsonProvider = jsonProvider")
                .addStatement("$T.getDefaultRegistry().register(packageNameResolverProvider)", ClassName.get(NameResolverRegistry.class));

        packageNameSet.forEach(packageName ->
                builder.addStatement("this.$L = $T.newReactorStub($T.forTarget($S).defaultLoadBalancingPolicy($S).usePlaintext().build())",
                        packageNameToUnderline(packageName) + "_GraphQLServiceStub",
                        ClassName.get(packageName + ".grpc", "ReactorGraphQLServiceGrpc"),
                        ClassName.get(ManagedChannelBuilder.class),
                        "package:///" + packageName,
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, packageConfig.getPackageLoadBalance())
                )
        );
        return builder.build();
    }

    private MethodSpec buildOperationMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("request")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ParameterizedTypeName.get(ClassName.get(Mono.class), ClassName.get(JsonValue.class)))
                .addParameter(ParameterSpec.builder(ClassName.get(String.class), "packageName").build())
                .addParameter(ParameterSpec.builder(ClassName.get(Operation.class), "operation").build())
                .beginControlFlow("switch (packageName)");

        packageNameSet.forEach(packageName ->
                builder
                        .addStatement(
                                CodeBlock.builder()
                                        .add("case $S:\n", packageName)
                                        .indent()
                                        .add("return this.$L.request($T.newBuilder().setRequest(operation.toString()).build()).map(response -> jsonProvider.createReader(new $T(response.getResponse())).readValue())",
                                                packageNameToUnderline(packageName) + "_GraphQLServiceStub",
                                                ClassName.get(packageName + ".grpc", "GraphQLRequest"),
                                                ClassName.get(StringReader.class)
                                        )
                                        .unindent()
                                        .build()
                        )
        );

        return builder
                .addStatement(
                        CodeBlock.builder().add("default :\n")
                                .indent()
                                .add("return $T.empty()", ClassName.get(Mono.class))
                                .unindent()
                                .build()
                )
                .endControlFlow()
                .build();
    }
}
