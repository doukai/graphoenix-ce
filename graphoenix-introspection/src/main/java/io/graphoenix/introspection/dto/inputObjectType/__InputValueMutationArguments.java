package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.inputObjectType.MetaInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Mutation Arguments for __InputValue
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Arguments for __InputValue")
public class __InputValueMutationArguments implements MetaInput, __InputValueInputBase {
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
   * fieldId
   */
  @Description("fieldId")
  private Integer fieldId;

  /**
   * directiveName
   */
  @Description("directiveName")
  private String directiveName;

  /**
   * description
   */
  @Description("description")
  private String description;

  /**
   * type
   */
  @Description("type")
  private __TypeInput type;

  /**
   * defaultValue
   */
  @Description("defaultValue")
  private String defaultValue;

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
  @DefaultValue("__InputValue")
  @Description("Type Name")
  private String __typename = "__InputValue";

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  private String ofTypeName;

  /**
   * type Reference
   */
  @Description("type Reference")
  private String typeName;

  /**
   * Input
   */
  @Description("Input")
  private __InputValueInput input;

  /**
   * Where
   */
  @Description("Where")
  private __InputValueExpression where;

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
  public Integer getFieldId() {
    return this.fieldId;
  }

  @Override
  public void setFieldId(Integer fieldId) {
    this.fieldId = (Integer)fieldId;
  }

  @Override
  public String getDirectiveName() {
    return this.directiveName;
  }

  @Override
  public void setDirectiveName(String directiveName) {
    this.directiveName = (String)directiveName;
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
  public __TypeInput getType() {
    return this.type;
  }

  @Override
  public void setType(__TypeInput type) {
    this.type = (__TypeInput)type;
  }

  @Override
  public String getDefaultValue() {
    return this.defaultValue;
  }

  @Override
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = (String)defaultValue;
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

  @Override
  public String getTypeName() {
    return this.typeName;
  }

  @Override
  public void setTypeName(String typeName) {
    this.typeName = (String)typeName;
  }

  public __InputValueInput getInput() {
    return this.input;
  }

  public void setInput(__InputValueInput input) {
    this.input = input;
  }

  @Override
  public __InputValueExpression getWhere() {
    return this.where;
  }

  @Override
  public void setWhere(__InputValueExpression where) {
    this.where = (__InputValueExpression)where;
  }
}
