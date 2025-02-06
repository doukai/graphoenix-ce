package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.__TypeKind;
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
 * Mutation Input for __Type
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Input for __Type")
public class __TypeInput implements MetaInput, __TypeInputBase {
  /**
   * name
   */
  @Description("name")
  private String name;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  private __SchemaInput ofSchema;

  /**
   * kind
   */
  @Description("kind")
  private __TypeKind kind;

  /**
   * description
   */
  @Description("description")
  private String description;

  /**
   * fields
   */
  @Description("fields")
  private Collection<__FieldInput> fields;

  /**
   * interfaces
   */
  @Description("interfaces")
  private Collection<__TypeInput> interfaces;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  private Collection<__TypeInput> possibleTypes;

  /**
   * enumValues
   */
  @Description("enumValues")
  private Collection<__EnumValueInput> enumValues;

  /**
   * inputFields
   */
  @Description("inputFields")
  private Collection<__InputValueInput> inputFields;

  /**
   * ofType
   */
  @Description("ofType")
  private __TypeInput ofType;

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
  @DefaultValue("__Type")
  @Description("Type Name")
  private String __typename = "__Type";

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  private Integer schemaId;

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  private String ofTypeName;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  private Collection<__TypeInterfacesInput> __typeInterfaces;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  private Collection<__TypePossibleTypesInput> __typePossibleTypes;

  /**
   * Where
   */
  @Description("Where")
  private __TypeExpression where;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = (String)name;
  }

  @Override
  public __SchemaInput getOfSchema() {
    return this.ofSchema;
  }

  @Override
  public void setOfSchema(__SchemaInput ofSchema) {
    this.ofSchema = (__SchemaInput)ofSchema;
  }

  @Override
  public __TypeKind getKind() {
    return this.kind;
  }

  @Override
  public void setKind(__TypeKind kind) {
    this.kind = (__TypeKind)kind;
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
  public Collection<__FieldInput> getFields() {
    return this.fields;
  }

  @Override
  public void setFields(Collection<__FieldInput> fields) {
    this.fields = (Collection<__FieldInput>)fields;
  }

  @Override
  public Collection<__TypeInput> getInterfaces() {
    return this.interfaces;
  }

  @Override
  public void setInterfaces(Collection<__TypeInput> interfaces) {
    this.interfaces = (Collection<__TypeInput>)interfaces;
  }

  @Override
  public Collection<__TypeInput> getPossibleTypes() {
    return this.possibleTypes;
  }

  @Override
  public void setPossibleTypes(Collection<__TypeInput> possibleTypes) {
    this.possibleTypes = (Collection<__TypeInput>)possibleTypes;
  }

  @Override
  public Collection<__EnumValueInput> getEnumValues() {
    return this.enumValues;
  }

  @Override
  public void setEnumValues(Collection<__EnumValueInput> enumValues) {
    this.enumValues = (Collection<__EnumValueInput>)enumValues;
  }

  @Override
  public Collection<__InputValueInput> getInputFields() {
    return this.inputFields;
  }

  @Override
  public void setInputFields(Collection<__InputValueInput> inputFields) {
    this.inputFields = (Collection<__InputValueInput>)inputFields;
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
  public Integer getSchemaId() {
    return this.schemaId;
  }

  @Override
  public void setSchemaId(Integer schemaId) {
    this.schemaId = (Integer)schemaId;
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
  public Collection<__TypeInterfacesInput> get__typeInterfaces() {
    return this.__typeInterfaces;
  }

  @Override
  public void set__typeInterfaces(Collection<__TypeInterfacesInput> __typeInterfaces) {
    this.__typeInterfaces = (Collection<__TypeInterfacesInput>)__typeInterfaces;
  }

  @Override
  public Collection<__TypePossibleTypesInput> get__typePossibleTypes() {
    return this.__typePossibleTypes;
  }

  @Override
  public void set__typePossibleTypes(Collection<__TypePossibleTypesInput> __typePossibleTypes) {
    this.__typePossibleTypes = (Collection<__TypePossibleTypesInput>)__typePossibleTypes;
  }

  public __TypeExpression getWhere() {
    return this.where;
  }

  public void setWhere(__TypeExpression where) {
    this.where = where;
  }
}
