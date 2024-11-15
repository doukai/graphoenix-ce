package io.graphoenix.core.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.core.dto.enumType.Protocol;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Query Expression Input for Protocol
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for Protocol")
public class ProtocolExpression {
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
  private Protocol val;

  /**
   * Array
   */
  @Description("Array")
  private Collection<Protocol> arr;

  public Operator getOpr() {
    return this.opr;
  }

  public void setOpr(Operator opr) {
    this.opr = opr;
  }

  public Protocol getVal() {
    return this.val;
  }

  public void setVal(Protocol val) {
    this.val = val;
  }

  public Collection<Protocol> getArr() {
    return this.arr;
  }

  public void setArr(Collection<Protocol> arr) {
    this.arr = arr;
  }
}
