package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.__DirectiveLocation;
import io.graphoenix.core.dto.interfaceType.Meta;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __DirectiveLocationsRelation implements Meta {
  @Id
  private String id;

  private String __directiveRef;

  private __Directive __directive;

  private __DirectiveLocation locationsRef;

  private Boolean isDeprecated = false;

  private Integer version;

  private Integer realmId;

  private String createUserId;

  private LocalDateTime createTime;

  private String updateUserId;

  private LocalDateTime updateTime;

  private String createGroupId;

  private String __typename = "__DirectiveLocationsRelation";

  private Integer idCount;

  private Integer idMax;

  private Integer idMin;

  private Integer __directiveRefCount;

  private String __directiveRefMax;

  private String __directiveRefMin;

  private Integer locationsRefCount;

  private __DirectiveLocation locationsRefMax;

  private __DirectiveLocation locationsRefMin;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
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
}
