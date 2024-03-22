package io.graphoenix.jsonschema.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Query {
  private String jsonSchema;

  public String getJsonSchema() {
    return this.jsonSchema;
  }

  public void setJsonSchema(String jsonSchema) {
    this.jsonSchema = jsonSchema;
  }
}
