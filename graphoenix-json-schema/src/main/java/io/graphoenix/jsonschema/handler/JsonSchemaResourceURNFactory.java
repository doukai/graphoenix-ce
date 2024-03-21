package io.graphoenix.jsonschema.handler;

import com.networknt.schema.urn.URNFactory;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@ApplicationScoped
public class JsonSchemaResourceURNFactory implements URNFactory {

    @Override
    public URI create(String urn) {
        try {
            return Objects.requireNonNull(getClass().getClassLoader().getResource("META-INF/schema/" + urn)).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
