package io.graphoenix.introspection.event;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.introspection.handler.IntrospectionMutationBuilder;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.OperationHandler;
import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.inject.Inject;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import java.util.Map;

@ApplicationScoped
@Initialized(ApplicationScoped.class)
@Priority(2)
public class IntrospectionBuildEvent implements ScopeEvent {

    private final GraphQLConfig graphQLConfig;
    private final IntrospectionMutationBuilder introspectionMutationBuilder;
    private final OperationHandler operationHandler;

    @Inject
    public IntrospectionBuildEvent(GraphQLConfig graphQLConfig, IntrospectionMutationBuilder introspectionMutationBuilder, OperationHandler operationHandler) {
        this.graphQLConfig = graphQLConfig;
        this.introspectionMutationBuilder = introspectionMutationBuilder;
        this.operationHandler = operationHandler;
    }

    @Override
    public Mono<Void> fireAsync(Map<String, Object> context) {
        if (graphQLConfig.getBuildIntrospection()) {
            Logger.info("introspection build started");
            Operation operation = introspectionMutationBuilder.buildIntrospectionSchemaMutation();
            return Mono.from(operationHandler.handle(operation))
                    .doOnSuccess((jsonValue) -> Logger.info("introspection build success"))
                    .then();
        }
        return Mono.empty();
    }
}
