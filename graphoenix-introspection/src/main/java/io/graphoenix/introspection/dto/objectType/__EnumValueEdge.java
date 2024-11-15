package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Edge Object for __EnumValue
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Edge Object for __EnumValue")
public class __EnumValueEdge {
  /**
   * Node
   */
  @Description("Node")
  private __EnumValue node;

  /**
   * Cursor
   */
  @Description("Cursor")
  private String cursor;

  public __EnumValue getNode() {
    return this.node;
  }

  public void setNode(__EnumValue node) {
    this.node = node;
  }

  public String getCursor() {
    return this.cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }
}
