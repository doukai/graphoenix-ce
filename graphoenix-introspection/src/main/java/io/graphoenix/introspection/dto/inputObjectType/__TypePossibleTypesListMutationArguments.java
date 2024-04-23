package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.MetaInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __TypePossibleTypesListMutationArguments implements MetaInput {
  private String id;

  private String typeRef;

  private __TypeInput type;

  private String possibleTypeRef;

  private __TypeInput possibleType;

  private Boolean isDeprecated;

  private Integer version;

  private Integer realmId;

  private String createUserId;

  private LocalDateTime createTime;

  private String updateUserId;

  private LocalDateTime updateTime;

  private String createGroupId;

  @DefaultValue("\"__TypePossibleTypes\"")
  private String __typename;

  private Collection<__TypePossibleTypesInput> list;

  private __TypePossibleTypesExpression where;

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

  public __TypeInput getType() {
    return this.type;
  }

  public void setType(__TypeInput type) {
    this.type = type;
  }

  public String getPossibleTypeRef() {
    return this.possibleTypeRef;
  }

  public void setPossibleTypeRef(String possibleTypeRef) {
    this.possibleTypeRef = possibleTypeRef;
  }

  public __TypeInput getPossibleType() {
    return this.possibleType;
  }

  public void setPossibleType(__TypeInput possibleType) {
    this.possibleType = possibleType;
  }

  public Boolean getIsDeprecated() {
    return this.isDeprecated;
  }

  public void setIsDeprecated(Boolean isDeprecated) {
    this.isDeprecated = isDeprecated;
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

  public Collection<__TypePossibleTypesInput> getList() {
    return this.list;
  }

  public void setList(Collection<__TypePossibleTypesInput> list) {
    this.list = list;
  }

  @Override
  public __TypePossibleTypesExpression getWhere() {
    return this.where;
  }

  @Override
  public void setWhere(MetaExpression where) {
    this.where = (__TypePossibleTypesExpression)where;
  }
}
