package io.graphoenix.subscription.handler;

import io.graphoenix.spi.graphql.operation.Field;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@ApplicationScoped
public class SubscriptionFilterFieldsManager {

    private final Map<String, List<Field>> typeFilterFieldListMap = new ConcurrentHashMap<>();

    public List<Field> get(String typeName) {
        return typeFilterFieldListMap.get(typeName);
    }

    public List<Field> merge(String key, List<Field> value, BiFunction<? super List<Field>, ? super List<Field>, ? extends List<Field>> remappingFunction) {
        return typeFilterFieldListMap.merge(key, value, remappingFunction);
    }
}
