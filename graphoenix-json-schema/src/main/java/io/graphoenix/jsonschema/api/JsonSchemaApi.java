package io.graphoenix.jsonschema.api;

import io.graphoenix.jsonschema.handler.JsonSchemaManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

@ApplicationScoped
@GraphQLApi
public class JsonSchemaApi {

    private final JsonSchemaManager jsonSchemaManager;

    @Inject
    public JsonSchemaApi(JsonSchemaManager jsonSchemaManager) {
        this.jsonSchemaManager = jsonSchemaManager;
    }

    @Query
    public String jsonSchema(String name) {
        return jsonSchemaManager.getJsonSchema(name);
    }
}
