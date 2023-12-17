package io.graphoenix.core.utils;

import static io.graphoenix.spi.constant.Hammurabi.PREFIX_INTROSPECTION;

public final class NameUtil {

    public static String getGrpcTypeName(String name) {
        return name.replaceFirst(PREFIX_INTROSPECTION, "Intro");
    }
}
