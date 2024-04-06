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
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __TypePossibleTypes implements Meta {
  @Id
  private String id;

  private String typeRef;

  private __Type type;

  private String possibleTypeRef;

  private __Type possibleType;

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

  private Integer possibleTypeRefCount;

  private String possibleTypeRefMax;

  private String possibleTypeRefMin;

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

  public String getPossibleTypeRef() {
    return this.possibleTypeRef;
  }

  public void setPossibleTypeRef(String possibleTypeRef) {
    this.possibleTypeRef = possibleTypeRef;
  }

  public __Type getPossibleType() {
    return this.possibleType;
  }

  public void setPossibleType(__Type possibleType) {
    this.possibleType = possibleType;
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

  public Integer getPossibleTypeRefCount() {
    return this.possibleTypeRefCount;
  }

  public void setPossibleTypeRefCount(Integer possibleTypeRefCount) {
    this.possibleTypeRefCount = possibleTypeRefCount;
  }

  public String getPossibleTypeRefMax() {
    return this.possibleTypeRefMax;
  }

  public void setPossibleTypeRefMax(String possibleTypeRefMax) {
    this.possibleTypeRefMax = possibleTypeRefMax;
  }

  public String getPossibleTypeRefMin() {
    return this.possibleTypeRefMin;
  }

  public void setPossibleTypeRefMin(String possibleTypeRefMin) {
    this.possibleTypeRefMin = possibleTypeRefMin;
  }
}
