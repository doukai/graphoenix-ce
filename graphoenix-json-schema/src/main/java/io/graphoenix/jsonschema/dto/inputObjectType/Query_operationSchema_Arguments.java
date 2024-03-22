package io.graphoenix.jsonschema.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Query_operationSchema_Arguments {
  private String operationType;

  private Collection<String> fieldNameSet;

  public String getOperationType() {
    return this.operationType;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  public Collection<String> getFieldNameSet() {
    return this.fieldNameSet;
  }

  public void setFieldNameSet(Collection<String> fieldNameSet) {
    this.fieldNameSet = fieldNameSet;
  }
}
