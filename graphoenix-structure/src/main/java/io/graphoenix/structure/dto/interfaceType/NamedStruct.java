package io.graphoenix.structure.dto.interfaceType;

import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Interface;

@Interface
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public interface NamedStruct {
  String name = null;

  String description = null;

  default String getName() {
    return name;
  }

  void setName(String name);

  default String getDescription() {
    return description;
  }

  void setDescription(String description);
}
