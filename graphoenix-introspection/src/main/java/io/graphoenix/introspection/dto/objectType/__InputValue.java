package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import io.graphoenix.introspection.dto.inputObjectType.__InputValueInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __InputValue implements Meta {
  @Id
  private String id;

  private String name;

  private __Type ofType;

  private Integer fieldId;

  private String directiveName;

  private String description;

  @NonNull
  private __Type type;

  private String defaultValue;

  /**
   * Is Deprecated
   */
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
  @Description("Type Name")
  private String __typename = "__InputValue";

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  private String ofTypeName;

  /**
   * type Reference
   */
  @Description("type Reference")
  private String typeName;

  /**
   * Count of __InputValue
   */
  @Description("Count of __InputValue")
  private Integer idCount;

  /**
   * Max of id
   */
  @Description("Max of id")
  private Integer idMax;

  /**
   * Min of id
   */
  @Description("Min of id")
  private Integer idMin;

  /**
   * Count of name
   */
  @Description("Count of name")
  private Integer nameCount;

  /**
   * Max of name
   */
  @Description("Max of name")
  private String nameMax;

  /**
   * Min of name
   */
  @Description("Min of name")
  private String nameMin;

  /**
   * Count of directiveName
   */
  @Description("Count of directiveName")
  private Integer directiveNameCount;

  /**
   * Max of directiveName
   */
  @Description("Max of directiveName")
  private String directiveNameMax;

  /**
   * Min of directiveName
   */
  @Description("Min of directiveName")
  private String directiveNameMin;

  /**
   * Count of description
   */
  @Description("Count of description")
  private Integer descriptionCount;

  /**
   * Max of description
   */
  @Description("Max of description")
  private String descriptionMax;

  /**
   * Min of description
   */
  @Description("Min of description")
  private String descriptionMin;

  /**
   * Count of defaultValue
   */
  @Description("Count of defaultValue")
  private Integer defaultValueCount;

  /**
   * Max of defaultValue
   */
  @Description("Max of defaultValue")
  private String defaultValueMax;

  /**
   * Min of defaultValue
   */
  @Description("Min of defaultValue")
  private String defaultValueMin;

  /**
   * Count of ofType Reference
   */
  @Description("Count of ofType Reference")
  private Integer ofTypeNameCount;

  /**
   * Max of ofType Reference
   */
  @Description("Max of ofType Reference")
  private String ofTypeNameMax;

  /**
   * Min of ofType Reference
   */
  @Description("Min of ofType Reference")
  private String ofTypeNameMin;

  /**
   * Count of type Reference
   */
  @Description("Count of type Reference")
  private Integer typeNameCount;

  /**
   * Max of type Reference
   */
  @Description("Max of type Reference")
  private String typeNameMax;

  /**
   * Min of type Reference
   */
  @Description("Min of type Reference")
  private String typeNameMin;

  /**
   * Count of fieldId
   */
  @Description("Count of fieldId")
  private Integer fieldIdCount;

  /**
   * Sum of fieldId
   */
  @Description("Sum of fieldId")
  private Integer fieldIdSum;

  /**
   * Avg of fieldId
   */
  @Description("Avg of fieldId")
  private Integer fieldIdAvg;

  /**
   * Max of fieldId
   */
  @Description("Max of fieldId")
  private Integer fieldIdMax;

  /**
   * Min of fieldId
   */
  @Description("Min of fieldId")
  private Integer fieldIdMin;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = (String)id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public __Type getOfType() {
    return this.ofType;
  }

  public void setOfType(__Type ofType) {
    this.ofType = ofType;
  }

  public Integer getFieldId() {
    return this.fieldId;
  }

  public void setFieldId(Integer fieldId) {
    this.fieldId = fieldId;
  }

  public String getDirectiveName() {
    return this.directiveName;
  }

  public void setDirectiveName(String directiveName) {
    this.directiveName = directiveName;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public __Type getType() {
    return this.type;
  }

  public void setType(__Type type) {
    this.type = type;
  }

  public String getDefaultValue() {
    return this.defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
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

  public String get__typename() {
    return this.__typename;
  }

  public void set__typename(String __typename) {
    this.__typename = __typename;
  }

  public String getOfTypeName() {
    return this.ofTypeName;
  }

  public void setOfTypeName(String ofTypeName) {
    this.ofTypeName = ofTypeName;
  }

  public String getTypeName() {
    return this.typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public Integer getIdCount() {
    return this.idCount;
  }

  public void setIdCount(Integer idCount) {
    this.idCount = idCount;
  }

  public Integer getIdMax() {
    return this.idMax;
  }

  public void setIdMax(Integer idMax) {
    this.idMax = idMax;
  }

  public Integer getIdMin() {
    return this.idMin;
  }

  public void setIdMin(Integer idMin) {
    this.idMin = idMin;
  }

  public Integer getNameCount() {
    return this.nameCount;
  }

  public void setNameCount(Integer nameCount) {
    this.nameCount = nameCount;
  }

  public String getNameMax() {
    return this.nameMax;
  }

  public void setNameMax(String nameMax) {
    this.nameMax = nameMax;
  }

  public String getNameMin() {
    return this.nameMin;
  }

  public void setNameMin(String nameMin) {
    this.nameMin = nameMin;
  }

  public Integer getDirectiveNameCount() {
    return this.directiveNameCount;
  }

  public void setDirectiveNameCount(Integer directiveNameCount) {
    this.directiveNameCount = directiveNameCount;
  }

  public String getDirectiveNameMax() {
    return this.directiveNameMax;
  }

  public void setDirectiveNameMax(String directiveNameMax) {
    this.directiveNameMax = directiveNameMax;
  }

  public String getDirectiveNameMin() {
    return this.directiveNameMin;
  }

  public void setDirectiveNameMin(String directiveNameMin) {
    this.directiveNameMin = directiveNameMin;
  }

  public Integer getDescriptionCount() {
    return this.descriptionCount;
  }

  public void setDescriptionCount(Integer descriptionCount) {
    this.descriptionCount = descriptionCount;
  }

  public String getDescriptionMax() {
    return this.descriptionMax;
  }

  public void setDescriptionMax(String descriptionMax) {
    this.descriptionMax = descriptionMax;
  }

  public String getDescriptionMin() {
    return this.descriptionMin;
  }

  public void setDescriptionMin(String descriptionMin) {
    this.descriptionMin = descriptionMin;
  }

  public Integer getDefaultValueCount() {
    return this.defaultValueCount;
  }

  public void setDefaultValueCount(Integer defaultValueCount) {
    this.defaultValueCount = defaultValueCount;
  }

  public String getDefaultValueMax() {
    return this.defaultValueMax;
  }

  public void setDefaultValueMax(String defaultValueMax) {
    this.defaultValueMax = defaultValueMax;
  }

  public String getDefaultValueMin() {
    return this.defaultValueMin;
  }

  public void setDefaultValueMin(String defaultValueMin) {
    this.defaultValueMin = defaultValueMin;
  }

  public Integer getOfTypeNameCount() {
    return this.ofTypeNameCount;
  }

  public void setOfTypeNameCount(Integer ofTypeNameCount) {
    this.ofTypeNameCount = ofTypeNameCount;
  }

  public String getOfTypeNameMax() {
    return this.ofTypeNameMax;
  }

  public void setOfTypeNameMax(String ofTypeNameMax) {
    this.ofTypeNameMax = ofTypeNameMax;
  }

  public String getOfTypeNameMin() {
    return this.ofTypeNameMin;
  }

  public void setOfTypeNameMin(String ofTypeNameMin) {
    this.ofTypeNameMin = ofTypeNameMin;
  }

  public Integer getTypeNameCount() {
    return this.typeNameCount;
  }

  public void setTypeNameCount(Integer typeNameCount) {
    this.typeNameCount = typeNameCount;
  }

  public String getTypeNameMax() {
    return this.typeNameMax;
  }

  public void setTypeNameMax(String typeNameMax) {
    this.typeNameMax = typeNameMax;
  }

  public String getTypeNameMin() {
    return this.typeNameMin;
  }

  public void setTypeNameMin(String typeNameMin) {
    this.typeNameMin = typeNameMin;
  }

  public Integer getFieldIdCount() {
    return this.fieldIdCount;
  }

  public void setFieldIdCount(Integer fieldIdCount) {
    this.fieldIdCount = fieldIdCount;
  }

  public Integer getFieldIdSum() {
    return this.fieldIdSum;
  }

  public void setFieldIdSum(Integer fieldIdSum) {
    this.fieldIdSum = fieldIdSum;
  }

  public Integer getFieldIdAvg() {
    return this.fieldIdAvg;
  }

  public void setFieldIdAvg(Integer fieldIdAvg) {
    this.fieldIdAvg = fieldIdAvg;
  }

  public Integer getFieldIdMax() {
    return this.fieldIdMax;
  }

  public void setFieldIdMax(Integer fieldIdMax) {
    this.fieldIdMax = fieldIdMax;
  }

  public Integer getFieldIdMin() {
    return this.fieldIdMin;
  }

  public void setFieldIdMin(Integer fieldIdMin) {
    this.fieldIdMin = fieldIdMin;
  }

  public __InputValueInput toInput() {
    __InputValueInput input = new __InputValueInput();
    input.setId(this.getId());
    input.setName(this.getName());
    if(getOfType() != null) {
      input.setOfType(this.getOfType().toInput());
    }
    input.setFieldId(this.getFieldId());
    input.setDirectiveName(this.getDirectiveName());
    input.setDescription(this.getDescription());
    if(getType() != null) {
      input.setType(this.getType().toInput());
    }
    input.setDefaultValue(this.getDefaultValue());
    input.setIsDeprecated(this.getIsDeprecated());
    input.setVersion(this.getVersion());
    input.setRealmId(this.getRealmId());
    input.setCreateUserId(this.getCreateUserId());
    input.setCreateTime(this.getCreateTime());
    input.setUpdateUserId(this.getUpdateUserId());
    input.setUpdateTime(this.getUpdateTime());
    input.setCreateGroupId(this.getCreateGroupId());
    input.set__typename(this.get__typename());
    input.setOfTypeName(this.getOfTypeName());
    input.setTypeName(this.getTypeName());
    return input;
  }
}
