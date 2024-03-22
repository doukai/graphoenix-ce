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

  private String operationSchema;

  public String getJsonSchema() {
    return this.jsonSchema;
  }

  public void setJsonSchema(String jsonSchema) {
    this.jsonSchema = jsonSchema;
  }

  public String getOperationSchema() {
    return this.operationSchema;
  }

  public void setOperationSchema(String operationSchema) {
    this.operationSchema = operationSchema;
  }
}
