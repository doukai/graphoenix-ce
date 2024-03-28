package io.graphoenix.structure.dto.interfaceType;

import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Interface;

@Interface
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public interface NamedStruct {
  String name = null;

  String description = null;

  String getName();

  void setName(String name);

  String getDescription();

  void setDescription(String description);
}
