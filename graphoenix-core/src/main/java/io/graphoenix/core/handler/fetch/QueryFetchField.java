package io.graphoenix.core.handler.fetch;

import io.graphoenix.core.handler.fetch.patcher.Patcher;
import io.graphoenix.spi.graphql.operation.Field;

public class QueryFetchField {

    private String packageName;

    private String protocol;

    private Field field;

    private Patcher patcher;

    public QueryFetchField(String packageName, String protocol, Field field) {
        this.packageName = packageName;
        this.protocol = protocol;
        this.field = field;
    }

    public String getPackageName() {
        return packageName;
    }

    public QueryFetchField setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public QueryFetchField setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public Field getField() {
        return field;
    }

    public QueryFetchField setField(Field field) {
        this.field = field;
        return this;
    }

    public Patcher getPatcher() {
        return patcher;
    }

    public QueryFetchField setPatcher(Patcher patcher) {
        this.patcher = patcher;
        return this;
    }
}
