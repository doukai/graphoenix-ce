package io.graphoenix.core.dto.inputObjectType;

import jakarta.annotation.Generated;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.Input;

@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public interface MetaInput {
  Integer version = null;

  Integer realmId = null;

  String createUserId = null;

  LocalDateTime createTime = null;

  String updateUserId = null;

  LocalDateTime updateTime = null;

  String createGroupId = null;

  Integer getVersion();

  void setVersion(Integer version);

  Integer getRealmId();

  void setRealmId(Integer realmId);

  String getCreateUserId();

  void setCreateUserId(String createUserId);

  LocalDateTime getCreateTime();

  void setCreateTime(LocalDateTime createTime);

  String getUpdateUserId();

  void setUpdateUserId(String updateUserId);

  LocalDateTime getUpdateTime();

  void setUpdateTime(LocalDateTime updateTime);

  String getCreateGroupId();

  void setCreateGroupId(String createGroupId);
}
