package io.graphoenix.subscription.handler.before;

import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.nozdormu.spi.context.PublisherBeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.Map;

import static io.graphoenix.subscription.handler.before.SubscriptionArgumentsFieldsRegister.SUBSCRIPTION_ARGUMENTS_FIELDS_REGISTER_PRIORITY;

@ApplicationScoped
@Priority(SubscriptionBeforeDataListener.SUBSCRIPTION_DATA_LISTENER_PRIORITY)
public class SubscriptionBeforeDataListener implements OperationBeforeHandler {

    public static final int SUBSCRIPTION_DATA_LISTENER_PRIORITY = SUBSCRIPTION_ARGUMENTS_FIELDS_REGISTER_PRIORITY + 100;

    @Override
    public Mono<Operation> subscription(Operation operation, Map<String, JsonValue> variables) {
        return PublisherBeanContext.get(io.graphoenix.spi.handler.SubscriptionDataListener.class)
                .map(subscriptionDataListener -> subscriptionDataListener.beforeSubscription(operation))
                .thenReturn(operation);
    }
}
