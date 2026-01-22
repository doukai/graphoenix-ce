package io.graphoenix.core.event;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static io.graphoenix.core.event.DocumentInitializer.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY;

@ApplicationScoped
public class BannerPrinter {

    private static final Logger logger = LoggerFactory.getLogger(BannerPrinter.class);

    public static final int BANNER_SCOPE_EVENT_PRIORITY = DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY - 1;

    public static final String BANNER_FILE_NAME = "banner.txt";

    public void printBanner(@Observes @Initialized(ApplicationScoped.class) @Priority(BannerPrinter.BANNER_SCOPE_EVENT_PRIORITY) Object event) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(BANNER_FILE_NAME)) {
            if (inputStream != null) {
                logger.info(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
        }
    }
}
