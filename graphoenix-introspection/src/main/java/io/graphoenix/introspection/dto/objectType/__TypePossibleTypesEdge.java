package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Edge Object for Relationship Object between __Type and __Type
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Edge Object for Relationship Object between __Type and __Type")
public class __TypePossibleTypesEdge {
  /**
   * Node
   */
  @Description("Node")
  private __TypePossibleTypes node;

  /**
   * Cursor
   */
  @Description("Cursor")
  private String cursor;

  public __TypePossibleTypes getNode() {
    return this.node;
  }

  public void setNode(__TypePossibleTypes node) {
    this.node = node;
  }

  public String getCursor() {
    return this.cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }
}
