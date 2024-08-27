package io.graphoenix.file.local.handler;

import io.graphoenix.file.config.FileConfig;
import io.graphoenix.file.dto.inputObjectType.FileInput;
import io.graphoenix.file.dto.objectType.File;
import io.graphoenix.file.repository.FileRepository;
import io.graphoenix.spi.handler.FileSaveHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@ApplicationScoped
public class LocalFileSaveHandler implements FileSaveHandler {

    private final FileRepository fileRepository;
    private final FileConfig fileConfig;

    @Inject
    public LocalFileSaveHandler(FileRepository fileRepository, FileConfig fileConfig) {
        this.fileConfig = fileConfig;
        this.fileRepository = fileRepository;
    }

    @Override
    public Mono<String> save(byte[] data, String filename, String contentType) {
        String id = UUID.randomUUID().toString();
        try {
            Path path = Paths.get(fileConfig.getPath());
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path filePath = Files.createFile(path.resolve(id));
            try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
                outputStream.write(data);
                outputStream.close();
                FileInput fileInput = new FileInput();
                fileInput.setId(id);
                fileInput.setName(filename);
                fileInput.setContentType(contentType);
                return fileRepository.insertFile(fileInput)
                        .map(File::getId);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
