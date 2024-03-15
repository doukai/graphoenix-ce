package io.graphoenix.gossip.handler;

import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.gossip.config.GossipConfig;
import io.graphoenix.spi.bootstrap.Runner;
import io.nozdormu.spi.context.BeanContext;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.net.Address;
import io.scalecube.transport.netty.tcp.TcpTransportFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tinylog.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.dto.PackageURL.PORT_NAME;
import static io.graphoenix.spi.dto.PackageURL.PROTOCOL_NAME;

@ApplicationScoped
public class GossipPackageCluster implements Runnable {

    public static final String PACKAGE_NAME = "package";
    public static final String SERVICES_NAME = "services";

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
                                packageManager.getLocalPackages()
                                        .map(packageName -> Map.of(PACKAGE_NAME, packageName, SERVICES_NAME, getServices()))
                                        .collect(Collectors.toList())
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
                                                        gossipPackageProvider.mergeMemberURLs(event.member(), (List<Map<String, Object>>) metadata)
                                                );
                                        Logger.debug(event.member().toString() + " merged");
                                        break;
                                    case LEAVING:
                                        gossipPackageProvider.removeMemberURLs(event.member());
                                        Logger.debug(event.member().toString() + " leaving");
                                        break;
                                    case REMOVED:
                                        Logger.debug(event.member().toString() + " removed");
                                        break;
                                }
                            }
                        }
                )
                .startAwait();
    }

    private List<Map<String, Object>> getServices() {
        return BeanContext.getList(Runner.class).stream()
                .map(runner -> Map.of(PROTOCOL_NAME, (Object) runner.protocol(), PORT_NAME, runner.port()))
                .collect(Collectors.toList());
    }
}
