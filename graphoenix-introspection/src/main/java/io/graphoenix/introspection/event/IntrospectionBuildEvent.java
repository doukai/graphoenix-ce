package io.graphoenix.introspection.event;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.introspection.handler.IntrospectionMutationBuilder;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.MutationHandler;
import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.inject.Inject;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import java.util.Map;

import static io.graphoenix.core.event.DocumentInitializedEvent.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY;

@ApplicationScoped
@Initialized(ApplicationScoped.class)
@Priority(IntrospectionBuildEvent.INTROSPECTION_BUILD_SCOPE_EVENT_PRIORITY)
public class IntrospectionBuildEvent implements ScopeEvent {

    public static final int INTROSPECTION_BUILD_SCOPE_EVENT_PRIORITY = DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY + 200;

    private final GraphQLConfig graphQLConfig;
    private final IntrospectionMutationBuilder introspectionMutationBuilder;
    private final MutationHandler mutationHandler;

    @Inject
    public IntrospectionBuildEvent(GraphQLConfig graphQLConfig, IntrospectionMutationBuilder introspectionMutationBuilder, MutationHandler mutationHandler) {
        this.graphQLConfig = graphQLConfig;
        this.introspectionMutationBuilder = introspectionMutationBuilder;
        this.mutationHandler = mutationHandler;
    }

    @Override
    public Mono<Void> fireAsync(Map<String, Object> context) {
        if (graphQLConfig.getBuildIntrospection()) {
            Logger.info("introspection build started");
            Operation operation = introspectionMutationBuilder.buildIntrospectionSchemaMutation();
            return Mono.from(mutationHandler.mutation(operation, 500))
                    .doOnSuccess((jsonValue) -> Logger.info("introspection build success"))
                    .then();
        }
        return Mono.empty();
    }
}
