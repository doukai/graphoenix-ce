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
 * Mutation Arguments for __EnumValue List
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Arguments for __EnumValue List")
public class __EnumValueListMutationArguments implements MetaInput, __EnumValueInputBase {
  /**
   * id
   */
  @Description("id")
  private String id;

  /**
   * name
   */
  @Description("name")
  private String name;

  /**
   * ofType
   */
  @Description("ofType")
  private __TypeInput ofType;

  /**
   * description
   */
  @Description("description")
  private String description;

  /**
   * deprecationReason
   */
  @Description("deprecationReason")
  private String deprecationReason;

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
  @DefaultValue("__EnumValue")
  @Description("Type Name")
  private String __typename = "__EnumValue";

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  private String ofTypeName;

  /**
   * Input List
   */
  @Description("Input List")
  private Collection<__EnumValueInput> list;

  /**
   * Where
   */
  @Description("Where")
  private __EnumValueExpression where;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = (String)id;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = (String)name;
  }

  @Override
  public __TypeInput getOfType() {
    return this.ofType;
  }

  @Override
  public void setOfType(__TypeInput ofType) {
    this.ofType = (__TypeInput)ofType;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public void setDescription(String description) {
    this.description = (String)description;
  }

  @Override
  public String getDeprecationReason() {
    return this.deprecationReason;
  }

  @Override
  public void setDeprecationReason(String deprecationReason) {
    this.deprecationReason = (String)deprecationReason;
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

  @Override
  public String getOfTypeName() {
    return this.ofTypeName;
  }

  @Override
  public void setOfTypeName(String ofTypeName) {
    this.ofTypeName = (String)ofTypeName;
  }

  public Collection<__EnumValueInput> getList() {
    return this.list;
  }

  public void setList(Collection<__EnumValueInput> list) {
    this.list = list;
  }

  public __EnumValueExpression getWhere() {
    return this.where;
  }

  public void setWhere(__EnumValueExpression where) {
    this.where = where;
  }
}
