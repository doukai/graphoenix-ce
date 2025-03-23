package io.graphoenix.structure.dto.interfaceType;

import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Interface;

@Interface
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public interface TreeStruct {
  String parentId = null;

  String name = null;

  default String getParentId() {
    return parentId;
  }

  void setParentId(String parentId);

  default String getName() {
    return name;
  }

  void setName(String name);
}
