package io.graphoenix.core.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.core.dto.enumType.__DirectiveLocation;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __DirectiveLocationExpression {
  @DefaultValue("EQ")
  private Operator opr;

  private __DirectiveLocation val;

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
