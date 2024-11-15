package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Edge Object for __Field
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Edge Object for __Field")
public class __FieldEdge {
  /**
   * Node
   */
  @Description("Node")
  private __Field node;

  /**
   * Cursor
   */
  @Description("Cursor")
  private String cursor;

  public __Field getNode() {
    return this.node;
  }

  public void setNode(__Field node) {
    this.node = node;
  }

  public String getCursor() {
    return this.cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }
}
