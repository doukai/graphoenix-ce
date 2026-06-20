package io.graphoenix.core.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.json.JsonObject;
import jakarta.json.spi.JsonProvider;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class OperationBuilderOrderTest {

  private final JsonProvider jsonProvider = JsonProvider.provider();

  @Test
  void shouldPreserveOriginalNestedObjectOrderWhenUpdating() {
    OperationBuilder operationBuilder = new OperationBuilder(null);
    JsonObject original =
        jsonProvider
            .createObjectBuilder()
            .add(
                "orders",
                jsonProvider
                    .createObjectBuilder()
                    .add("urgent", "DESC")
                    .add("estimatedDeparture", "ASC")
                    .add("createTime", "ASC"))
            .build();
    JsonObject updated =
        jsonProvider
            .createObjectBuilder()
            .add(
                "orders",
                jsonProvider
                    .createObjectBuilder()
                    .add("createTime", "ASC")
                    .add("estimatedDeparture", "ASC")
                    .add("urgent", "DESC"))
            .build();

    JsonObject result = operationBuilder.updateJsonObject(original, updated);

    assertEquals(
        List.of("urgent", "estimatedDeparture", "createTime"),
        new ArrayList<>(result.getJsonObject("orders").keySet()));
  }
}
