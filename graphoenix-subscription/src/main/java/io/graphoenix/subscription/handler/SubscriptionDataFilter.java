package io.graphoenix.subscription.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.json.JsonValue;

public interface SubscriptionDataFilter {
    void indexFilter(Operation operation);

    boolean merged(JsonValue jsonValue);
}
