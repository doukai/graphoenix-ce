package io.graphoenix.core.handler;

import com.google.common.reflect.ClassPath;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.annotation.Package;
import io.graphoenix.spi.graphql.Definition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public class PackageManager {

    private final PackageConfig packageConfig;

    @Inject
    public PackageManager(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
        getDefaultPackageName().ifPresent(packageConfig::setPackageName);
    }

    public Optional<String> getDefaultPackageName() {
        try {
            ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
            return classPath.getTopLevelClasses()
                    .stream()
                    .filter(classInfo -> classInfo.getSimpleName().equals("package-info"))
                    .filter(classInfo -> classInfo.load().getPackage().isAnnotationPresent(Package.class))
                    .findFirst()
                    .map(ClassPath.ClassInfo::getPackageName);
        } catch (IOException e) {
            Logger.error(e);
        }
        return Optional.empty();
    }

    public boolean isOwnPackage(Definition abstractDefinition) {
        return abstractDefinition.getPackageName().map(this::isOwnPackage).orElse(true);
    }

    public boolean isOwnPackage(String packageName) {
        return packageConfig.getPackageName().equals(packageName);
    }

    public boolean isNotOwnPackage(Definition abstractDefinition) {
        return !isOwnPackage(abstractDefinition);
    }

    public boolean isLocalPackage(Definition abstractDefinition) {
        return abstractDefinition.getPackageName().map(this::isLocalPackage).orElse(true);
    }

    public boolean isLocalPackage(String packageName) {
        return getLocalPackages().anyMatch(localPackageName -> localPackageName.equals(packageName));
    }

    public boolean isNotLocalPackage(Definition abstractDefinition) {
        return !isLocalPackage(abstractDefinition);
    }

    public Stream<String> getLocalPackages() {
        return Stream.concat(
                Stream.ofNullable(packageConfig.getPackageName()),
                Stream.ofNullable(packageConfig.getLocalPackageNames()).flatMap(Collection::stream)
        );
    }
}
