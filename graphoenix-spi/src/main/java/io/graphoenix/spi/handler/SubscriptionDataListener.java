package io.graphoenix.spi.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;

public interface SubscriptionDataListener {

    SubscriptionDataListener beforeSubscription(Operation operation);

    SubscriptionDataListener afterSubscription(Operation operation, JsonValue jsonValue);

    boolean changed(JsonValue messageJsonObject);
}
