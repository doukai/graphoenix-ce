package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.graphoenix.spi.handler.OperationHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.FragmentHandler.FRAGMENT_HANDLER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_OPERATOR_INPUT_VALUE_VAL_NAME;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_WHERE_NAME;
import static io.graphoenix.spi.constant.Hammurabi.OPERATION_QUERY_NAME;

@ApplicationScoped
@Priority(UniqueMergeHandler.UNIQUE_MERGE_HANDLER_PRIORITY)
public class UniqueMergeHandler implements OperationBeforeHandler {

  public static final int UNIQUE_MERGE_HANDLER_PRIORITY = FRAGMENT_HANDLER_PRIORITY + 25;

  private final DocumentManager documentManager;
  private final JsonProvider jsonProvider;
  private final Provider<OperationHandler> operationHandlerProvider;
  private final UniqueItemBuilder uniqueItemBuilder;

  @Inject
  public UniqueMergeHandler(
      DocumentManager documentManager,
      JsonProvider jsonProvider,
      Provider<OperationHandler> operationHandlerProvider,
      UniqueItemBuilder uniqueItemBuilder) {
    this.documentManager = documentManager;
    this.jsonProvider = jsonProvider;
    this.operationHandlerProvider = operationHandlerProvider;
    this.uniqueItemBuilder = uniqueItemBuilder;
  }

  @Override
  public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
    ObjectType operationType = documentManager.getOperationTypeOrError(operation);
    return Flux.fromStream(
            operation.getFields().stream()
                .filter(Field::isUniqueMerge)
                .flatMap(
                    field ->
                        uniqueItemBuilder.buildUniqueItems(
                            operationType.getFieldOrError(field.getName()), field, true))
                .collect(Collectors.groupingBy(UniqueItem::getObjectType, Collectors.toList()))
                .entrySet()
                .stream()
                .map(
                    typeEntry ->
                        new AbstractMap.SimpleEntry<>(
                            uniqueItemBuilder.buildUniqueQueryField(
                                typeEntry.getKey(), typeEntry.getValue()),
                            typeEntry.getValue())))
        .collectList()
        .flatMapMany(
            fieldEntryList ->
                Mono.just(
                        fieldEntryList.stream()
                            .map(AbstractMap.SimpleEntry::getKey)
                            .collect(Collectors.toList()))
                    .filter(fieldList -> !fieldList.isEmpty())
                    .flatMap(
                        fieldList ->
                            Mono.from(
                                operationHandlerProvider
                                    .get()
                                    .handle(
                                        new Operation()
                                            .setOperationType(OPERATION_QUERY_NAME)
                                            .setSelections(fieldList))))
                    .flatMapMany(
                        jsonValue ->
                            Flux.fromStream(
                                    fieldEntryList.stream()
                                        .filter(
                                            fieldEntry ->
                                                !jsonValue
                                                    .asJsonObject()
                                                    .isNull(fieldEntry.getKey().getAlias()))
                                        .flatMap(
                                            fieldEntry ->
                                                buildMergePatches(
                                                    fieldEntry.getValue(),
                                                    jsonValue
                                                        .asJsonObject()
                                                        .getJsonArray(
                                                            fieldEntry.getKey().getAlias())
                                                        .stream()
                                                        .filter(
                                                            item ->
                                                                item.getValueType()
                                                                    .equals(
                                                                        JsonValue.ValueType.OBJECT))
                                                        .map(JsonValue::asJsonObject)
                                                        .collect(Collectors.toList())))
                                        .collect(
                                            Collectors.groupingBy(
                                                Map.Entry::getKey,
                                                Collectors.mapping(
                                                    Map.Entry::getValue, Collectors.toList())))
                                        .entrySet()
                                        .stream())
                                .map(
                                    entry ->
                                        entry
                                            .getKey()
                                            .setArguments(
                                                (JsonObject)
                                                    jsonProvider
                                                        .createPatchBuilder(
                                                            entry.getValue().stream()
                                                                .collect(
                                                                    JsonCollectors.toJsonArray()))
                                                        .build()
                                                        .apply(entry.getKey().getArguments())))))
        .then()
        .thenReturn(operation);
  }

  private Stream<Map.Entry<Field, JsonObject>> buildMergePatches(
      List<UniqueItem> uniqueItems, List<JsonObject> matchedItems) {
    Map<String, JsonObject> matchedItemIndex =
        uniqueItemBuilder.buildMatchedItemIndex(uniqueItems, matchedItems);
    return uniqueItems.stream()
        .flatMap(
            uniqueItem ->
                java.util.Optional.ofNullable(
                        matchedItemIndex.get(uniqueItemBuilder.buildUniqueKey(uniqueItem)))
                    .stream()
                    .map(
                        item ->
                            new AbstractMap.SimpleEntry<>(
                                uniqueItem.getField(),
                                jsonProvider
                                    .createObjectBuilder()
                                    .add("op", "add")
                                    .add(
                                        "path", uniqueItem.getPath() + "/" + INPUT_VALUE_WHERE_NAME)
                                    .add(
                                        "value",
                                        jsonProvider
                                            .createObjectBuilder()
                                            .add(
                                                uniqueItem
                                                    .getObjectType()
                                                    .getIDFieldOrError()
                                                    .getName(),
                                                jsonProvider
                                                    .createObjectBuilder()
                                                    .add(
                                                        INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                        item.get(
                                                            uniqueItem
                                                                .getObjectType()
                                                                .getIDFieldOrError()
                                                                .getName()))
                                                    .build())
                                            .build())
                                    .build())));
  }
}
