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

import static io.graphoenix.spi.constant.Hammurabi.DIRECTIVE_HIDE_NAME;

@ApplicationScoped
@Priority(575)
public class SubscriptionIDFieldsMergeHandler implements OperationBeforeHandler {

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
                                        .flatMap(field -> mergeIDField(operationType.getField(field.getName()), field))
                                        .collect(Collectors.toList())
                        )
        );
    }

    private Stream<Field> mergeIDField(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            return Stream.of(
                    field.mergeSelection(
                            Stream
                                    .concat(
                                            Stream.of(new Field(fieldTypeDefinition.asObject().getIDFieldOrError().getName()).addDirective(new Directive(DIRECTIVE_HIDE_NAME))),
                                            Stream.ofNullable(field.getFields())
                                                    .flatMap(Collection::stream)
                                                    .flatMap(subField -> mergeIDField(fieldTypeDefinition.asObject().getField(subField.getName()), subField))
                                    )
                                    .collect(Collectors.toList())
                    )
            );
        }
        return Stream.of(field);
    }
}
