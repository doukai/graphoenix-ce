package io.graphoenix.spi.error;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

public class ErrorInfo {

    private static final Map<Class<? extends Throwable>, Integer> codeMap = new HashMap<>();

    private static final Map<Class<? extends Throwable>, String> messageMap = new HashMap<>();

    public static final Integer UNKNOWN_CODE = -99999;

    public static final String UNKNOWN_MESSAGE = "Unknown error";

    static {
        ServiceLoader.load(ErrorInfoLoader.class, ErrorInfo.class.getClassLoader()).forEach(ErrorInfoLoader::load);
    }

    public static void put(Class<? extends Throwable> type, Integer code, String message) {
        codeMap.put(type, code);
        messageMap.put(type, message);
    }

    public static Integer getErrorCode(Class<? extends Throwable> type) {
        return Optional.ofNullable(codeMap.get(type)).orElse(UNKNOWN_CODE);
    }

    public static String getErrorMessage(Class<? extends Throwable> type) {
        return Optional.ofNullable(messageMap.get(type)).orElse(UNKNOWN_MESSAGE);
    }
}
