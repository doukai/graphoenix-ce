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
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __TypeListMutationArguments implements MetaInput {
  private String name;

  private __SchemaInput ofSchema;

  private __TypeKind kind;

  private String description;

  private Collection<__FieldInput> fields;

  private Collection<__TypeInput> interfaces;

  private Collection<__TypeInput> possibleTypes;

  private Collection<__EnumValueInput> enumValues;

  private Collection<__InputValueInput> inputFields;

  private __TypeInput ofType;

  private Boolean isDeprecated;

  private Integer version;

  private Integer realmId;

  private String createUserId;

  private LocalDateTime createTime;

  private String updateUserId;

  private LocalDateTime updateTime;

  private String createGroupId;

  @DefaultValue("\"__Type\"")
  private String __typename;

  private Integer schemaId;

  private String ofTypeName;

  private Collection<__TypeInterfacesInput> __typeInterfaces;

  private Collection<__TypePossibleTypesInput> __typePossibleTypes;

  private Collection<__TypeInput> list;

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
    this.version = version;
  }

  @Override
  public Integer getRealmId() {
    return this.realmId;
  }

  @Override
  public void setRealmId(Integer realmId) {
    this.realmId = realmId;
  }

  @Override
  public String getCreateUserId() {
    return this.createUserId;
  }

  @Override
  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }

  @Override
  public LocalDateTime getCreateTime() {
    return this.createTime;
  }

  @Override
  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  @Override
  public String getUpdateUserId() {
    return this.updateUserId;
  }

  @Override
  public void setUpdateUserId(String updateUserId) {
    this.updateUserId = updateUserId;
  }

  @Override
  public LocalDateTime getUpdateTime() {
    return this.updateTime;
  }

  @Override
  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public String getCreateGroupId() {
    return this.createGroupId;
  }

  @Override
  public void setCreateGroupId(String createGroupId) {
    this.createGroupId = createGroupId;
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

  public Collection<__TypeInput> getList() {
    return this.list;
  }

  public void setList(Collection<__TypeInput> list) {
    this.list = list;
  }

  public __TypeExpression getWhere() {
    return this.where;
  }

  public void setWhere(__TypeExpression where) {
    this.where = where;
  }
}
