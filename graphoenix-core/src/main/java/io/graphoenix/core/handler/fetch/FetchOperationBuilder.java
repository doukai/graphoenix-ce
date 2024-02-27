package io.graphoenix.core.handler.fetch;

import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.stream.JsonCollectors;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

@ApplicationScoped
public class FetchOperationBuilder {

    public Operation buildQuery(List<QueryFetchField> queryFetchFieldList) {
        return new Operation()
                .setOperationType(OPERATION_QUERY_NAME)
                .setSelections(
                        queryFetchFieldList.stream()
                                .filter(queryFetchField -> !queryFetchField.getPatcher().isValuePatcher())
                                .map(QueryFetchField::getField)
                                .collect(Collectors.toList())
                );
    }

    public Operation buildMutation(List<MutationFetchInput> mutationFetchInputList) {
        return new Operation()
                .setOperationType(OPERATION_MUTATION_NAME)
                .setSelections(
                        mutationFetchInputList.stream()
                                .collect(
                                        Collectors.groupingBy(
                                                MutationFetchInput::getTypeName,
                                                Collectors.toList()
                                        )
                                )
                                .entrySet().stream()
                                .map(entry ->
                                        new Field(typeNameToFieldName(entry.getKey()) + SUFFIX_LIST)
                                                .setArguments(
                                                        Map.of(
                                                                INPUT_VALUE_LIST_NAME,
                                                                entry.getValue().stream()
                                                                        .filter(distinctByKey(MutationFetchInput::getId))
                                                                        .map(MutationFetchInput::getJsonValue)
                                                                        .collect(JsonCollectors.toJsonArray())
                                                        )
                                                )
                                                .mergeSelection(
                                                        entry.getValue().stream()
                                                                .filter(mutationFetchInput -> mutationFetchInput.getFieldArgumentsPatcher().getTarget() != null)
                                                                .map(mutationFetchInput -> new Field(mutationFetchInput.getFieldArgumentsPatcher().getTarget()))
                                                                .collect(Collectors.toList())
                                                )
                                )
                                .collect(Collectors.toList())
                );
    }
}
