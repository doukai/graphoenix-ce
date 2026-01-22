package io.graphoenix.core.event;

import io.graphoenix.core.handler.DocumentManager;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ApplicationScoped
public class DocumentInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DocumentInitializer.class);

    public static final int DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY = 0;

    private final DocumentManager documentManager;

    @Inject
    public DocumentInitializer(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public void initializeDocument(@Observes @Initialized(ApplicationScoped.class) @Priority(DocumentInitializer.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY) Object event) {
        try {
            documentManager.getDocument().addDefinitions(getClass().getClassLoader().getResourceAsStream("META-INF/graphql/main.gql"));
            logger.info("document initialized success");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
