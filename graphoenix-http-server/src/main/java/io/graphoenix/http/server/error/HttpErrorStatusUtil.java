package io.graphoenix.http.server.error;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public final class HttpErrorStatusUtil {

    private static final Map<String, HttpResponseStatus> errorStatusMap = new HashMap<>();

    public static HttpResponseStatus getStatus(Class<? extends Throwable> type) {
        return errorStatusMap.computeIfAbsent(
                type.getCanonicalName(),
                (key) -> ServiceLoader.load(HttpErrorStatus.class, HttpErrorStatusUtil.class.getClassLoader()).stream()
                        .map(ServiceLoader.Provider::get)
                        .flatMap(httpErrorStatus -> httpErrorStatus.getStatus(type).stream())
                        .findFirst()
                        .map(HttpResponseStatus::valueOf)
                        .orElse(HttpResponseStatus.BAD_REQUEST)
        );
    }
}
