package io.graphoenix.introspection.dto.inputObjectType;

import io.graphoenix.core.dto.inputObjectType.MetaInput;
import io.graphoenix.introspection.dto.enumType.__DirectiveLocation;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Mutation Input for Relationship Object between __Directive and locations
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Input for Relationship Object between __Directive and locations")
public interface __DirectiveLocationsRelationInputBase extends MetaInput {
  /**
   * ID
   */
  @Description("ID")
  String id = null;

  /**
   * __Directive Reference
   */
  @Description("__Directive Reference")
  String __directiveRef = null;

  /**
   * __Directive
   */
  @Description("__Directive")
  __DirectiveInput __directive = null;

  /**
   * locations Reference
   */
  @Description("locations Reference")
  __DirectiveLocation locationsRef = null;

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
  String __typename = "__DirectiveLocationsRelation";

  /**
   * Where
   */
  @Description("Where")
  __DirectiveLocationsRelationExpression where = null;

  default String getId() {
    return id;
  }

  void setId(String id);

  default String get__directiveRef() {
    return __directiveRef;
  }

  void set__directiveRef(String __directiveRef);

  default __DirectiveInput get__directive() {
    return __directive;
  }

  void set__directive(__DirectiveInput __directive);

  default __DirectiveLocation getLocationsRef() {
    return locationsRef;
  }

  void setLocationsRef(__DirectiveLocation locationsRef);

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

  default __DirectiveLocationsRelationExpression getWhere() {
    return where;
  }

  void setWhere(__DirectiveLocationsRelationExpression where);
}
