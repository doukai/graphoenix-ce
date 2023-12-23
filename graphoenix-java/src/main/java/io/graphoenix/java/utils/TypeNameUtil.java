package io.graphoenix.java.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.Arrays;

import static io.graphoenix.core.utils.TypeNameUtil.getArgumentTypeNames;
import static io.graphoenix.core.utils.TypeNameUtil.getClassName;

public final class TypeNameUtil {

    public static TypeName toTypeName(String typeName) {
        String className = getClassName(typeName);
        String[] argumentTypeNames = getArgumentTypeNames(typeName);
        if (argumentTypeNames.length > 0) {
            return ParameterizedTypeName.get(toClassName(className), Arrays.stream(argumentTypeNames).map(TypeNameUtil::toTypeName).toArray(TypeName[]::new));
        } else {
            int i = typeName.lastIndexOf(".");
            if (i == -1) {
                switch (typeName) {
                    case "void":
                        return TypeName.VOID;
                    case "boolean":
                        return TypeName.BOOLEAN;
                    case "byte":
                        return TypeName.BYTE;
                    case "short":
                        return TypeName.SHORT;
                    case "int":
                        return TypeName.INT;
                    case "long":
                        return TypeName.LONG;
                    case "char":
                        return TypeName.CHAR;
                    case "float":
                        return TypeName.FLOAT;
                    case "double":
                        return TypeName.DOUBLE;
                    default:
                        return ClassName.bestGuess(typeName);
                }
            } else {
                return toClassName(typeName);
            }
        }
    }

    public static ClassName toClassName(String typeName) {
        int i = typeName.lastIndexOf(".");
        if (i == -1) {
            switch (typeName) {
                case "void":
                    return (ClassName) TypeName.VOID.box();
                case "boolean":
                    return (ClassName) TypeName.BOOLEAN.box();
                case "byte":
                    return (ClassName) TypeName.BYTE.box();
                case "short":
                    return (ClassName) TypeName.SHORT.box();
                case "int":
                    return (ClassName) TypeName.INT.box();
                case "long":
                    return (ClassName) TypeName.LONG.box();
                case "char":
                    return (ClassName) TypeName.CHAR.box();
                case "float":
                    return (ClassName) TypeName.FLOAT.box();
                case "double":
                    return (ClassName) TypeName.DOUBLE.box();
                default:
                    return ClassName.bestGuess(typeName);
            }
        } else {
            return ClassName.get(typeName.substring(0, i), typeName.substring(i + 1));
        }
    }
}
