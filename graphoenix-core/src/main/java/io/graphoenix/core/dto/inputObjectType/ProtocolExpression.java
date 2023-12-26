package io.graphoenix.core.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.core.dto.enumType.Protocol;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class ProtocolExpression {
  @DefaultValue("EQ")
  private Operator opr;

  private Protocol val;

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
