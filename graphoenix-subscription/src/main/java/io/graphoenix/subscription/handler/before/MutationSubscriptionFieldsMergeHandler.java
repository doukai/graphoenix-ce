package io.graphoenix.subscription.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.graphoenix.subscription.handler.SubscriptionFilterFieldsManager;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static io.graphoenix.core.handler.before.MutationFetchFieldsMergeHandler.MUTATION_FETCH_FIELDS_MERGE_HANDLER_PRIORITY;

@ApplicationScoped
@Priority(MutationSubscriptionFieldsMergeHandler.MUTATION_SUBSCRIPTION_FIELDS_MERGE_HANDLER_PRIORITY)
public class MutationSubscriptionFieldsMergeHandler implements OperationBeforeHandler {

    public static final int MUTATION_SUBSCRIPTION_FIELDS_MERGE_HANDLER_PRIORITY = MUTATION_FETCH_FIELDS_MERGE_HANDLER_PRIORITY + 100;

    private final DocumentManager documentManager;

    private final SubscriptionFilterFieldsManager subscriptionFilterFieldsManager;

    @Inject
    public MutationSubscriptionFieldsMergeHandler(DocumentManager documentManager, SubscriptionFilterFieldsManager subscriptionFilterFieldsManager) {
        this.documentManager = documentManager;
        this.subscriptionFilterFieldsManager = subscriptionFilterFieldsManager;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.just(
                operation.mergeSelection(
                        operation.getFields().stream()
                                .map(field -> {
                                            FieldDefinition fieldDefinition = operationType.getField(field.getName());
                                            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
                                            if (fieldTypeDefinition.isObject()) {
                                                return field.mergeSelection(subscriptionFilterFieldsManager.get(fieldTypeDefinition.getName()));
                                            } else {
                                                return field;
                                            }
                                        }
                                )
                                .collect(Collectors.toList())
                )
        );
    }
}
