package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.inputObjectType.MetaInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Mutation Input for __Schema
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Input for __Schema")
public class __SchemaInput implements MetaInput, __SchemaInputBase {
  /**
   * id
   */
  @Description("id")
  private String id;

  /**
   * types
   */
  @Description("types")
  private Collection<__TypeInput> types;

  /**
   * queryType
   */
  @Description("queryType")
  private __TypeInput queryType;

  /**
   * mutationType
   */
  @Description("mutationType")
  private __TypeInput mutationType;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  private __TypeInput subscriptionType;

  /**
   * directives
   */
  @Description("directives")
  private Collection<__DirectiveInput> directives;

  /**
   * Is Deprecated
   */
  @DefaultValue("false")
  @Description("Is Deprecated")
  private Boolean isDeprecated = false;

  /**
   * Version
   */
  @Description("Version")
  private Integer version;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  private Integer realmId;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  private String createUserId;

  /**
   * Create Time
   */
  @Description("Create Time")
  private LocalDateTime createTime;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  private String updateUserId;

  /**
   * Update Time
   */
  @Description("Update Time")
  private LocalDateTime updateTime;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  private String createGroupId;

  /**
   * Type Name
   */
  @DefaultValue("__Schema")
  @Description("Type Name")
  private String __typename = "__Schema";

  /**
   * queryType Reference
   */
  @Description("queryType Reference")
  private String queryTypeName;

  /**
   * mutationType Reference
   */
  @Description("mutationType Reference")
  private String mutationTypeName;

  /**
   * subscriptionType Reference
   */
  @Description("subscriptionType Reference")
  private String subscriptionTypeName;

  /**
   * Where
   */
  @Description("Where")
  private __SchemaExpression where;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = (String)id;
  }

  @Override
  public Collection<__TypeInput> getTypes() {
    return this.types;
  }

  @Override
  public void setTypes(Collection<__TypeInput> types) {
    this.types = (Collection<__TypeInput>)types;
  }

  @Override
  public __TypeInput getQueryType() {
    return this.queryType;
  }

  @Override
  public void setQueryType(__TypeInput queryType) {
    this.queryType = (__TypeInput)queryType;
  }

  @Override
  public __TypeInput getMutationType() {
    return this.mutationType;
  }

  @Override
  public void setMutationType(__TypeInput mutationType) {
    this.mutationType = (__TypeInput)mutationType;
  }

  @Override
  public __TypeInput getSubscriptionType() {
    return this.subscriptionType;
  }

  @Override
  public void setSubscriptionType(__TypeInput subscriptionType) {
    this.subscriptionType = (__TypeInput)subscriptionType;
  }

  @Override
  public Collection<__DirectiveInput> getDirectives() {
    return this.directives;
  }

  @Override
  public void setDirectives(Collection<__DirectiveInput> directives) {
    this.directives = (Collection<__DirectiveInput>)directives;
  }

  @Override
  public Boolean getIsDeprecated() {
    return this.isDeprecated;
  }

  @Override
  public void setIsDeprecated(Boolean isDeprecated) {
    this.isDeprecated = (Boolean)isDeprecated;
  }

  @Override
  public Integer getVersion() {
    return this.version;
  }

  @Override
  public void setVersion(Integer version) {
    this.version = (Integer)version;
  }

  @Override
  public Integer getRealmId() {
    return this.realmId;
  }

  @Override
  public void setRealmId(Integer realmId) {
    this.realmId = (Integer)realmId;
  }

  @Override
  public String getCreateUserId() {
    return this.createUserId;
  }

  @Override
  public void setCreateUserId(String createUserId) {
    this.createUserId = (String)createUserId;
  }

  @Override
  public LocalDateTime getCreateTime() {
    return this.createTime;
  }

  @Override
  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = (LocalDateTime)createTime;
  }

  @Override
  public String getUpdateUserId() {
    return this.updateUserId;
  }

  @Override
  public void setUpdateUserId(String updateUserId) {
    this.updateUserId = (String)updateUserId;
  }

  @Override
  public LocalDateTime getUpdateTime() {
    return this.updateTime;
  }

  @Override
  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = (LocalDateTime)updateTime;
  }

  @Override
  public String getCreateGroupId() {
    return this.createGroupId;
  }

  @Override
  public void setCreateGroupId(String createGroupId) {
    this.createGroupId = (String)createGroupId;
  }

  @Override
  public String get__typename() {
    return this.__typename;
  }

  @Override
  public void set__typename(String __typename) {
    this.__typename = (String)__typename;
  }

  @Override
  public String getQueryTypeName() {
    return this.queryTypeName;
  }

  @Override
  public void setQueryTypeName(String queryTypeName) {
    this.queryTypeName = (String)queryTypeName;
  }

  @Override
  public String getMutationTypeName() {
    return this.mutationTypeName;
  }

  @Override
  public void setMutationTypeName(String mutationTypeName) {
    this.mutationTypeName = (String)mutationTypeName;
  }

  @Override
  public String getSubscriptionTypeName() {
    return this.subscriptionTypeName;
  }

  @Override
  public void setSubscriptionTypeName(String subscriptionTypeName) {
    this.subscriptionTypeName = (String)subscriptionTypeName;
  }

  public __SchemaExpression getWhere() {
    return this.where;
  }

  public void setWhere(__SchemaExpression where) {
    this.where = where;
  }
}
