package io.graphoenix.jsonschema.handler;

import com.networknt.schema.urn.URNFactory;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@ApplicationScoped
public class JsonSchemaResourceURNFactory implements URNFactory {

    @Override
    public URI create(String name) {
        try {
            return Objects.requireNonNull(getClass().getClassLoader().getResource("META-INF/schema/" + name)).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
