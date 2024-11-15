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
 * Mutation Arguments for __Type
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Arguments for __Type")
public class __TypeMutationArguments implements MetaInput {
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
  @DefaultValue("\"__Type\"")
  @Description("Type Name")
  private String __typename = "\"__Type\"";

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
   * Input
   */
  @Description("Input")
  private __TypeInput input;

  /**
   * Where
   */
  @Description("Where")
  private __TypeExpression where;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public __SchemaInput getOfSchema() {
    return this.ofSchema;
  }

  public void setOfSchema(__SchemaInput ofSchema) {
    this.ofSchema = ofSchema;
  }

  public __TypeKind getKind() {
    return this.kind;
  }

  public void setKind(__TypeKind kind) {
    this.kind = kind;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Collection<__FieldInput> getFields() {
    return this.fields;
  }

  public void setFields(Collection<__FieldInput> fields) {
    this.fields = fields;
  }

  public Collection<__TypeInput> getInterfaces() {
    return this.interfaces;
  }

  public void setInterfaces(Collection<__TypeInput> interfaces) {
    this.interfaces = interfaces;
  }

  public Collection<__TypeInput> getPossibleTypes() {
    return this.possibleTypes;
  }

  public void setPossibleTypes(Collection<__TypeInput> possibleTypes) {
    this.possibleTypes = possibleTypes;
  }

  public Collection<__EnumValueInput> getEnumValues() {
    return this.enumValues;
  }

  public void setEnumValues(Collection<__EnumValueInput> enumValues) {
    this.enumValues = enumValues;
  }

  public Collection<__InputValueInput> getInputFields() {
    return this.inputFields;
  }

  public void setInputFields(Collection<__InputValueInput> inputFields) {
    this.inputFields = inputFields;
  }

  public __TypeInput getOfType() {
    return this.ofType;
  }

  public void setOfType(__TypeInput ofType) {
    this.ofType = ofType;
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

  public Integer getSchemaId() {
    return this.schemaId;
  }

  public void setSchemaId(Integer schemaId) {
    this.schemaId = schemaId;
  }

  public String getOfTypeName() {
    return this.ofTypeName;
  }

  public void setOfTypeName(String ofTypeName) {
    this.ofTypeName = ofTypeName;
  }

  public Collection<__TypeInterfacesInput> get__typeInterfaces() {
    return this.__typeInterfaces;
  }

  public void set__typeInterfaces(Collection<__TypeInterfacesInput> __typeInterfaces) {
    this.__typeInterfaces = __typeInterfaces;
  }

  public Collection<__TypePossibleTypesInput> get__typePossibleTypes() {
    return this.__typePossibleTypes;
  }

  public void set__typePossibleTypes(Collection<__TypePossibleTypesInput> __typePossibleTypes) {
    this.__typePossibleTypes = __typePossibleTypes;
  }

  public __TypeInput getInput() {
    return this.input;
  }

  public void setInput(__TypeInput input) {
    this.input = input;
  }

  public __TypeExpression getWhere() {
    return this.where;
  }

  public void setWhere(__TypeExpression where) {
    this.where = where;
  }
}
