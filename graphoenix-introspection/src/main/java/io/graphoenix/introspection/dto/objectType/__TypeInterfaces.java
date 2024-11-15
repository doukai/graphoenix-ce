package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Type;

/**
 * Relationship Object between __Type and __Type
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Relationship Object between __Type and __Type")
public class __TypeInterfaces implements Meta {
  /**
   * ID
   */
  @Id
  @Description("ID")
  private String id;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  private String typeRef;

  /**
   * __Type
   */
  @Description("__Type")
  private __Type type;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  private String interfaceRef;

  /**
   * __Type
   */
  @Name("interface")
  @Description("__Type")
  private __Type _interface;

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
  private String __typename = "__TypeInterfaces";

  /**
   * Count of Relationship Object between __Type and __Type
   */
  @Description("Count of Relationship Object between __Type and __Type")
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
   * Count of __Type Reference
   */
  @Description("Count of __Type Reference")
  private Integer typeRefCount;

  /**
   * Max of __Type Reference
   */
  @Description("Max of __Type Reference")
  private String typeRefMax;

  /**
   * Min of __Type Reference
   */
  @Description("Min of __Type Reference")
  private String typeRefMin;

  /**
   * Count of __Type Reference
   */
  @Description("Count of __Type Reference")
  private Integer interfaceRefCount;

  /**
   * Max of __Type Reference
   */
  @Description("Max of __Type Reference")
  private String interfaceRefMax;

  /**
   * Min of __Type Reference
   */
  @Description("Min of __Type Reference")
  private String interfaceRefMin;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTypeRef() {
    return this.typeRef;
  }

  public void setTypeRef(String typeRef) {
    this.typeRef = typeRef;
  }

  public __Type getType() {
    return this.type;
  }

  public void setType(__Type type) {
    this.type = type;
  }

  public String getInterfaceRef() {
    return this.interfaceRef;
  }

  public void setInterfaceRef(String interfaceRef) {
    this.interfaceRef = interfaceRef;
  }

  public __Type get_interface() {
    return this._interface;
  }

  public void set_interface(__Type _interface) {
    this._interface = _interface;
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

  public Integer getTypeRefCount() {
    return this.typeRefCount;
  }

  public void setTypeRefCount(Integer typeRefCount) {
    this.typeRefCount = typeRefCount;
  }

  public String getTypeRefMax() {
    return this.typeRefMax;
  }

  public void setTypeRefMax(String typeRefMax) {
    this.typeRefMax = typeRefMax;
  }

  public String getTypeRefMin() {
    return this.typeRefMin;
  }

  public void setTypeRefMin(String typeRefMin) {
    this.typeRefMin = typeRefMin;
  }

  public Integer getInterfaceRefCount() {
    return this.interfaceRefCount;
  }

  public void setInterfaceRefCount(Integer interfaceRefCount) {
    this.interfaceRefCount = interfaceRefCount;
  }

  public String getInterfaceRefMax() {
    return this.interfaceRefMax;
  }

  public void setInterfaceRefMax(String interfaceRefMax) {
    this.interfaceRefMax = interfaceRefMax;
  }

  public String getInterfaceRefMin() {
    return this.interfaceRefMin;
  }

  public void setInterfaceRefMin(String interfaceRefMin) {
    this.interfaceRefMin = interfaceRefMin;
  }
}
