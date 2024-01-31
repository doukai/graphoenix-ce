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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@ApplicationScoped
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
            URL resource = this.getClass().getClassLoader().getResource(BANNER_FILE_NAME);
            if (resource != null) {
                String banner = Files.readString(Path.of(resource.toURI()), StandardCharsets.US_ASCII);
                Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
                String version = "";
                List<String> database = new ArrayList<>();
                List<String> protocol = new ArrayList<>();
                while (resources.hasMoreElements()) {
                    Manifest manifest = new Manifest(resources.nextElement().openStream());
                    Attributes mainAttributes = manifest.getMainAttributes();
                    if (mainAttributes.containsKey(VERSION_KEY)) {
                        version = mainAttributes.getValue(VERSION_KEY);
                    }

                    if (mainAttributes.containsKey(DATABASE_KEY)) {
                        database.add(mainAttributes.getValue(DATABASE_KEY));
                    }

                    if (mainAttributes.containsKey(PROTOCOL_KEY)) {
                        protocol.add(mainAttributes.getValue(PROTOCOL_KEY));
                    }
                }

                Logger.info(
                        banner
                                .replace("${" + VERSION_KEY + "}", version)
                                .replace("${" + DATABASE_KEY + "}", String.join(", ", database))
                                .replace("${" + PROTOCOL_KEY + "}", String.join(", ", protocol))
                );
            }
        } catch (IOException | URISyntaxException ignored) {
        }
    }
}
