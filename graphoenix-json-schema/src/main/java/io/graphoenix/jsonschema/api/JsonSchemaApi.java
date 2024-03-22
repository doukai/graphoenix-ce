package io.graphoenix.jsonschema.api;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.jsonschema.handler.JsonSchemaManager;
import io.graphoenix.spi.error.GraphQLErrors;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;
import static jakarta.json.JsonValue.TRUE;

@ApplicationScoped
@GraphQLApi
public class JsonSchemaApi {
    private final JsonSchemaManager jsonSchemaManager;

    private final JsonProvider jsonProvider;

    private final JsonObject queryJsonSchema;

    private final JsonObject mutationJsonSchema;

    private final JsonObject subscriptionJsonSchema;

    private final Map<String, String> jsonSchemaMap = new ConcurrentHashMap<>();

    @Inject
    public JsonSchemaApi(DocumentManager documentManager, JsonSchemaManager jsonSchemaManager, JsonProvider jsonProvider) {
        this.jsonSchemaManager = jsonSchemaManager;
        this.jsonProvider = jsonProvider;
        this.queryJsonSchema = jsonProvider
                .createReader(new StringReader(jsonSchemaManager.getJsonSchema(documentManager.getDocument().getQueryOperationTypeOrError().getName())))
                .readObject();
        this.mutationJsonSchema = jsonProvider
                .createReader(new StringReader(jsonSchemaManager.getJsonSchema(documentManager.getDocument().getMutationOperationTypeOrError().getName())))
                .readObject();
        this.subscriptionJsonSchema = jsonProvider
                .createReader(new StringReader(jsonSchemaManager.getJsonSchema(documentManager.getDocument().getSubscriptionOperationTypeOrError().getName())))
                .readObject();
    }

    @Query
    public String jsonSchema(String jsonSchemaName) {
        return jsonSchemaManager.getJsonSchema(jsonSchemaName);
    }

    @Query
    public String operationSchema(String operationType, Set<String> fieldNameSet) {
        return jsonSchemaMap
                .computeIfAbsent(
                        operationType + "_" + String.join("_", fieldNameSet),
                        k -> {
                            JsonObjectBuilder jsonObjectBuilder = jsonProvider.createObjectBuilder()
                                    .add("$id", k)
                                    .add("type", "object")
                                    .add("additionalProperties", TRUE);

                            switch (operationType) {
                                case OPERATION_QUERY_NAME:
                                    return jsonObjectBuilder
                                            .add(
                                                    "properties",
                                                    queryJsonSchema.getJsonObject("properties").entrySet().stream()
                                                            .filter(entry -> fieldNameSet.contains(entry.getKey()))
                                                            .collect(JsonCollectors.toJsonObject())
                                            )
                                            .build()
                                            .toString();
                                case OPERATION_MUTATION_NAME:
                                    return jsonObjectBuilder
                                            .add(
                                                    "properties",
                                                    mutationJsonSchema.getJsonObject("properties").entrySet().stream()
                                                            .filter(entry -> fieldNameSet.contains(entry.getKey()))
                                                            .collect(JsonCollectors.toJsonObject())
                                            )
                                            .build()
                                            .toString();
                                case OPERATION_SUBSCRIPTION_NAME:
                                    return jsonObjectBuilder
                                            .add(
                                                    "properties",
                                                    subscriptionJsonSchema.getJsonObject("properties").entrySet().stream()
                                                            .filter(entry -> fieldNameSet.contains(entry.getKey()))
                                                            .collect(JsonCollectors.toJsonObject())
                                            )
                                            .build()
                                            .toString();
                                default:
                                    throw new GraphQLErrors(UNSUPPORTED_OPERATION_TYPE.bind(operationType));
                            }
                        }
                );
    }
}
