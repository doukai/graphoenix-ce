package io.graphoenix.spi.utils;

import com.google.common.base.CaseFormat;

import java.util.Arrays;

import static io.graphoenix.spi.constant.Hammurabi.*;

public final class NameUtil {

    public static String typeNameToFieldName(String name) {
        if (name.startsWith(PREFIX_INTROSPECTION)) {
            return PREFIX_INTROSPECTION + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name.replace(PREFIX_INTROSPECTION, ""));
        }
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
    }

    public static String getRelationTypeName(String... typeNames) {
        Arrays.sort(typeNames);
        return String.join("", typeNames) + SUFFIX_RELATION;
    }

    public static String getFieldRelationTypeName(String typeName, String fieldName) {
        return typeName + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName) + SUFFIX_RELATION;
    }

    public static String getTypeRefFieldName(String typeName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, typeName) + SUFFIX_REF;
    }

    public static String getAliasFromPath(String path) {
        return path.replaceAll("/", "_");
    }

    public static String packageNameToUnderline(String packageName) {
        return String.join("_", packageName.split("\\."));
    }

    public static String getGrpcName(String name) {
        if (name.startsWith(PREFIX_INTROSPECTION)) {
            return name.replaceFirst(PREFIX_INTROSPECTION, "Intro");
        }
        return name;
    }

    public static String getGrpcFieldName(String name) {
        if (name.startsWith(PREFIX_INTROSPECTION)) {
            return "intro" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name.replace(PREFIX_INTROSPECTION, ""));
        }
        return name;
    }

    public static String getGrpcEnumFieldName(String fieldName) {
        if (fieldName.startsWith(PREFIX_INTROSPECTION)) {
            return "INTRO_" + getGrpcEnumFieldName(fieldName.replaceFirst(PREFIX_INTROSPECTION, ""));
        }
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fieldName);
    }

    public static String getGrpcMessageFiledName(String fieldName) {
        if (fieldName.startsWith(PREFIX_INTROSPECTION)) {
            return "intro_" + getGrpcMessageFiledName(fieldName.replaceFirst(PREFIX_INTROSPECTION, ""));
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
    }

    public static String getGrpcServiceRpcName(String fieldName) {
        if (fieldName.startsWith(PREFIX_INTROSPECTION)) {
            return "Intro" + getGrpcServiceRpcName(fieldName.replaceFirst(PREFIX_INTROSPECTION, ""));
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
    }
}
