package io.graphoenix.core.event;

import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
        try {
            URL resource = this.getClass().getClassLoader().getResource(BANNER_FILE_NAME);
            if (resource != null) {
                String banner = Files.readString(Path.of(resource.toURI()), StandardCharsets.US_ASCII);
                Logger.info(banner);
            }
        } catch (IOException | URISyntaxException ignored) {
        }
    }
}
