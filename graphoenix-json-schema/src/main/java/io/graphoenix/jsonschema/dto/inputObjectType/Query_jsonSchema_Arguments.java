package io.graphoenix.jsonschema.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Query_jsonSchema_Arguments {
  private String jsonSchemaName;

  public String getJsonSchemaName() {
    return this.jsonSchemaName;
  }

  public void setJsonSchemaName(String jsonSchemaName) {
    this.jsonSchemaName = jsonSchemaName;
  }
}
