package io.graphoenix.core.handler.before;

import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.type.ObjectType;

import java.util.List;
import java.util.Map;

public class UniqueItem {

  private final ObjectType objectType;
  private final Map<String, ValueWithVariable> uniqueValues;
  private final Field field;
  private final String path;
  private final List<String> errorPaths;
  private final ValueWithVariable currentId;

  UniqueItem(
      ObjectType objectType,
      Map<String, ValueWithVariable> uniqueValues,
      Field field,
      String path,
      List<String> errorPaths,
      ValueWithVariable currentId) {
    this.objectType = objectType;
    this.uniqueValues = uniqueValues;
    this.field = field;
    this.path = path;
    this.errorPaths = errorPaths;
    this.currentId = currentId;
  }

  ObjectType getObjectType() {
    return objectType;
  }

  Map<String, ValueWithVariable> getUniqueValues() {
    return uniqueValues;
  }

  Field getField() {
    return field;
  }

  String getPath() {
    return path;
  }

  List<String> getErrorPaths() {
    return errorPaths;
  }

  ValueWithVariable getCurrentId() {
    return currentId;
  }
}
