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

  default Integer getVersion() {
    return version;
  }

  void setVersion(Integer version);

  default Integer getRealmId() {
    return realmId;
  }

  void setRealmId(Integer realmId);

  default String getCreateUserId() {
    return createUserId;
  }

  void setCreateUserId(String createUserId);

  default LocalDateTime getCreateTime() {
    return createTime;
  }

  void setCreateTime(LocalDateTime createTime);

  default String getUpdateUserId() {
    return updateUserId;
  }

  void setUpdateUserId(String updateUserId);

  default LocalDateTime getUpdateTime() {
    return updateTime;
  }

  void setUpdateTime(LocalDateTime updateTime);

  default String getCreateGroupId() {
    return createGroupId;
  }

  void setCreateGroupId(String createGroupId);
}
