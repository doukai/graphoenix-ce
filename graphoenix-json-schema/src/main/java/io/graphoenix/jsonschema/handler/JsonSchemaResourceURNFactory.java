package io.graphoenix.jsonschema.handler;

import com.networknt.schema.urn.URNFactory;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@ApplicationScoped
public class JsonSchemaResourceURNFactory implements URNFactory {

    @Override
    public URI create(String name) {
        try {
            URL url = getClass().getClassLoader().getResource("META-INF/schema/" + name);
            if (url == null) {
                return null;
            }
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
