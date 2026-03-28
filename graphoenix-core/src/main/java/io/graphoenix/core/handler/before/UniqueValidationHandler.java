package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.error.GraphQLError;
import io.graphoenix.spi.error.GraphQLErrors;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.FragmentHandler.FRAGMENT_HANDLER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.OPERATION_QUERY_NAME;
import static io.graphoenix.spi.error.GraphQLErrorType.EXISTED_UNIQUE_VALUES;

@ApplicationScoped
@Priority(UniqueValidationHandler.UNIQUE_VALIDATION_HANDLER_PRIORITY)
public class UniqueValidationHandler implements OperationBeforeHandler {

  public static final int UNIQUE_VALIDATION_HANDLER_PRIORITY = FRAGMENT_HANDLER_PRIORITY + 75;

  private final DocumentManager documentManager;
  private final Provider<OperationHandler> operationHandlerProvider;
  private final UniqueItemBuilder uniqueItemBuilder;

  @Inject
  public UniqueValidationHandler(
      DocumentManager documentManager,
      Provider<OperationHandler> operationHandlerProvider,
      UniqueItemBuilder uniqueItemBuilder) {
    this.documentManager = documentManager;
    this.operationHandlerProvider = operationHandlerProvider;
    this.uniqueItemBuilder = uniqueItemBuilder;
  }

  @Override
  public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
    ObjectType operationType = documentManager.getOperationTypeOrError(operation);
    Map<ObjectType, List<UniqueItem>> uniqueItemGroups =
        operation.getFields().stream()
            .flatMap(
                field ->
                    uniqueItemBuilder.buildUniqueItems(
                        operationType.getFieldOrError(field.getName()), field, false))
            .collect(
                Collectors.groupingBy(
                    UniqueItem::getObjectType, LinkedHashMap::new, Collectors.toList()));
    return Flux.fromStream(
            uniqueItemGroups.entrySet().stream()
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
                                            buildValidationErrors(
                                                fieldEntry.getValue(),
                                                jsonValue
                                                    .asJsonObject()
                                                    .getJsonArray(fieldEntry.getKey().getAlias())
                                                    .stream()
                                                    .filter(
                                                        item ->
                                                            item.getValueType()
                                                                .equals(JsonValue.ValueType.OBJECT))
                                                    .map(JsonValue::asJsonObject)
                                                    .collect(Collectors.toList()))))))
        .collectList()
        .flatMap(
            graphQLErrors -> {
              List<GraphQLError> requestGraphQLErrors =
                  buildRequestDuplicateErrors(uniqueItemGroups);
              if (!requestGraphQLErrors.isEmpty()) {
                graphQLErrors.addAll(requestGraphQLErrors);
              }
              if (!graphQLErrors.isEmpty()) {
                return Mono.error(new GraphQLErrors().addAll(graphQLErrors));
              }
              return Mono.just(operation);
            });
  }

  private Stream<GraphQLError> buildValidationErrors(
      List<UniqueItem> uniqueItems, List<JsonObject> matchedItems) {
    Map<String, JsonObject> matchedItemIndex =
        uniqueItemBuilder.buildMatchedItemIndex(uniqueItems, matchedItems);
    return uniqueItems.stream()
        .filter(
            uniqueItem ->
                java.util.Optional.ofNullable(
                        matchedItemIndex.get(uniqueItemBuilder.buildUniqueKey(uniqueItem)))
                    .stream()
                    .anyMatch(
                        item ->
                            uniqueItem.getCurrentId().isNull()
                                || !item.get(
                                        uniqueItem.getObjectType().getIDFieldOrError().getName())
                                    .toString()
                                    .equals(uniqueItem.getCurrentId().toString())))
        .flatMap(this::buildUniqueErrors);
  }

  private List<GraphQLError> buildRequestDuplicateErrors(
      Map<ObjectType, List<UniqueItem>> uniqueItemGroups) {
    return uniqueItemGroups.values().stream()
        .flatMap(
            uniqueItems ->
                uniqueItems.stream()
                    .collect(
                        Collectors.groupingBy(
                            uniqueItemBuilder::buildUniqueKey,
                            LinkedHashMap::new,
                            Collectors.toList()))
                    .values()
                    .stream()
                    .filter(items -> items.size() > 1)
                    .flatMap(items -> buildRequestDuplicateErrors(items).stream()))
        .collect(Collectors.toList());
  }

  private List<GraphQLError> buildRequestDuplicateErrors(List<UniqueItem> duplicatedItems) {
    if (!hasRequestDuplicateConflict(duplicatedItems)) {
      return List.of();
    }
    return duplicatedItems.stream().flatMap(this::buildUniqueErrors).collect(Collectors.toList());
  }

  private boolean hasRequestDuplicateConflict(List<UniqueItem> duplicatedItems) {
    boolean hasNullId = false;
    Set<String> ids = new HashSet<>();
    for (UniqueItem duplicatedItem : duplicatedItems) {
      if (duplicatedItem.getCurrentId().isNull()) {
        hasNullId = true;
      } else {
        ids.add(duplicatedItem.getCurrentId().toString());
      }
      if (hasNullId && (!ids.isEmpty() || duplicatedItems.size() > 1)) {
        return true;
      }
      if (ids.size() > 1) {
        return true;
      }
    }
    return false;
  }

  private Stream<GraphQLError> buildUniqueErrors(UniqueItem uniqueItem) {
    return uniqueItem.getErrorPaths().stream()
        .map(errorPath -> new GraphQLError(EXISTED_UNIQUE_VALUES).setPath(errorPath));
  }
}
