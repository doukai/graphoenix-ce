package io.graphoenix.introspection.event;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.introspection.handler.IntrospectionMutationBuilder;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.MutationHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import static io.graphoenix.core.event.DocumentInitializer.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY;

@ApplicationScoped
public class IntrospectionBuilder {

    private static final Logger logger = LoggerFactory.getLogger(IntrospectionBuilder.class);

    public static final int INTROSPECTION_BUILD_SCOPE_EVENT_PRIORITY = DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY + 200;

    private final GraphQLConfig graphQLConfig;
    private final IntrospectionMutationBuilder introspectionMutationBuilder;
    private final MutationHandler mutationHandler;

    @Inject
    public IntrospectionBuilder(GraphQLConfig graphQLConfig, IntrospectionMutationBuilder introspectionMutationBuilder, MutationHandler mutationHandler) {
        this.graphQLConfig = graphQLConfig;
        this.introspectionMutationBuilder = introspectionMutationBuilder;
        this.mutationHandler = mutationHandler;
    }

    public Mono<Void> buildIntrospection(@Observes @Initialized(ApplicationScoped.class) @Priority(IntrospectionBuilder.INTROSPECTION_BUILD_SCOPE_EVENT_PRIORITY) Object event) {
        if (graphQLConfig.getBuildIntrospection()) {
            logger.info("introspection build started");
            Operation operation = introspectionMutationBuilder.buildIntrospectionSchemaMutation();
            return Mono.from(mutationHandler.mutation(operation, 500))
                    .doOnSuccess((jsonValue) -> logger.info("introspection build success"))
                    .then();
        }
        return Mono.empty();
    }
}
