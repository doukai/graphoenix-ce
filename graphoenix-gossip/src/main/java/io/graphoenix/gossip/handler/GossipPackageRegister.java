package io.graphoenix.gossip.handler;

import com.google.common.collect.Iterators;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.dto.PackageURL;
import io.graphoenix.spi.handler.PackageProvider;
import io.scalecube.net.Address;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.tinylog.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.PackageManager.LOAD_BALANCE_ROUND_ROBIN;

@ApplicationScoped
@Named("gossip")
public class GossipPackageRegister implements PackageProvider {

    private final PackageConfig packageConfig;

    private final Map<String, Set<PackageURL>> packageURLs = new ConcurrentHashMap<>();

    private final Map<String, Set<PackageURL>> memberAddressURLs = new ConcurrentHashMap<>();

    private final Map<String, Map<String, List<PackageURL>>> packageSchemaURLListMap = new ConcurrentHashMap<>();

    private final Map<String, Map<String, Iterator<PackageURL>>> packageSchemaURLIteratorMap = new ConcurrentHashMap<>();

    @Inject
    public GossipPackageRegister(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
    }

    @Override
    public List<PackageURL> getProtocolURLList(String packageName, String schema) {
        return packageSchemaURLListMap.get(packageName).get(schema);
    }

    @Override
    public Iterator<PackageURL> getProtocolURLIterator(String packageName, String schema) {
        return packageSchemaURLIteratorMap.get(packageName).get(schema);
    }

    public void mergeMemberURLs(Address address, String packageName, Map<String, Object> map) {
        PackageURL url = new PackageURL(map);
        if (url.getHost() == null) {
            url.setHost(address.host());
        }
        packageURLs.computeIfAbsent(packageName, k -> new LinkedHashSet<>());
        packageURLs.get(packageName).add(url);
        memberAddressURLs.computeIfAbsent(address.toString(), k -> new LinkedHashSet<>());
        memberAddressURLs.get(address.toString()).add(url);
        Logger.info("package " + packageName + " service: " + url + " registered from " + address);
    }

    public void mergeMemberSchemaURLIterator(String packageName) {
        Map<String, Iterator<PackageURL>> iteratorMap = packageURLs.get(packageName).stream()
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
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), Iterators.cycle(entry.getValue())))
                .collect(
                        Collectors
                                .toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue
                                )
                );
        packageSchemaURLIteratorMap.put(packageName, iteratorMap);
    }

    public void mergeMemberSchemaURLList(String packageName) {
        Map<String, List<PackageURL>> listMap = packageURLs.get(packageName).stream()
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
                );
        packageSchemaURLListMap.put(packageName, listMap);
    }

    public void removeMemberURLs(String address) {
        Stream.ofNullable(memberAddressURLs.get(address))
                .map(memberAddressURLs ->
                        packageURLs.entrySet().stream()
                                .filter(entry -> entry.getValue().removeAll(memberAddressURLs))
                                .peek(entry -> {
                                            mergeMemberSchemaURLList(entry.getKey());
                                            if (packageConfig.getPackageLoadBalance().equals(LOAD_BALANCE_ROUND_ROBIN)) {
                                                mergeMemberSchemaURLIterator(entry.getKey());
                                            }
                                        }
                                )
                )

        packageURLs.forEach((key, value) -> {
                    Set<PackageURL> urls = memberAddressURLs.get(address);
                    if (urls != null && !urls.isEmpty()) {
                        boolean changed = value.removeAll(urls);
                        if (changed) {
                            mergeMemberSchemaURLList(key);
                            if (packageConfig.getPackageLoadBalance().equals(LOAD_BALANCE_ROUND_ROBIN)) {
                                mergeMemberSchemaURLIterator(key);
                            }
                        }
                    }
                }
        );
        memberAddressURLs.remove(address);
    }
}
