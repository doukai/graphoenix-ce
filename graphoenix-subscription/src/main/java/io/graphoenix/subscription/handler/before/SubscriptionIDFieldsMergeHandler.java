package io.graphoenix.subscription.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
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
@Priority(SubscriptionIDFieldsMergeHandler.SUBSCRIPTION_ID_FIELDS_MERGE_HANDLER_PRIORITY)
public class SubscriptionIDFieldsMergeHandler implements OperationBeforeHandler {

    public static final int SUBSCRIPTION_ID_FIELDS_MERGE_HANDLER_PRIORITY = CONNECTION_SPLITTER_PRIORITY + 175;

    private final DocumentManager documentManager;

    @Inject
    public SubscriptionIDFieldsMergeHandler(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Override
    public Mono<Operation> subscription(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.just(
                operation
                        .mergeSelection(
                                operation.getFields().stream()
                                        .flatMap(field -> mergeIDField(operationType.getFieldOrError(field.getName()), field))
                                        .collect(Collectors.toList())
                        )
        );
    }

    private Stream<Field> mergeIDField(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldDefinition.isConnectionField()) {
            return Stream.of(
                    field.mergeSelection(
                            Stream
                                    .concat(
                                            fieldTypeDefinition.asObject().getIDField()
                                                    .map(idFieldDefinition ->
                                                            (Field) new Field(idFieldDefinition.getName())
                                                                    .addDirective(new Directive(DIRECTIVE_HIDE_NAME))
                                                    )
                                                    .stream(),
                                            Stream.ofNullable(field.getFields())
                                                    .flatMap(Collection::stream)
                                                    .flatMap(subField -> mergeIDField(fieldTypeDefinition.asObject().getFieldOrError(subField.getName()), subField))
                                    )
                                    .collect(Collectors.toList())
                    )
            );
        }
        return Stream.of(field);
    }
}
