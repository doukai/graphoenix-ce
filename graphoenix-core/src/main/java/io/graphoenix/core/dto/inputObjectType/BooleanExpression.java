package io.graphoenix.core.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Operator;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class BooleanExpression {
  @DefaultValue("EQ")
  private Operator opr = Operator.EQ;

  private Boolean val;

  private Collection<Boolean> arr;

  @DefaultValue("false")
  private Boolean skipNull = false;

  public Operator getOpr() {
    return this.opr;
  }

  public void setOpr(Operator opr) {
    this.opr = opr;
  }

  public Boolean getVal() {
    return this.val;
  }

  public void setVal(Boolean val) {
    this.val = val;
  }

  public Collection<Boolean> getArr() {
    return this.arr;
  }

  public void setArr(Collection<Boolean> arr) {
    this.arr = arr;
  }

  public Boolean getSkipNull() {
    return this.skipNull;
  }

  public void setSkipNull(Boolean skipNull) {
    this.skipNull = skipNull;
  }
}
