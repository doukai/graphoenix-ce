package io.graphoenix.introspection.event;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.handler.TypeEmptyHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static io.graphoenix.introspection.event.IntrospectionBuilder.INTROSPECTION_BUILD_SCOPE_EVENT_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.PREFIX_INTROSPECTION;

@ApplicationScoped
public class IntrospectionCleaner {

    private static final Logger logger = LoggerFactory.getLogger(IntrospectionCleaner.class.getName());

    public static final int INTROSPECTION_CLEAN_SCOPE_EVENT_PRIORITY = INTROSPECTION_BUILD_SCOPE_EVENT_PRIORITY - 1;

    private final GraphQLConfig graphQLConfig;
    private final DocumentManager documentManager;
    private final TypeEmptyHandler typeEmptyHandler;

    @Inject
    public IntrospectionCleaner(GraphQLConfig graphQLConfig, DocumentManager documentManager, TypeEmptyHandler typeEmptyHandler) {
        this.graphQLConfig = graphQLConfig;
        this.documentManager = documentManager;
        this.typeEmptyHandler = typeEmptyHandler;
    }

    public Mono<Void> cleanIntrospection(@Observes @Initialized(ApplicationScoped.class) @Priority(IntrospectionCleaner.INTROSPECTION_CLEAN_SCOPE_EVENT_PRIORITY) Object event) {
        if (graphQLConfig.getBuildIntrospection()) {
            logger.info("introspection clean started");
            return typeEmptyHandler
                    .empty(
                            documentManager.getDocument().getObjectTypes()
                                    .filter(objectType -> !objectType.isContainer())
                                    .map(AbstractDefinition::getName)
                                    .filter(name -> name.startsWith(PREFIX_INTROSPECTION))
                                    .collect(Collectors.toSet())
                    )
                    .doOnSuccess((v) -> logger.info("introspection clean success"));
        }
        return Mono.empty();
    }
}
