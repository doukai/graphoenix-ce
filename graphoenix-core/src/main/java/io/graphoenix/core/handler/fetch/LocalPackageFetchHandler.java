package io.graphoenix.core.handler.fetch;

import io.graphoenix.spi.constant.Hammurabi;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.FetchHandler;
import io.graphoenix.spi.handler.PackageFetchHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

@ApplicationScoped
@Named(Hammurabi.ENUM_PROTOCOL_ENUM_VALUE_LOCAL)
public class LocalPackageFetchHandler implements PackageFetchHandler {

    private final Provider<FetchHandler> fetchHandlerProvider;

    @Inject
    public LocalPackageFetchHandler(Provider<FetchHandler> fetchHandlerProvider) {
        this.fetchHandlerProvider = fetchHandlerProvider;
    }

    @Override
    public Mono<JsonValue> request(String packageName, Operation operation) {
        return Mono.from(fetchHandlerProvider.get().request(operation));
    }
}
