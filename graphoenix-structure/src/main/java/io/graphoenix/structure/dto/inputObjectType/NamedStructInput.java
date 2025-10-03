package io.graphoenix.structure.dto.inputObjectType;

import io.graphoenix.core.dto.inputObjectType.MetaInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Mutation Input for NamedStruct
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Input for NamedStruct")
public interface NamedStructInput extends MetaInput {
  /**
   * name
   */
  @Description("name")
  String name = null;

  /**
   * description
   */
  @Description("description")
  String description = null;

  /**
   * id
   */
  @Description("id")
  String id = null;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  Boolean isDeprecated = false;

  /**
   * version
   */
  @Description("version")
  Integer version = null;

  /**
   * realmId
   */
  @Description("realmId")
  Integer realmId = null;

  /**
   * createUserId
   */
  @Description("createUserId")
  String createUserId = null;

  /**
   * createTime
   */
  @Description("createTime")
  LocalDateTime createTime = null;

  /**
   * updateUserId
   */
  @Description("updateUserId")
  String updateUserId = null;

  /**
   * updateTime
   */
  @Description("updateTime")
  LocalDateTime updateTime = null;

  /**
   * createGroupId
   */
  @Description("createGroupId")
  String createGroupId = null;

  default String getName() {
    return name;
  }

  void setName(String name);

  default String getDescription() {
    return description;
  }

  void setDescription(String description);

  default String getId() {
    return id;
  }

  void setId(String id);

  default Boolean getIsDeprecated() {
    return isDeprecated;
  }

  void setIsDeprecated(Boolean isDeprecated);

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
