package io.graphoenix.introspection.dto.inputObjectType;

import io.graphoenix.core.dto.inputObjectType.MetaInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Mutation Input for __Field
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Input for __Field")
public interface __FieldInputBase extends MetaInput {
  /**
   * id
   */
  @Description("id")
  String id = null;

  /**
   * name
   */
  @Description("name")
  String name = null;

  /**
   * ofType
   */
  @Description("ofType")
  __TypeInput ofType = null;

  /**
   * description
   */
  @Description("description")
  String description = null;

  /**
   * args
   */
  @Description("args")
  Collection<__InputValueInput> args = null;

  /**
   * type
   */
  @Description("type")
  __TypeInput type = null;

  /**
   * deprecationReason
   */
  @Description("deprecationReason")
  String deprecationReason = null;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  Boolean isDeprecated = false;

  /**
   * Version
   */
  @Description("Version")
  Integer version = null;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  Integer realmId = null;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  String createUserId = null;

  /**
   * Create Time
   */
  @Description("Create Time")
  LocalDateTime createTime = null;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  String updateUserId = null;

  /**
   * Update Time
   */
  @Description("Update Time")
  LocalDateTime updateTime = null;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  String createGroupId = null;

  /**
   * Type Name
   */
  @Description("Type Name")
  String __typename = "__Field";

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  String ofTypeName = null;

  /**
   * type Reference
   */
  @Description("type Reference")
  String typeName = null;

  /**
   * Where
   */
  @Description("Where")
  __FieldExpression where = null;

  default String getId() {
    return id;
  }

  void setId(String id);

  default String getName() {
    return name;
  }

  void setName(String name);

  default __TypeInput getOfType() {
    return ofType;
  }

  void setOfType(__TypeInput ofType);

  default String getDescription() {
    return description;
  }

  void setDescription(String description);

  default Collection<__InputValueInput> getArgs() {
    return args;
  }

  void setArgs(Collection<__InputValueInput> args);

  default __TypeInput getType() {
    return type;
  }

  void setType(__TypeInput type);

  default String getDeprecationReason() {
    return deprecationReason;
  }

  void setDeprecationReason(String deprecationReason);

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

  default String get__typename() {
    return __typename;
  }

  void set__typename(String __typename);

  default String getOfTypeName() {
    return ofTypeName;
  }

  void setOfTypeName(String ofTypeName);

  default String getTypeName() {
    return typeName;
  }

  void setTypeName(String typeName);

  default __FieldExpression getWhere() {
    return where;
  }

  void setWhere(__FieldExpression where);
}
