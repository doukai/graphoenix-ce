package io.graphoenix.showcase.user.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Query_currentTime_Arguments {
  private String local;

  public String getLocal() {
    return this.local;
  }

  public void setLocal(String local) {
    this.local = local;
  }
}
