package io.graphoenix.core.dto;

import com.dslplatform.json.CompiledJson;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.spi.JsonbProvider;
import jakarta.json.spi.JsonProvider;

import java.io.StringReader;
import java.util.Map;

@CompiledJson
public class GraphQLRequest {

    private String query;

    private String operationName;

    private JsonObject variables;

    private static final JsonProvider jsonProvider = JsonProvider.provider();

    private static final Jsonb jsonb = JsonbProvider.provider().create().build();

    public GraphQLRequest() {
    }

    public GraphQLRequest(String query) {
        this.query = query;
    }

    public GraphQLRequest(String query, String operationName, JsonObject variables) {
        this.query = query;
        this.operationName = operationName;
        this.variables = variables;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public static GraphQLRequest fromJson(JsonObject jsonObject) {
        return jsonb.fromJson(jsonObject.toString(), GraphQLRequest.class);
    }

    public static GraphQLRequest fromJson(String json) {
        return fromJson(jsonProvider.createReader(new StringReader(json)).readObject());
    }

    public Map<String, JsonValue> getVariables() {
        if (variables == null) {
            return JsonValue.EMPTY_JSON_OBJECT;
        }
        return variables;
    }

    public void setVariables(JsonObject variables) {
        this.variables = variables;
    }
}
