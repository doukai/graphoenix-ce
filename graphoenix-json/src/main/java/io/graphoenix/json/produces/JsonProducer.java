package io.graphoenix.json.produces;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.spi.JsonbProvider;
import jakarta.json.spi.JsonProvider;

@ApplicationScoped
public class JsonProducer {

    private static final JsonProvider jsonProvider = JsonProvider.provider();

    private static final JsonbProvider jsonbProvider = JsonbProvider.provider();

    @Produces
    @ApplicationScoped
    public JsonProvider jsonProvider() {
        return jsonProvider;
    }

    @Produces
    @ApplicationScoped
    public JsonbProvider jsonbProvider() {
        return jsonbProvider;
    }

    @Produces
    @ApplicationScoped
    public Jsonb jsonb() {
        return jsonbProvider.create().build();
    }
}
