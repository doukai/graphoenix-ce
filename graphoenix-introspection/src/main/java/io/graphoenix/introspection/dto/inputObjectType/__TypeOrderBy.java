package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for __Type
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Order Input for __Type")
public class __TypeOrderBy {
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
  private Sort nameCount;

  /**
   * Max of name
   */
  @Description("Max of name")
  private Sort nameMax;

  /**
   * Min of name
   */
  @Description("Min of name")
  private Sort nameMin;

  /**
   * Count of kind
   */
  @Description("Count of kind")
  private Sort kindCount;

  /**
   * Max of kind
   */
  @Description("Max of kind")
  private Sort kindMax;

  /**
   * Min of kind
   */
  @Description("Min of kind")
  private Sort kindMin;

  /**
   * Count of description
   */
  @Description("Count of description")
  private Sort descriptionCount;

  /**
   * Max of description
   */
  @Description("Max of description")
  private Sort descriptionMax;

  /**
   * Min of description
   */
  @Description("Min of description")
  private Sort descriptionMin;

  /**
   * Count of ofType Reference
   */
  @Description("Count of ofType Reference")
  private Sort ofTypeNameCount;

  /**
   * Max of ofType Reference
   */
  @Description("Max of ofType Reference")
  private Sort ofTypeNameMax;

  /**
   * Min of ofType Reference
   */
  @Description("Min of ofType Reference")
  private Sort ofTypeNameMin;

  /**
   * Count of ofSchema Reference
   */
  @Description("Count of ofSchema Reference")
  private Sort schemaIdCount;

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
   * Max of ofSchema Reference
   */
  @Description("Max of ofSchema Reference")
  private Sort schemaIdMax;

  /**
   * Min of ofSchema Reference
   */
  @Description("Min of ofSchema Reference")
  private Sort schemaIdMin;

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

  public Sort getNameCount() {
    return this.nameCount;
  }

  public void setNameCount(Sort nameCount) {
    this.nameCount = nameCount;
  }

  public Sort getNameMax() {
    return this.nameMax;
  }

  public void setNameMax(Sort nameMax) {
    this.nameMax = nameMax;
  }

  public Sort getNameMin() {
    return this.nameMin;
  }

  public void setNameMin(Sort nameMin) {
    this.nameMin = nameMin;
  }

  public Sort getKindCount() {
    return this.kindCount;
  }

  public void setKindCount(Sort kindCount) {
    this.kindCount = kindCount;
  }

  public Sort getKindMax() {
    return this.kindMax;
  }

  public void setKindMax(Sort kindMax) {
    this.kindMax = kindMax;
  }

  public Sort getKindMin() {
    return this.kindMin;
  }

  public void setKindMin(Sort kindMin) {
    this.kindMin = kindMin;
  }

  public Sort getDescriptionCount() {
    return this.descriptionCount;
  }

  public void setDescriptionCount(Sort descriptionCount) {
    this.descriptionCount = descriptionCount;
  }

  public Sort getDescriptionMax() {
    return this.descriptionMax;
  }

  public void setDescriptionMax(Sort descriptionMax) {
    this.descriptionMax = descriptionMax;
  }

  public Sort getDescriptionMin() {
    return this.descriptionMin;
  }

  public void setDescriptionMin(Sort descriptionMin) {
    this.descriptionMin = descriptionMin;
  }

  public Sort getOfTypeNameCount() {
    return this.ofTypeNameCount;
  }

  public void setOfTypeNameCount(Sort ofTypeNameCount) {
    this.ofTypeNameCount = ofTypeNameCount;
  }

  public Sort getOfTypeNameMax() {
    return this.ofTypeNameMax;
  }

  public void setOfTypeNameMax(Sort ofTypeNameMax) {
    this.ofTypeNameMax = ofTypeNameMax;
  }

  public Sort getOfTypeNameMin() {
    return this.ofTypeNameMin;
  }

  public void setOfTypeNameMin(Sort ofTypeNameMin) {
    this.ofTypeNameMin = ofTypeNameMin;
  }

  public Sort getSchemaIdCount() {
    return this.schemaIdCount;
  }

  public void setSchemaIdCount(Sort schemaIdCount) {
    this.schemaIdCount = schemaIdCount;
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
}
