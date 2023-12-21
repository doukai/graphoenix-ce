package io.graphoenix.core.utils;

import org.tinylog.Logger;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public final class FilerUtil {

    public static Path getGeneratedSourcePath(Filer filer) {
        try {
            FileObject tmp = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", UUID.randomUUID().toString());
            Writer writer = tmp.openWriter();
            writer.write("");
            writer.close();
            Path path = Paths.get(tmp.toUri());
            Files.deleteIfExists(path);
            Path generatedSourcePath = path.getParent();
            Logger.info("generated source path: {}", generatedSourcePath.toString());
            return generatedSourcePath;
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public static Path getResourcesPath(Filer filer) {
        Path sourcePath = Objects.requireNonNull(getGeneratedSourcePath(filer)).getParent().getParent().getParent().getParent().getParent().getParent().resolve("src/main/resources");
        Logger.info("resources path: {}", sourcePath.toString());
        return sourcePath;
    }

    public static Path getTestResourcesPath(Filer filer) {
        Path sourcePath = Objects.requireNonNull(getGeneratedSourcePath(filer)).getParent().getParent().getParent().getParent().getParent().getParent().resolve("src/test/resources");
        Logger.info("test resources path: {}", sourcePath.toString());
        return sourcePath;
    }


    public static InputStream getResource(Filer filer, String resourceName) {
        try {
            return filer.getResource(StandardLocation.SOURCE_PATH, "", resourceName).openInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
