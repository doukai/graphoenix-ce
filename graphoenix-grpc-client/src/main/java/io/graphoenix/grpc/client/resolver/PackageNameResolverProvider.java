package io.graphoenix.grpc.client.resolver;

import io.graphoenix.core.handler.PackageManager;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import io.grpc.Status;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PackageNameResolverProvider extends NameResolverProvider {

    private static final String SCHEME = "package";

    private final PackageManager packageManager;

    @Inject
    public PackageNameResolverProvider(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public NameResolver newNameResolver(URI targetUri, final NameResolver.Args args) {
        return new NameResolver() {
            private NameResolver.Listener2 listener;

            @Override
            public String getServiceAuthority() {
                return targetUri.getHost();
            }

            @Override
            public void start(Listener2 listener) {
                this.listener = listener;
                this.resolve();
            }

            @Override
            public void shutdown() {
            }

            @Override
            public void refresh() {
                this.resolve();
            }

            private void resolve() {
                try {
                    List<EquivalentAddressGroup> addresses = packageManager.getURLList(targetUri.getHost(), "grpc").stream()
                            .map(packageURL -> new InetSocketAddress(packageURL.getHost(), packageURL.getPort()))
                            .map(EquivalentAddressGroup::new)
                            .collect(Collectors.toList());

                    ResolutionResult resolutionResult = ResolutionResult.newBuilder()
                            .setAddresses(addresses)
                            .build();

                    this.listener.onResult(resolutionResult);

                } catch (Exception e) {
                    // when error occurs, notify listener
                    this.listener.onError(Status.UNAVAILABLE.withDescription("Unable to resolve host ").withCause(e));
                }
            }
        };
    }

    @Override
    public String getDefaultScheme() {
        return SCHEME;
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 5;
    }
}
