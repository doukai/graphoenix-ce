package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __DirectiveLocationsRelationOrderBy {
  private Sort id;

  private Sort __directiveRef;

  private __DirectiveOrderBy __directive;

  private Sort locationsRef;

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

  private Sort __directiveRefCount;

  private Sort __directiveRefMax;

  private Sort __directiveRefMin;

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

  public __DirectiveOrderBy get__directive() {
    return this.__directive;
  }

  public void set__directive(__DirectiveOrderBy __directive) {
    this.__directive = __directive;
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
}
