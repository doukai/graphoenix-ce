package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for __InputValue
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
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
   * Count of name
   */
  @Description("Count of name")
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
   * Count of directiveName
   */
  @Description("Count of directiveName")
  private Sort directiveNameCount;

  /**
   * Max of directiveName
   */
  @Description("Max of directiveName")
  private Sort directiveNameMax;

  /**
   * Min of directiveName
   */
  @Description("Min of directiveName")
  private Sort directiveNameMin;

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
   * Count of defaultValue
   */
  @Description("Count of defaultValue")
  private Sort defaultValueCount;

  /**
   * Max of defaultValue
   */
  @Description("Max of defaultValue")
  private Sort defaultValueMax;

  /**
   * Min of defaultValue
   */
  @Description("Min of defaultValue")
  private Sort defaultValueMin;

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
   * Count of type Reference
   */
  @Description("Count of type Reference")
  private Sort typeNameCount;

  /**
   * Max of type Reference
   */
  @Description("Max of type Reference")
  private Sort typeNameMax;

  /**
   * Min of type Reference
   */
  @Description("Min of type Reference")
  private Sort typeNameMin;

  /**
   * Count of fieldId
   */
  @Description("Count of fieldId")
  private Sort fieldIdCount;

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
   * Max of fieldId
   */
  @Description("Max of fieldId")
  private Sort fieldIdMax;

  /**
   * Min of fieldId
   */
  @Description("Min of fieldId")
  private Sort fieldIdMin;

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

  public Sort getDirectiveNameCount() {
    return this.directiveNameCount;
  }

  public void setDirectiveNameCount(Sort directiveNameCount) {
    this.directiveNameCount = directiveNameCount;
  }

  public Sort getDirectiveNameMax() {
    return this.directiveNameMax;
  }

  public void setDirectiveNameMax(Sort directiveNameMax) {
    this.directiveNameMax = directiveNameMax;
  }

  public Sort getDirectiveNameMin() {
    return this.directiveNameMin;
  }

  public void setDirectiveNameMin(Sort directiveNameMin) {
    this.directiveNameMin = directiveNameMin;
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

  public Sort getDefaultValueCount() {
    return this.defaultValueCount;
  }

  public void setDefaultValueCount(Sort defaultValueCount) {
    this.defaultValueCount = defaultValueCount;
  }

  public Sort getDefaultValueMax() {
    return this.defaultValueMax;
  }

  public void setDefaultValueMax(Sort defaultValueMax) {
    this.defaultValueMax = defaultValueMax;
  }

  public Sort getDefaultValueMin() {
    return this.defaultValueMin;
  }

  public void setDefaultValueMin(Sort defaultValueMin) {
    this.defaultValueMin = defaultValueMin;
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

  public Sort getTypeNameCount() {
    return this.typeNameCount;
  }

  public void setTypeNameCount(Sort typeNameCount) {
    this.typeNameCount = typeNameCount;
  }

  public Sort getTypeNameMax() {
    return this.typeNameMax;
  }

  public void setTypeNameMax(Sort typeNameMax) {
    this.typeNameMax = typeNameMax;
  }

  public Sort getTypeNameMin() {
    return this.typeNameMin;
  }

  public void setTypeNameMin(Sort typeNameMin) {
    this.typeNameMin = typeNameMin;
  }

  public Sort getFieldIdCount() {
    return this.fieldIdCount;
  }

  public void setFieldIdCount(Sort fieldIdCount) {
    this.fieldIdCount = fieldIdCount;
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
}
