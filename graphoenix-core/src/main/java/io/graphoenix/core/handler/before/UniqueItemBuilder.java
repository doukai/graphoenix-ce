package io.graphoenix.core.handler.before;

import com.google.common.collect.Streams;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Arguments;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.common.NullValue;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.INPUT_CONDITIONAL_INPUT_VALUE_OR;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_OPERATOR_INPUT_VALUE_VAL_NAME;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_COND_NAME;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_EXS_NAME;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_INPUT_NAME;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_LIST_NAME;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_WHERE_NAME;
import static io.graphoenix.spi.constant.Hammurabi.SUFFIX_LIST;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
public class UniqueItemBuilder {

  private final DocumentManager documentManager;
  private final JsonProvider jsonProvider;

  @Inject
  public UniqueItemBuilder(DocumentManager documentManager, JsonProvider jsonProvider) {
    this.documentManager = documentManager;
    this.jsonProvider = jsonProvider;
  }

  public Stream<UniqueItem> buildUniqueItems(
      FieldDefinition fieldDefinition, Field field, boolean skipIfIdPresent) {
    Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
    if (!fieldTypeDefinition.isObject() || fieldTypeDefinition.isContainer()) {
      return Stream.empty();
    }
    ObjectType objectType = fieldTypeDefinition.asObject();
    FieldDefinition idField = objectType.getIDFieldOrError();
    return Streams.concat(
        buildUniqueItems(
            field,
            objectType,
            fieldDefinition.getArguments(),
            field.getArguments(),
            idField.getName(),
            skipIfIdPresent),
        fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_INPUT_NAME).stream()
            .flatMap(
                inputValue ->
                    Stream.ofNullable(field.getArguments())
                        .flatMap(
                            arguments ->
                                arguments
                                    .getArgumentOrEmpty(inputValue.getName())
                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                                    .stream())
                        .filter(ValueWithVariable::isObject)
                        .map(ValueWithVariable::asObject)
                        .flatMap(
                            objectValueWithVariable ->
                                buildUniqueItems(
                                    field,
                                    objectType,
                                    "/" + INPUT_VALUE_INPUT_NAME,
                                    documentManager
                                        .getInputValueTypeDefinition(inputValue)
                                        .asInputObject()
                                        .getInputValues(),
                                    objectValueWithVariable,
                                    idField.getName(),
                                    skipIfIdPresent))),
        fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_LIST_NAME).stream()
            .flatMap(
                listInputValue ->
                    Stream.ofNullable(field.getArguments())
                        .flatMap(
                            arguments ->
                                arguments
                                    .getArgumentOrEmpty(listInputValue.getName())
                                    .or(() -> Optional.ofNullable(listInputValue.getDefaultValue()))
                                    .stream())
                        .filter(ValueWithVariable::isArray)
                        .map(ValueWithVariable::asArray)
                        .flatMap(
                            arrayValueWithVariable ->
                                IntStream.range(0, arrayValueWithVariable.size())
                                    .boxed()
                                    .flatMap(
                                        index ->
                                            buildUniqueItems(
                                                field,
                                                objectType,
                                                "/" + INPUT_VALUE_LIST_NAME + "/" + index,
                                                documentManager
                                                    .getInputValueTypeDefinition(listInputValue)
                                                    .asInputObject()
                                                    .getInputValues(),
                                                arrayValueWithVariable
                                                    .getValueWithVariable(index)
                                                    .asObject(),
                                                idField.getName(),
                                                skipIfIdPresent)))));
  }

  public Field buildUniqueQueryField(ObjectType objectType, List<UniqueItem> uniqueItems) {
    return new Field(typeNameToFieldName(objectType.getName()) + SUFFIX_LIST)
        .setAlias(typeNameToFieldName(objectType.getName()) + "_unique")
        .setArguments(
            jsonProvider
                .createObjectBuilder()
                .add(INPUT_VALUE_COND_NAME, new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_OR))
                .add(
                    INPUT_VALUE_EXS_NAME,
                    uniqueItems.stream()
                        .map(this::buildUniqueExpression)
                        .collect(JsonCollectors.toJsonArray()))
                .build())
        .addSelection(new Field(objectType.getIDFieldOrError().getName()))
        .addSelections(buildUniqueSelectionFields(uniqueItems));
  }

  public String buildUniqueKey(UniqueItem uniqueItem) {
    return buildUniqueKey(uniqueItem.getUniqueValues());
  }

  private String buildUniqueKey(Map<String, ValueWithVariable> uniqueValues) {
    return uniqueValues.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(entry -> entry.getKey() + "=" + entry.getValue())
        .collect(Collectors.joining("|"));
  }

  private Optional<String> buildUniqueKey(
      Map<String, ValueWithVariable> uniqueValues, JsonObject jsonObject) {
    StringJoiner uniqueKey = new StringJoiner("|");
    for (String fieldName : new TreeSet<>(uniqueValues.keySet())) {
      JsonValue value = getValueFromPath(jsonObject, fieldName);
      if (value == null) {
        return Optional.empty();
      }
      uniqueKey.add(fieldName + "=" + value);
    }
    return Optional.of(uniqueKey.toString());
  }

  public Map<String, JsonObject> buildMatchedItemIndex(
      List<UniqueItem> uniqueItems, List<JsonObject> matchedItems) {
    if (uniqueItems.isEmpty() || matchedItems.isEmpty()) {
      return Map.of();
    }
    Map<String, JsonObject> matchedItemIndex = new LinkedHashMap<>();
    for (JsonObject matchedItem : matchedItems) {
      buildUniqueKey(uniqueItems.get(0).getUniqueValues(), matchedItem)
          .ifPresent(uniqueKey -> matchedItemIndex.putIfAbsent(uniqueKey, matchedItem));
    }
    return matchedItemIndex;
  }

  private Optional<ValueWithVariable> getIDValueWithVariable(
      String idFieldName, ObjectValueWithVariable objectValueWithVariable) {
    if (objectValueWithVariable != null) {
      if (objectValueWithVariable.containsKey(idFieldName)) {
        return Optional.of(objectValueWithVariable.getValueWithVariable(idFieldName));
      } else if (objectValueWithVariable.containsKey(INPUT_VALUE_WHERE_NAME)
          && objectValueWithVariable
              .getValueWithVariable(INPUT_VALUE_WHERE_NAME)
              .asObject()
              .containsKey(idFieldName)
          && objectValueWithVariable
              .getValueWithVariable(INPUT_VALUE_WHERE_NAME)
              .asObject()
              .getValueWithVariable(idFieldName)
              .asObject()
              .containsKey(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)) {
        return Optional.of(
            objectValueWithVariable
                .getValueWithVariable(INPUT_VALUE_WHERE_NAME)
                .asObject()
                .getValueWithVariable(idFieldName)
                .asObject()
                .getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME));
      }
    }
    return Optional.empty();
  }

  private Optional<ValueWithVariable> getIDValueWithVariable(
      String idFieldName, Arguments arguments) {
    if (arguments != null) {
      if (arguments.containsKey(idFieldName)) {
        return Optional.of(arguments.getArgument(idFieldName));
      } else if (arguments.containsKey(INPUT_VALUE_WHERE_NAME)
          && arguments.getArgument(INPUT_VALUE_WHERE_NAME).asObject().containsKey(idFieldName)
          && arguments
              .getArgument(INPUT_VALUE_WHERE_NAME)
              .asObject()
              .getValueWithVariable(idFieldName)
              .asObject()
              .containsKey(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)) {
        return Optional.of(
            arguments
                .getArgument(INPUT_VALUE_WHERE_NAME)
                .asObject()
                .getValueWithVariable(idFieldName)
                .asObject()
                .getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME));
      }
    }
    return Optional.empty();
  }

  private Stream<UniqueItem> buildUniqueItems(
      Field field,
      ObjectType objectType,
      Collection<InputValue> inputValues,
      Arguments arguments,
      String idFieldName,
      boolean skipIfIdPresent) {
    if (arguments == null
        || (skipIfIdPresent && getIDValueWithVariable(idFieldName, arguments).isPresent())) {
      return Stream.empty();
    }
    ValueWithVariable currentId =
        getIDValueWithVariable(idFieldName, arguments).orElseGet(NullValue::new);
    return buildUniqueItems(
        field,
        objectType,
        "",
        inputValues,
        inputValue ->
            arguments
                .getArgumentOrEmpty(inputValue.getName())
                .or(() -> Optional.ofNullable(inputValue.getDefaultValue())),
        currentId,
        skipIfIdPresent);
  }

  private Stream<UniqueItem> buildUniqueItems(
      Field field,
      ObjectType objectType,
      String path,
      Collection<InputValue> inputValues,
      ObjectValueWithVariable objectValueWithVariable,
      String idFieldName,
      boolean skipIfIdPresent) {
    if (objectValueWithVariable == null
        || (skipIfIdPresent
            && getIDValueWithVariable(idFieldName, objectValueWithVariable).isPresent())) {
      return Stream.empty();
    }
    ValueWithVariable currentId =
        getIDValueWithVariable(idFieldName, objectValueWithVariable).orElseGet(NullValue::new);
    return buildUniqueItems(
        field,
        objectType,
        path,
        inputValues,
        inputValue ->
            objectValueWithVariable
                .getValueWithVariableOrEmpty(inputValue.getName())
                .or(() -> Optional.ofNullable(inputValue.getDefaultValue())),
        currentId,
        skipIfIdPresent);
  }

  private Stream<UniqueItem> buildUniqueItems(
      Field field,
      ObjectType objectType,
      String path,
      Collection<InputValue> inputValues,
      Function<InputValue, Optional<ValueWithVariable>> valueProvider,
      ValueWithVariable currentId,
      boolean skipIfIdPresent) {
    Stream<UniqueItem> current =
        buildCombinedUniqueItem(field, objectType, path, inputValues, valueProvider, currentId)
            .stream();
    Stream<UniqueItem> nested =
        inputValues.stream()
            .flatMap(
                inputValue ->
                    Stream.ofNullable(objectType.getField(inputValue.getName()))
                        .flatMap(
                            subFieldDefinition ->
                                valueProvider.apply(inputValue).stream()
                                    .flatMap(
                                        valueWithVariable ->
                                            buildNestedUniqueItems(
                                                field,
                                                path,
                                                subFieldDefinition,
                                                inputValue,
                                                valueWithVariable,
                                                skipIfIdPresent))));
    return Stream.concat(current, nested);
  }

  private Stream<UniqueItem> buildNestedUniqueItems(
      Field field,
      String path,
      FieldDefinition fieldDefinition,
      InputValue inputValue,
      ValueWithVariable valueWithVariable,
      boolean skipIfIdPresent) {
    if (valueWithVariable == null || valueWithVariable.isNull()) {
      return Stream.empty();
    }
    Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
    if (fieldTypeDefinition == null
        || !fieldTypeDefinition.isObject()
        || fieldTypeDefinition.isContainer()) {
      return Stream.empty();
    }
    ObjectType nestedObjectType = fieldTypeDefinition.asObject();
    FieldDefinition idField = nestedObjectType.getIDFieldOrError();
    Collection<InputValue> nestedInputValues =
        documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues();
    if (fieldDefinition.getType().hasList()) {
      return IntStream.range(0, valueWithVariable.asArray().size())
          .boxed()
          .flatMap(
              index ->
                  buildUniqueItems(
                      field,
                      nestedObjectType,
                      path + "/" + fieldDefinition.getName() + "/" + index,
                      nestedInputValues,
                      valueWithVariable.asArray().getValueWithVariable(index).asObject(),
                      idField.getName(),
                      skipIfIdPresent));
    }
    return buildUniqueItems(
        field,
        nestedObjectType,
        path + "/" + fieldDefinition.getName(),
        nestedInputValues,
        valueWithVariable.asObject(),
        idField.getName(),
        skipIfIdPresent);
  }

  private Optional<UniqueItem> buildCombinedUniqueItem(
      Field field,
      ObjectType objectType,
      String path,
      Collection<InputValue> inputValues,
      Function<InputValue, Optional<ValueWithVariable>> valueProvider,
      ValueWithVariable currentId) {
    if (inputValues == null) {
      return Optional.empty();
    }
    Map<String, ValueWithVariable> uniqueValues = new LinkedHashMap<>();
    LinkedHashSet<String> errorPaths = new LinkedHashSet<>();
    long uniqueFieldCount =
        collectCombinedUniqueValues(
            objectType, path, inputValues, valueProvider, "", uniqueValues, errorPaths);
    if (uniqueFieldCount > 0 && uniqueValues.size() == uniqueFieldCount) {
      return Optional.of(
          new UniqueItem(
              objectType, uniqueValues, field, path, List.copyOf(errorPaths), currentId));
    }
    return Optional.empty();
  }

  private JsonObject buildUniqueExpression(UniqueItem uniqueItem) {
    Map<String, Object> expression = new LinkedHashMap<>();
    uniqueItem
        .getUniqueValues()
        .forEach(
            (fieldName, valueWithVariable) ->
                putNestedExpression(expression, splitPath(fieldName), valueWithVariable));
    return new ObjectValueWithVariable(expression);
  }

  private long collectCombinedUniqueValues(
      ObjectType objectType,
      String path,
      Collection<InputValue> inputValues,
      Function<InputValue, Optional<ValueWithVariable>> valueProvider,
      String keyPrefix,
      Map<String, ValueWithVariable> uniqueValues,
      LinkedHashSet<String> errorPaths) {
    long uniqueFieldCount = 0;
    for (InputValue inputValue : inputValues) {
      FieldDefinition subFieldDefinition = objectType.getField(inputValue.getName());
      if (subFieldDefinition == null || !subFieldDefinition.isUnique()) {
        continue;
      }
      Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(subFieldDefinition);
      Optional<ValueWithVariable> currentValue = valueProvider.apply(inputValue);
      String fieldKey =
          keyPrefix.isEmpty()
              ? subFieldDefinition.getName()
              : keyPrefix + "." + subFieldDefinition.getName();
      String fieldPath = path + "/" + subFieldDefinition.getName();
      if (fieldTypeDefinition != null
          && fieldTypeDefinition.isObject()
          && !fieldTypeDefinition.isContainer()) {
        Collection<InputValue> nestedInputValues =
            documentManager
                .getInputValueTypeDefinition(inputValue)
                .asInputObject()
                .getInputValues();
        uniqueFieldCount +=
            collectCombinedUniqueValues(
                fieldTypeDefinition.asObject(),
                fieldPath,
                nestedInputValues,
                buildNestedValueProvider(currentValue.orElse(null)),
                fieldKey,
                uniqueValues,
                errorPaths);
      } else {
        uniqueFieldCount++;
        errorPaths.add(fieldPath);
        currentValue
            .filter(value -> !value.isNull())
            .ifPresent(withVariable -> uniqueValues.put(fieldKey, withVariable));
      }
    }
    return uniqueFieldCount;
  }

  private Function<InputValue, Optional<ValueWithVariable>> buildNestedValueProvider(
      ValueWithVariable parentValue) {
    if (parentValue == null || parentValue.isNull() || !parentValue.isObject()) {
      return inputValue -> Optional.empty();
    }
    return inputValue ->
        parentValue
            .asObject()
            .getValueWithVariableOrEmpty(inputValue.getName())
            .or(() -> Optional.ofNullable(inputValue.getDefaultValue()));
  }

  private void putNestedExpression(
      Map<String, Object> expression, List<String> path, ValueWithVariable valueWithVariable) {
    Map<String, Object> current = expression;
    for (int index = 0; index < path.size(); index++) {
      String segment = path.get(index);
      if (index == path.size() - 1) {
        Map<String, Object> operator = new LinkedHashMap<>();
        operator.put(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME, valueWithVariable);
        current.put(segment, operator);
      } else {
        Object nestedExpression = current.get(segment);
        if (nestedExpression == null) {
          Map<String, Object> childExpression = new LinkedHashMap<>();
          current.put(segment, childExpression);
          current = childExpression;
        } else {
          current = asExpressionMap(nestedExpression, segment);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> asExpressionMap(Object value, String segment) {
    if (!(value instanceof Map<?, ?>)) {
      throw new IllegalStateException("expression segment '" + segment + "' is not an object");
    }
    return (Map<String, Object>) value;
  }

  private List<String> splitPath(String fieldName) {
    return List.of(fieldName.split("\\."));
  }

  private JsonValue getValueFromPath(JsonObject jsonObject, String fieldPath) {
    JsonValue current = jsonObject;
    for (String segment : splitPath(fieldPath)) {
      if (current == null || !JsonValue.ValueType.OBJECT.equals(current.getValueType())) {
        return null;
      }
      JsonObject currentObject = current.asJsonObject();
      if (!currentObject.containsKey(segment) || currentObject.isNull(segment)) {
        return null;
      }
      current = currentObject.get(segment);
    }
    return current;
  }

  private List<Field> buildUniqueSelectionFields(List<UniqueItem> uniqueItems) {
    LinkedHashMap<String, SelectionNode> selectionTree = new LinkedHashMap<>();
    uniqueItems.stream()
        .flatMap(uniqueItem -> uniqueItem.getUniqueValues().keySet().stream())
        .distinct()
        .forEach(fieldPath -> addSelectionPath(selectionTree, splitPath(fieldPath), 0));
    return buildSelectionFields(selectionTree);
  }

  private void addSelectionPath(
      LinkedHashMap<String, SelectionNode> selectionTree, List<String> segments, int index) {
    if (index >= segments.size()) {
      return;
    }
    SelectionNode node =
        selectionTree.computeIfAbsent(segments.get(index), key -> new SelectionNode());
    addSelectionPath(node.children, segments, index + 1);
  }

  private List<Field> buildSelectionFields(LinkedHashMap<String, SelectionNode> tree) {
    return tree.entrySet().stream()
        .map(
            entry -> {
              Field field = new Field(entry.getKey());
              List<Field> childSelections = buildSelectionFields(entry.getValue().children);
              if (!childSelections.isEmpty()) {
                field.addSelections(childSelections);
              }
              return field;
            })
        .collect(Collectors.toList());
  }

  private static final class SelectionNode {

    private final LinkedHashMap<String, SelectionNode> children = new LinkedHashMap<>();
  }
}
