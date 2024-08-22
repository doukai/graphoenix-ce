package io.graphoenix.core.event;

import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import org.tinylog.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.graphoenix.core.event.DocumentInitializedEvent.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY;

@ApplicationScoped
@Initialized(ApplicationScoped.class)
@Priority(BannerScopeEvent.BANNER_SCOPE_EVENT_PRIORITY)
public class BannerScopeEvent implements ScopeEvent {

    public static final int BANNER_SCOPE_EVENT_PRIORITY = DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY - 1;

    public static final String BANNER_FILE_NAME = "banner.txt";

    @Override
    public void fire(Map<String, Object> context) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(BANNER_FILE_NAME)) {
            if (inputStream != null) {
                Logger.info(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
        }
    }
}
