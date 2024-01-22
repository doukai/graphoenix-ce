package io.graphoenix.core.utils;

import java.util.ArrayList;
import java.util.Arrays;

public final class TypeNameUtil {

    public static String getClassName(String typeName) {
        if (typeName.contains("<")) {
            int index = typeName.indexOf('<');
            return typeName.substring(0, index);
        } else {
            return typeName;
        }
    }

    public static String[] getArgumentTypeNames(String typeName) {
        if (typeName.contains("<")) {
            int index = typeName.indexOf('<');
            String argumentTypeNames = typeName.substring(index + 1, typeName.length() - 1);
            if (argumentTypeNames.contains(",")) {
                return Arrays.stream(argumentTypeNames.split(","))
                        .map(String::trim)
                        .reduce(new ArrayList<String>(),
                                (argumentTypeNameList, argumentClassName) -> {
                                    if (!argumentTypeNameList.isEmpty()) {
                                        int lastIndex = argumentTypeNameList.size() - 1;
                                        String lastArgumentClassName = argumentTypeNameList.get(lastIndex);
                                        if (lastArgumentClassName.contains("<") && !lastArgumentClassName.contains(">")) {
                                            argumentTypeNameList.set(lastIndex, lastArgumentClassName + "," + argumentClassName);
                                        } else {
                                            argumentTypeNameList.add(argumentClassName);
                                        }
                                    } else {
                                        argumentTypeNameList.add(argumentClassName);
                                    }
                                    return argumentTypeNameList;
                                },
                                (x, y) -> y
                        )
                        .toArray(new String[]{});
            } else {
                return new String[]{argumentTypeNames};
            }
        }
        return new String[]{};
    }

    public static String getArgumentTypeName0(String typeName) {
        String[] argumentTypeNames = getArgumentTypeNames(typeName);
        if (argumentTypeNames.length == 0) {
            throw new RuntimeException("argument type not exist in " + typeName);
        }
        return argumentTypeNames[0];
    }
}
