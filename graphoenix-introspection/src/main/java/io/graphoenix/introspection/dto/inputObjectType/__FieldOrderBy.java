package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for __Field
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Order Input for __Field")
public class __FieldOrderBy {
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
   * description
   */
  @Description("description")
  private Sort description;

  /**
   * args
   */
  @Description("args")
  private __InputValueOrderBy args;

  /**
   * type
   */
  @Description("type")
  private __TypeOrderBy type;

  /**
   * deprecationReason
   */
  @Description("deprecationReason")
  private Sort deprecationReason;

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
   * Aggregate Field for args
   */
  @Description("Aggregate Field for args")
  private __InputValueOrderBy argsAggregate;

  /**
   * Count of __Field
   */
  @Description("Count of __Field")
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
   * Count of deprecationReason
   */
  @Description("Count of deprecationReason")
  private Sort deprecationReasonCount;

  /**
   * Max of deprecationReason
   */
  @Description("Max of deprecationReason")
  private Sort deprecationReasonMax;

  /**
   * Min of deprecationReason
   */
  @Description("Min of deprecationReason")
  private Sort deprecationReasonMin;

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

  public Sort getDescription() {
    return this.description;
  }

  public void setDescription(Sort description) {
    this.description = description;
  }

  public __InputValueOrderBy getArgs() {
    return this.args;
  }

  public void setArgs(__InputValueOrderBy args) {
    this.args = args;
  }

  public __TypeOrderBy getType() {
    return this.type;
  }

  public void setType(__TypeOrderBy type) {
    this.type = type;
  }

  public Sort getDeprecationReason() {
    return this.deprecationReason;
  }

  public void setDeprecationReason(Sort deprecationReason) {
    this.deprecationReason = deprecationReason;
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

  public __InputValueOrderBy getArgsAggregate() {
    return this.argsAggregate;
  }

  public void setArgsAggregate(__InputValueOrderBy argsAggregate) {
    this.argsAggregate = argsAggregate;
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

  public Sort getDeprecationReasonCount() {
    return this.deprecationReasonCount;
  }

  public void setDeprecationReasonCount(Sort deprecationReasonCount) {
    this.deprecationReasonCount = deprecationReasonCount;
  }

  public Sort getDeprecationReasonMax() {
    return this.deprecationReasonMax;
  }

  public void setDeprecationReasonMax(Sort deprecationReasonMax) {
    this.deprecationReasonMax = deprecationReasonMax;
  }

  public Sort getDeprecationReasonMin() {
    return this.deprecationReasonMin;
  }

  public void setDeprecationReasonMin(Sort deprecationReasonMin) {
    this.deprecationReasonMin = deprecationReasonMin;
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
}
