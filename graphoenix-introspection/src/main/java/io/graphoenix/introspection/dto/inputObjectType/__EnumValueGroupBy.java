package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Group Input for __EnumValue
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Group Input for __EnumValue")
public class __EnumValueGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  private Collection<String> by;

  /**
   * ofType
   */
  @Description("ofType")
  private __TypeGroupBy ofType;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<__EnumValueGroupBy> gbs;

  public Collection<String> getBy() {
    return this.by;
  }

  public void setBy(Collection<String> by) {
    this.by = by;
  }

  public __TypeGroupBy getOfType() {
    return this.ofType;
  }

  public void setOfType(__TypeGroupBy ofType) {
    this.ofType = ofType;
  }

  public Collection<__EnumValueGroupBy> getGbs() {
    return this.gbs;
  }

  public void setGbs(Collection<__EnumValueGroupBy> gbs) {
    this.gbs = gbs;
  }
}
