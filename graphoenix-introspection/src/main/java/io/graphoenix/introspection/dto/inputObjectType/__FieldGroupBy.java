package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Group Input for __Field
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Group Input for __Field")
public class __FieldGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  private Collection<String> fieldNames;

  /**
   * ofType
   */
  @Description("ofType")
  private __TypeGroupBy ofType;

  /**
   * args
   */
  @Description("args")
  private __InputValueGroupBy args;

  /**
   * type
   */
  @Description("type")
  private __TypeGroupBy type;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<__FieldGroupBy> gbs;

  public Collection<String> getFieldNames() {
    return this.fieldNames;
  }

  public void setFieldNames(Collection<String> fieldNames) {
    this.fieldNames = fieldNames;
  }

  public __TypeGroupBy getOfType() {
    return this.ofType;
  }

  public void setOfType(__TypeGroupBy ofType) {
    this.ofType = ofType;
  }

  public __InputValueGroupBy getArgs() {
    return this.args;
  }

  public void setArgs(__InputValueGroupBy args) {
    this.args = args;
  }

  public __TypeGroupBy getType() {
    return this.type;
  }

  public void setType(__TypeGroupBy type) {
    this.type = type;
  }

  public Collection<__FieldGroupBy> getGbs() {
    return this.gbs;
  }

  public void setGbs(Collection<__FieldGroupBy> gbs) {
    this.gbs = gbs;
  }
}
