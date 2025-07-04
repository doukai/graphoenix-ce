package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.introspection.dto.enumType.__DirectiveLocation;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Query Expression Input for __DirectiveLocation
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for __DirectiveLocation")
public class __DirectiveLocationExpression {
  /**
   * Operators
   */
  @DefaultValue("EQ")
  @Description("Operators")
  private Operator opr = Operator.EQ;

  /**
   * Value
   */
  @Description("Value")
  private __DirectiveLocation val;

  /**
   * Array
   */
  @Description("Array")
  private Collection<__DirectiveLocation> arr;

  public Operator getOpr() {
    return this.opr;
  }

  public void setOpr(Operator opr) {
    this.opr = opr;
  }

  public __DirectiveLocation getVal() {
    return this.val;
  }

  public void setVal(__DirectiveLocation val) {
    this.val = val;
  }

  public Collection<__DirectiveLocation> getArr() {
    return this.arr;
  }

  public void setArr(Collection<__DirectiveLocation> arr) {
    this.arr = arr;
  }
}
