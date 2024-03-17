package io.graphoenix.core.handler;

import com.google.common.reflect.ClassPath;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.annotation.Package;
import io.graphoenix.spi.dto.PackageURL;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.handler.PackageProvider;
import io.nozdormu.spi.context.BeanContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class PackageManager {

    public static final String LOAD_BALANCE_ROUND_ROBIN = "roundRobin";
    public static final String LOAD_BALANCE_RANDOM = "random";
    public static final String SEEDS_MEMBER_KEY = "seeds";
    public static final String PACKAGE_PROVIDER_GOSSIP_NAME = "gossip";

    private final PackageConfig packageConfig;
    private final PackageProvider packageProvider;
    private final Set<String> seedMembers;
    private final Random random = new Random();

    @Inject
    public PackageManager(PackageConfig packageConfig, PackageProvider packageProvider) {
        this.packageConfig = packageConfig;
        //noinspection unchecked
        this.seedMembers = Stream.ofNullable(packageConfig.getMembers())
                .flatMap(packageMap -> Stream.ofNullable(packageMap.get(SEEDS_MEMBER_KEY)))
                .map(seedList -> (List<Map<String, Object>>) seedList)
                .flatMap(Collection::stream)
                .map(PackageURL::new)
                .map(packageURL -> packageURL.getHost() + ":" + packageURL.getPort())
                .collect(Collectors.toSet());

        if (!getSeedMembers().isEmpty()) {
            this.packageProvider = BeanContext.getName(PackageProvider.class, PACKAGE_PROVIDER_GOSSIP_NAME);
        } else {
            this.packageProvider = BeanContext.getDefault(PackageProvider.class);
        }
    }

    public PackageURL getURL(String packageName, String protocol) {
        switch (packageConfig.getPackageLoadBalance()) {
            case LOAD_BALANCE_ROUND_ROBIN:
                return packageProvider.getProtocolURLIterator(packageName, protocol).next();
            case LOAD_BALANCE_RANDOM:
                List<PackageURL> urlList = packageProvider.getProtocolURLList(packageName, protocol);
                if (urlList.size() == 1) {
                    return urlList.get(0);
                } else {
                    return urlList.get(random.nextInt(urlList.size()));
                }
            default:
                return packageProvider.getProtocolURLList(packageName, protocol).get(0);
        }
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
                Stream.ofNullable(packageConfig.getLocalPackageNames())
                        .flatMap(Collection::stream)
        );
    }

    public Set<String> getSeedMembers() {
        return seedMembers;
    }
}
