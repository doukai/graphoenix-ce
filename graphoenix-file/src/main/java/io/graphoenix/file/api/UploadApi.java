package io.graphoenix.file.api;

import io.graphoenix.file.dto.objectType.File;
import io.graphoenix.file.repository.FileRepository;
import io.graphoenix.spi.annotation.TypeName;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import reactor.core.publisher.Mono;

import java.util.List;

@GraphQLApi
public class UploadApi {

    private final FileRepository fileRepository;

    @Inject
    public UploadApi(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Mutation
    public Mono<File> singleUpload(@TypeName("Upload") String file) {
        return fileRepository.getFileById(file);
    }

    @Mutation
    public Mono<List<File>> multipleUpload(List<@TypeName("Upload") String> files) {
        return fileRepository.selectFileListByIdList(files);
    }
}
