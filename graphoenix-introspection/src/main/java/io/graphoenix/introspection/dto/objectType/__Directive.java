package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import io.graphoenix.introspection.dto.enumType.__DirectiveLocation;
import io.graphoenix.introspection.dto.inputObjectType.__DirectiveInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __Directive implements Meta {
  @Id
  private String id;

  @NonNull
  private String name;

  private __Schema ofSchema;

  private String description;

  @NonNull
  private Collection<__DirectiveLocation> locations;

  @NonNull
  private Collection<__InputValue> args;

  @NonNull
  private Boolean isRepeatable;

  /**
   * Is Deprecated
   */
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
  private Collection<__DirectiveLocationsRelation> __directiveLocationsRelation;

  /**
   * Aggregate Field for args
   */
  @Description("Aggregate Field for args")
  private __InputValue argsAggregate;

  /**
   * Connection Field for args
   */
  @Description("Connection Field for args")
  private __InputValueConnection argsConnection;

  /**
   * Aggregate Field for Relationship Object between __Directive and locations
   */
  @Description("Aggregate Field for Relationship Object between __Directive and locations")
  private __DirectiveLocationsRelation __directiveLocationsRelationAggregate;

  /**
   * Connection Field for Relationship Object between __Directive and locations
   */
  @Description("Connection Field for Relationship Object between __Directive and locations")
  private __DirectiveLocationsRelationConnection __directiveLocationsRelationConnection;

  /**
   * Count of __Directive
   */
  @Description("Count of __Directive")
  private Integer idCount;

  /**
   * Max of id
   */
  @Description("Max of id")
  private Integer idMax;

  /**
   * Min of id
   */
  @Description("Min of id")
  private Integer idMin;

  /**
   * Count of name
   */
  @Description("Count of name")
  private Integer nameCount;

  /**
   * Max of name
   */
  @Description("Max of name")
  private String nameMax;

  /**
   * Min of name
   */
  @Description("Min of name")
  private String nameMin;

  /**
   * Count of description
   */
  @Description("Count of description")
  private Integer descriptionCount;

  /**
   * Max of description
   */
  @Description("Max of description")
  private String descriptionMax;

  /**
   * Min of description
   */
  @Description("Min of description")
  private String descriptionMin;

  /**
   * Count of ofSchema Reference
   */
  @Description("Count of ofSchema Reference")
  private Integer schemaIdCount;

  /**
   * Sum of ofSchema Reference
   */
  @Description("Sum of ofSchema Reference")
  private Integer schemaIdSum;

  /**
   * Avg of ofSchema Reference
   */
  @Description("Avg of ofSchema Reference")
  private Integer schemaIdAvg;

  /**
   * Max of ofSchema Reference
   */
  @Description("Max of ofSchema Reference")
  private Integer schemaIdMax;

  /**
   * Min of ofSchema Reference
   */
  @Description("Min of ofSchema Reference")
  private Integer schemaIdMin;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = (String)id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public __Schema getOfSchema() {
    return this.ofSchema;
  }

  public void setOfSchema(__Schema ofSchema) {
    this.ofSchema = ofSchema;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Collection<__DirectiveLocation> getLocations() {
    return this.locations;
  }

  public void setLocations(Collection<__DirectiveLocation> locations) {
    this.locations = locations;
  }

  public Collection<__InputValue> getArgs() {
    return this.args;
  }

  public void setArgs(Collection<__InputValue> args) {
    this.args = args;
  }

  public Boolean getIsRepeatable() {
    return this.isRepeatable;
  }

  public void setIsRepeatable(Boolean isRepeatable) {
    this.isRepeatable = isRepeatable;
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

  public Collection<__DirectiveLocationsRelation> get__directiveLocationsRelation() {
    return this.__directiveLocationsRelation;
  }

  public void set__directiveLocationsRelation(
      Collection<__DirectiveLocationsRelation> __directiveLocationsRelation) {
    this.__directiveLocationsRelation = __directiveLocationsRelation;
  }

  public __InputValue getArgsAggregate() {
    return this.argsAggregate;
  }

  public void setArgsAggregate(__InputValue argsAggregate) {
    this.argsAggregate = argsAggregate;
  }

  public __InputValueConnection getArgsConnection() {
    return this.argsConnection;
  }

  public void setArgsConnection(__InputValueConnection argsConnection) {
    this.argsConnection = argsConnection;
  }

  public __DirectiveLocationsRelation get__directiveLocationsRelationAggregate() {
    return this.__directiveLocationsRelationAggregate;
  }

  public void set__directiveLocationsRelationAggregate(
      __DirectiveLocationsRelation __directiveLocationsRelationAggregate) {
    this.__directiveLocationsRelationAggregate = __directiveLocationsRelationAggregate;
  }

  public __DirectiveLocationsRelationConnection get__directiveLocationsRelationConnection() {
    return this.__directiveLocationsRelationConnection;
  }

  public void set__directiveLocationsRelationConnection(
      __DirectiveLocationsRelationConnection __directiveLocationsRelationConnection) {
    this.__directiveLocationsRelationConnection = __directiveLocationsRelationConnection;
  }

  public Integer getIdCount() {
    return this.idCount;
  }

  public void setIdCount(Integer idCount) {
    this.idCount = idCount;
  }

  public Integer getIdMax() {
    return this.idMax;
  }

  public void setIdMax(Integer idMax) {
    this.idMax = idMax;
  }

  public Integer getIdMin() {
    return this.idMin;
  }

  public void setIdMin(Integer idMin) {
    this.idMin = idMin;
  }

  public Integer getNameCount() {
    return this.nameCount;
  }

  public void setNameCount(Integer nameCount) {
    this.nameCount = nameCount;
  }

  public String getNameMax() {
    return this.nameMax;
  }

  public void setNameMax(String nameMax) {
    this.nameMax = nameMax;
  }

  public String getNameMin() {
    return this.nameMin;
  }

  public void setNameMin(String nameMin) {
    this.nameMin = nameMin;
  }

  public Integer getDescriptionCount() {
    return this.descriptionCount;
  }

  public void setDescriptionCount(Integer descriptionCount) {
    this.descriptionCount = descriptionCount;
  }

  public String getDescriptionMax() {
    return this.descriptionMax;
  }

  public void setDescriptionMax(String descriptionMax) {
    this.descriptionMax = descriptionMax;
  }

  public String getDescriptionMin() {
    return this.descriptionMin;
  }

  public void setDescriptionMin(String descriptionMin) {
    this.descriptionMin = descriptionMin;
  }

  public Integer getSchemaIdCount() {
    return this.schemaIdCount;
  }

  public void setSchemaIdCount(Integer schemaIdCount) {
    this.schemaIdCount = schemaIdCount;
  }

  public Integer getSchemaIdSum() {
    return this.schemaIdSum;
  }

  public void setSchemaIdSum(Integer schemaIdSum) {
    this.schemaIdSum = schemaIdSum;
  }

  public Integer getSchemaIdAvg() {
    return this.schemaIdAvg;
  }

  public void setSchemaIdAvg(Integer schemaIdAvg) {
    this.schemaIdAvg = schemaIdAvg;
  }

  public Integer getSchemaIdMax() {
    return this.schemaIdMax;
  }

  public void setSchemaIdMax(Integer schemaIdMax) {
    this.schemaIdMax = schemaIdMax;
  }

  public Integer getSchemaIdMin() {
    return this.schemaIdMin;
  }

  public void setSchemaIdMin(Integer schemaIdMin) {
    this.schemaIdMin = schemaIdMin;
  }

  public __DirectiveInput toInput() {
    __DirectiveInput input = new __DirectiveInput();
    input.setId(this.getId());
    input.setName(this.getName());
    if(getOfSchema() != null) {
      input.setOfSchema(this.getOfSchema().toInput());
    }
    input.setDescription(this.getDescription());
    input.setLocations(this.getLocations());
    if(getArgs() != null) {
      input.setArgs(this.getArgs().stream().map(item -> item.toInput()).collect(Collectors.toList()));
    }
    input.setIsRepeatable(this.getIsRepeatable());
    input.setIsDeprecated(this.getIsDeprecated());
    input.setVersion(this.getVersion());
    input.setRealmId(this.getRealmId());
    input.setCreateUserId(this.getCreateUserId());
    input.setCreateTime(this.getCreateTime());
    input.setUpdateUserId(this.getUpdateUserId());
    input.setUpdateTime(this.getUpdateTime());
    input.setCreateGroupId(this.getCreateGroupId());
    input.set__typename(this.get__typename());
    input.setSchemaId(this.getSchemaId());
    if(get__directiveLocationsRelation() != null) {
      input.set__directiveLocationsRelation(this.get__directiveLocationsRelation().stream().map(item -> item.toInput()).collect(Collectors.toList()));
    }
    return input;
  }
}
