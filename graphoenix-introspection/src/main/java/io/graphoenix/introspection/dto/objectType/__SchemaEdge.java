package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Edge Object for __Schema
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Edge Object for __Schema")
public class __SchemaEdge {
  /**
   * Node
   */
  @Description("Node")
  private __Schema node;

  /**
   * Cursor
   */
  @Description("Cursor")
  private String cursor;

  public __Schema getNode() {
    return this.node;
  }

  public void setNode(__Schema node) {
    this.node = node;
  }

  public String getCursor() {
    return this.cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }
}
