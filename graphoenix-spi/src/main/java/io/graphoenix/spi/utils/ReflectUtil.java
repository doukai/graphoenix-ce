package io.graphoenix.spi.utils;


import java.lang.reflect.Field;

public final class ReflectUtil {

    public static Object getFieldValue(Object object, Field field) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
