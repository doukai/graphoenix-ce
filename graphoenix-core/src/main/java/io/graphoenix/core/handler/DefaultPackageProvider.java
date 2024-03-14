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

@ApplicationScoped
@Default
public class DefaultPackageProvider implements PackageProvider {

    private final Map<String, Map<String, List<PackageURL>>> packageSchemaURLListMap;

    private final Map<String, Map<String, Iterator<PackageURL>>> packageSchemaURLIteratorMap;

    @SuppressWarnings("unchecked")
    @Inject
    public DefaultPackageProvider(PackageConfig packageConfig) {
        this.packageSchemaURLListMap = Stream.ofNullable(packageConfig.getMembers())
                .flatMap(packageMembers -> packageMembers.entrySet().stream())
                .flatMap(packageEntry ->
                        ((List<Map<String, Object>>) packageEntry.getValue()).stream()
                                .map(PackageURL::new)
                                .map(url -> new AbstractMap.SimpleEntry<>(packageEntry.getKey(), new AbstractMap.SimpleEntry<>(url.getSchema(), url)))
                )
                .collect(Collectors.groupingBy(
                        Map.Entry<String, AbstractMap.SimpleEntry<String, PackageURL>>::getKey,
                        Collectors
                                .mapping(
                                        Map.Entry<String, AbstractMap.SimpleEntry<String, PackageURL>>::getValue,
                                        Collectors
                                                .groupingBy(
                                                        Map.Entry<String, PackageURL>::getKey,
                                                        Collectors
                                                                .mapping(
                                                                        Map.Entry<String, PackageURL>::getValue,
                                                                        Collectors.toList()
                                                                )
                                                )
                                )
                        )
                );
        if (packageConfig.getPackageLoadBalance().equals(LOAD_BALANCE_ROUND_ROBIN)) {
            this.packageSchemaURLIteratorMap = Stream.ofNullable(packageConfig.getMembers())
                    .flatMap(packageMembers -> packageMembers.entrySet().stream())
                    .flatMap(packageEntry ->
                            ((List<Map<String, Object>>) packageEntry.getValue()).stream()
                                    .map(PackageURL::new)
                                    .map(url -> new AbstractMap.SimpleEntry<>(url.getSchema(), url))
                                    .collect(
                                            Collectors
                                                    .groupingBy(
                                                            Map.Entry<String, PackageURL>::getKey,
                                                            Collectors
                                                                    .mapping(
                                                                            Map.Entry<String, PackageURL>::getValue,
                                                                            Collectors.toList()
                                                                    )
                                                    )
                                    )
                                    .entrySet()
                                    .stream()
                                    .map(protocolEntry -> new AbstractMap.SimpleEntry<>(packageEntry.getKey(), new AbstractMap.SimpleEntry<>(protocolEntry.getKey(), Iterators.cycle(protocolEntry.getValue()))))
                    )
                    .collect(
                            Collectors
                                    .groupingBy(
                                            Map.Entry<String, AbstractMap.SimpleEntry<String, Iterator<PackageURL>>>::getKey,
                                            Collectors
                                                    .mapping(
                                                            Map.Entry<String, AbstractMap.SimpleEntry<String, Iterator<PackageURL>>>::getValue,
                                                            Collectors
                                                                    .toMap(
                                                                            Map.Entry<String, Iterator<PackageURL>>::getKey,
                                                                            Map.Entry<String, Iterator<PackageURL>>::getValue
                                                                    )
                                                    )
                                    )
                    );
        } else {
            this.packageSchemaURLIteratorMap = null;
        }
    }

    @Override
    public List<PackageURL> getProtocolURLList(String packageName, String schema) {
        return packageSchemaURLListMap.get(packageName).get(schema);
    }

    @Override
    public Iterator<PackageURL> getProtocolURLIterator(String packageName, String schema) {
        return packageSchemaURLIteratorMap.get(packageName).get(schema);
    }
}
