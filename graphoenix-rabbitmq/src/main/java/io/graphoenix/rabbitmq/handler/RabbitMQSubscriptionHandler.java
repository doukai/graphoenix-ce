package io.graphoenix.rabbitmq.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.SubscriptionHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonValue;
import reactor.core.publisher.Flux;

@ApplicationScoped
public class RabbitMQSubscriptionHandler implements SubscriptionHandler {

    @Override
    public Flux<JsonValue> subscription(Operation operation, String token, String operationId) {
        return null;
    }
}
