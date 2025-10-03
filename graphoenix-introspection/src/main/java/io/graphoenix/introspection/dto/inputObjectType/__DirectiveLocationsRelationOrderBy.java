package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for Relationship Object between __Directive and locations
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Order Input for Relationship Object between __Directive and locations")
public class __DirectiveLocationsRelationOrderBy {
  /**
   * ID
   */
  @Description("ID")
  private Sort id;

  /**
   * __Directive Reference
   */
  @Description("__Directive Reference")
  private Sort __directiveRef;

  /**
   * locations Reference
   */
  @Description("locations Reference")
  private Sort locationsRef;

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
   * Count of Relationship Object between __Directive and locations
   */
  @Description("Count of Relationship Object between __Directive and locations")
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
   * Count of __Directive Reference
   */
  @Description("Count of __Directive Reference")
  private Sort __directiveRefCount;

  /**
   * Max of __Directive Reference
   */
  @Description("Max of __Directive Reference")
  private Sort __directiveRefMax;

  /**
   * Min of __Directive Reference
   */
  @Description("Min of __Directive Reference")
  private Sort __directiveRefMin;

  /**
   * Count of locations Reference
   */
  @Description("Count of locations Reference")
  private Sort locationsRefCount;

  /**
   * Max of locations Reference
   */
  @Description("Max of locations Reference")
  private Sort locationsRefMax;

  /**
   * Min of locations Reference
   */
  @Description("Min of locations Reference")
  private Sort locationsRefMin;

  public Sort getId() {
    return this.id;
  }

  public void setId(Sort id) {
    this.id = id;
  }

  public Sort get__directiveRef() {
    return this.__directiveRef;
  }

  public void set__directiveRef(Sort __directiveRef) {
    this.__directiveRef = __directiveRef;
  }

  public Sort getLocationsRef() {
    return this.locationsRef;
  }

  public void setLocationsRef(Sort locationsRef) {
    this.locationsRef = locationsRef;
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

  public Sort get__directiveRefCount() {
    return this.__directiveRefCount;
  }

  public void set__directiveRefCount(Sort __directiveRefCount) {
    this.__directiveRefCount = __directiveRefCount;
  }

  public Sort get__directiveRefMax() {
    return this.__directiveRefMax;
  }

  public void set__directiveRefMax(Sort __directiveRefMax) {
    this.__directiveRefMax = __directiveRefMax;
  }

  public Sort get__directiveRefMin() {
    return this.__directiveRefMin;
  }

  public void set__directiveRefMin(Sort __directiveRefMin) {
    this.__directiveRefMin = __directiveRefMin;
  }

  public Sort getLocationsRefCount() {
    return this.locationsRefCount;
  }

  public void setLocationsRefCount(Sort locationsRefCount) {
    this.locationsRefCount = locationsRefCount;
  }

  public Sort getLocationsRefMax() {
    return this.locationsRefMax;
  }

  public void setLocationsRefMax(Sort locationsRefMax) {
    this.locationsRefMax = locationsRefMax;
  }

  public Sort getLocationsRefMin() {
    return this.locationsRefMin;
  }

  public void setLocationsRefMin(Sort locationsRefMin) {
    this.locationsRefMin = locationsRefMin;
  }
}
