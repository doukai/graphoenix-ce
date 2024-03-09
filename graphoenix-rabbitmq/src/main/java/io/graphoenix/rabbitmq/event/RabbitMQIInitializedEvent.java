package io.graphoenix.rabbitmq.event;

import com.google.auto.service.AutoService;
import io.nozdormu.spi.context.BeanContext;
import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Sender;

import java.util.Map;

import static io.graphoenix.rabbitmq.handler.RabbitMQSubscriptionHandler.SUBSCRIPTION_EXCHANGE_NAME;
import static reactor.rabbitmq.ExchangeSpecification.exchange;

@Initialized(ApplicationScoped.class)
@Priority(2)
@AutoService(ScopeEvent.class)
public class RabbitMQIInitializedEvent implements ScopeEvent {

    private final Sender sender;

    public RabbitMQIInitializedEvent() {
        this.sender = BeanContext.get(Sender.class);
    }

    @Override
    public Mono<Void> fireAsync(Map<String, Object> context) {
        return sender.declare(exchange(SUBSCRIPTION_EXCHANGE_NAME)).then();
    }
}
