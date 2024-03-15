package io.graphoenix.gossip.handler;

import com.google.common.collect.Iterators;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.dto.PackageURL;
import io.graphoenix.spi.handler.PackageProvider;
import io.scalecube.cluster.Member;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.PackageManager.LOAD_BALANCE_ROUND_ROBIN;
import static io.graphoenix.core.handler.PackageManager.PACKAGE_PROVIDER_GOSSIP_NAME;
import static io.graphoenix.gossip.handler.GossipPackageCluster.PACKAGE_NAME;
import static io.graphoenix.gossip.handler.GossipPackageCluster.SERVICES_NAME;

@ApplicationScoped
@Named(PACKAGE_PROVIDER_GOSSIP_NAME)
public class GossipPackageProvider implements PackageProvider {

    private final PackageConfig packageConfig;

    private final Map<String, Map<String, List<PackageURL>>> packageProtocolURLListMap = new ConcurrentHashMap<>();

    private final Map<String, Map<String, Iterator<PackageURL>>> packageProtocolURLIteratorMap = new ConcurrentHashMap<>();

    @Inject
    public GossipPackageProvider(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
    }

    @Override
    public List<PackageURL> getProtocolURLList(String packageName, String protocol) {
        return packageProtocolURLListMap.get(packageName).get(protocol);
    }

    @Override
    public Iterator<PackageURL> getProtocolURLIterator(String packageName, String protocol) {
        return packageProtocolURLIteratorMap.get(packageName).get(protocol);
    }

    @SuppressWarnings("unchecked")
    public void mergeMemberURLs(Member member, List<Map<String, Object>> metadata) {
        mergeURLs(
                metadata.stream()
                        .flatMap(packageMap ->
                                Stream.concat(
                                                Stream.ofNullable(packageProtocolURLListMap.get((String) packageMap.get(PACKAGE_NAME)))
                                                        .flatMap(portocolMap -> portocolMap.values().stream())
                                                        .flatMap(Collection::stream)
                                                        .filter(packageURL ->
                                                                !packageURL.getHost().equals(member.address().host()) &&
                                                                        packageURL.getPort() != member.address().port()
                                                        ),
                                                ((List<Map<String, Object>>) packageMap.get(SERVICES_NAME)).stream()
                                                        .map(PackageURL::new)
                                                        .peek(packageURL -> {
                                                                    if (packageURL.getHost() == null) {
                                                                        packageURL.setHost(member.address().host());
                                                                    }
                                                                }
                                                        )
                                        )
                                        .map(packageURL ->
                                                new AbstractMap.SimpleEntry<>(
                                                        (String) packageMap.get(PACKAGE_NAME),
                                                        packageURL
                                                )
                                        )
                        )
                        .collect(Collectors.toList())
        );
    }

    public void removeMemberURLs(Member member) {
        mergeURLs(
                packageProtocolURLListMap.entrySet().stream()
                        .flatMap(entry ->
                                entry.getValue().values().stream()
                                        .flatMap(Collection::stream)
                                        .filter(packageURL ->
                                                !packageURL.getHost().equals(member.address().host()) &&
                                                        packageURL.getPort() != member.address().port()
                                        )
                                        .map(packageURL ->
                                                new AbstractMap.SimpleEntry<>(
                                                        entry.getKey(),
                                                        packageURL
                                                )
                                        )
                        )
                        .collect(Collectors.toList())
        );
    }

    private void mergeURLs(List<Map.Entry<String, PackageURL>> packageURLList) {
        packageProtocolURLListMap
                .putAll(
                        packageURLList.stream()
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
                                )
                );

        if (packageConfig.getPackageLoadBalance().equals(LOAD_BALANCE_ROUND_ROBIN)) {
            packageProtocolURLIteratorMap
                    .putAll(
                            packageURLList.stream()
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
                                    )
                    );
        }
    }
}
