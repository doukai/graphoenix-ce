package io.graphoenix.file.repository;

import io.graphoenix.core.dto.annotation.StringExpression;
import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.file.dto.annotation.*;
import io.graphoenix.file.dto.inputObjectType.FileInput;
import io.graphoenix.file.dto.objectType.File;
import io.graphoenix.spi.annotation.GraphQLOperation;
import io.graphoenix.spi.annotation.SelectionSet;
import reactor.core.publisher.Mono;

import java.util.List;

@GraphQLOperation
public interface FileRepository {

    @Query(file = @FileQueryArguments(id = @StringExpression($val = "id")))
    @SelectionSet("{ id name contentType content url }")
    Mono<File> getFileById(String id);

    @Query(fileList = @FileListQueryArguments(id = @StringExpression(opr = Operator.IN, $arr = "idList")))
    @SelectionSet("{ id name contentType content url }")
    Mono<List<File>> selectFileListByIdList(List<String> idList);

    @Mutation(file = @FileMutationArguments($input = "fileInput"))
    @SelectionSet("{ id }")
    Mono<File> insertFile(FileInput fileInput);
}
