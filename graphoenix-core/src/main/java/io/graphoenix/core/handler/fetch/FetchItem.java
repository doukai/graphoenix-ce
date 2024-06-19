package io.graphoenix.core.handler.fetch;

import io.graphoenix.spi.graphql.operation.Field;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonCollectors;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_LIST_NAME;
import static io.graphoenix.spi.constant.Hammurabi.SUFFIX_LIST;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class FetchItem {

    private String packageName;

    private String protocol;

    private String path;

    private Field fetchField;

    private String typeName;

    private JsonValue jsonValue;

    private String id;

    private String target;

    private Field field;

    private String fetchFrom;

    private Integer index;

    public static List<Field> buildMutationFields(Collection<FetchItem> fetchItems) {
        return Stream
                .concat(
                        fetchItems.stream()
                                .map(FetchItem::getFetchField)
                                .filter(Objects::nonNull),
                        fetchItems.stream()
                                .filter(fetchItem -> fetchItem.getFetchField() == null)
                                .collect(
                                        Collectors.groupingBy(
                                                FetchItem::getTypeName,
                                                Collectors.toList()
                                        )
                                )
                                .entrySet().stream()
                                .map(entry ->
                                        new Field(typeNameToFieldName(entry.getKey()) + SUFFIX_LIST)
                                                .setArguments(
                                                        Map.of(
                                                                INPUT_VALUE_LIST_NAME,
                                                                entry.getValue().stream()
                                                                        .filter(distinctByKey(FetchItem::getId))
                                                                        .map(FetchItem::getJsonValue)
                                                                        .collect(JsonCollectors.toJsonArray())
                                                        )
                                                )
                                                .mergeSelection(
                                                        entry.getValue().stream()
                                                                .filter(fetchItem -> fetchItem.getTarget() != null)
                                                                .map(fetchItem -> new Field(fetchItem.getTarget()))
                                                                .collect(Collectors.toList())
                                                )
                                )
                )
                .collect(Collectors.toList());
    }

    public static Stream<FetchItem> buildMutationItems(Collection<FetchItem> fetchItems) {
        return fetchItems.stream()
                .collect(
                        Collectors.groupingBy(
                                FetchItem::getTypeName,
                                Collectors.toList()
                        )
                )
                .entrySet().stream()
                .flatMap(entry -> {
                            List<String> idList = entry.getValue().stream()
                                    .filter(distinctByKey(FetchItem::getId))
                                    .map(FetchItem::getId)
                                    .collect(Collectors.toList());
                            return entry.getValue().stream()
                                    .map(fetchItem -> fetchItem.setIndex(idList.indexOf(fetchItem.getId())));
                        }
                );
    }

    public FetchItem(String packageName, String protocol, String path) {
        this.packageName = packageName;
        this.protocol = protocol;
        this.path = path;
    }

    public FetchItem(String packageName, String protocol, String typeName, Field fetchField) {
        this.packageName = packageName;
        this.protocol = protocol;
        this.typeName = typeName;
        this.fetchField = fetchField;
    }

    public FetchItem(String packageName, String protocol, Field field, String path, String typeName) {
        this.packageName = packageName;
        this.protocol = protocol;
        this.field = field;
        this.typeName = typeName;
        this.path = path;
    }

    public FetchItem(String packageName, String protocol, Field field, String path, String typeName, String target, String id) {
        this(packageName, protocol, field, path, typeName);
        this.target = target;
        this.id = id;
    }

    public FetchItem(String packageName, String protocol, String path, Field fetchField, String target) {
        this(packageName, protocol, path);
        this.fetchField = fetchField;
        this.target = target;
    }

    public FetchItem(String packageName, String protocol, String path, Field fetchField, String target, Field field, String fetchFrom) {
        this(packageName, protocol, path, fetchField, target);
        this.field = field;
        this.fetchFrom = fetchFrom;
    }

    public FetchItem(String packageName, String protocol, String path, String typeName, JsonValue jsonValue, String id, String target) {
        this(packageName, protocol, path);
        this.typeName = typeName;
        this.jsonValue = jsonValue;
        this.id = id;
        this.target = target;
    }

    public FetchItem(String packageName, String protocol, String path, String typeName, JsonValue jsonValue, String id, String target, Field field, String fetchFrom) {
        this(packageName, protocol, path, typeName, jsonValue, id, target);
        this.field = field;
        this.fetchFrom = fetchFrom;
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

    public String getTypeName() {
        return typeName;
    }

    public FetchItem setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public String getId() {
        return id;
    }

    public FetchItem setId(String id) {
        this.id = id;
        return this;
    }

    public JsonValue getJsonValue() {
        return jsonValue;
    }

    public FetchItem setJsonValue(JsonValue jsonValue) {
        this.jsonValue = jsonValue;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public FetchItem setTarget(String target) {
        this.target = target;
        return this;
    }

    public Field getField() {
        return field;
    }

    public FetchItem setField(Field field) {
        this.field = field;
        return this;
    }

    public String getFetchFrom() {
        return fetchFrom;
    }

    public FetchItem setFetchFrom(String fetchFrom) {
        this.fetchFrom = fetchFrom;
        return this;
    }

    public Integer getIndex() {
        return index;
    }

    public FetchItem setIndex(Integer index) {
        this.index = index;
        return this;
    }
}
