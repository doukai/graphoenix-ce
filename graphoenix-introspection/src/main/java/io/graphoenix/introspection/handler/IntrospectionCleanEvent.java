package io.graphoenix.introspection.handler;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.handler.TypeEmptyHandler;
import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.inject.Inject;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static io.graphoenix.core.event.DocumentInitializedEvent.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.PREFIX_INTROSPECTION;

@ApplicationScoped
@Initialized(ApplicationScoped.class)
@Priority(IntrospectionCleanEvent.INTROSPECTION_CLEAN_SCOPE_EVENT_PRIORITY)
public class IntrospectionCleanEvent implements ScopeEvent {

    public static final int INTROSPECTION_CLEAN_SCOPE_EVENT_PRIORITY = DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY + 199;

    private final GraphQLConfig graphQLConfig;
    private final DocumentManager documentManager;
    private final TypeEmptyHandler typeEmptyHandler;

    @Inject
    public IntrospectionCleanEvent(GraphQLConfig graphQLConfig, DocumentManager documentManager, TypeEmptyHandler typeEmptyHandler) {
        this.graphQLConfig = graphQLConfig;
        this.documentManager = documentManager;
        this.typeEmptyHandler = typeEmptyHandler;
    }


    @Override
    public Mono<Void> fireAsync(Map<String, Object> context) {
        if (graphQLConfig.getBuildIntrospection()) {
            Logger.info("introspection clean started");
            return typeEmptyHandler
                    .empty(
                            documentManager.getDocument().getObjectTypes()
                                    .map(AbstractDefinition::getName)
                                    .filter(name -> name.startsWith(PREFIX_INTROSPECTION))
                                    .collect(Collectors.toSet())
                    )
                    .doOnSuccess((v) -> Logger.info("introspection clean success"));
        }
        return Mono.empty();
    }
}
