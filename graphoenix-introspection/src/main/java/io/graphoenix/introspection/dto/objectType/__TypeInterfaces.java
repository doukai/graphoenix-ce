package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __TypeInterfaces implements Meta {
  @Id
  private String id;

  private String typeRef;

  private __Type type;

  private String interfaceRef;

  @Name("interface")
  private __Type _interface;

  private Boolean isDeprecated;

  private Integer version;

  private Integer realmId;

  private String createUserId;

  private LocalDateTime createTime;

  private String updateUserId;

  private LocalDateTime updateTime;

  private String createGroupId;

  private String __typename;

  private Integer idCount;

  private Integer idMax;

  private Integer idMin;

  private Integer typeRefCount;

  private String typeRefMax;

  private String typeRefMin;

  private Integer interfaceRefCount;

  private String interfaceRefMax;

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
