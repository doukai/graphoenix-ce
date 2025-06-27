package io.graphoenix.introspection.dto.inputObjectType;

import io.graphoenix.core.dto.inputObjectType.MetaInput;
import io.graphoenix.introspection.dto.enumType.__TypeKind;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Mutation Input for __Type
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Input for __Type")
public interface __TypeInputBase extends MetaInput {
  /**
   * name
   */
  @Description("name")
  String name = null;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaInput ofSchema = null;

  /**
   * kind
   */
  @Description("kind")
  __TypeKind kind = null;

  /**
   * description
   */
  @Description("description")
  String description = null;

  /**
   * fields
   */
  @Description("fields")
  Collection<__FieldInput> fields = null;

  /**
   * interfaces
   */
  @Description("interfaces")
  Collection<__TypeInput> interfaces = null;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  Collection<__TypeInput> possibleTypes = null;

  /**
   * enumValues
   */
  @Description("enumValues")
  Collection<__EnumValueInput> enumValues = null;

  /**
   * inputFields
   */
  @Description("inputFields")
  Collection<__InputValueInput> inputFields = null;

  /**
   * ofType
   */
  @Description("ofType")
  __TypeInput ofType = null;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  Boolean isDeprecated = false;

  /**
   * Version
   */
  @Description("Version")
  Integer version = null;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  Integer realmId = null;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  String createUserId = null;

  /**
   * Create Time
   */
  @Description("Create Time")
  LocalDateTime createTime = null;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  String updateUserId = null;

  /**
   * Update Time
   */
  @Description("Update Time")
  LocalDateTime updateTime = null;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  String createGroupId = null;

  /**
   * Type Name
   */
  @Description("Type Name")
  String __typename = "__Type";

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  Integer schemaId = null;

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  String ofTypeName = null;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  Collection<__TypeInterfacesInput> __typeInterfaces = null;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  Collection<__TypePossibleTypesInput> __typePossibleTypes = null;

  /**
   * Where
   */
  @Description("Where")
  __TypeExpression where = null;

  default String getName() {
    return name;
  }

  void setName(String name);

  default __SchemaInput getOfSchema() {
    return ofSchema;
  }

  void setOfSchema(__SchemaInput ofSchema);

  default __TypeKind getKind() {
    return kind;
  }

  void setKind(__TypeKind kind);

  default String getDescription() {
    return description;
  }

  void setDescription(String description);

  default Collection<__FieldInput> getFields() {
    return fields;
  }

  void setFields(Collection<__FieldInput> fields);

  default Collection<__TypeInput> getInterfaces() {
    return interfaces;
  }

  void setInterfaces(Collection<__TypeInput> interfaces);

  default Collection<__TypeInput> getPossibleTypes() {
    return possibleTypes;
  }

  void setPossibleTypes(Collection<__TypeInput> possibleTypes);

  default Collection<__EnumValueInput> getEnumValues() {
    return enumValues;
  }

  void setEnumValues(Collection<__EnumValueInput> enumValues);

  default Collection<__InputValueInput> getInputFields() {
    return inputFields;
  }

  void setInputFields(Collection<__InputValueInput> inputFields);

  default __TypeInput getOfType() {
    return ofType;
  }

  void setOfType(__TypeInput ofType);

  default Boolean getIsDeprecated() {
    return isDeprecated;
  }

  void setIsDeprecated(Boolean isDeprecated);

  default Integer getVersion() {
    return version;
  }

  void setVersion(Integer version);

  default Integer getRealmId() {
    return realmId;
  }

  void setRealmId(Integer realmId);

  default String getCreateUserId() {
    return createUserId;
  }

  void setCreateUserId(String createUserId);

  default LocalDateTime getCreateTime() {
    return createTime;
  }

  void setCreateTime(LocalDateTime createTime);

  default String getUpdateUserId() {
    return updateUserId;
  }

  void setUpdateUserId(String updateUserId);

  default LocalDateTime getUpdateTime() {
    return updateTime;
  }

  void setUpdateTime(LocalDateTime updateTime);

  default String getCreateGroupId() {
    return createGroupId;
  }

  void setCreateGroupId(String createGroupId);

  default String get__typename() {
    return __typename;
  }

  void set__typename(String __typename);

  default Integer getSchemaId() {
    return schemaId;
  }

  void setSchemaId(Integer schemaId);

  default String getOfTypeName() {
    return ofTypeName;
  }

  void setOfTypeName(String ofTypeName);

  default Collection<__TypeInterfacesInput> get__typeInterfaces() {
    return __typeInterfaces;
  }

  void set__typeInterfaces(Collection<__TypeInterfacesInput> __typeInterfaces);

  default Collection<__TypePossibleTypesInput> get__typePossibleTypes() {
    return __typePossibleTypes;
  }

  void set__typePossibleTypes(Collection<__TypePossibleTypesInput> __typePossibleTypes);

  default __TypeExpression getWhere() {
    return where;
  }

  void setWhere(__TypeExpression where);
}
