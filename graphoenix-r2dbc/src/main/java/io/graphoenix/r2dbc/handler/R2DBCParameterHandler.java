package io.graphoenix.r2dbc.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class R2DBCParameterHandler {

    private final Jsonb jsonb;

    private final JsonProvider jsonProvider;

    @Inject
    public R2DBCParameterHandler(Jsonb jsonb, JsonProvider jsonProvider) {
        this.jsonb = jsonb;
        this.jsonProvider = jsonProvider;
    }

    public Map<String, Object> process(Map<String, Object> parameters) {
        return parameters.entrySet().stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), processValue(v.getValue())), HashMap::putAll);
    }

    @SuppressWarnings("unchecked")
    private Object processValue(Object value) {
        if (value == null) {
            return null;
        } else if (value.getClass().isPrimitive() ||
                value instanceof String ||
                value instanceof Character ||
                value instanceof Number ||
                value instanceof Boolean ||
                value instanceof LocalDateTime ||
                value instanceof LocalDate ||
                value instanceof LocalTime) {
            return value;
        } else if (value.getClass().isEnum()) {
            return ((Enum<?>) value).name();
        } else if (value instanceof JsonValue) {
            return value.toString();
        } else if (value instanceof Map) {
            return toJsonValue(value).toString();
        } else if (value instanceof Collection) {
            return toJsonValue(value).toString();
        } else {
            return jsonb.toJson(value);
        }
    }

    @SuppressWarnings("unchecked")
    private JsonValue toJsonValue(Object value) {
        if (value == null) {
            return JsonValue.NULL;
        } else if (value instanceof Integer) {
            return jsonProvider.createValue((Integer) value);
        } else if (value instanceof Long) {
            return jsonProvider.createValue((Long) value);
        } else if (value instanceof Double) {
            return jsonProvider.createValue((Double) value);
        } else if (value instanceof Float) {
            return jsonProvider.createValue((Float) value);
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? JsonValue.TRUE : JsonValue.FALSE;
        } else if (value instanceof String) {
            return jsonProvider.createValue((String) value);
        } else if (value instanceof BigDecimal) {
            return jsonProvider.createValue((BigDecimal) value);
        } else if (value instanceof BigInteger) {
            return jsonProvider.createValue((BigInteger) value);
        } else if (value instanceof Number) {
            return jsonProvider.createValue((Number) value);
        } else if (value instanceof LocalDateTime ||
                value instanceof LocalDate ||
                value instanceof LocalTime) {
            return jsonProvider.createValue(value.toString());
        } else if (value.getClass().isEnum()) {
            return jsonProvider.createValue(((Enum<?>) value).name());
        } else if (value instanceof JsonValue) {
            return (JsonValue) value;
        } else if (value instanceof Map) {
            return ((Map<String, ?>) value).entrySet().stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), toJsonValue(entry.getValue())))
                    .collect(JsonCollectors.toJsonObject());
        } else if (value instanceof Collection) {
            return ((Collection<?>) value).stream()
                    .map(this::toJsonValue)
                    .collect(JsonCollectors.toJsonArray());
        } else {
            return jsonProvider.createReader(new StringReader(jsonb.toJson(value))).readValue();
        }
    }
}
