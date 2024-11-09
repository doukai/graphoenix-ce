package io.graphoenix.core.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Invoke {
  private String className;

  private String methodName;

  private Collection<InvokeParameter> parameters;

  private String returnClassName;

  private Collection<String> thrownTypes;

  @DefaultValue("false")
  private Boolean async = false;

  private String directiveName;

  @DefaultValue("false")
  private Boolean onField = false;

  @DefaultValue("false")
  private Boolean onInputValue = false;

  @DefaultValue("false")
  private Boolean onExpression = false;

  public String getClassName() {
    return this.className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getMethodName() {
    return this.methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Collection<InvokeParameter> getParameters() {
    return this.parameters;
  }

  public void setParameters(Collection<InvokeParameter> parameters) {
    this.parameters = parameters;
  }

  public String getReturnClassName() {
    return this.returnClassName;
  }

  public void setReturnClassName(String returnClassName) {
    this.returnClassName = returnClassName;
  }

  public Collection<String> getThrownTypes() {
    return this.thrownTypes;
  }

  public void setThrownTypes(Collection<String> thrownTypes) {
    this.thrownTypes = thrownTypes;
  }

  public Boolean getAsync() {
    return this.async;
  }

  public void setAsync(Boolean async) {
    this.async = async;
  }

  public String getDirectiveName() {
    return this.directiveName;
  }

  public void setDirectiveName(String directiveName) {
    this.directiveName = directiveName;
  }

  public Boolean getOnField() {
    return this.onField;
  }

  public void setOnField(Boolean onField) {
    this.onField = onField;
  }

  public Boolean getOnInputValue() {
    return this.onInputValue;
  }

  public void setOnInputValue(Boolean onInputValue) {
    this.onInputValue = onInputValue;
  }

  public Boolean getOnExpression() {
    return this.onExpression;
  }

  public void setOnExpression(Boolean onExpression) {
    this.onExpression = onExpression;
  }
}
