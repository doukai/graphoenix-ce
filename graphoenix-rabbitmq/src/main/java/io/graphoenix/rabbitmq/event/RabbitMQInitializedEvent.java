package io.graphoenix.rabbitmq.event;

import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Sender;

import java.util.Map;

import static io.graphoenix.core.event.DocumentInitializedEvent.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY;
import static io.graphoenix.rabbitmq.handler.RabbitMQSubscriptionHandler.SUBSCRIPTION_EXCHANGE_NAME;
import static reactor.rabbitmq.ExchangeSpecification.exchange;

@ApplicationScoped
@Initialized(ApplicationScoped.class)
@Priority(RabbitMQInitializedEvent.RABBITMQ_INITIALIZED_SCOPE_EVENT_PRIORITY)
public class RabbitMQInitializedEvent implements ScopeEvent {

    public static final int RABBITMQ_INITIALIZED_SCOPE_EVENT_PRIORITY = DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY + 175;

    private final Sender sender;

    @Inject
    public RabbitMQInitializedEvent(Sender sender) {
        this.sender = sender;
    }

    @Override
    public Mono<Void> fireAsync(Map<String, Object> context) {
        return sender.declare(exchange(SUBSCRIPTION_EXCHANGE_NAME)).then();
    }
}
