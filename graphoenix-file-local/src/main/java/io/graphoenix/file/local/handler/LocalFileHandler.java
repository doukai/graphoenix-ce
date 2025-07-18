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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public Mono<String> save(byte[] data, FileInfo fileInfo) {
        FileInput fileInput = new FileInput();
        fileInput.setName(fileInfo.getFilename());
        fileInput.setContentType(fileInfo.getContentType());
        return fileRepository.insertFile(fileInput)
                .doOnSuccess(file -> {
                            try {
                                Path filePath = Files.createFile(path.resolve(file.getId()));
                                try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
                                    outputStream.write(data);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .map(File::getId);
    }

    @Override
    public Mono<FileInfo> getFileInfo(String id) {
        return fileRepository.getFileById(id)
                .map(file -> new FileInfo(file.getName(), file.getContentType()));
    }

    @Override
    public InputStream get(String id) {
        Path filePath = path.resolve(id);
        try {
            return new FileInputStream(filePath.toFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
