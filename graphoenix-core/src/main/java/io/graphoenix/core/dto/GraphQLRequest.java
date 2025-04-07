package io.graphoenix.core.dto;

import com.dslplatform.json.CompiledJson;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;

import java.io.StringReader;
import java.util.Map;

@CompiledJson
public class GraphQLRequest {

    private String query;

    private String operationName;

    private JsonObject variables;

    private JsonObject map;

    private static final JsonProvider jsonProvider = JsonProvider.provider();

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
        return new GraphQLRequest(
                jsonObject.containsKey("query") && !jsonObject.isNull("query") ? jsonObject.getString("query") : null,
                jsonObject.containsKey("operationName") && !jsonObject.isNull("operationName") ? jsonObject.getString("operationName") : null,
                jsonObject.containsKey("variables") && !jsonObject.isNull("variables") ? jsonObject.getJsonObject("variables") : null
        );
    }

    public static GraphQLRequest fromJson(String json) {
        return fromJson(jsonProvider.createReader(new StringReader(json)).readObject());
    }

    public void setOperations(String operations) {
        JsonObject jsonObject = jsonProvider.createReader(new StringReader(operations)).readObject();
        this.query = jsonObject.containsKey("query") && !jsonObject.isNull("query") ? jsonObject.getString("query") : null;
        this.operationName = jsonObject.containsKey("operationName") && !jsonObject.isNull("operationName") ? jsonObject.getString("operationName") : null;
        this.variables = jsonObject.containsKey("variables") && !jsonObject.isNull("variables") ? jsonObject.getJsonObject("variables") : null;
    }

    public void setMap(String map) {
        this.map = jsonProvider.createReader(new StringReader(map)).readObject();
    }

    public void setFileID(String name, String id) {
        this.variables = this.map.getJsonArray(name).stream()
                .map(jsonValue -> (JsonString) jsonValue)
                .reduce(this.variables,
                        (variables, jsonString) ->
                                jsonProvider
                                        .createPointer(
                                                "/" + jsonString.getString()
                                                        .replaceFirst("variables/", "")
                                                        .replaceAll("\\.", "/")
                                        )
                                        .replace(this.variables, jsonProvider.createValue(id)),
                        (x, y) -> y
                );
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
