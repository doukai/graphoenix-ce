package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.MutationBeforeHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.DIRECTIVE_HIDE_NAME;
import static io.graphoenix.spi.constant.Hammurabi.SUFFIX_INPUT;

@ApplicationScoped
@Priority(100)
public class MutationFieldsMergeHandler implements MutationBeforeHandler {

    private final DocumentManager documentManager;

    @Inject
    public MutationFieldsMergeHandler(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.just(
                operation
                        .mergeSelection(
                                operation.getFields().stream()
                                        .map(field -> buildFetch(operationType.getField(field.getName()), field))
                                        .collect(Collectors.toList())
                        )
        );
    }

    private Field buildFetch(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            return field.mergeSelection(
                    Stream.ofNullable(fieldDefinition.getArguments())
                            .flatMap(Collection::stream)
                            .filter(inputValue -> inputValue.getName().endsWith(SUFFIX_INPUT))
                            .flatMap(inputValue ->
                                    Stream.ofNullable(fieldTypeDefinition.asObject().getField(inputValue.getName()))
                                            .flatMap(subFieldDefinition -> buildFetch(subFieldDefinition, inputValue))
                            )
                            .collect(Collectors.toList())
            );
        } else {
            return field;
        }
    }

    private Stream<Field> buildFetch(FieldDefinition fieldDefinition, InputValue inputValue) {
        if (fieldDefinition.isFetchField()) {
            return Stream.of(new Field(fieldDefinition.getFetchFromOrError()).addDirective(new Directive(DIRECTIVE_HIDE_NAME)));
        } else {
            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
            if (fieldTypeDefinition.isObject()) {
                List<Field> fieldList = documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                        .filter(subInputValue -> subInputValue.getName().endsWith(SUFFIX_INPUT))
                        .flatMap(subInputValue ->
                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                        .flatMap(subFieldDefinition -> buildFetch(subFieldDefinition, subInputValue))
                        )
                        .collect(Collectors.toList());
                if (fieldList.isEmpty()) {
                    return Stream.empty();
                } else {
                    return Stream.of(new Field(fieldDefinition.getName()).setSelections(fieldList).addDirective(new Directive(DIRECTIVE_HIDE_NAME)));
                }
            }
        }
        return Stream.empty();
    }
}
