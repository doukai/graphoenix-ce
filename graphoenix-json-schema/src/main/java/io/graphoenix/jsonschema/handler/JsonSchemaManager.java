package io.graphoenix.jsonschema.handler;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class JsonSchemaManager {

    private final Map<String, String> jsonSchemaMap = new ConcurrentHashMap<>();

    public String getJsonSchema(String objectName) {
        return jsonSchemaMap
                .computeIfAbsent(
                        objectName,
                        k -> new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("META-INF/schema/" + k)), StandardCharsets.UTF_8))
                                .lines()
                                .collect(Collectors.joining("\n"))
                );
    }
}
