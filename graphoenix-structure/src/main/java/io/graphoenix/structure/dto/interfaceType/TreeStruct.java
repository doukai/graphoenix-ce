package io.graphoenix.structure.dto.interfaceType;

import jakarta.annotation.Generated;
import java.lang.Integer;
import java.lang.String;
import org.eclipse.microprofile.graphql.Interface;

@Interface
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public interface TreeStruct {
  String name = null;

  String path = null;

  Integer deep = null;

  String parentId = null;

  default String getName() {
    return name;
  }

  void setName(String name);

  default String getPath() {
    return path;
  }

  void setPath(String path);

  default Integer getDeep() {
    return deep;
  }

  void setDeep(Integer deep);

  default String getParentId() {
    return parentId;
  }

  void setParentId(String parentId);
}
