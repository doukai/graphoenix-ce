package io.graphoenix.core.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Func;
import io.graphoenix.core.dto.enumType.Operator;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Query Expression Input for Func
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for Func")
public class FuncExpression {
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
  private Func val;

  /**
   * Array
   */
  @Description("Array")
  private Collection<Func> arr;

  public Operator getOpr() {
    return this.opr;
  }

  public void setOpr(Operator opr) {
    this.opr = opr;
  }

  public Func getVal() {
    return this.val;
  }

  public void setVal(Func val) {
    this.val = val;
  }

  public Collection<Func> getArr() {
    return this.arr;
  }

  public void setArr(Collection<Func> arr) {
    this.arr = arr;
  }
}
