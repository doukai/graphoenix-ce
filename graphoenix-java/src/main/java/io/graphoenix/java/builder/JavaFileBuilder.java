package io.graphoenix.java.builder;

import com.google.common.collect.Streams;
import com.squareup.javapoet.JavaFile;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class JavaFileBuilder {

    private static final Logger logger = LoggerFactory.getLogger(JavaFileBuilder.class.getName());

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final TypeSpecBuilder typeSpecBuilder;
    private final PackageConfig packageConfig;

    @Inject
    public JavaFileBuilder(DocumentManager documentManager, PackageManager packageManager, TypeSpecBuilder typeSpecBuilder, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.typeSpecBuilder = typeSpecBuilder;
        this.packageConfig = packageConfig;
    }

    public void writeToPath(File path) {
        for (JavaFile javaFile : buildJavaFileList().collect(Collectors.toList())) {
            try {
                javaFile.writeTo(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("all graphql entity generated");
    }

    public Stream<JavaFile> buildJavaFileList() {
        return Streams.concat(
                documentManager.getDocument().getDirectives()
                        .filter(packageManager::isOwnPackage)
                        .map(typeSpecBuilder::buildType)
                        .map(typeSpec -> JavaFile.builder(packageConfig.getDirectivePackageName(), typeSpec).build()),
                documentManager.getDocument().getEnums()
                        .filter(packageManager::isOwnPackage)
                        .filter(enumType -> !enumType.classExists())
                        .map(typeSpecBuilder::buildType)
                        .map(typeSpec -> JavaFile.builder(packageConfig.getEnumTypePackageName(), typeSpec).build()),
                documentManager.getDocument().getInterfaceTypes()
                        .filter(packageManager::isOwnPackage)
                        .filter(interfaceType -> !interfaceType.classExists())
                        .map(typeSpecBuilder::buildType)
                        .map(typeSpec -> JavaFile.builder(packageConfig.getInterfaceTypePackageName(), typeSpec).build()),
                documentManager.getDocument().getInputObjectTypes()
                        .filter(packageManager::isOwnPackage)
                        .filter(inputObjectType -> !inputObjectType.classExists())
                        .map(typeSpecBuilder::buildType)
                        .map(typeSpec -> JavaFile.builder(packageConfig.getInputObjectTypePackageName(), typeSpec).build()),
                documentManager.getDocument().getInputObjectTypes()
                        .filter(packageManager::isOwnPackage)
                        .filter(inputObjectType -> inputObjectType.getAnnotationName().isPresent())
                        .flatMap(typeSpecBuilder::buildAnnotations)
                        .map(typeSpec -> JavaFile.builder(packageConfig.getAnnotationPackageName(), typeSpec).build()),
                typeSpecBuilder.buildOperationTypeAnnotations()
                        .map(typeSpec -> JavaFile.builder(packageConfig.getAnnotationPackageName(), typeSpec).build()),
                documentManager.getDocument().getObjectTypes()
                        .filter(objectType -> !documentManager.isOperationType(objectType))
                        .filter(packageManager::isOwnPackage)
                        .filter(objectType -> !objectType.classExists())
                        .map(typeSpecBuilder::buildType)
                        .map(typeSpec -> JavaFile.builder(packageConfig.getObjectTypePackageName(), typeSpec).build()),
                documentManager.getDocument().getObjectTypes()
                        .filter(documentManager::isOperationType)
                        .map(typeSpecBuilder::buildType)
                        .map(typeSpec -> JavaFile.builder(packageConfig.getObjectTypePackageName(), typeSpec).build())
        );
    }
}
