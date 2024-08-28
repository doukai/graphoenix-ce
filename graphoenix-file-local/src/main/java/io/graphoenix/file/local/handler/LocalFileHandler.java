package io.graphoenix.file.local.handler;

import io.graphoenix.file.config.FileConfig;
import io.graphoenix.file.dto.inputObjectType.FileInput;
import io.graphoenix.file.dto.objectType.File;
import io.graphoenix.file.repository.FileRepository;
import io.graphoenix.spi.dto.FileInfo;
import io.graphoenix.spi.handler.FileHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@ApplicationScoped
public class LocalFileHandler implements FileHandler {

    private final FileRepository fileRepository;
    private final Path path;

    @Inject
    public LocalFileHandler(FileRepository fileRepository, FileConfig fileConfig) {
        this.fileRepository = fileRepository;
        this.path = Paths.get(fileConfig.getPath());
        if (!Files.exists(this.path)) {
            try {
                Files.createDirectories(this.path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Mono<String> save(FileInfo fileInfo) {
        String id = UUID.randomUUID().toString();
        try {
            Path filePath = Files.createFile(path.resolve(id));
            try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
                outputStream.write(fileInfo.getData());
                FileInput fileInput = new FileInput();
                fileInput.setId(id);
                fileInput.setName(fileInfo.getFilename());
                fileInput.setContentType(fileInfo.getContentType());
                return fileRepository.insertFile(fileInput)
                        .map(File::getId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<FileInfo> get(String id) {
        return fileRepository.getFileById(id)
                .map(file -> {
                            Path filePath = path.resolve(id);
                            try (FileInputStream inputStream = new FileInputStream(filePath.toFile())) {
                                FileInfo fileInfo = new FileInfo();
                                fileInfo.setData(inputStream.readAllBytes());
                                fileInfo.setFilename(file.getName());
                                fileInfo.setContentType(file.getContentType());
                                return fileInfo;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }
}
