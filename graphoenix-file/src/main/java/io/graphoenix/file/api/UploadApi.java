package io.graphoenix.file.api;

import io.graphoenix.file.dto.objectType.File;
import io.graphoenix.file.repository.FileRepository;
import io.graphoenix.spi.annotation.TypeName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.graphoenix.spi.constant.Hammurabi.SCALA_UPLOAD_NAME;

@ApplicationScoped
@GraphQLApi
public class UploadApi {

    private final FileRepository fileRepository;

    @Inject
    public UploadApi(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Mutation
    public Mono<File> singleUpload(@TypeName(SCALA_UPLOAD_NAME) String file) {
        return fileRepository.getFileById(file);
    }

    @Mutation
    public Mono<List<File>> multipleUpload(List<@TypeName(SCALA_UPLOAD_NAME) String> files) {
        return fileRepository.selectFileListByIdList(files);
    }
}
