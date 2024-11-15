package io.graphoenix.core.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.enumType.Operator;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Query Expression Input for Conditional
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for Conditional")
public class ConditionalExpression {
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
  private Conditional val;

  /**
   * Array
   */
  @Description("Array")
  private Collection<Conditional> arr;

  public Operator getOpr() {
    return this.opr;
  }

  public void setOpr(Operator opr) {
    this.opr = opr;
  }

  public Conditional getVal() {
    return this.val;
  }

  public void setVal(Conditional val) {
    this.val = val;
  }

  public Collection<Conditional> getArr() {
    return this.arr;
  }

  public void setArr(Collection<Conditional> arr) {
    this.arr = arr;
  }
}
