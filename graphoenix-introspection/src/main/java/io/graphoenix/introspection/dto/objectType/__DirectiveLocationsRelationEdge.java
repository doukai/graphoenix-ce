package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __DirectiveLocationsRelationEdge {
  private __DirectiveLocationsRelation node;

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
