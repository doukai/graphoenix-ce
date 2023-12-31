package io.graphoenix.spi.graphql.common;

import graphql.parser.antlr.GraphqlParser;
import jakarta.json.*;
import jakarta.validation.constraints.NotNull;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.AnnotationMirror;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static io.graphoenix.spi.utils.ElementUtil.getNameFromElement;
import static io.graphoenix.spi.utils.ReflectUtil.getFieldValue;

public class ObjectValueWithVariable extends AbstractMap<String, JsonValue> implements ValueWithVariable, JsonObject, Iterable<JsonValue> {

    private final STGroupFile stGroupFile = new STGroupFile("stg/common/ObjectValueWithVariable.stg");

    private final Map<String, ValueWithVariable> objectValueWithVariable;

    public ObjectValueWithVariable() {
        this.objectValueWithVariable = new LinkedHashMap<>();
    }

    public ObjectValueWithVariable(Map<?, ?> objectValueWithVariable) {
        this.objectValueWithVariable = objectValueWithVariable.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> (String) entry.getKey(),
                                entry -> ValueWithVariable.of(entry.getValue()),
                                (x, y) -> y,
                                LinkedHashMap::new
                        )
                );
    }

    public ObjectValueWithVariable(JsonObject objectValueWithVariable) {
        this.objectValueWithVariable = objectValueWithVariable.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Entry::getKey,
                                entry -> ValueWithVariable.of(entry.getValue()),
                                (x, y) -> y,
                                LinkedHashMap::new
                        )
                );
    }

    public ObjectValueWithVariable(AnnotationMirror objectValueWithVariable) {
        this.objectValueWithVariable = objectValueWithVariable.getElementValues().entrySet().stream()
                .collect(
                        Collectors
                                .toMap(
                                        entry -> getNameFromElement(entry.getKey()),
                                        entry -> ValueWithVariable.of(entry.getValue()),
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                );
    }

    public ObjectValueWithVariable(Object objectValueWithVariable) {
        Class<?> clazz = objectValueWithVariable.getClass();
        this.objectValueWithVariable = Arrays.stream(clazz.getDeclaredFields())
                .collect(
                        Collectors
                                .toMap(
                                        Field::getName,
                                        field -> ValueWithVariable.of(getFieldValue(objectValueWithVariable, field)),
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                );
    }

    public ObjectValueWithVariable(GraphqlParser.ObjectValueWithVariableContext objectValueWithVariableContext) {
        this.objectValueWithVariable = objectValueWithVariableContext.objectFieldWithVariable().stream()
                .collect(
                        Collectors
                                .toMap(
                                        objectFieldWithVariableContext -> objectFieldWithVariableContext.name().getText(),
                                        objectFieldWithVariableContext -> ValueWithVariable.of(objectFieldWithVariableContext.valueWithVariable()),
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                );
    }

    public ObjectValueWithVariable(GraphqlParser.ObjectValueContext objectValueContext) {
        this.objectValueWithVariable = objectValueContext.objectField().stream()
                .collect(
                        Collectors
                                .toMap(
                                        objectFieldContext -> objectFieldContext.name().getText(),
                                        objectFieldContext -> ValueWithVariable.of(objectFieldContext.value()),
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                );
    }

    public Map<String, ValueWithVariable> getObjectValueWithVariable() {
        return objectValueWithVariable;
    }

    public ValueWithVariable put(String key, Object value) {
        return objectValueWithVariable.put(key, ValueWithVariable.of(value));
    }

    public ValueWithVariable putIfAbsent(String key, Object value) {
        return objectValueWithVariable.putIfAbsent(key, ValueWithVariable.of(value));
    }

    public ValueWithVariable compute(String key, Object value) {
        return objectValueWithVariable.compute(key, (k, v) -> ValueWithVariable.of(value));
    }

    public ValueWithVariable computeIfAbsent(String key, Object value) {
        return objectValueWithVariable.computeIfAbsent(key, k -> ValueWithVariable.of(value));
    }

    @NotNull
    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        return objectValueWithVariable.entrySet().stream()
                .map(entry -> new SimpleEntry<>(entry.getKey(), (JsonValue) entry.getValue()))
                .collect(Collectors.toSet());
    }

    public ValueWithVariable getValueWithVariable(String name) {
        return objectValueWithVariable.get(name);
    }

    @Override
    public JsonArray getJsonArray(String name) {
        return (JsonArray) get(name);
    }

    @Override
    public JsonObject getJsonObject(String name) {
        return (JsonObject) get(name);
    }

    @Override
    public JsonNumber getJsonNumber(String name) {
        return (JsonNumber) get(name);
    }

    @Override
    public JsonString getJsonString(String name) {
        return (JsonString) get(name);
    }

    @Override
    public String getString(String name) {
        return getJsonString(name).getString();
    }

    @Override
    public String getString(String name, String defaultValue) {
        JsonValue value = get(name);
        if (value instanceof JsonString) {
            return ((JsonString) value).getString();
        } else {
            return defaultValue;
        }
    }

    @Override
    public int getInt(String name) {
        return getJsonNumber(name).intValue();
    }

    @Override
    public int getInt(String name, int defaultValue) {
        JsonValue value = get(name);
        if (value instanceof JsonNumber) {
            return ((JsonNumber) value).intValue();
        } else {
            return defaultValue;
        }
    }

    @Override
    public boolean getBoolean(String name) {
        JsonValue value = get(name);
        if (value == null) {
            throw new NullPointerException();
        } else if (value == JsonValue.TRUE) {
            return true;
        } else if (value == JsonValue.FALSE) {
            return false;
        } else {
            throw new ClassCastException();
        }
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        JsonValue value = get(name);
        if (value == JsonValue.TRUE) {
            return true;
        } else if (value == JsonValue.FALSE) {
            return false;
        } else {
            return defaultValue;
        }
    }

    @Override
    public boolean isNull(String name) {
        return get(name).equals(JsonValue.NULL);
    }

    @Override
    public ValueType getValueType() {
        return ValueType.OBJECT;
    }

    @Override
    public JsonObject asJsonObject() {
        return this;
    }

    @Override
    public int size() {
        return objectValueWithVariable.size();
    }

    @Override
    public JsonValue get(Object key) {
        return objectValueWithVariable.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return objectValueWithVariable.containsKey(key);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Iterator<JsonValue> iterator() {
        return null;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    public static <K, V> ObjectValueWithVariable of(K k1, V v1) {
        return new ObjectValueWithVariable(Map.of(k1, v1));
    }

    public static <K, V> ObjectValueWithVariable of(K k1, V v1, K k2, V v2) {
        return new ObjectValueWithVariable(Map.of(k1, v1, k2, v2));
    }

    public static <K, V> ObjectValueWithVariable of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return new ObjectValueWithVariable(Map.of(k1, v1, k2, v2, k3, v3));
    }

    public static <K, V> ObjectValueWithVariable of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new ObjectValueWithVariable(Map.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    public static <K, V> ObjectValueWithVariable of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new ObjectValueWithVariable(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    public static <K, V> ObjectValueWithVariable of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return new ObjectValueWithVariable(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
    }

    public static <K, V> ObjectValueWithVariable of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return new ObjectValueWithVariable(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7));
    }

    public static <K, V> ObjectValueWithVariable of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return new ObjectValueWithVariable(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8));
    }

    public static <K, V> ObjectValueWithVariable of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return new ObjectValueWithVariable(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9));
    }

    public static <K, V> ObjectValueWithVariable of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return new ObjectValueWithVariable(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10));
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("objectValueWithVariableDefinition");
        st.add("objectValueWithVariable", objectValueWithVariable);
        return st.render();
    }
}
