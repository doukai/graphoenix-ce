package io.graphoenix.java.utils;

import com.google.common.base.CaseFormat;

import javax.lang.model.SourceVersion;

import static io.graphoenix.spi.constant.Hammurabi.PREFIX_INTROSPECTION;

public final class NameUtil {

    public static String getFieldGetterMethodName(String fieldName) {
        boolean isKeyword = SourceVersion.isKeyword(fieldName);
        if (isKeyword) {
            fieldName = "_" + fieldName;
        }
        if (fieldName.startsWith(PREFIX_INTROSPECTION)) {
            return "get" + fieldName;
        } else {
            return "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
        }
    }

    public static String getFieldSetterMethodName(String fieldName) {
        boolean isKeyword = SourceVersion.isKeyword(fieldName);
        if (isKeyword) {
            fieldName = "_" + fieldName;
        }
        if (fieldName.startsWith(PREFIX_INTROSPECTION)) {
            return "set" + fieldName;
        } else {
            return "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
        }
    }

    public static String getFieldName(String fieldName) {
        if (fieldName.startsWith("get")) {
            fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, fieldName.replaceFirst("get", ""));
        } else if (fieldName.startsWith("set")) {
            fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, fieldName.replaceFirst("set", ""));
        }
        boolean isKeyword = SourceVersion.isKeyword(fieldName);
        if (isKeyword) {
            fieldName = "_" + fieldName;
        }
        return fieldName;
    }
}
