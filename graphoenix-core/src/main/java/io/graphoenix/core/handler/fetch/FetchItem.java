package io.graphoenix.core.handler.fetch;

import io.graphoenix.spi.graphql.operation.Field;
import jakarta.json.stream.JsonCollectors;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_LIST_NAME;

public class FetchItem {

    private String packageName;

    private String protocol;

    private String path;

    private Field fetchField;

    private String target;

    private Field field;

    private String fetchFrom;

    private Integer index;

    public static List<Field> mergeFields(Stream<FetchItem> fetchItemStream) {
        return fetchItemStream
                .collect(
                        Collectors.groupingBy(
                                fetchItem -> fetchItem.getFetchField().getName(),
                                Collectors.toList()
                        )
                )
                .entrySet().stream()
                .map(entry ->
                        new Field(entry.getKey())
                                .setArguments(
                                        Map.of(
                                                INPUT_VALUE_LIST_NAME,
                                                entry.getValue().stream()
                                                        .map(fetchItem -> fetchItem.getFetchField().getArguments().asJsonObject())
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
                .collect(Collectors.toList());
    }

    public static List<FetchItem> mergeItems(Stream<FetchItem> fetchItemStream) {
        return fetchItemStream
                .collect(
                        Collectors.groupingBy(
                                fetchItem -> fetchItem.getFetchField().getName(),
                                Collectors.toList()
                        )
                )
                .entrySet().stream()
                .flatMap(entry ->
                        IntStream.range(0, entry.getValue().size())
                                .mapToObj(index -> {
                                            FetchItem fetchItem = entry.getValue().get(index);
                                            return new FetchItem(null, null, fetchItem.getPath(), new Field(entry.getKey()), fetchItem.getTarget(), fetchItem.getField(), fetchItem.getFetchFrom(), index);
                                        }
                                )
                )
                .collect(Collectors.toList());
    }

    public FetchItem(String packageName, String protocol, String path, Field fetchField, String target) {
        this.packageName = packageName;
        this.protocol = protocol;
        this.path = path;
        this.fetchField = fetchField;
        this.target = target;
    }

    public FetchItem(String packageName, String protocol, String path, Field fetchField, String target, Field field, String fetchFrom) {
        this(packageName, protocol, path, fetchField, target);
        this.field = field;
        this.fetchFrom = fetchFrom;
    }

    public FetchItem(String packageName, String protocol, String path, Field fetchField, String target, Field field, String fetchFrom, Integer index) {
        this(packageName, protocol, path, fetchField, target, field, fetchFrom);
        this.index = index;
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
