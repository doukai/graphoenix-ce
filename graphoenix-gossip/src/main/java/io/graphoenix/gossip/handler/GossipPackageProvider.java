package io.graphoenix.gossip.handler;

import com.google.common.collect.Iterators;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.dto.PackageURL;
import io.graphoenix.spi.handler.PackageProvider;
import io.scalecube.cluster.Member;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.PackageManager.LOAD_BALANCE_ROUND_ROBIN;

@ApplicationScoped
@Named("gossip")
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

    public void mergeMemberURLs(Member member, List<Map<String, Object>> urls) {
        mergeURLs(
                Stream
                        .concat(
                                packageProtocolURLListMap.values().stream()
                                        .flatMap(protocolMap -> protocolMap.values().stream())
                                        .flatMap(Collection::stream)
                                        .filter(packageURL ->
                                                !packageURL.getHost().equals(member.address().host()) &&
                                                        packageURL.getPort() != member.address().port()
                                        ),
                                urls.stream()
                                        .map(PackageURL::new)
                                        .peek(packageURL -> {
                                                    if (packageURL.getHost() == null) {
                                                        packageURL.setHost(member.address().host());
                                                    }
                                                }
                                        )
                        )
                        .collect(Collectors.toList())
        );
    }

    public void removeMemberURLs(Member member) {
        mergeURLs(
                packageProtocolURLListMap.values().stream()
                        .flatMap(protocolMap -> protocolMap.values().stream())
                        .flatMap(Collection::stream)
                        .filter(packageURL ->
                                !packageURL.getHost().equals(member.address().host()) &&
                                        packageURL.getPort() != member.address().port()
                        )
                        .collect(Collectors.toList())
        );
    }

    private void mergeURLs(List<PackageURL> packageURLList) {
        packageProtocolURLListMap.clear();
        packageProtocolURLListMap
                .putAll(
                        packageURLList.stream()
                                .collect(
                                        Collectors.groupingBy(
                                                PackageURL::getPackageName,
                                                Collectors.mapping(
                                                        packageURL -> packageURL,
                                                        Collectors.groupingBy(
                                                                PackageURL::getProtocol,
                                                                Collectors.toList()
                                                        )
                                                )
                                        )
                                )
                );

        if (packageConfig.getPackageLoadBalance().equals(LOAD_BALANCE_ROUND_ROBIN)) {
            packageProtocolURLIteratorMap.clear();
            packageProtocolURLIteratorMap
                    .putAll(
                            packageURLList.stream()
                                    .collect(
                                            Collectors.groupingBy(
                                                    PackageURL::getPackageName,
                                                    Collectors.mapping(
                                                            packageURL -> packageURL,
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