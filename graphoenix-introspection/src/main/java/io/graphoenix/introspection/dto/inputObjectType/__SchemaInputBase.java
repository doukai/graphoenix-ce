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
 * Mutation Input for __Schema
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Input for __Schema")
public interface __SchemaInputBase extends MetaInput {
  /**
   * id
   */
  @Description("id")
  String id = null;

  /**
   * types
   */
  @Description("types")
  Collection<__TypeInput> types = null;

  /**
   * queryType
   */
  @Description("queryType")
  __TypeInput queryType = null;

  /**
   * mutationType
   */
  @Description("mutationType")
  __TypeInput mutationType = null;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  __TypeInput subscriptionType = null;

  /**
   * directives
   */
  @Description("directives")
  Collection<__DirectiveInput> directives = null;

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
  String __typename = "__Schema";

  /**
   * queryType Reference
   */
  @Description("queryType Reference")
  String queryTypeName = null;

  /**
   * mutationType Reference
   */
  @Description("mutationType Reference")
  String mutationTypeName = null;

  /**
   * subscriptionType Reference
   */
  @Description("subscriptionType Reference")
  String subscriptionTypeName = null;

  default String getId() {
    return id;
  }

  void setId(String id);

  default Collection<__TypeInput> getTypes() {
    return types;
  }

  void setTypes(Collection<__TypeInput> types);

  default __TypeInput getQueryType() {
    return queryType;
  }

  void setQueryType(__TypeInput queryType);

  default __TypeInput getMutationType() {
    return mutationType;
  }

  void setMutationType(__TypeInput mutationType);

  default __TypeInput getSubscriptionType() {
    return subscriptionType;
  }

  void setSubscriptionType(__TypeInput subscriptionType);

  default Collection<__DirectiveInput> getDirectives() {
    return directives;
  }

  void setDirectives(Collection<__DirectiveInput> directives);

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

  default String getQueryTypeName() {
    return queryTypeName;
  }

  void setQueryTypeName(String queryTypeName);

  default String getMutationTypeName() {
    return mutationTypeName;
  }

  void setMutationTypeName(String mutationTypeName);

  default String getSubscriptionTypeName() {
    return subscriptionTypeName;
  }

  void setSubscriptionTypeName(String subscriptionTypeName);
}
