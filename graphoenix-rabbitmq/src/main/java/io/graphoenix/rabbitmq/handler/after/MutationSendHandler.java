package io.graphoenix.rabbitmq.handler.after;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Arguments;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationAfterHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

import java.util.Optional;

import static io.graphoenix.core.handler.after.SelectionHandler.SELECTION_HANDLER_PRIORITY;
import static io.graphoenix.rabbitmq.handler.RabbitMQSubscriptionHandler.*;
import static io.graphoenix.spi.constant.Hammurabi.*;

@ApplicationScoped
@Priority(MutationSendHandler.MUTATION_SEND_HANDLER_PRIORITY)
public class MutationSendHandler implements OperationAfterHandler {

    public static final int MUTATION_SEND_HANDLER_PRIORITY = SELECTION_HANDLER_PRIORITY - 150;

    private final DocumentManager documentManager;

    private final JsonProvider jsonProvider;

    private final Sender sender;

    @Inject
    public MutationSendHandler(DocumentManager documentManager, JsonProvider jsonProvider, Sender sender) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
        this.sender = sender;
    }

    @Override
    public Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        Flux<OutboundMessage> messageFlux = Flux.fromIterable(operation.getFields())
                .filter(field -> !operationType.getField(field.getName()).isInvokeField())
                .flatMap(field -> {
                            FieldDefinition fieldDefinition = operationType.getField(field.getName());
                            String packageName = fieldDefinition.getPackageNameOrError();
                            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
                            JsonValue fieldJsonValue = jsonValue.asJsonObject().get(Optional.ofNullable(field.getAlias()).orElseGet(field::getName));
                            if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer() && !fieldJsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
                                String typeName = fieldTypeDefinition.getName();
                                Arguments arguments = field.getArguments();
                                JsonObjectBuilder messageJsonObject = jsonProvider.createObjectBuilder().add(BODY_TYPE_KEY, typeName);
                                if (fieldDefinition.getType().hasList()) {
                                    if (arguments.containsKey(INPUT_VALUE_LIST_NAME)) {
                                        messageJsonObject
                                                .add(BODY_ARGUMENTS_KEY, jsonProvider.createArrayBuilder(arguments.get(INPUT_VALUE_LIST_NAME).asJsonArray()))
                                                .add(BODY_MUTATION_KEY, jsonProvider.createArrayBuilder(fieldJsonValue.asJsonArray()));
                                    } else {
                                        messageJsonObject
                                                .add(BODY_ARGUMENTS_KEY, jsonProvider.createArrayBuilder().add(jsonProvider.createObjectBuilder(arguments.asJsonObject())))
                                                .add(BODY_MUTATION_KEY, jsonProvider.createArrayBuilder(fieldJsonValue.asJsonArray()));
                                    }
                                } else {
                                    if (arguments.containsKey(INPUT_VALUE_INPUT_NAME)) {
                                        messageJsonObject
                                                .add(BODY_ARGUMENTS_KEY, jsonProvider.createArrayBuilder().add(jsonProvider.createObjectBuilder(arguments.get(INPUT_VALUE_INPUT_NAME).asJsonObject())))
                                                .add(BODY_MUTATION_KEY, jsonProvider.createArrayBuilder().add(jsonProvider.createObjectBuilder(fieldJsonValue.asJsonObject())));
                                    } else {
                                        messageJsonObject
                                                .add(BODY_ARGUMENTS_KEY, jsonProvider.createArrayBuilder().add(jsonProvider.createObjectBuilder(arguments.asJsonObject())))
                                                .add(BODY_MUTATION_KEY, jsonProvider.createArrayBuilder().add(jsonProvider.createObjectBuilder(fieldJsonValue.asJsonObject())));
                                    }
                                }
                                return Mono.just(
                                        new OutboundMessage(
                                                SUBSCRIPTION_EXCHANGE_NAME,
                                                packageName + "." + typeName,
                                                messageJsonObject.build().toString().getBytes()
                                        )
                                );
                            }
                            return Mono.empty();
                        }
                );
        return sender.send(messageFlux).thenReturn(jsonValue);
    }
}
