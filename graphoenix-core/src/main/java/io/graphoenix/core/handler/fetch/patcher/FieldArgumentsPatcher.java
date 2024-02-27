package io.graphoenix.core.handler.fetch.patcher;

import io.graphoenix.spi.graphql.operation.Field;

public class FieldArgumentsPatcher implements Patcher {

    private Field field;

    private String path;

    private String fetchFrom;

    private Integer index;

    private String target;

    public FieldArgumentsPatcher(Field field, String path, String fetchFrom, Integer index, String target) {
        this.field = field;
        this.path = path;
        this.fetchFrom = fetchFrom;
        this.index = index;
        this.target = target;
    }

    public Field getField() {
        return field;
    }

    public FieldArgumentsPatcher setField(Field field) {
        this.field = field;
        return this;
    }

    public String getPath() {
        return path;
    }

    public FieldArgumentsPatcher setPath(String path) {
        this.path = path;
        return this;
    }

    public String getFetchFrom() {
        return fetchFrom;
    }

    public FieldArgumentsPatcher setFetchFrom(String fetchFrom) {
        this.fetchFrom = fetchFrom;
        return this;
    }

    public Integer getIndex() {
        return index;
    }

    public FieldArgumentsPatcher setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public FieldArgumentsPatcher setTarget(String target) {
        this.target = target;
        return this;
    }

    @Override
    public boolean isFieldArgumentsPatcher() {
        return true;
    }
}
