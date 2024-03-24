package io.graphoenix.jsonschema.handler;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class JsonSchemaManager {

    private final Map<String, String> jsonSchemaMap = new ConcurrentHashMap<>();

    public String getJsonSchema(String name) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("META-INF/schema/" + name)) {
            if (inputStream == null) {
                return null;
            }
            String jsonSchema = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            return jsonSchemaMap.computeIfAbsent(name, k -> jsonSchema);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
