package io.graphoenix.subscription.handler.before;

import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.graphoenix.subscription.handler.DefaultSubscriptionDataListener;
import io.nozdormu.spi.context.PublisherBeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Map;

@ApplicationScoped
@Priority(775)
public class SubscriptionDataListenerBeforeHandler implements OperationBeforeHandler {

    @Override
    public Mono<Operation> subscription(Operation operation, Map<String, JsonValue> variables) {
        return PublisherBeanContext.get(DefaultSubscriptionDataListener.class)
                .map(subscriptionDataListener -> subscriptionDataListener.beforeSubscription(operation))
                .thenReturn(operation);
    }
}
