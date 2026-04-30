package io.graphoenix.file.repository;

import io.graphoenix.core.dto.annotation.StringExpression;
import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.file.dto.annotation.FileExpression;
import io.graphoenix.file.dto.annotation.Mutation;
import io.graphoenix.file.dto.annotation.Query;
import io.graphoenix.file.dto.inputObjectType.FileInput;
import io.graphoenix.file.dto.objectType.File;
import io.graphoenix.spi.annotation.GraphQLOperation;
import io.graphoenix.spi.annotation.SelectionSet;
import reactor.core.publisher.Mono;

import java.util.List;

@GraphQLOperation
public interface FileRepository {

  @Query(file = @FileExpression(id = @StringExpression($val = "id")))
  Mono<File> getFileById(String id);

  @Query(fileList = @FileExpression(id = @StringExpression(opr = Operator.IN, $arr = "idList")))
  Mono<List<File>> selectFileListByIdList(List<String> idList);

  @Mutation(file = @io.graphoenix.file.dto.annotation.FileInput($input = "fileInput"))
  @SelectionSet("{ id }")
  Mono<File> insertFile(FileInput fileInput);
}
