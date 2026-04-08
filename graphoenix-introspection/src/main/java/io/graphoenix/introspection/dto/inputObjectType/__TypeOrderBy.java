package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for __Type
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Order Input for __Type")
public class __TypeOrderBy {
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
   * kind
   */
  @Description("kind")
  private Sort kind;

  /**
   * description
   */
  @Description("description")
  private Sort description;

  /**
   * fields
   */
  @Description("fields")
  private __FieldOrderBy fields;

  /**
   * interfaces
   */
  @Description("interfaces")
  private __TypeOrderBy interfaces;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  private __TypeOrderBy possibleTypes;

  /**
   * enumValues
   */
  @Description("enumValues")
  private __EnumValueOrderBy enumValues;

  /**
   * inputFields
   */
  @Description("inputFields")
  private __InputValueOrderBy inputFields;

  /**
   * ofType
   */
  @Description("ofType")
  private __TypeOrderBy ofType;

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
   * ofType Reference
   */
  @Description("ofType Reference")
  private Sort ofTypeName;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  private __TypeInterfacesOrderBy __typeInterfaces;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  private __TypePossibleTypesOrderBy __typePossibleTypes;

  /**
   * Aggregate Field for fields
   */
  @Description("Aggregate Field for fields")
  private __FieldOrderBy fieldsAggregate;

  /**
   * Aggregate Field for interfaces
   */
  @Description("Aggregate Field for interfaces")
  private __TypeOrderBy interfacesAggregate;

  /**
   * Aggregate Field for possibleTypes
   */
  @Description("Aggregate Field for possibleTypes")
  private __TypeOrderBy possibleTypesAggregate;

  /**
   * Aggregate Field for enumValues
   */
  @Description("Aggregate Field for enumValues")
  private __EnumValueOrderBy enumValuesAggregate;

  /**
   * Aggregate Field for inputFields
   */
  @Description("Aggregate Field for inputFields")
  private __InputValueOrderBy inputFieldsAggregate;

  /**
   * Aggregate Field for Relationship Object between __Type and __Type
   */
  @Description("Aggregate Field for Relationship Object between __Type and __Type")
  private __TypeInterfacesOrderBy __typeInterfacesAggregate;

  /**
   * Aggregate Field for Relationship Object between __Type and __Type
   */
  @Description("Aggregate Field for Relationship Object between __Type and __Type")
  private __TypePossibleTypesOrderBy __typePossibleTypesAggregate;

  /**
   * Count of __Type
   */
  @Description("Count of __Type")
  private Sort idCount;

  /**
   * Count of name
   */
  @Description("Count of name")
  private Sort nameCount;

  /**
   * Count of kind
   */
  @Description("Count of kind")
  private Sort kindCount;

  /**
   * Count of description
   */
  @Description("Count of description")
  private Sort descriptionCount;

  /**
   * Count of ofSchema Reference
   */
  @Description("Count of ofSchema Reference")
  private Sort schemaIdCount;

  /**
   * Count of ofType Reference
   */
  @Description("Count of ofType Reference")
  private Sort ofTypeNameCount;

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
  private Collection<__TypeOrderBy> obs;

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

  public Sort getKind() {
    return this.kind;
  }

  public void setKind(Sort kind) {
    this.kind = kind;
  }

  public Sort getDescription() {
    return this.description;
  }

  public void setDescription(Sort description) {
    this.description = description;
  }

  public __FieldOrderBy getFields() {
    return this.fields;
  }

  public void setFields(__FieldOrderBy fields) {
    this.fields = fields;
  }

  public __TypeOrderBy getInterfaces() {
    return this.interfaces;
  }

  public void setInterfaces(__TypeOrderBy interfaces) {
    this.interfaces = interfaces;
  }

  public __TypeOrderBy getPossibleTypes() {
    return this.possibleTypes;
  }

  public void setPossibleTypes(__TypeOrderBy possibleTypes) {
    this.possibleTypes = possibleTypes;
  }

  public __EnumValueOrderBy getEnumValues() {
    return this.enumValues;
  }

  public void setEnumValues(__EnumValueOrderBy enumValues) {
    this.enumValues = enumValues;
  }

  public __InputValueOrderBy getInputFields() {
    return this.inputFields;
  }

  public void setInputFields(__InputValueOrderBy inputFields) {
    this.inputFields = inputFields;
  }

  public __TypeOrderBy getOfType() {
    return this.ofType;
  }

  public void setOfType(__TypeOrderBy ofType) {
    this.ofType = ofType;
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

  public Sort getOfTypeName() {
    return this.ofTypeName;
  }

  public void setOfTypeName(Sort ofTypeName) {
    this.ofTypeName = ofTypeName;
  }

  public __TypeInterfacesOrderBy get__typeInterfaces() {
    return this.__typeInterfaces;
  }

  public void set__typeInterfaces(__TypeInterfacesOrderBy __typeInterfaces) {
    this.__typeInterfaces = __typeInterfaces;
  }

  public __TypePossibleTypesOrderBy get__typePossibleTypes() {
    return this.__typePossibleTypes;
  }

  public void set__typePossibleTypes(__TypePossibleTypesOrderBy __typePossibleTypes) {
    this.__typePossibleTypes = __typePossibleTypes;
  }

  public __FieldOrderBy getFieldsAggregate() {
    return this.fieldsAggregate;
  }

  public void setFieldsAggregate(__FieldOrderBy fieldsAggregate) {
    this.fieldsAggregate = fieldsAggregate;
  }

  public __TypeOrderBy getInterfacesAggregate() {
    return this.interfacesAggregate;
  }

  public void setInterfacesAggregate(__TypeOrderBy interfacesAggregate) {
    this.interfacesAggregate = interfacesAggregate;
  }

  public __TypeOrderBy getPossibleTypesAggregate() {
    return this.possibleTypesAggregate;
  }

  public void setPossibleTypesAggregate(__TypeOrderBy possibleTypesAggregate) {
    this.possibleTypesAggregate = possibleTypesAggregate;
  }

  public __EnumValueOrderBy getEnumValuesAggregate() {
    return this.enumValuesAggregate;
  }

  public void setEnumValuesAggregate(__EnumValueOrderBy enumValuesAggregate) {
    this.enumValuesAggregate = enumValuesAggregate;
  }

  public __InputValueOrderBy getInputFieldsAggregate() {
    return this.inputFieldsAggregate;
  }

  public void setInputFieldsAggregate(__InputValueOrderBy inputFieldsAggregate) {
    this.inputFieldsAggregate = inputFieldsAggregate;
  }

  public __TypeInterfacesOrderBy get__typeInterfacesAggregate() {
    return this.__typeInterfacesAggregate;
  }

  public void set__typeInterfacesAggregate(__TypeInterfacesOrderBy __typeInterfacesAggregate) {
    this.__typeInterfacesAggregate = __typeInterfacesAggregate;
  }

  public __TypePossibleTypesOrderBy get__typePossibleTypesAggregate() {
    return this.__typePossibleTypesAggregate;
  }

  public void set__typePossibleTypesAggregate(
      __TypePossibleTypesOrderBy __typePossibleTypesAggregate) {
    this.__typePossibleTypesAggregate = __typePossibleTypesAggregate;
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

  public Sort getKindCount() {
    return this.kindCount;
  }

  public void setKindCount(Sort kindCount) {
    this.kindCount = kindCount;
  }

  public Sort getDescriptionCount() {
    return this.descriptionCount;
  }

  public void setDescriptionCount(Sort descriptionCount) {
    this.descriptionCount = descriptionCount;
  }

  public Sort getSchemaIdCount() {
    return this.schemaIdCount;
  }

  public void setSchemaIdCount(Sort schemaIdCount) {
    this.schemaIdCount = schemaIdCount;
  }

  public Sort getOfTypeNameCount() {
    return this.ofTypeNameCount;
  }

  public void setOfTypeNameCount(Sort ofTypeNameCount) {
    this.ofTypeNameCount = ofTypeNameCount;
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

  public Collection<__TypeOrderBy> getObs() {
    return this.obs;
  }

  public void setObs(Collection<__TypeOrderBy> obs) {
    this.obs = obs;
  }
}
