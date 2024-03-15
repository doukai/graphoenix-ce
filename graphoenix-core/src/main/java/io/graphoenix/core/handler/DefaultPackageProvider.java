package io.graphoenix.core.handler;

import com.google.common.collect.Iterators;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.dto.PackageURL;
import io.graphoenix.spi.handler.PackageProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.PackageManager.LOAD_BALANCE_ROUND_ROBIN;
import static io.graphoenix.core.handler.PackageManager.SEEDS_MEMBER_KEY;

@ApplicationScoped
@Default
public class DefaultPackageProvider implements PackageProvider {

    private final Map<String, Map<String, List<PackageURL>>> packageProtocolURLListMap;

    private final Map<String, Map<String, Iterator<PackageURL>>> packageProtocolURLIteratorMap;

    @SuppressWarnings("unchecked")
    @Inject
    public DefaultPackageProvider(PackageConfig packageConfig) {
        List<Map.Entry<String, PackageURL>> packageURLList = Stream.ofNullable(packageConfig.getMembers())
                .flatMap(packageMembers -> packageMembers.entrySet().stream())
                .filter(packageEntry -> !packageEntry.getKey().equals(SEEDS_MEMBER_KEY))
                .flatMap(packageEntry ->
                        ((List<Map<String, Object>>) packageEntry.getValue()).stream()
                                .map(PackageURL::new)
                                .map(packageURL ->
                                        new AbstractMap.SimpleEntry<>(
                                                packageEntry.getKey(),
                                                packageURL
                                        )
                                )
                )
                .collect(Collectors.toList());

        this.packageProtocolURLListMap = packageURLList.stream()
                .collect(
                        Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.mapping(
                                        Map.Entry::getValue,
                                        Collectors.groupingBy(
                                                PackageURL::getProtocol,
                                                Collectors.toList()
                                        )
                                )
                        )
                );

        if (packageConfig.getPackageLoadBalance().equals(LOAD_BALANCE_ROUND_ROBIN)) {
            this.packageProtocolURLIteratorMap = packageURLList.stream()
                    .collect(
                            Collectors.groupingBy(
                                    Map.Entry::getKey,
                                    Collectors.mapping(
                                            Map.Entry::getValue,
                                            Collectors.groupingBy(
                                                    PackageURL::getProtocol,
                                                    Collectors.collectingAndThen(Collectors.toList(), Iterators::cycle)
                                            )
                                    )
                            )
                    );
        } else {
            this.packageProtocolURLIteratorMap = null;
        }
    }

    @Override
    public List<PackageURL> getProtocolURLList(String packageName, String protocol) {
        return packageProtocolURLListMap.get(packageName).get(protocol);
    }

    @Override
    public Iterator<PackageURL> getProtocolURLIterator(String packageName, String protocol) {
        return packageProtocolURLIteratorMap.get(packageName).get(protocol);
    }
}
