package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for __Directive
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Order Input for __Directive")
public class __DirectiveOrderBy {
  /**
   * id
   */
  @Description("id")
  private Sort id;

  /**
   * name
   */
  @Description("name")
  private Sort name;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  private __SchemaOrderBy ofSchema;

  /**
   * description
   */
  @Description("description")
  private Sort description;

  /**
   * locations
   */
  @Description("locations")
  private Sort locations;

  /**
   * args
   */
  @Description("args")
  private __InputValueOrderBy args;

  /**
   * isRepeatable
   */
  @Description("isRepeatable")
  private Sort isRepeatable;

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
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  private Sort schemaId;

  /**
   * Relationship Object between __Directive and locations
   */
  @Description("Relationship Object between __Directive and locations")
  private __DirectiveLocationsRelationOrderBy __directiveLocationsRelation;

  /**
   * Aggregate Field for args
   */
  @Description("Aggregate Field for args")
  private __InputValueOrderBy argsAggregate;

  /**
   * Aggregate Field for Relationship Object between __Directive and locations
   */
  @Description("Aggregate Field for Relationship Object between __Directive and locations")
  private __DirectiveLocationsRelationOrderBy __directiveLocationsRelationAggregate;

  /**
   * Count of __Directive
   */
  @Description("Count of __Directive")
  private Sort idCount;

  /**
   * Count of name
   */
  @Description("Count of name")
  private Sort nameCount;

  /**
   * Count of description
   */
  @Description("Count of description")
  private Sort descriptionCount;

  /**
   * Count of isRepeatable
   */
  @Description("Count of isRepeatable")
  private Sort isRepeatableCount;

  /**
   * Count of ofSchema Reference
   */
  @Description("Count of ofSchema Reference")
  private Sort schemaIdCount;

  /**
   * Max of ofSchema Reference
   */
  @Description("Max of ofSchema Reference")
  private Sort schemaIdMax;

  /**
   * Min of ofSchema Reference
   */
  @Description("Min of ofSchema Reference")
  private Sort schemaIdMin;

  /**
   * Sum of ofSchema Reference
   */
  @Description("Sum of ofSchema Reference")
  private Sort schemaIdSum;

  /**
   * Avg of ofSchema Reference
   */
  @Description("Avg of ofSchema Reference")
  private Sort schemaIdAvg;

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
  private Collection<__DirectiveOrderBy> obs;

  public Sort getId() {
    return this.id;
  }

  public void setId(Sort id) {
    this.id = id;
  }

  public Sort getName() {
    return this.name;
  }

  public void setName(Sort name) {
    this.name = name;
  }

  public __SchemaOrderBy getOfSchema() {
    return this.ofSchema;
  }

  public void setOfSchema(__SchemaOrderBy ofSchema) {
    this.ofSchema = ofSchema;
  }

  public Sort getDescription() {
    return this.description;
  }

  public void setDescription(Sort description) {
    this.description = description;
  }

  public Sort getLocations() {
    return this.locations;
  }

  public void setLocations(Sort locations) {
    this.locations = locations;
  }

  public __InputValueOrderBy getArgs() {
    return this.args;
  }

  public void setArgs(__InputValueOrderBy args) {
    this.args = args;
  }

  public Sort getIsRepeatable() {
    return this.isRepeatable;
  }

  public void setIsRepeatable(Sort isRepeatable) {
    this.isRepeatable = isRepeatable;
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

  public Sort getSchemaId() {
    return this.schemaId;
  }

  public void setSchemaId(Sort schemaId) {
    this.schemaId = schemaId;
  }

  public __DirectiveLocationsRelationOrderBy get__directiveLocationsRelation() {
    return this.__directiveLocationsRelation;
  }

  public void set__directiveLocationsRelation(
      __DirectiveLocationsRelationOrderBy __directiveLocationsRelation) {
    this.__directiveLocationsRelation = __directiveLocationsRelation;
  }

  public __InputValueOrderBy getArgsAggregate() {
    return this.argsAggregate;
  }

  public void setArgsAggregate(__InputValueOrderBy argsAggregate) {
    this.argsAggregate = argsAggregate;
  }

  public __DirectiveLocationsRelationOrderBy get__directiveLocationsRelationAggregate() {
    return this.__directiveLocationsRelationAggregate;
  }

  public void set__directiveLocationsRelationAggregate(
      __DirectiveLocationsRelationOrderBy __directiveLocationsRelationAggregate) {
    this.__directiveLocationsRelationAggregate = __directiveLocationsRelationAggregate;
  }

  public Sort getIdCount() {
    return this.idCount;
  }

  public void setIdCount(Sort idCount) {
    this.idCount = idCount;
  }

  public Sort getNameCount() {
    return this.nameCount;
  }

  public void setNameCount(Sort nameCount) {
    this.nameCount = nameCount;
  }

  public Sort getDescriptionCount() {
    return this.descriptionCount;
  }

  public void setDescriptionCount(Sort descriptionCount) {
    this.descriptionCount = descriptionCount;
  }

  public Sort getIsRepeatableCount() {
    return this.isRepeatableCount;
  }

  public void setIsRepeatableCount(Sort isRepeatableCount) {
    this.isRepeatableCount = isRepeatableCount;
  }

  public Sort getSchemaIdCount() {
    return this.schemaIdCount;
  }

  public void setSchemaIdCount(Sort schemaIdCount) {
    this.schemaIdCount = schemaIdCount;
  }

  public Sort getSchemaIdMax() {
    return this.schemaIdMax;
  }

  public void setSchemaIdMax(Sort schemaIdMax) {
    this.schemaIdMax = schemaIdMax;
  }

  public Sort getSchemaIdMin() {
    return this.schemaIdMin;
  }

  public void setSchemaIdMin(Sort schemaIdMin) {
    this.schemaIdMin = schemaIdMin;
  }

  public Sort getSchemaIdSum() {
    return this.schemaIdSum;
  }

  public void setSchemaIdSum(Sort schemaIdSum) {
    this.schemaIdSum = schemaIdSum;
  }

  public Sort getSchemaIdAvg() {
    return this.schemaIdAvg;
  }

  public void setSchemaIdAvg(Sort schemaIdAvg) {
    this.schemaIdAvg = schemaIdAvg;
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

  public Collection<__DirectiveOrderBy> getObs() {
    return this.obs;
  }

  public void setObs(Collection<__DirectiveOrderBy> obs) {
    this.obs = obs;
  }
}
