package io.graphoenix.spi.graphql.common;

import jakarta.json.JsonValue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import io.graphoenix.spi.utils.StringTemplateLoader;

public class NullValue implements ValueWithVariable, JsonValue {

  private final STGroup stGroupFile = StringTemplateLoader.load("stg/common/NullValue.stg");

  @Override
  public ValueType getValueType() {
    return ValueType.NULL;
  }

  @Override
  public boolean isNull() {
    return true;
  }

  @Override
  public String toString() {
    ST st = stGroupFile.getInstanceOf("nullValueDefinition");
    return st.render();
  }
}
