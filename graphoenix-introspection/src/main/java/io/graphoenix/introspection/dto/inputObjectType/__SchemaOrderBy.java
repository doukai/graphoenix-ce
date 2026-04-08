package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for __Schema
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Order Input for __Schema")
public class __SchemaOrderBy {
  /**
   * id
   */
  @Description("id")
  private Sort id;

  /**
   * types
   */
  @Description("types")
  private __TypeOrderBy types;

  /**
   * queryType
   */
  @Description("queryType")
  private __TypeOrderBy queryType;

  /**
   * mutationType
   */
  @Description("mutationType")
  private __TypeOrderBy mutationType;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  private __TypeOrderBy subscriptionType;

  /**
   * directives
   */
  @Description("directives")
  private __DirectiveOrderBy directives;

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
   * Aggregate Field for types
   */
  @Description("Aggregate Field for types")
  private __TypeOrderBy typesAggregate;

  /**
   * Aggregate Field for directives
   */
  @Description("Aggregate Field for directives")
  private __DirectiveOrderBy directivesAggregate;

  /**
   * Count of __Schema
   */
  @Description("Count of __Schema")
  private Sort idCount;

  /**
   * Count of queryType Reference
   */
  @Description("Count of queryType Reference")
  private Sort queryTypeNameCount;

  /**
   * Count of mutationType Reference
   */
  @Description("Count of mutationType Reference")
  private Sort mutationTypeNameCount;

  /**
   * Count of subscriptionType Reference
   */
  @Description("Count of subscriptionType Reference")
  private Sort subscriptionTypeNameCount;

  /**
   * Year of Create Time
   */
  @Description("Year of Create Time")
  private Sort createTimeYear;

  /**
   * Month of Create Time
   */
  @Description("Month of Create Time")
  private Sort createTimeMonth;

  /**
   * Day of Create Time
   */
  @Description("Day of Create Time")
  private Sort createTimeDay;

  /**
   * Week of Create Time
   */
  @Description("Week of Create Time")
  private Sort createTimeWeek;

  /**
   * Quarter of Create Time
   */
  @Description("Quarter of Create Time")
  private Sort createTimeQuarter;

  /**
   * Year of Update Time
   */
  @Description("Year of Update Time")
  private Sort updateTimeYear;

  /**
   * Month of Update Time
   */
  @Description("Month of Update Time")
  private Sort updateTimeMonth;

  /**
   * Day of Update Time
   */
  @Description("Day of Update Time")
  private Sort updateTimeDay;

  /**
   * Week of Update Time
   */
  @Description("Week of Update Time")
  private Sort updateTimeWeek;

  /**
   * Quarter of Update Time
   */
  @Description("Quarter of Update Time")
  private Sort updateTimeQuarter;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<__SchemaOrderBy> obs;

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

  public __TypeOrderBy getTypesAggregate() {
    return this.typesAggregate;
  }

  public void setTypesAggregate(__TypeOrderBy typesAggregate) {
    this.typesAggregate = typesAggregate;
  }

  public __DirectiveOrderBy getDirectivesAggregate() {
    return this.directivesAggregate;
  }

  public void setDirectivesAggregate(__DirectiveOrderBy directivesAggregate) {
    this.directivesAggregate = directivesAggregate;
  }

  public Sort getIdCount() {
    return this.idCount;
  }

  public void setIdCount(Sort idCount) {
    this.idCount = idCount;
  }

  public Sort getQueryTypeNameCount() {
    return this.queryTypeNameCount;
  }

  public void setQueryTypeNameCount(Sort queryTypeNameCount) {
    this.queryTypeNameCount = queryTypeNameCount;
  }

  public Sort getMutationTypeNameCount() {
    return this.mutationTypeNameCount;
  }

  public void setMutationTypeNameCount(Sort mutationTypeNameCount) {
    this.mutationTypeNameCount = mutationTypeNameCount;
  }

  public Sort getSubscriptionTypeNameCount() {
    return this.subscriptionTypeNameCount;
  }

  public void setSubscriptionTypeNameCount(Sort subscriptionTypeNameCount) {
    this.subscriptionTypeNameCount = subscriptionTypeNameCount;
  }

  public Sort getCreateTimeYear() {
    return this.createTimeYear;
  }

  public void setCreateTimeYear(Sort createTimeYear) {
    this.createTimeYear = createTimeYear;
  }

  public Sort getCreateTimeMonth() {
    return this.createTimeMonth;
  }

  public void setCreateTimeMonth(Sort createTimeMonth) {
    this.createTimeMonth = createTimeMonth;
  }

  public Sort getCreateTimeDay() {
    return this.createTimeDay;
  }

  public void setCreateTimeDay(Sort createTimeDay) {
    this.createTimeDay = createTimeDay;
  }

  public Sort getCreateTimeWeek() {
    return this.createTimeWeek;
  }

  public void setCreateTimeWeek(Sort createTimeWeek) {
    this.createTimeWeek = createTimeWeek;
  }

  public Sort getCreateTimeQuarter() {
    return this.createTimeQuarter;
  }

  public void setCreateTimeQuarter(Sort createTimeQuarter) {
    this.createTimeQuarter = createTimeQuarter;
  }

  public Sort getUpdateTimeYear() {
    return this.updateTimeYear;
  }

  public void setUpdateTimeYear(Sort updateTimeYear) {
    this.updateTimeYear = updateTimeYear;
  }

  public Sort getUpdateTimeMonth() {
    return this.updateTimeMonth;
  }

  public void setUpdateTimeMonth(Sort updateTimeMonth) {
    this.updateTimeMonth = updateTimeMonth;
  }

  public Sort getUpdateTimeDay() {
    return this.updateTimeDay;
  }

  public void setUpdateTimeDay(Sort updateTimeDay) {
    this.updateTimeDay = updateTimeDay;
  }

  public Sort getUpdateTimeWeek() {
    return this.updateTimeWeek;
  }

  public void setUpdateTimeWeek(Sort updateTimeWeek) {
    this.updateTimeWeek = updateTimeWeek;
  }

  public Sort getUpdateTimeQuarter() {
    return this.updateTimeQuarter;
  }

  public void setUpdateTimeQuarter(Sort updateTimeQuarter) {
    this.updateTimeQuarter = updateTimeQuarter;
  }

  public Collection<__SchemaOrderBy> getObs() {
    return this.obs;
  }

  public void setObs(Collection<__SchemaOrderBy> obs) {
    this.obs = obs;
  }
}
