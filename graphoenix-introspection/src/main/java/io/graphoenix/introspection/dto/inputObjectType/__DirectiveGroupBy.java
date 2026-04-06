package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Group Input for __Directive
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Group Input for __Directive")
public class __DirectiveGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  private Collection<String> fieldNames;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  private __SchemaGroupBy ofSchema;

  /**
   * args
   */
  @Description("args")
  private __InputValueGroupBy args;

  /**
   * Relationship Object between __Directive and locations
   */
  @Description("Relationship Object between __Directive and locations")
  private __DirectiveLocationsRelationGroupBy __directiveLocationsRelation;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<__DirectiveGroupBy> gbs;

  public Collection<String> getFieldNames() {
    return this.fieldNames;
  }

  public void setFieldNames(Collection<String> fieldNames) {
    this.fieldNames = fieldNames;
  }

  public __SchemaGroupBy getOfSchema() {
    return this.ofSchema;
  }

  public void setOfSchema(__SchemaGroupBy ofSchema) {
    this.ofSchema = ofSchema;
  }

  public __InputValueGroupBy getArgs() {
    return this.args;
  }

  public void setArgs(__InputValueGroupBy args) {
    this.args = args;
  }

  public __DirectiveLocationsRelationGroupBy get__directiveLocationsRelation() {
    return this.__directiveLocationsRelation;
  }

  public void set__directiveLocationsRelation(
      __DirectiveLocationsRelationGroupBy __directiveLocationsRelation) {
    this.__directiveLocationsRelation = __directiveLocationsRelation;
  }

  public Collection<__DirectiveGroupBy> getGbs() {
    return this.gbs;
  }

  public void setGbs(Collection<__DirectiveGroupBy> gbs) {
    this.gbs = gbs;
  }
}
