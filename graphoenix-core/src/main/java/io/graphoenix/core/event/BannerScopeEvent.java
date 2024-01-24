package io.graphoenix.core.event;

import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@Initialized(ApplicationScoped.class)
@Priority(0)
public class BannerScopeEvent implements ScopeEvent {

    public static final String BANNER_FILE_NAME = "banner.txt";
    public static final String VERSION_KEY = "GP-Version";
    public static final String DATABASE_KEY = "GP-Database";
    public static final String PROTOCOL_KEY = "GP-Protocol";

    @Override
    public void fire(Map<String, Object> context) {
        try {
            String banner = Files.readString(Path.of(Objects.requireNonNull(this.getClass().getClassLoader().getResource(BANNER_FILE_NAME)).toURI()), StandardCharsets.US_ASCII);
            if (banner != null) {
                Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
                int index = 0;
                while (resources.hasMoreElements()) {
                    Manifest manifest = new Manifest(resources.nextElement().openStream());
                    Attributes mainAttributes = manifest.getMainAttributes();
                    if (mainAttributes.containsKey(VERSION_KEY)) {
                        banner = banner.replace("${" + VERSION_KEY + "}", mainAttributes.getValue(VERSION_KEY));
                    }

                    if (mainAttributes.containsKey(DATABASE_KEY)) {
                        banner = banner.replace("${" + DATABASE_KEY + "}", mainAttributes.getValue(DATABASE_KEY));
                        banner = banner.replace("${" + DATABASE_KEY + index + "}", mainAttributes.getValue(DATABASE_KEY));
                    }

                    if (mainAttributes.containsKey(PROTOCOL_KEY)) {
                        banner = banner.replace("${" + PROTOCOL_KEY + "}", mainAttributes.getValue(PROTOCOL_KEY));
                        banner = banner.replace("${" + PROTOCOL_KEY + index + "}", mainAttributes.getValue(PROTOCOL_KEY));
                    }
                    index++;
                }
            }
        } catch (IOException | URISyntaxException ignored) {
        }
    }
}
