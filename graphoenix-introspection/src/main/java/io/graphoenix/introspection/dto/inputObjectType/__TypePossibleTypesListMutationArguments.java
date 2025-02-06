package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.inputObjectType.MetaInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Mutation Arguments for Relationship Object between __Type and __Type List
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Arguments for Relationship Object between __Type and __Type List")
public class __TypePossibleTypesListMutationArguments implements MetaInput, __TypePossibleTypesInputBase {
  /**
   * ID
   */
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
  private __TypeInput type;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  private String possibleTypeRef;

  /**
   * __Type
   */
  @Description("__Type")
  private __TypeInput possibleType;

  /**
   * Is Deprecated
   */
  @DefaultValue("false")
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
  @DefaultValue("__TypePossibleTypes")
  @Description("Type Name")
  private String __typename = "__TypePossibleTypes";

  /**
   * Input List
   */
  @Description("Input List")
  private Collection<__TypePossibleTypesInput> list;

  /**
   * Where
   */
  @Description("Where")
  private __TypePossibleTypesExpression where;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = (String)id;
  }

  @Override
  public String getTypeRef() {
    return this.typeRef;
  }

  @Override
  public void setTypeRef(String typeRef) {
    this.typeRef = (String)typeRef;
  }

  @Override
  public __TypeInput getType() {
    return this.type;
  }

  @Override
  public void setType(__TypeInput type) {
    this.type = (__TypeInput)type;
  }

  @Override
  public String getPossibleTypeRef() {
    return this.possibleTypeRef;
  }

  @Override
  public void setPossibleTypeRef(String possibleTypeRef) {
    this.possibleTypeRef = (String)possibleTypeRef;
  }

  @Override
  public __TypeInput getPossibleType() {
    return this.possibleType;
  }

  @Override
  public void setPossibleType(__TypeInput possibleType) {
    this.possibleType = (__TypeInput)possibleType;
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

  @Override
  public String get__typename() {
    return this.__typename;
  }

  @Override
  public void set__typename(String __typename) {
    this.__typename = (String)__typename;
  }

  public Collection<__TypePossibleTypesInput> getList() {
    return this.list;
  }

  public void setList(Collection<__TypePossibleTypesInput> list) {
    this.list = list;
  }

  public __TypePossibleTypesExpression getWhere() {
    return this.where;
  }

  public void setWhere(__TypePossibleTypesExpression where) {
    this.where = where;
  }
}
