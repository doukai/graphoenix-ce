package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;
import org.eclipse.microprofile.graphql.Name;

/**
 * Group Input for Relationship Object between __Type and __Type
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Group Input for Relationship Object between __Type and __Type")
public class __TypeInterfacesGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  private Collection<String> fieldNames;

  /**
   * __Type
   */
  @Description("__Type")
  private __TypeGroupBy type;

  /**
   * __Type
   */
  @Name("interface")
  @Description("__Type")
  private __TypeGroupBy _interface;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<__TypeInterfacesGroupBy> gbs;

  public Collection<String> getFieldNames() {
    return this.fieldNames;
  }

  public void setFieldNames(Collection<String> fieldNames) {
    this.fieldNames = fieldNames;
  }

  public __TypeGroupBy getType() {
    return this.type;
  }

  public void setType(__TypeGroupBy type) {
    this.type = type;
  }

  public __TypeGroupBy get_interface() {
    return this._interface;
  }

  public void set_interface(__TypeGroupBy _interface) {
    this._interface = _interface;
  }

  public Collection<__TypeInterfacesGroupBy> getGbs() {
    return this.gbs;
  }

  public void setGbs(Collection<__TypeInterfacesGroupBy> gbs) {
    this.gbs = gbs;
  }
}
