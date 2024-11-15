package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Edge Object for __Type
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Edge Object for __Type")
public class __TypeEdge {
  /**
   * Node
   */
  @Description("Node")
  private __Type node;

  /**
   * Cursor
   */
  @Description("Cursor")
  private String cursor;

  public __Type getNode() {
    return this.node;
  }

  public void setNode(__Type node) {
    this.node = node;
  }

  public String getCursor() {
    return this.cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }
}
