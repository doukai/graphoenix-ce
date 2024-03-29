package io.graphoenix.http.server.error;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

public class HttpErrorStatus {

    private static final Map<String, Integer> statusMap = new HashMap<>();

    static {
        ServiceLoader.load(HttpErrorStatusLoader.class, HttpErrorStatus.class.getClassLoader()).forEach(HttpErrorStatusLoader::load);
    }

    public static void put(Class<? extends Throwable> type, Integer status) {
        statusMap.put(type.getName(), status);
    }

    public static HttpResponseStatus getStatus(Class<? extends Throwable> type) {
        return Optional.ofNullable(statusMap.get(type.getName()))
                .map(HttpResponseStatus::valueOf)
                .orElse(HttpResponseStatus.BAD_REQUEST);
    }
}
