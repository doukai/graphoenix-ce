package io.graphoenix.gossip.handler;

import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.gossip.config.GossipConfig;
import io.graphoenix.spi.bootstrap.Runner;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.net.Address;
import io.scalecube.transport.netty.tcp.TcpTransportFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.dto.PackageURL.*;

@ApplicationScoped
public class GossipPackageCluster implements Runner {

    private static final Logger logger = LoggerFactory.getLogger(GossipPackageCluster.class.getName());

    public static final String URLS_NAME = "urls";

    private final GossipConfig gossipConfig;
    private final PackageManager packageManager;
    private final GossipPackageProvider gossipPackageProvider;

    @Inject
    public GossipPackageCluster(GossipConfig gossipConfig, PackageManager packageManager, GossipPackageProvider gossipPackageProvider) {
        this.gossipConfig = gossipConfig;
        this.packageManager = packageManager;
        this.gossipPackageProvider = gossipPackageProvider;
    }

    @Override
    public void run() {
        new ClusterImpl()
                .membership(opts ->
                        opts.seedMembers(
                                Stream.ofNullable(packageManager.getSeedMembers())
                                        .flatMap(Collection::stream)
                                        .map(Address::from)
                                        .collect(Collectors.toList())
                        )
                )
                .transport(opts -> opts.port(gossipConfig.getPort()))
                .config(opts ->
                        opts.metadata(
                                Map.of(
                                        URLS_NAME, getURLs()
                                )
                        )
                )
                .transportFactory(TcpTransportFactory::new)
                .handler(cluster -> new ClusterMessageHandler() {
                            @SuppressWarnings("unchecked")
                            @Override
                            public void onMembershipEvent(MembershipEvent event) {
                                switch (event.type()) {
                                    case ADDED:
                                    case UPDATED:
                                        cluster.metadata(event.member())
                                                .ifPresent(metadata ->
                                                        gossipPackageProvider.mergeMemberURLs(event.member(), (List<Map<String, Object>>) ((Map<String, ?>) metadata).get(URLS_NAME))
                                                );
                                        logger.debug("{} merged", event.member().toString());
                                        break;
                                    case LEAVING:
                                        cluster.metadata(event.member())
                                                .ifPresent(metadata ->
                                                        gossipPackageProvider.removeMemberURLs(event.member(), (List<Map<String, Object>>) ((Map<String, ?>) metadata).get(URLS_NAME))
                                                );
                                        logger.debug("{} leaving", event.member().toString());
                                        break;
                                    case REMOVED:
                                        logger.debug("{} removed", event.member().toString());
                                        break;
                                }
                            }
                        }
                )
                .startAwait();
    }

    private List<Map<String, Object>> getURLs() {
        return CDI.current().select(Runner.class).stream()
                .filter(runner -> runner.protocol() != null)
                .flatMap(runner ->
                        packageManager.getLocalPackages()
                                .map(packageName ->
                                        Map.of(
                                                PACKAGE_NAME_NAME, (Object) packageName,
                                                PROTOCOL_NAME, runner.protocol(),
                                                PORT_NAME, runner.port()
                                        )
                                )
                )
                .collect(Collectors.toList());
    }

    @Override
    public int port() {
        return gossipConfig.getPort();
    }
}
