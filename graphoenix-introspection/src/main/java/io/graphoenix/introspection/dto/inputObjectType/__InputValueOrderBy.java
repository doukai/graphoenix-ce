package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for __InputValue
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Order Input for __InputValue")
public class __InputValueOrderBy {
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
   * ofType
   */
  @Description("ofType")
  private __TypeOrderBy ofType;

  /**
   * fieldId
   */
  @Description("fieldId")
  private Sort fieldId;

  /**
   * directiveName
   */
  @Description("directiveName")
  private Sort directiveName;

  /**
   * description
   */
  @Description("description")
  private Sort description;

  /**
   * type
   */
  @Description("type")
  private __TypeOrderBy type;

  /**
   * defaultValue
   */
  @Description("defaultValue")
  private Sort defaultValue;

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
   * ofType Reference
   */
  @Description("ofType Reference")
  private Sort ofTypeName;

  /**
   * type Reference
   */
  @Description("type Reference")
  private Sort typeName;

  /**
   * Count of __InputValue
   */
  @Description("Count of __InputValue")
  private Sort idCount;

  /**
   * Count of name
   */
  @Description("Count of name")
  private Sort nameCount;

  /**
   * Count of fieldId
   */
  @Description("Count of fieldId")
  private Sort fieldIdCount;

  /**
   * Count of directiveName
   */
  @Description("Count of directiveName")
  private Sort directiveNameCount;

  /**
   * Count of description
   */
  @Description("Count of description")
  private Sort descriptionCount;

  /**
   * Count of defaultValue
   */
  @Description("Count of defaultValue")
  private Sort defaultValueCount;

  /**
   * Count of ofType Reference
   */
  @Description("Count of ofType Reference")
  private Sort ofTypeNameCount;

  /**
   * Count of type Reference
   */
  @Description("Count of type Reference")
  private Sort typeNameCount;

  /**
   * Max of fieldId
   */
  @Description("Max of fieldId")
  private Sort fieldIdMax;

  /**
   * Min of fieldId
   */
  @Description("Min of fieldId")
  private Sort fieldIdMin;

  /**
   * Sum of fieldId
   */
  @Description("Sum of fieldId")
  private Sort fieldIdSum;

  /**
   * Avg of fieldId
   */
  @Description("Avg of fieldId")
  private Sort fieldIdAvg;

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
  private Collection<__InputValueOrderBy> obs;

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

  public __TypeOrderBy getOfType() {
    return this.ofType;
  }

  public void setOfType(__TypeOrderBy ofType) {
    this.ofType = ofType;
  }

  public Sort getFieldId() {
    return this.fieldId;
  }

  public void setFieldId(Sort fieldId) {
    this.fieldId = fieldId;
  }

  public Sort getDirectiveName() {
    return this.directiveName;
  }

  public void setDirectiveName(Sort directiveName) {
    this.directiveName = directiveName;
  }

  public Sort getDescription() {
    return this.description;
  }

  public void setDescription(Sort description) {
    this.description = description;
  }

  public __TypeOrderBy getType() {
    return this.type;
  }

  public void setType(__TypeOrderBy type) {
    this.type = type;
  }

  public Sort getDefaultValue() {
    return this.defaultValue;
  }

  public void setDefaultValue(Sort defaultValue) {
    this.defaultValue = defaultValue;
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

  public Sort getOfTypeName() {
    return this.ofTypeName;
  }

  public void setOfTypeName(Sort ofTypeName) {
    this.ofTypeName = ofTypeName;
  }

  public Sort getTypeName() {
    return this.typeName;
  }

  public void setTypeName(Sort typeName) {
    this.typeName = typeName;
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

  public Sort getFieldIdCount() {
    return this.fieldIdCount;
  }

  public void setFieldIdCount(Sort fieldIdCount) {
    this.fieldIdCount = fieldIdCount;
  }

  public Sort getDirectiveNameCount() {
    return this.directiveNameCount;
  }

  public void setDirectiveNameCount(Sort directiveNameCount) {
    this.directiveNameCount = directiveNameCount;
  }

  public Sort getDescriptionCount() {
    return this.descriptionCount;
  }

  public void setDescriptionCount(Sort descriptionCount) {
    this.descriptionCount = descriptionCount;
  }

  public Sort getDefaultValueCount() {
    return this.defaultValueCount;
  }

  public void setDefaultValueCount(Sort defaultValueCount) {
    this.defaultValueCount = defaultValueCount;
  }

  public Sort getOfTypeNameCount() {
    return this.ofTypeNameCount;
  }

  public void setOfTypeNameCount(Sort ofTypeNameCount) {
    this.ofTypeNameCount = ofTypeNameCount;
  }

  public Sort getTypeNameCount() {
    return this.typeNameCount;
  }

  public void setTypeNameCount(Sort typeNameCount) {
    this.typeNameCount = typeNameCount;
  }

  public Sort getFieldIdMax() {
    return this.fieldIdMax;
  }

  public void setFieldIdMax(Sort fieldIdMax) {
    this.fieldIdMax = fieldIdMax;
  }

  public Sort getFieldIdMin() {
    return this.fieldIdMin;
  }

  public void setFieldIdMin(Sort fieldIdMin) {
    this.fieldIdMin = fieldIdMin;
  }

  public Sort getFieldIdSum() {
    return this.fieldIdSum;
  }

  public void setFieldIdSum(Sort fieldIdSum) {
    this.fieldIdSum = fieldIdSum;
  }

  public Sort getFieldIdAvg() {
    return this.fieldIdAvg;
  }

  public void setFieldIdAvg(Sort fieldIdAvg) {
    this.fieldIdAvg = fieldIdAvg;
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

  public Collection<__InputValueOrderBy> getObs() {
    return this.obs;
  }

  public void setObs(Collection<__InputValueOrderBy> obs) {
    this.obs = obs;
  }
}
