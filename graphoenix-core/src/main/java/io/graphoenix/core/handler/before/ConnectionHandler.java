package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
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

import java.util.Map;

@ApplicationScoped
@Priority(500)
public class ConnectionHandler implements OperationBeforeHandler {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;

    @Inject
    public ConnectionHandler(DocumentManager documentManager, PackageManager packageManager) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
    }

    @Override
    public Mono<Operation> query(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    @Override
    public Mono<Operation> subscription(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.just(
                operation
                        .setSelections(
                                operation.getFields().stream()
                                        .filter(field -> packageManager.isLocalPackage(operationType.getField(field.getName())))
                                        .filter(field -> !operationType.getField(field.getName()).isInvokeField())
                                        .flatMap(field -> {
                                                    if (operationType.getField(field.getName()).isConnectionField()) {

                                                    } else {
                                                        return field;
                                                    }
                                                }
                                        )
                        )
        );
    }

    private Field buildListField(ObjectType objectType, FieldDefinition fieldDefinition, Field field){

    }

    private Field buildAggField(ObjectType objectType, FieldDefinition fieldDefinition, Field field){

    }
}
