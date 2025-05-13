package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchBeforeHandler;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.ConnectionSplitter.CONNECTION_SPLITTER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.DIRECTIVE_HIDE_NAME;
import static io.graphoenix.spi.constant.Hammurabi.SUFFIX_INPUT;

@ApplicationScoped
@Priority(MutationFetchFieldsMergeHandler.MUTATION_FETCH_FIELDS_MERGE_HANDLER_PRIORITY)
public class MutationFetchFieldsMergeHandler implements OperationBeforeHandler, FetchBeforeHandler {

    public static final int MUTATION_FETCH_FIELDS_MERGE_HANDLER_PRIORITY = CONNECTION_SPLITTER_PRIORITY + 150;

    private final DocumentManager documentManager;

    @Inject
    public MutationFetchFieldsMergeHandler(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
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
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            return Stream.of(
                    field.mergeSelection(
                            Stream.ofNullable(fieldDefinition.getArguments())
                                    .flatMap(Collection::stream)
                                    .filter(inputValue -> inputValue.getType().getTypeName().getName().endsWith(SUFFIX_INPUT))
                                    .flatMap(inputValue ->
                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(inputValue.getName()))
                                                    .flatMap(subFieldDefinition ->
                                                            Stream.ofNullable(field.getArguments())
                                                                    .flatMap(arguments ->
                                                                            arguments.getArgumentOrEmpty(inputValue.getName())
                                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                    )
                                                                    .flatMap(valueWithVariable -> buildFetch(subFieldDefinition, inputValue, valueWithVariable))
                                                    )
                                    )
                                    .collect(Collectors.toList())
                    )
            );
        } else {
            return Stream.of(field);
        }
    }

    private Stream<Field> buildFetch(FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        if (fieldDefinition.isFetchField()) {
            return Stream.of(new Field(fieldDefinition.getFetchFromOrError()).addDirective(new Directive(DIRECTIVE_HIDE_NAME)));
        } else {
            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
            if (fieldTypeDefinition.isObject()) {
                List<Field> fieldList = documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                        .filter(subInputValue -> subInputValue.getType().getTypeName().getName().endsWith(SUFFIX_INPUT))
                        .flatMap(subInputValue ->
                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                        .flatMap(subFieldDefinition ->
                                                Stream.ofNullable(valueWithVariable)
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(objectValueWithVariable ->
                                                                objectValueWithVariable.getValueWithVariableOrEmpty(subFieldDefinition.getName()).stream()
                                                                        .flatMap(subValueWithVariable -> buildFetch(subFieldDefinition, subInputValue, subValueWithVariable))
                                                        )
                                        )
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
