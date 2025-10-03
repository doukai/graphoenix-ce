package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for Relationship Object between __Type and __Type
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Order Input for Relationship Object between __Type and __Type")
public class __TypePossibleTypesOrderBy {
  /**
   * ID
   */
  @Description("ID")
  private Sort id;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  private Sort typeRef;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  private Sort possibleTypeRef;

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
   * Count of Relationship Object between __Type and __Type
   */
  @Description("Count of Relationship Object between __Type and __Type")
  private Sort idCount;

  /**
   * Max of ID
   */
  @Description("Max of ID")
  private Sort idMax;

  /**
   * Min of ID
   */
  @Description("Min of ID")
  private Sort idMin;

  /**
   * Count of __Type Reference
   */
  @Description("Count of __Type Reference")
  private Sort typeRefCount;

  /**
   * Max of __Type Reference
   */
  @Description("Max of __Type Reference")
  private Sort typeRefMax;

  /**
   * Min of __Type Reference
   */
  @Description("Min of __Type Reference")
  private Sort typeRefMin;

  /**
   * Count of __Type Reference
   */
  @Description("Count of __Type Reference")
  private Sort possibleTypeRefCount;

  /**
   * Max of __Type Reference
   */
  @Description("Max of __Type Reference")
  private Sort possibleTypeRefMax;

  /**
   * Min of __Type Reference
   */
  @Description("Min of __Type Reference")
  private Sort possibleTypeRefMin;

  public Sort getId() {
    return this.id;
  }

  public void setId(Sort id) {
    this.id = id;
  }

  public Sort getTypeRef() {
    return this.typeRef;
  }

  public void setTypeRef(Sort typeRef) {
    this.typeRef = typeRef;
  }

  public Sort getPossibleTypeRef() {
    return this.possibleTypeRef;
  }

  public void setPossibleTypeRef(Sort possibleTypeRef) {
    this.possibleTypeRef = possibleTypeRef;
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

  public Sort getTypeRefCount() {
    return this.typeRefCount;
  }

  public void setTypeRefCount(Sort typeRefCount) {
    this.typeRefCount = typeRefCount;
  }

  public Sort getTypeRefMax() {
    return this.typeRefMax;
  }

  public void setTypeRefMax(Sort typeRefMax) {
    this.typeRefMax = typeRefMax;
  }

  public Sort getTypeRefMin() {
    return this.typeRefMin;
  }

  public void setTypeRefMin(Sort typeRefMin) {
    this.typeRefMin = typeRefMin;
  }

  public Sort getPossibleTypeRefCount() {
    return this.possibleTypeRefCount;
  }

  public void setPossibleTypeRefCount(Sort possibleTypeRefCount) {
    this.possibleTypeRefCount = possibleTypeRefCount;
  }

  public Sort getPossibleTypeRefMax() {
    return this.possibleTypeRefMax;
  }

  public void setPossibleTypeRefMax(Sort possibleTypeRefMax) {
    this.possibleTypeRefMax = possibleTypeRefMax;
  }

  public Sort getPossibleTypeRefMin() {
    return this.possibleTypeRefMin;
  }

  public void setPossibleTypeRefMin(Sort possibleTypeRefMin) {
    this.possibleTypeRefMin = possibleTypeRefMin;
  }
}
