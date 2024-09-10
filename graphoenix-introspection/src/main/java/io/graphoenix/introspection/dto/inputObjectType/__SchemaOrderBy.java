package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __SchemaOrderBy {
  private Sort id;

  private __TypeOrderBy types;

  private __TypeOrderBy queryType;

  private __TypeOrderBy mutationType;

  private __TypeOrderBy subscriptionType;

  private __DirectiveOrderBy directives;

  private Sort isDeprecated;

  private Sort version;

  private Sort realmId;

  private Sort createUserId;

  private Sort createTime;

  private Sort updateUserId;

  private Sort updateTime;

  private Sort createGroupId;

  private Sort __typename;

  private Sort queryTypeName;

  private Sort mutationTypeName;

  private Sort subscriptionTypeName;

  private Sort idCount;

  private Sort idMax;

  private Sort idMin;

  private Sort queryTypeNameCount;

  private Sort queryTypeNameMax;

  private Sort queryTypeNameMin;

  private Sort mutationTypeNameCount;

  private Sort mutationTypeNameMax;

  private Sort mutationTypeNameMin;

  private Sort subscriptionTypeNameCount;

  private Sort subscriptionTypeNameMax;

  private Sort subscriptionTypeNameMin;

  public Sort getId() {
    return this.id;
  }

  public void setId(Sort id) {
    this.id = id;
  }

  public __TypeOrderBy getTypes() {
    return this.types;
  }

  public void setTypes(__TypeOrderBy types) {
    this.types = types;
  }

  public __TypeOrderBy getQueryType() {
    return this.queryType;
  }

  public void setQueryType(__TypeOrderBy queryType) {
    this.queryType = queryType;
  }

  public __TypeOrderBy getMutationType() {
    return this.mutationType;
  }

  public void setMutationType(__TypeOrderBy mutationType) {
    this.mutationType = mutationType;
  }

  public __TypeOrderBy getSubscriptionType() {
    return this.subscriptionType;
  }

  public void setSubscriptionType(__TypeOrderBy subscriptionType) {
    this.subscriptionType = subscriptionType;
  }

  public __DirectiveOrderBy getDirectives() {
    return this.directives;
  }

  public void setDirectives(__DirectiveOrderBy directives) {
    this.directives = directives;
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
