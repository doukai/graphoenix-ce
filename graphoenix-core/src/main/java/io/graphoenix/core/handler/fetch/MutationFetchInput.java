package io.graphoenix.core.handler.fetch;

import io.graphoenix.core.handler.fetch.patcher.FieldArgumentsPatcher;
import jakarta.json.JsonValue;

import java.util.UUID;

public class MutationFetchInput {

    private String packageName;

    private String protocol;

    private String typeName;

    private JsonValue jsonValue;

    private String id;

    private FieldArgumentsPatcher fieldArgumentsPatcher;

    public MutationFetchInput(String packageName, String protocol, String typeName, JsonValue jsonValue, String id) {
        this.packageName = packageName;
        this.protocol = protocol;
        this.typeName = typeName;
        this.jsonValue = jsonValue;
        if (id != null) {
            this.id = id;
        } else {
            this.id = UUID.randomUUID().toString();
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public MutationFetchInput setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public MutationFetchInput setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public MutationFetchInput setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public JsonValue getJsonValue() {
        return jsonValue;
    }

    public MutationFetchInput setJsonValue(JsonValue jsonValue) {
        this.jsonValue = jsonValue;
        return this;
    }

    public String getId() {
        return id;
    }

    public MutationFetchInput setId(String id) {
        this.id = id;
        return this;
    }

    public FieldArgumentsPatcher getFieldArgumentsPatcher() {
        return fieldArgumentsPatcher;
    }

    public MutationFetchInput setFieldArgumentsPatcher(FieldArgumentsPatcher fieldArgumentsPatcher) {
        this.fieldArgumentsPatcher = fieldArgumentsPatcher;
        return this;
    }
}
