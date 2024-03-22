package io.graphoenix.jsonschema.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Input;
import org.eclipse.microprofile.graphql.NonNull;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Property {
  @NonNull
  private String name;

  private JsonSchema validation;

  private Collection<String> required;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public JsonSchema getValidation() {
    return this.validation;
  }

  public void setValidation(JsonSchema validation) {
    this.validation = validation;
  }

  public Collection<String> getRequired() {
    return this.required;
  }

  public void setRequired(Collection<String> required) {
    this.required = required;
  }
}
