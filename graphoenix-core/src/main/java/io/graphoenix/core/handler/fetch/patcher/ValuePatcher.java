package io.graphoenix.core.handler.fetch.patcher;

import jakarta.json.JsonValue;

public class ValuePatcher implements Patcher {

    private String path;

    private JsonValue jsonValue;

    public ValuePatcher(String path, JsonValue jsonValue) {
        this.path = path;
        this.jsonValue = jsonValue;
    }

    public String getPath() {
        return path;
    }

    public ValuePatcher setPath(String path) {
        this.path = path;
        return this;
    }

    public JsonValue getJsonValue() {
        return jsonValue;
    }

    public ValuePatcher setJsonValue(JsonValue jsonValue) {
        this.jsonValue = jsonValue;
        return this;
    }

    @Override
    public boolean isValuePatcher() {
        return true;
    }
}
