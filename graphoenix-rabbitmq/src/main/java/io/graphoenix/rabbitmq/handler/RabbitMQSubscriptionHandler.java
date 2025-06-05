package io.graphoenix.rabbitmq.handler;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.QueryHandler;
import io.graphoenix.spi.handler.SubscriptionDataListener;
import io.graphoenix.spi.handler.SubscriptionHandler;
import io.nozdormu.spi.context.PublisherBeanContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

import java.io.StringReader;
import java.util.Optional;

import static reactor.rabbitmq.BindingSpecification.binding;
import static reactor.rabbitmq.QueueSpecification.queue;

@ApplicationScoped
public class RabbitMQSubscriptionHandler implements SubscriptionHandler {

    public static final String SUBSCRIPTION_EXCHANGE_NAME = "graphoenix.subscription";

    public static final String REQUEST_ID = "requestId";

    private final QueryHandler queryHandler;

    private final DocumentManager documentManager;

    private final JsonProvider jsonProvider;

    private final Sender sender;

    private final Receiver receiver;

    @Inject
    public RabbitMQSubscriptionHandler(GraphQLConfig graphQLConfig, DocumentManager documentManager, JsonProvider jsonProvider, Sender sender, Receiver receiver) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
        this.sender = sender;
        this.receiver = receiver;
        this.queryHandler = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
                .map(name -> CDI.current().select(QueryHandler.class, NamedLiteral.of(name)).get())
                .orElseGet(() -> CDI.current().select(QueryHandler.class).get());
    }

    @Override
    public Flux<JsonValue> subscription(Operation operation, String token, String operationId) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.deferContextual(contextView -> Mono.justOrEmpty(contextView.getOrEmpty(REQUEST_ID)))
                .map(requestId -> (String) requestId)
                .flatMapMany(requestId ->
                        sender.declare(queue(requestId).autoDelete(true))
                                .thenMany(
                                        Flux.fromIterable(operation.getFields())
                                                .map(field -> operationType.getFieldOrError(field.getName()))
                                                .filter(fieldDefinition -> {
                                                            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
                                                            return fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer();
                                                        }
                                                )
                                                .map(fieldDefinition -> fieldDefinition.getPackageNameOrError() + "." + documentManager.getFieldTypeDefinition(fieldDefinition).getName())
                                                .distinct()
                                                .flatMap(routingKey -> sender.bind(binding(SUBSCRIPTION_EXCHANGE_NAME, routingKey, requestId)))
                                )
                                .flatMap(bindOk ->
                                        PublisherBeanContext.get(SubscriptionDataListener.class)
                                                .flatMapMany(subscriptionDataListener ->
                                                        Flux
                                                                .concat(
                                                                        queryHandler.query(operation),
                                                                        receiver.consumeAutoAck(requestId)
                                                                                .filter(delivery -> {
                                                                                            String routingKey = delivery.getEnvelope().getRoutingKey();
                                                                                            String typeName = delivery.getEnvelope().getRoutingKey().substring(routingKey.lastIndexOf(".") + 1);
                                                                                            JsonArray mutations = jsonProvider.createReader(new StringReader(new String(delivery.getBody()))).readArray();
                                                                                            return subscriptionDataListener.changed(typeName, mutations);
                                                                                        }
                                                                                )
                                                                                .flatMap(delivery -> queryHandler.query(operation))
                                                                )
                                                )
                                )
                );
    }
}
