package io.graphoenix.core.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Func;
import io.graphoenix.core.dto.enumType.Operator;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class FuncExpression {
  @DefaultValue("EQ")
  private Operator opr = Operator.EQ;

  private Func val;

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
