package io.graphoenix.spi.utils;

import com.google.common.base.CaseFormat;

import java.util.Arrays;
import java.util.stream.Collectors;

import static io.graphoenix.spi.constant.Hammurabi.*;

public final class NameUtil {

    public static String typeNameToFieldName(String name) {
        if (name.startsWith(PREFIX_INTROSPECTION)) {
            return PREFIX_INTROSPECTION + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name.replace(PREFIX_INTROSPECTION, ""));
        } else {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
        }
    }

    public static String getRelationTypeName(String... typeNames) {
        String[] upperCamelTypeNames = Arrays.stream(typeNames).map(name -> CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name)).toArray(String[]::new);
        Arrays.sort(upperCamelTypeNames);
        return String.join("", upperCamelTypeNames) + SUFFIX_RELATION;
    }

    public static String getTypeRefFieldName(String typeName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, typeName) + SUFFIX_REF;
    }
}
