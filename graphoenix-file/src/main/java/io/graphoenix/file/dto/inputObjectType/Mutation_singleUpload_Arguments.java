package io.graphoenix.file.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Mutation_singleUpload_Arguments {
  private String file;

  public String getFile() {
    return this.file;
  }

  public void setFile(String file) {
    this.file = file;
  }
}
