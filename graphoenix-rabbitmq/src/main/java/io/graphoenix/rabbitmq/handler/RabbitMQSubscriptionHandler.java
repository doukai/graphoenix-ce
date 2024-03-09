package io.graphoenix.rabbitmq.handler;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.QueryHandler;
import io.graphoenix.spi.handler.SubscriptionHandler;
import io.graphoenix.subscription.handler.DefaultSubscriptionDataListener;
import io.nozdormu.spi.context.BeanContext;
import io.nozdormu.spi.context.PublisherBeanContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
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
    public static final String BODY_TYPE_KEY = "type";
    public static final String BODY_ARGUMENTS_KEY = "arguments";
    public static final String BODY_MUTATION_KEY = "mutation";
    public static final String REQUEST_ID = "requestId";

    private final GraphQLConfig graphQLConfig = BeanContext.get(GraphQLConfig.class);

    private final Provider<QueryHandler> queryHandlerProvider = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
            .map(name -> BeanContext.getProvider(QueryHandler.class, name))
            .orElseGet(() -> BeanContext.getProvider(QueryHandler.class));

    private final DocumentManager documentManager;

    private final JsonProvider jsonProvider;

    private final Sender sender;

    private final Receiver receiver;

    @Inject
    public RabbitMQSubscriptionHandler(DocumentManager documentManager, JsonProvider jsonProvider, Sender sender, Receiver receiver) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
        this.sender = sender;
        this.receiver = receiver;
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
                                                .filter(field -> documentManager.getFieldTypeDefinition(operationType.getField(field.getName())).isObject())
                                                .map(field -> operationType.getField(field.getName()))
                                                .map(fieldDefinition -> fieldDefinition.getPackageNameOrError() + "." + documentManager.getFieldTypeDefinition(fieldDefinition).getName())
                                                .distinct()
                                                .flatMap(routingKey -> sender.bind(binding(SUBSCRIPTION_EXCHANGE_NAME, routingKey, requestId)))
                                )
                                .flatMap(bindOk ->
                                        PublisherBeanContext.get(DefaultSubscriptionDataListener.class)
                                                .flatMapMany(subscriptionDataListener ->
                                                        Flux
                                                                .concat(
                                                                        queryHandlerProvider.get().query(operation),
                                                                        receiver.consumeAutoAck(requestId)
                                                                                .filter(delivery -> {
                                                                                            JsonObject messageJsonObject = jsonProvider.createReader(new StringReader(new String(delivery.getBody()))).readObject();
                                                                                            String typeName = messageJsonObject.getString(BODY_TYPE_KEY);
                                                                                            JsonArray arguments = messageJsonObject.getJsonArray(BODY_ARGUMENTS_KEY);
                                                                                            JsonArray mutation = messageJsonObject.getJsonArray(BODY_MUTATION_KEY);
                                                                                            return subscriptionDataListener.changed(typeName, arguments, mutation);
                                                                                        }
                                                                                )
                                                                                .flatMap(delivery -> queryHandlerProvider.get().query(operation))
                                                                )
                                                )
                                )
                );
    }
}
