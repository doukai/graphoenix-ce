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
 * Mutation Input for TreeStruct
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Input for TreeStruct")
public interface TreeStructInput extends MetaInput {
  /**
   * name
   */
  @Description("name")
  String name = null;

  /**
   * path
   */
  @Description("path")
  String path = null;

  /**
   * deep
   */
  @Description("deep")
  Integer deep = null;

  /**
   * parentId
   */
  @Description("parentId")
  String parentId = null;

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

  String getName();

  void setName(String name);

  String getPath();

  void setPath(String path);

  Integer getDeep();

  void setDeep(Integer deep);

  String getParentId();

  void setParentId(String parentId);

  Boolean getIsDeprecated();

  void setIsDeprecated(Boolean isDeprecated);

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
