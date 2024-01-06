package io.graphoenix.showcase.user.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Query_age_Arguments {
  private UserInput user;

  public UserInput getUser() {
    return this.user;
  }

  public void setUser(UserInput user) {
    this.user = user;
  }
}
