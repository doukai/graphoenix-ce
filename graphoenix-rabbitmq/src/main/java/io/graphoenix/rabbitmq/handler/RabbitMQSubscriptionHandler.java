package io.graphoenix.rabbitmq.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.SubscriptionHandler;
import io.graphoenix.subscription.handler.SubscriptionDataListener;
import io.nozdormu.spi.context.PublisherBeanContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonValue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

import static reactor.rabbitmq.BindingSpecification.binding;
import static reactor.rabbitmq.QueueSpecification.queue;

@ApplicationScoped
public class RabbitMQSubscriptionHandler implements SubscriptionHandler {

    public static final String SUBSCRIPTION_EXCHANGE_NAME = "graphoenix.subscription";
    public static final String REQUEST_ID = "requestId";

    private final Provider<SubscriptionDataListener> subscriptionDataListenerProvider;

    private final Sender sender;

    private final Receiver receiver;

    @Inject
    public RabbitMQSubscriptionHandler(Provider<SubscriptionDataListener> subscriptionDataListenerProvider, Sender sender, Receiver receiver) {
        this.subscriptionDataListenerProvider = subscriptionDataListenerProvider;
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public Flux<JsonValue> subscription(Operation operation, String token, String operationId) {
        return Mono.deferContextual(contextView -> Mono.justOrEmpty(contextView.getOrEmpty(REQUEST_ID)))
                .map(requestId -> (String) requestId)
                .flatMapMany(requestId ->
                        sender.declare(queue(requestId).autoDelete(true))
                                .thenMany(
                                        Flux.fromStream(typeNameStream)
                                                .flatMap(typeName -> sender.bind(binding(SUBSCRIPTION_EXCHANGE_NAME, typeName, requestId)))
                                )
                                .flatMap(bindOk ->
                                        PublisherBeanContext.get(SubscriptionDataListener.class)
                                                .map(subscriptionDataListener -> subscriptionDataListener.indexFilter(operation))
                                                .flatMapMany(subscriptionDataListener ->
                                                        Flux.concat(
                                                                        subscriptionHandler.subscription(operationHandler, operationDefinitionContext),
                                                                        receiver.consumeAutoAck(requestId)
                                                                                .map(this::toJsonValue)
                                                                                .filter(subscriptionDataListener::merged)
                                                                                .flatMap(jsonValue -> subscriptionHandler.subscription(operationHandler, operationDefinitionContext))
                                                                )
                                                                .doOnNext(jsonValue -> subscriptionDataListener.indexData(operation, jsonValue))
                                                                .flatMap(jsonValue -> subscriptionHandler.invoke(operationDefinitionContext, jsonValue))
                                                )
                                )
                )
                .contextWrite(PublisherBeanContext.of(SubscriptionDataListener.class, subscriptionDataListenerProvider.get()));
    }
}
