package io.graphoenix.spi.graphql.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.json.JsonObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ArgumentsOrderTest {

  @Test
  void shouldPreserveNestedObjectArgumentOrder() {
    Map<String, Object> orders = new LinkedHashMap<>();
    orders.put("urgent", new EnumValue("DESC"));
    orders.put("estimatedDeparture", new EnumValue("ASC"));
    orders.put("createTime", new EnumValue("ASC"));

    Map<String, Object> orderBy = new LinkedHashMap<>();
    orderBy.put("orders", orders);

    Map<String, Object> argumentMap = new LinkedHashMap<>();
    argumentMap.put("orderBy", orderBy);

    Arguments arguments = new Arguments(argumentMap);
    Arguments copiedArguments = new Arguments((JsonObject) arguments);

    JsonObject copiedOrders = copiedArguments.getJsonObject("orderBy").getJsonObject("orders");
    assertEquals(
        List.of("urgent", "estimatedDeparture", "createTime"),
        new ArrayList<>(copiedOrders.keySet()));

    String json = copiedArguments.toJson();
    assertTrue(json.indexOf("\"urgent\"") < json.indexOf("\"estimatedDeparture\""));
    assertTrue(json.indexOf("\"estimatedDeparture\"") < json.indexOf("\"createTime\""));
  }
}
