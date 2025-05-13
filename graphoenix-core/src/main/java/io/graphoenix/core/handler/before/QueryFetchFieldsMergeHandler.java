package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchBeforeHandler;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.ConnectionSplitter.CONNECTION_SPLITTER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.DIRECTIVE_HIDE_NAME;

@ApplicationScoped
@Priority(QueryFetchFieldsMergeHandler.QUERY_FETCH_FIELDS_MERGE_HANDLER_PRIORITY)
public class QueryFetchFieldsMergeHandler implements OperationBeforeHandler, FetchBeforeHandler {

    public static final int QUERY_FETCH_FIELDS_MERGE_HANDLER_PRIORITY = CONNECTION_SPLITTER_PRIORITY + 100;

    private final DocumentManager documentManager;

    @Inject
    public QueryFetchFieldsMergeHandler(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Override
    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.just(
                operation
                        .mergeSelection(
                                operation.getFields().stream()
                                        .flatMap(field -> buildFetch(operationType.getField(field.getName()), field))
                                        .collect(Collectors.toList())
                        )
        );
    }

    private Stream<Field> buildFetch(FieldDefinition fieldDefinition, Field field) {
        if (fieldDefinition == null) {
            return Stream.empty();
        }
        if (fieldDefinition.isFetchField()) {
            return Stream.of(field, new Field(fieldDefinition.getFetchFromOrError()).addDirective(new Directive(DIRECTIVE_HIDE_NAME)));
        } else {
            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
            if (fieldTypeDefinition.isObject()) {
                return Stream.of(
                        field.mergeSelection(
                                Stream.ofNullable(field.getFields())
                                        .flatMap(Collection::stream)
                                        .flatMap(subField -> buildFetch(fieldTypeDefinition.asObject().getField(subField.getName()), subField))
                                        .collect(Collectors.toList())
                        )
                );
            }
        }
        return Stream.of(field);
    }
}
