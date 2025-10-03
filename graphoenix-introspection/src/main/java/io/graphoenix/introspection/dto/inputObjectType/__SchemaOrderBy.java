package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for __Schema
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Order Input for __Schema")
public class __SchemaOrderBy {
  /**
   * id
   */
  @Description("id")
  private Sort id;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  private Sort isDeprecated;

  /**
   * Version
   */
  @Description("Version")
  private Sort version;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  private Sort realmId;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  private Sort createUserId;

  /**
   * Create Time
   */
  @Description("Create Time")
  private Sort createTime;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  private Sort updateUserId;

  /**
   * Update Time
   */
  @Description("Update Time")
  private Sort updateTime;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  private Sort createGroupId;

  /**
   * Type Name
   */
  @Description("Type Name")
  private Sort __typename;

  /**
   * queryType Reference
   */
  @Description("queryType Reference")
  private Sort queryTypeName;

  /**
   * mutationType Reference
   */
  @Description("mutationType Reference")
  private Sort mutationTypeName;

  /**
   * subscriptionType Reference
   */
  @Description("subscriptionType Reference")
  private Sort subscriptionTypeName;

  /**
   * Count of __Schema
   */
  @Description("Count of __Schema")
  private Sort idCount;

  /**
   * Max of id
   */
  @Description("Max of id")
  private Sort idMax;

  /**
   * Min of id
   */
  @Description("Min of id")
  private Sort idMin;

  /**
   * Count of queryType Reference
   */
  @Description("Count of queryType Reference")
  private Sort queryTypeNameCount;

  /**
   * Max of queryType Reference
   */
  @Description("Max of queryType Reference")
  private Sort queryTypeNameMax;

  /**
   * Min of queryType Reference
   */
  @Description("Min of queryType Reference")
  private Sort queryTypeNameMin;

  /**
   * Count of mutationType Reference
   */
  @Description("Count of mutationType Reference")
  private Sort mutationTypeNameCount;

  /**
   * Max of mutationType Reference
   */
  @Description("Max of mutationType Reference")
  private Sort mutationTypeNameMax;

  /**
   * Min of mutationType Reference
   */
  @Description("Min of mutationType Reference")
  private Sort mutationTypeNameMin;

  /**
   * Count of subscriptionType Reference
   */
  @Description("Count of subscriptionType Reference")
  private Sort subscriptionTypeNameCount;

  /**
   * Max of subscriptionType Reference
   */
  @Description("Max of subscriptionType Reference")
  private Sort subscriptionTypeNameMax;

  /**
   * Min of subscriptionType Reference
   */
  @Description("Min of subscriptionType Reference")
  private Sort subscriptionTypeNameMin;

  public Sort getId() {
    return this.id;
  }

  public void setId(Sort id) {
    this.id = id;
  }

  public Sort getIsDeprecated() {
    return this.isDeprecated;
  }

  public void setIsDeprecated(Sort isDeprecated) {
    this.isDeprecated = isDeprecated;
  }

  public Sort getVersion() {
    return this.version;
  }

  public void setVersion(Sort version) {
    this.version = version;
  }

  public Sort getRealmId() {
    return this.realmId;
  }

  public void setRealmId(Sort realmId) {
    this.realmId = realmId;
  }

  public Sort getCreateUserId() {
    return this.createUserId;
  }

  public void setCreateUserId(Sort createUserId) {
    this.createUserId = createUserId;
  }

  public Sort getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Sort createTime) {
    this.createTime = createTime;
  }

  public Sort getUpdateUserId() {
    return this.updateUserId;
  }

  public void setUpdateUserId(Sort updateUserId) {
    this.updateUserId = updateUserId;
  }

  public Sort getUpdateTime() {
    return this.updateTime;
  }

  public void setUpdateTime(Sort updateTime) {
    this.updateTime = updateTime;
  }

  public Sort getCreateGroupId() {
    return this.createGroupId;
  }

  public void setCreateGroupId(Sort createGroupId) {
    this.createGroupId = createGroupId;
  }

  public Sort get__typename() {
    return this.__typename;
  }

  public void set__typename(Sort __typename) {
    this.__typename = __typename;
  }

  public Sort getQueryTypeName() {
    return this.queryTypeName;
  }

  public void setQueryTypeName(Sort queryTypeName) {
    this.queryTypeName = queryTypeName;
  }

  public Sort getMutationTypeName() {
    return this.mutationTypeName;
  }

  public void setMutationTypeName(Sort mutationTypeName) {
    this.mutationTypeName = mutationTypeName;
  }

  public Sort getSubscriptionTypeName() {
    return this.subscriptionTypeName;
  }

  public void setSubscriptionTypeName(Sort subscriptionTypeName) {
    this.subscriptionTypeName = subscriptionTypeName;
  }

  public Sort getIdCount() {
    return this.idCount;
  }

  public void setIdCount(Sort idCount) {
    this.idCount = idCount;
  }

  public Sort getIdMax() {
    return this.idMax;
  }

  public void setIdMax(Sort idMax) {
    this.idMax = idMax;
  }

  public Sort getIdMin() {
    return this.idMin;
  }

  public void setIdMin(Sort idMin) {
    this.idMin = idMin;
  }

  public Sort getQueryTypeNameCount() {
    return this.queryTypeNameCount;
  }

  public void setQueryTypeNameCount(Sort queryTypeNameCount) {
    this.queryTypeNameCount = queryTypeNameCount;
  }

  public Sort getQueryTypeNameMax() {
    return this.queryTypeNameMax;
  }

  public void setQueryTypeNameMax(Sort queryTypeNameMax) {
    this.queryTypeNameMax = queryTypeNameMax;
  }

  public Sort getQueryTypeNameMin() {
    return this.queryTypeNameMin;
  }

  public void setQueryTypeNameMin(Sort queryTypeNameMin) {
    this.queryTypeNameMin = queryTypeNameMin;
  }

  public Sort getMutationTypeNameCount() {
    return this.mutationTypeNameCount;
  }

  public void setMutationTypeNameCount(Sort mutationTypeNameCount) {
    this.mutationTypeNameCount = mutationTypeNameCount;
  }

  public Sort getMutationTypeNameMax() {
    return this.mutationTypeNameMax;
  }

  public void setMutationTypeNameMax(Sort mutationTypeNameMax) {
    this.mutationTypeNameMax = mutationTypeNameMax;
  }

  public Sort getMutationTypeNameMin() {
    return this.mutationTypeNameMin;
  }

  public void setMutationTypeNameMin(Sort mutationTypeNameMin) {
    this.mutationTypeNameMin = mutationTypeNameMin;
  }

  public Sort getSubscriptionTypeNameCount() {
    return this.subscriptionTypeNameCount;
  }

  public void setSubscriptionTypeNameCount(Sort subscriptionTypeNameCount) {
    this.subscriptionTypeNameCount = subscriptionTypeNameCount;
  }

  public Sort getSubscriptionTypeNameMax() {
    return this.subscriptionTypeNameMax;
  }

  public void setSubscriptionTypeNameMax(Sort subscriptionTypeNameMax) {
    this.subscriptionTypeNameMax = subscriptionTypeNameMax;
  }

  public Sort getSubscriptionTypeNameMin() {
    return this.subscriptionTypeNameMin;
  }

  public void setSubscriptionTypeNameMin(Sort subscriptionTypeNameMin) {
    this.subscriptionTypeNameMin = subscriptionTypeNameMin;
  }
}
