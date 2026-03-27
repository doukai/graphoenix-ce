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
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        .addSelections(
            uniqueItems.stream()
                .flatMap(uniqueItem -> uniqueItem.getUniqueValues().keySet().stream())
                .distinct()
                .map(Field::new)
                .collect(Collectors.toList()));
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

  private String buildUniqueKey(
      Map<String, ValueWithVariable> uniqueValues, JsonObject jsonObject) {
    return uniqueValues.keySet().stream()
        .sorted(Comparator.naturalOrder())
        .filter(jsonObject::containsKey)
        .map(fieldName -> fieldName + "=" + jsonObject.get(fieldName))
        .collect(Collectors.joining("|"));
  }

  public Map<String, JsonObject> buildMatchedItemIndex(
      List<UniqueItem> uniqueItems, List<JsonObject> matchedItems) {
    if (uniqueItems.isEmpty() || matchedItems.isEmpty()) {
      return Map.of();
    }
    return matchedItems.stream()
        .collect(
            Collectors.toMap(
                item -> buildUniqueKey(uniqueItems.get(0).getUniqueValues(), item),
                Function.identity(),
                (left, right) -> left,
                LinkedHashMap::new));
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
    if (!fieldTypeDefinition.isObject() || fieldTypeDefinition.isContainer()) {
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
    long uniqueFieldCount = 0;
    for (InputValue inputValue : inputValues) {
      FieldDefinition subFieldDefinition = objectType.getField(inputValue.getName());
      if (subFieldDefinition != null && subFieldDefinition.isUnique()) {
        uniqueFieldCount++;
        errorPaths.add(path + "/" + subFieldDefinition.getName());
        Optional<ValueWithVariable> valueWithVariable =
            valueProvider.apply(inputValue).filter(value -> !value.isNull());
        valueWithVariable.ifPresent(
            withVariable -> uniqueValues.put(subFieldDefinition.getName(), withVariable));
      }
    }
    if (uniqueFieldCount > 0 && uniqueValues.size() == uniqueFieldCount) {
      return Optional.of(
          new UniqueItem(
              objectType, uniqueValues, field, path, List.copyOf(errorPaths), currentId));
    }
    return Optional.empty();
  }

  private JsonObject buildUniqueExpression(UniqueItem uniqueItem) {
    jakarta.json.JsonObjectBuilder expressionBuilder = jsonProvider.createObjectBuilder();
    uniqueItem
        .getUniqueValues()
        .forEach(
            (fieldName, valueWithVariable) ->
                expressionBuilder.add(
                    fieldName,
                    jsonProvider
                        .createObjectBuilder()
                        .add(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME, valueWithVariable)));
    return expressionBuilder.build();
  }
}
