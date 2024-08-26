package io.graphoenix.file.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Mutation_multipleUpload_Arguments {
  private Collection<String> files;

  public Collection<String> getFiles() {
    return this.files;
  }

  public void setFiles(Collection<String> files) {
    this.files = files;
  }
}
