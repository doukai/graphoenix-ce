package io.graphoenix.showcase.user.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Query_appName2_Arguments {
  private RoleInput role;

  public RoleInput getRole() {
    return this.role;
  }

  public void setRole(RoleInput role) {
    this.role = role;
  }
}
