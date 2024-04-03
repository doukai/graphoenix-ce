package io.graphoenix.core.event;

import io.graphoenix.core.handler.DocumentManager;
import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.inject.Inject;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.Map;

@ApplicationScoped
@Initialized(ApplicationScoped.class)
@Priority(DocumentInitializedEvent.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY)
public class DocumentInitializedEvent implements ScopeEvent {

    public static final int DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY = 0;

    private final DocumentManager documentManager;

    @Inject
    public DocumentInitializedEvent(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Override
    public void fire(Map<String, Object> context) {
        try {
            documentManager.getDocument().addDefinitions(getClass().getClassLoader().getResourceAsStream("META-INF/graphql/main.gql"));
            Logger.info("document initialized success");
        } catch (IOException e) {
            Logger.error(e);
            Logger.info("document initialized failed");
        }
    }
}
