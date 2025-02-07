package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.__DirectiveLocation;
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
 * Mutation Arguments for __Directive List
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Arguments for __Directive List")
public class __DirectiveListMutationArguments implements MetaInput, __DirectiveInputBase {
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
   * description
   */
  @Description("description")
  private String description;

  /**
   * locations
   */
  @Description("locations")
  private Collection<__DirectiveLocation> locations;

  /**
   * args
   */
  @Description("args")
  private Collection<__InputValueInput> args;

  /**
   * isRepeatable
   */
  @Description("isRepeatable")
  private Boolean isRepeatable;

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
  @DefaultValue("__Directive")
  @Description("Type Name")
  private String __typename = "__Directive";

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  private Integer schemaId;

  /**
   * Relationship Object between __Directive and locations
   */
  @Description("Relationship Object between __Directive and locations")
  private Collection<__DirectiveLocationsRelationInput> __directiveLocationsRelation;

  /**
   * Input List
   */
  @Description("Input List")
  private Collection<__DirectiveInput> list;

  /**
   * Where
   */
  @Description("Where")
  private __DirectiveExpression where;

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
  public String getDescription() {
    return this.description;
  }

  @Override
  public void setDescription(String description) {
    this.description = (String)description;
  }

  @Override
  public Collection<__DirectiveLocation> getLocations() {
    return this.locations;
  }

  @Override
  public void setLocations(Collection<__DirectiveLocation> locations) {
    this.locations = (Collection<__DirectiveLocation>)locations;
  }

  @Override
  public Collection<__InputValueInput> getArgs() {
    return this.args;
  }

  @Override
  public void setArgs(Collection<__InputValueInput> args) {
    this.args = (Collection<__InputValueInput>)args;
  }

  @Override
  public Boolean getIsRepeatable() {
    return this.isRepeatable;
  }

  @Override
  public void setIsRepeatable(Boolean isRepeatable) {
    this.isRepeatable = (Boolean)isRepeatable;
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
  public Collection<__DirectiveLocationsRelationInput> get__directiveLocationsRelation() {
    return this.__directiveLocationsRelation;
  }

  @Override
  public void set__directiveLocationsRelation(
      Collection<__DirectiveLocationsRelationInput> __directiveLocationsRelation) {
    this.__directiveLocationsRelation = (Collection<__DirectiveLocationsRelationInput>)__directiveLocationsRelation;
  }

  public Collection<__DirectiveInput> getList() {
    return this.list;
  }

  public void setList(Collection<__DirectiveInput> list) {
    this.list = list;
  }

  @Override
  public __DirectiveExpression getWhere() {
    return this.where;
  }

  @Override
  public void setWhere(__DirectiveExpression where) {
    this.where = (__DirectiveExpression)where;
  }
}
