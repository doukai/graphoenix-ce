package io.graphoenix.rabbitmq.handler.after;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationAfterHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.after.SelectionHandler.SELECTION_HANDLER_PRIORITY;
import static io.graphoenix.rabbitmq.handler.RabbitMQSubscriptionHandler.*;
import static io.graphoenix.spi.constant.Hammurabi.*;

@ApplicationScoped
@Priority(MutationSendHandler.MUTATION_SEND_HANDLER_PRIORITY)
public class MutationSendHandler implements OperationAfterHandler {

    public static final int MUTATION_SEND_HANDLER_PRIORITY = SELECTION_HANDLER_PRIORITY - 150;

    private final DocumentManager documentManager;

    private final Sender sender;

    @Inject
    public MutationSendHandler(DocumentManager documentManager, Sender sender) {
        this.documentManager = documentManager;
        this.sender = sender;
    }

    @Override
    public Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        Map<String, List<JsonObject>> typeJsonObjectListMap =
                operation.getFields().stream()
                        .filter(field -> !operationType.getField(field.getName()).isInvokeField())
                        .flatMap(field -> buildTypeJsonObjectEntryStream(operationType.getField(field.getName()), field.getArguments()))
                        .collect(
                                Collectors.groupingBy(
                                        Map.Entry::getKey,
                                        Collectors.mapping(
                                                Map.Entry::getValue,
                                                Collectors.toList()
                                        )
                                )
                        );
        return sender
                .send(
                        Flux.fromIterable(typeJsonObjectListMap.entrySet())
                                .map(entry ->
                                        new OutboundMessage(
                                                SUBSCRIPTION_EXCHANGE_NAME,
                                                entry.getKey(),
                                                entry.getValue().stream()
                                                        .collect(JsonCollectors.toJsonArray())
                                                        .toString()
                                                        .getBytes()
                                        )
                                )
                )
                .thenReturn(jsonValue);
    }

    private Stream<Map.Entry<String, JsonObject>> buildTypeJsonObjectEntryStream(FieldDefinition fieldDefinition, JsonValue jsonValue) {
        if (jsonValue == null || jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            String packageName = fieldTypeDefinition.getPackageNameOrError();
            String typeName = fieldTypeDefinition.getName();
            if (jsonValue.getValueType().equals(JsonValue.ValueType.ARRAY)) {
                return jsonValue.asJsonArray().stream()
                        .flatMap(item -> buildTypeJsonObjectEntryStream(fieldDefinition, item));
            } else if (jsonValue.getValueType().equals(JsonValue.ValueType.OBJECT) && jsonValue.asJsonObject().containsKey(INPUT_VALUE_LIST_NAME)) {
                return jsonValue.asJsonObject().getJsonArray(INPUT_VALUE_LIST_NAME).stream()
                        .flatMap(item -> buildTypeJsonObjectEntryStream(fieldDefinition, item));
            } else if (jsonValue.getValueType().equals(JsonValue.ValueType.OBJECT) && jsonValue.asJsonObject().containsKey(INPUT_VALUE_INPUT_NAME)) {
                return Stream
                        .concat(
                                Stream.of(
                                        new AbstractMap.SimpleEntry<>(
                                                packageName + "." + typeName,
                                                jsonValue.asJsonObject().getJsonObject(INPUT_VALUE_INPUT_NAME).entrySet().stream()
                                                        .filter(entry -> !entry.getKey().equals(INPUT_VALUE_WHERE_NAME))
                                                        .filter(entry -> documentManager.getFieldTypeDefinition(fieldTypeDefinition.asObject().getField(entry.getKey())).isLeaf())
                                                        .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()))
                                                        .collect(JsonCollectors.toJsonObject())
                                        )
                                ),
                                jsonValue.asJsonObject().getJsonObject(INPUT_VALUE_INPUT_NAME).entrySet().stream()
                                        .filter(entry -> !entry.getKey().equals(INPUT_VALUE_WHERE_NAME))
                                        .filter(entry -> !documentManager.getFieldTypeDefinition(fieldTypeDefinition.asObject().getField(entry.getKey())).isLeaf())
                                        .flatMap(entry -> buildTypeJsonObjectEntryStream(fieldTypeDefinition.asObject().getField(entry.getKey()), entry.getValue()))
                        );
            } else {
                return Stream
                        .concat(
                                Stream.of(
                                        new AbstractMap.SimpleEntry<>(
                                                packageName + "." + typeName,
                                                jsonValue.asJsonObject().entrySet().stream()
                                                        .filter(entry -> !entry.getKey().equals(INPUT_VALUE_WHERE_NAME))
                                                        .filter(entry -> documentManager.getFieldTypeDefinition(fieldTypeDefinition.asObject().getField(entry.getKey())).isLeaf())
                                                        .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()))
                                                        .collect(JsonCollectors.toJsonObject())
                                        )
                                ),
                                jsonValue.asJsonObject().entrySet().stream()
                                        .filter(entry -> !entry.getKey().equals(INPUT_VALUE_WHERE_NAME))
                                        .filter(entry -> !documentManager.getFieldTypeDefinition(fieldTypeDefinition.asObject().getField(entry.getKey())).isLeaf())
                                        .flatMap(entry -> buildTypeJsonObjectEntryStream(fieldTypeDefinition.asObject().getField(entry.getKey()), entry.getValue()))
                        );
            }
        }
        return Stream.empty();
    }
}
