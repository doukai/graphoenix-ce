package io.graphoenix.subscription.handler.after;

import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.OperationAfterHandler;
import io.graphoenix.subscription.handler.DefaultSubscriptionDataListener;
import io.nozdormu.spi.context.PublisherBeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

@ApplicationScoped
@Priority(Integer.MAX_VALUE - 225)
public class SubscriptionDataListenerAfterHandler implements OperationAfterHandler {

    @Override
    public Mono<JsonValue> subscription(Operation operation, JsonValue jsonValue) {
        return PublisherBeanContext.get(DefaultSubscriptionDataListener.class)
                .map(subscriptionDataListener -> subscriptionDataListener.afterSubscription(operation, jsonValue))
                .thenReturn(jsonValue);
    }
}
