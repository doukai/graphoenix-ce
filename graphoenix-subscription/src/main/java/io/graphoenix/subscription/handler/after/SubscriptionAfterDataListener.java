package io.graphoenix.subscription.handler.after;

import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.OperationAfterHandler;
import io.graphoenix.spi.handler.SubscriptionDataListener;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Provider;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import static io.graphoenix.core.handler.after.SelectionHandler.SELECTION_HANDLER_PRIORITY;

@ApplicationScoped
@Priority(SubscriptionAfterDataListener.SUBSCRIPTION_AFTER_DATA_LISTENER_PRIORITY)
public class SubscriptionAfterDataListener implements OperationAfterHandler {

    public static final int SUBSCRIPTION_AFTER_DATA_LISTENER_PRIORITY = SELECTION_HANDLER_PRIORITY - 125;

    private final Provider<Mono<SubscriptionDataListener>> subscriptionDataListenerProvider;

    public SubscriptionAfterDataListener(Provider<Mono<SubscriptionDataListener>> subscriptionDataListenerProvider) {
        this.subscriptionDataListenerProvider = subscriptionDataListenerProvider;
    }

    @Override
    public Mono<JsonValue> subscription(Operation operation, JsonValue jsonValue) {
        return subscriptionDataListenerProvider.get()
                .map(subscriptionDataListener -> subscriptionDataListener.afterSubscription(operation, jsonValue))
                .thenReturn(jsonValue);
    }
}
