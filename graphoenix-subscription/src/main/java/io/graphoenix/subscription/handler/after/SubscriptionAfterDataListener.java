package io.graphoenix.subscription.handler.after;

import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.OperationAfterHandler;
import io.graphoenix.spi.handler.SubscriptionDataListener;
import io.nozdormu.spi.context.PublisherBeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import static io.graphoenix.core.handler.after.SelectionHandler.SELECTION_HANDLER_PRIORITY;

@ApplicationScoped
@Priority(SubscriptionAfterDataListener.SUBSCRIPTION_AFTER_DATA_LISTENER_PRIORITY)
public class SubscriptionAfterDataListener implements OperationAfterHandler {

    public static final int SUBSCRIPTION_AFTER_DATA_LISTENER_PRIORITY = SELECTION_HANDLER_PRIORITY - 125;

    @Override
    public Mono<JsonValue> subscription(Operation operation, JsonValue jsonValue) {
        return PublisherBeanContext.get(SubscriptionDataListener.class)
                .map(subscriptionDataListener -> subscriptionDataListener.afterSubscription(operation, jsonValue))
                .thenReturn(jsonValue);
    }
}
