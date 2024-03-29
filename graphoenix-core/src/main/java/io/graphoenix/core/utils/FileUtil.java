package io.graphoenix.core.utils;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public final class FileUtil {

    public static <T> String fileToString(Class<T> beanClass, String fileName) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(beanClass.getResourceAsStream(fileName)), Charsets.UTF_8)) {
            return CharStreams.toString(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
