package io.graphoenix.core.handler.fetch;

import io.graphoenix.spi.graphql.operation.Field;
import jakarta.json.JsonObject;

public class FetchItem {

    private String packageName;

    private String protocol;

    private String path;

    private Field fetchField;

    private String target;

    private JsonObject jsonObject;

    public FetchItem(String packageName, String protocol, String path, Field fetchField, String target) {
        this.packageName = packageName;
        this.protocol = protocol;
        this.path = path;
        this.fetchField = fetchField;
        this.target = target;
    }

    public FetchItem(String packageName, String protocol, String path, Field fetchField, String target, JsonObject jsonObject) {
        this(packageName, protocol, path, fetchField, target);
        this.jsonObject = jsonObject;
    }

    public String getPackageName() {
        return packageName;
    }

    public FetchItem setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public FetchItem setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public String getPath() {
        return path;
    }

    public FetchItem setPath(String path) {
        this.path = path;
        return this;
    }

    public Field getFetchField() {
        return fetchField;
    }

    public FetchItem setFetchField(Field fetchField) {
        this.fetchField = fetchField;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public FetchItem setTarget(String target) {
        this.target = target;
        return this;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public FetchItem setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
