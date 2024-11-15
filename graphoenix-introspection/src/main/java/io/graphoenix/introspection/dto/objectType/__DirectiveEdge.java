package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Edge Object for __Directive
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Edge Object for __Directive")
public class __DirectiveEdge {
  /**
   * Node
   */
  @Description("Node")
  private __Directive node;

  /**
   * Cursor
   */
  @Description("Cursor")
  private String cursor;

  public __Directive getNode() {
    return this.node;
  }

  public void setNode(__Directive node) {
    this.node = node;
  }

  public String getCursor() {
    return this.cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }
}
