package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Group Input for Relationship Object between __Type and __Type
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Group Input for Relationship Object between __Type and __Type")
public class __TypePossibleTypesGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  private Collection<String> by;

  /**
   * __Type
   */
  @Description("__Type")
  private __TypeGroupBy type;

  /**
   * __Type
   */
  @Description("__Type")
  private __TypeGroupBy possibleType;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<__TypePossibleTypesGroupBy> gbs;

  public Collection<String> getBy() {
    return this.by;
  }

  public void setBy(Collection<String> by) {
    this.by = by;
  }

  public __TypeGroupBy getType() {
    return this.type;
  }

  public void setType(__TypeGroupBy type) {
    this.type = type;
  }

  public __TypeGroupBy getPossibleType() {
    return this.possibleType;
  }

  public void setPossibleType(__TypeGroupBy possibleType) {
    this.possibleType = possibleType;
  }

  public Collection<__TypePossibleTypesGroupBy> getGbs() {
    return this.gbs;
  }

  public void setGbs(Collection<__TypePossibleTypesGroupBy> gbs) {
    this.gbs = gbs;
  }
}
