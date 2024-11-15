package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Edge Object for Relationship Object between __Directive and locations
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Edge Object for Relationship Object between __Directive and locations")
public class __DirectiveLocationsRelationEdge {
  /**
   * Node
   */
  @Description("Node")
  private __DirectiveLocationsRelation node;

  /**
   * Cursor
   */
  @Description("Cursor")
  private String cursor;

  public __DirectiveLocationsRelation getNode() {
    return this.node;
  }

  public void setNode(__DirectiveLocationsRelation node) {
    this.node = node;
  }

  public String getCursor() {
    return this.cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }
}
