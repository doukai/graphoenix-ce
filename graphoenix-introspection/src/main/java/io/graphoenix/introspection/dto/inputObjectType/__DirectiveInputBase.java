package io.graphoenix.introspection.dto.inputObjectType;

import io.graphoenix.core.dto.enumType.__DirectiveLocation;
import io.graphoenix.core.dto.inputObjectType.MetaInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Mutation Input for __Directive
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Input for __Directive")
public interface __DirectiveInputBase extends MetaInput {
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
   * description
   */
  @Description("description")
  String description = null;

  /**
   * locations
   */
  @Description("locations")
  Collection<__DirectiveLocation> locations = null;

  /**
   * args
   */
  @Description("args")
  Collection<__InputValueInput> args = null;

  /**
   * isRepeatable
   */
  @Description("isRepeatable")
  Boolean isRepeatable = null;

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
  String __typename = "__Directive";

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  Integer schemaId = null;

  /**
   * Relationship Object between __Directive and locations
   */
  @Description("Relationship Object between __Directive and locations")
  Collection<__DirectiveLocationsRelationInput> __directiveLocationsRelation = null;

  /**
   * Where
   */
  @Description("Where")
  __DirectiveExpression where = null;

  default String getName() {
    return name;
  }

  void setName(String name);

  default __SchemaInput getOfSchema() {
    return ofSchema;
  }

  void setOfSchema(__SchemaInput ofSchema);

  default String getDescription() {
    return description;
  }

  void setDescription(String description);

  default Collection<__DirectiveLocation> getLocations() {
    return locations;
  }

  void setLocations(Collection<__DirectiveLocation> locations);

  default Collection<__InputValueInput> getArgs() {
    return args;
  }

  void setArgs(Collection<__InputValueInput> args);

  default Boolean getIsRepeatable() {
    return isRepeatable;
  }

  void setIsRepeatable(Boolean isRepeatable);

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

  default Collection<__DirectiveLocationsRelationInput> get__directiveLocationsRelation() {
    return __directiveLocationsRelation;
  }

  void set__directiveLocationsRelation(
      Collection<__DirectiveLocationsRelationInput> __directiveLocationsRelation);

  default __DirectiveExpression getWhere() {
    return where;
  }

  void setWhere(__DirectiveExpression where);
}
