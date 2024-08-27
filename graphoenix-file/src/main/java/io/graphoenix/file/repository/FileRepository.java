package io.graphoenix.file.repository;

import io.graphoenix.file.dto.annotation.FileMutationArguments;
import io.graphoenix.file.dto.annotation.Mutation;
import io.graphoenix.file.dto.inputObjectType.FileInput;
import io.graphoenix.file.dto.objectType.File;
import io.graphoenix.spi.annotation.Operation;
import io.graphoenix.spi.annotation.SelectionSet;
import reactor.core.publisher.Mono;

@Operation
public interface FileRepository {

    @Mutation(file = @FileMutationArguments($input = "fileInput"))
    @SelectionSet("{ id }")
    Mono<File> insertFile(FileInput fileInput);
}
