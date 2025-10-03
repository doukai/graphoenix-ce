package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import io.graphoenix.introspection.dto.enumType.__DirectiveLocation;
import io.graphoenix.introspection.dto.inputObjectType.__DirectiveLocationsRelationInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Type;

/**
 * Relationship Object between __Directive and locations
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Relationship Object between __Directive and locations")
public class __DirectiveLocationsRelation implements Meta {
  /**
   * ID
   */
  @Id
  @Description("ID")
  private String id;

  /**
   * __Directive Reference
   */
  @Description("__Directive Reference")
  private String __directiveRef;

  /**
   * __Directive
   */
  @Description("__Directive")
  private __Directive __directive;

  /**
   * locations Reference
   */
  @Description("locations Reference")
  private __DirectiveLocation locationsRef;

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
  private String __typename = "__DirectiveLocationsRelation";

  /**
   * Count of Relationship Object between __Directive and locations
   */
  @Description("Count of Relationship Object between __Directive and locations")
  private Integer idCount;

  /**
   * Max of ID
   */
  @Description("Max of ID")
  private Integer idMax;

  /**
   * Min of ID
   */
  @Description("Min of ID")
  private Integer idMin;

  /**
   * Count of __Directive Reference
   */
  @Description("Count of __Directive Reference")
  private Integer __directiveRefCount;

  /**
   * Max of __Directive Reference
   */
  @Description("Max of __Directive Reference")
  private String __directiveRefMax;

  /**
   * Min of __Directive Reference
   */
  @Description("Min of __Directive Reference")
  private String __directiveRefMin;

  /**
   * Count of locations Reference
   */
  @Description("Count of locations Reference")
  private Integer locationsRefCount;

  /**
   * Max of locations Reference
   */
  @Description("Max of locations Reference")
  private __DirectiveLocation locationsRefMax;

  /**
   * Min of locations Reference
   */
  @Description("Min of locations Reference")
  private __DirectiveLocation locationsRefMin;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = (String)id;
  }

  public String get__directiveRef() {
    return this.__directiveRef;
  }

  public void set__directiveRef(String __directiveRef) {
    this.__directiveRef = __directiveRef;
  }

  public __Directive get__directive() {
    return this.__directive;
  }

  public void set__directive(__Directive __directive) {
    this.__directive = __directive;
  }

  public __DirectiveLocation getLocationsRef() {
    return this.locationsRef;
  }

  public void setLocationsRef(__DirectiveLocation locationsRef) {
    this.locationsRef = locationsRef;
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

  public Integer get__directiveRefCount() {
    return this.__directiveRefCount;
  }

  public void set__directiveRefCount(Integer __directiveRefCount) {
    this.__directiveRefCount = __directiveRefCount;
  }

  public String get__directiveRefMax() {
    return this.__directiveRefMax;
  }

  public void set__directiveRefMax(String __directiveRefMax) {
    this.__directiveRefMax = __directiveRefMax;
  }

  public String get__directiveRefMin() {
    return this.__directiveRefMin;
  }

  public void set__directiveRefMin(String __directiveRefMin) {
    this.__directiveRefMin = __directiveRefMin;
  }

  public Integer getLocationsRefCount() {
    return this.locationsRefCount;
  }

  public void setLocationsRefCount(Integer locationsRefCount) {
    this.locationsRefCount = locationsRefCount;
  }

  public __DirectiveLocation getLocationsRefMax() {
    return this.locationsRefMax;
  }

  public void setLocationsRefMax(__DirectiveLocation locationsRefMax) {
    this.locationsRefMax = locationsRefMax;
  }

  public __DirectiveLocation getLocationsRefMin() {
    return this.locationsRefMin;
  }

  public void setLocationsRefMin(__DirectiveLocation locationsRefMin) {
    this.locationsRefMin = locationsRefMin;
  }

  public __DirectiveLocationsRelationInput toInput() {
    __DirectiveLocationsRelationInput input = new __DirectiveLocationsRelationInput();
    input.setId(this.getId());
    input.set__directiveRef(this.get__directiveRef());
    if(get__directive() != null) {
      input.set__directive(this.get__directive().toInput());
    }
    input.setLocationsRef(this.getLocationsRef());
    input.setIsDeprecated(this.getIsDeprecated());
    input.setVersion(this.getVersion());
    input.setRealmId(this.getRealmId());
    input.setCreateUserId(this.getCreateUserId());
    input.setCreateTime(this.getCreateTime());
    input.setUpdateUserId(this.getUpdateUserId());
    input.setUpdateTime(this.getUpdateTime());
    input.setCreateGroupId(this.getCreateGroupId());
    input.set__typename(this.get__typename());
    return input;
  }
}
