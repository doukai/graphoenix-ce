package io.graphoenix.core.utils;

import com.google.common.base.CaseFormat;

import java.util.Arrays;

import static io.graphoenix.spi.constant.Hammurabi.*;

public final class NameUtil {

    public static String getGrpcTypeName(String name) {
        return name.replaceFirst(PREFIX_INTROSPECTION, PREFIX_GRPC_INTROSPECTION);
    }

    public static String typeNameToFieldName(String name) {
        if (name.startsWith(PREFIX_INTROSPECTION)) {
            return PREFIX_INTROSPECTION + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name.replace(PREFIX_INTROSPECTION, ""));
        } else {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
        }
    }

    public static String getRelationTypeName(String... typeNames) {
        Arrays.sort(typeNames);
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, String.join("_", typeNames)) + SUFFIX_RELATION;
    }

    public static String getTypeRefFieldName(String typeName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, typeName) + SUFFIX_REF;
    }
}
