package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Edge Object for __InputValue
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Edge Object for __InputValue")
public class __InputValueEdge {
  /**
   * Node
   */
  @Description("Node")
  private __InputValue node;

  /**
   * Cursor
   */
  @Description("Cursor")
  private String cursor;

  public __InputValue getNode() {
    return this.node;
  }

  public void setNode(__InputValue node) {
    this.node = node;
  }

  public String getCursor() {
    return this.cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }
}
