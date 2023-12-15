package io.graphoenix.spi.utils;

import io.graphoenix.spi.error.ErrorInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import static io.graphoenix.spi.error.BaseErrorInfo.UNKNOWN_CODE;
import static io.graphoenix.spi.error.BaseErrorInfo.UNKNOWN_MESSAGE;

public final class ErrorInfoUtil {

    private static final Map<String, Integer> errorCodeMap = new HashMap<>();

    private static final Map<String, String> errorMessageMap = new HashMap<>();

    public static Integer getErrorCode(Class<? extends Throwable> type) {
        return errorCodeMap.computeIfAbsent(
                type.getCanonicalName(),
                (key) -> ServiceLoader.load(ErrorInfo.class, ErrorInfoUtil.class.getClassLoader()).stream()
                        .map(ServiceLoader.Provider::get)
                        .flatMap(errorInfo -> errorInfo.getCode(type).stream())
                        .findFirst()
                        .orElse(UNKNOWN_CODE)
        );
    }

    public static String getErrorMessage(Class<? extends Throwable> type) {
        return errorMessageMap.computeIfAbsent(
                type.getCanonicalName(),
                (key) -> ServiceLoader.load(ErrorInfo.class, ErrorInfoUtil.class.getClassLoader()).stream()
                        .map(ServiceLoader.Provider::get)
                        .flatMap(errorInfo -> errorInfo.getMessage(type).stream())
                        .findFirst()
                        .orElse(UNKNOWN_MESSAGE)
        );
    }
}
