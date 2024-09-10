package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Input;
import org.eclipse.microprofile.graphql.Name;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __TypeInterfacesOrderBy {
  private Sort id;

  private Sort typeRef;

  private __TypeOrderBy type;

  private Sort interfaceRef;

  @Name("interface")
  private __TypeOrderBy _interface;

  private Sort isDeprecated;

  private Sort version;

  private Sort realmId;

  private Sort createUserId;

  private Sort createTime;

  private Sort updateUserId;

  private Sort updateTime;

  private Sort createGroupId;

  private Sort __typename;

  private Sort idCount;

  private Sort idMax;

  private Sort idMin;

  private Sort typeRefCount;

  private Sort typeRefMax;

  private Sort typeRefMin;

  private Sort interfaceRefCount;

  private Sort interfaceRefMax;

  private Sort interfaceRefMin;

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

  public __TypeOrderBy getType() {
    return this.type;
  }

  public void setType(__TypeOrderBy type) {
    this.type = type;
  }

  public Sort getInterfaceRef() {
    return this.interfaceRef;
  }

  public void setInterfaceRef(Sort interfaceRef) {
    this.interfaceRef = interfaceRef;
  }

  public __TypeOrderBy get_interface() {
    return this._interface;
  }

  public void set_interface(__TypeOrderBy _interface) {
    this._interface = _interface;
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

  public Sort getInterfaceRefCount() {
    return this.interfaceRefCount;
  }

  public void setInterfaceRefCount(Sort interfaceRefCount) {
    this.interfaceRefCount = interfaceRefCount;
  }

  public Sort getInterfaceRefMax() {
    return this.interfaceRefMax;
  }

  public void setInterfaceRefMax(Sort interfaceRefMax) {
    this.interfaceRefMax = interfaceRefMax;
  }

  public Sort getInterfaceRefMin() {
    return this.interfaceRefMin;
  }

  public void setInterfaceRefMin(Sort interfaceRefMin) {
    this.interfaceRefMin = interfaceRefMin;
  }
}
