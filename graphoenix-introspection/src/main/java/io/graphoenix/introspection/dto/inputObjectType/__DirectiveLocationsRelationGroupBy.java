package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Group Input for Relationship Object between __Directive and locations
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Group Input for Relationship Object between __Directive and locations")
public class __DirectiveLocationsRelationGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  private Collection<String> fieldNames;

  /**
   * __Directive
   */
  @Description("__Directive")
  private __DirectiveGroupBy __directive;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<__DirectiveLocationsRelationGroupBy> gbs;

  public Collection<String> getFieldNames() {
    return this.fieldNames;
  }

  public void setFieldNames(Collection<String> fieldNames) {
    this.fieldNames = fieldNames;
  }

  public __DirectiveGroupBy get__directive() {
    return this.__directive;
  }

  public void set__directive(__DirectiveGroupBy __directive) {
    this.__directive = __directive;
  }

  public Collection<__DirectiveLocationsRelationGroupBy> getGbs() {
    return this.gbs;
  }

  public void setGbs(Collection<__DirectiveLocationsRelationGroupBy> gbs) {
    this.gbs = gbs;
  }
}
