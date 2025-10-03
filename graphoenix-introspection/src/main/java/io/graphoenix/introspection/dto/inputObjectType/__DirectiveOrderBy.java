package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for __Directive
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Order Input for __Directive")
public class __DirectiveOrderBy {
  /**
   * id
   */
  @Description("id")
  private Sort id;

  /**
   * name
   */
  @Description("name")
  private Sort name;

  /**
   * description
   */
  @Description("description")
  private Sort description;

  /**
   * locations
   */
  @Description("locations")
  private Sort locations;

  /**
   * isRepeatable
   */
  @Description("isRepeatable")
  private Sort isRepeatable;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  private Sort isDeprecated;

  /**
   * Version
   */
  @Description("Version")
  private Sort version;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  private Sort realmId;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  private Sort createUserId;

  /**
   * Create Time
   */
  @Description("Create Time")
  private Sort createTime;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  private Sort updateUserId;

  /**
   * Update Time
   */
  @Description("Update Time")
  private Sort updateTime;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  private Sort createGroupId;

  /**
   * Type Name
   */
  @Description("Type Name")
  private Sort __typename;

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  private Sort schemaId;

  /**
   * Count of __Directive
   */
  @Description("Count of __Directive")
  private Sort idCount;

  /**
   * Max of id
   */
  @Description("Max of id")
  private Sort idMax;

  /**
   * Min of id
   */
  @Description("Min of id")
  private Sort idMin;

  /**
   * Count of name
   */
  @Description("Count of name")
  private Sort nameCount;

  /**
   * Max of name
   */
  @Description("Max of name")
  private Sort nameMax;

  /**
   * Min of name
   */
  @Description("Min of name")
  private Sort nameMin;

  /**
   * Count of description
   */
  @Description("Count of description")
  private Sort descriptionCount;

  /**
   * Max of description
   */
  @Description("Max of description")
  private Sort descriptionMax;

  /**
   * Min of description
   */
  @Description("Min of description")
  private Sort descriptionMin;

  /**
   * Count of ofSchema Reference
   */
  @Description("Count of ofSchema Reference")
  private Sort schemaIdCount;

  /**
   * Sum of ofSchema Reference
   */
  @Description("Sum of ofSchema Reference")
  private Sort schemaIdSum;

  /**
   * Avg of ofSchema Reference
   */
  @Description("Avg of ofSchema Reference")
  private Sort schemaIdAvg;

  /**
   * Max of ofSchema Reference
   */
  @Description("Max of ofSchema Reference")
  private Sort schemaIdMax;

  /**
   * Min of ofSchema Reference
   */
  @Description("Min of ofSchema Reference")
  private Sort schemaIdMin;

  public Sort getId() {
    return this.id;
  }

  public void setId(Sort id) {
    this.id = id;
  }

  public Sort getName() {
    return this.name;
  }

  public void setName(Sort name) {
    this.name = name;
  }

  public Sort getDescription() {
    return this.description;
  }

  public void setDescription(Sort description) {
    this.description = description;
  }

  public Sort getLocations() {
    return this.locations;
  }

  public void setLocations(Sort locations) {
    this.locations = locations;
  }

  public Sort getIsRepeatable() {
    return this.isRepeatable;
  }

  public void setIsRepeatable(Sort isRepeatable) {
    this.isRepeatable = isRepeatable;
  }

  public Sort getIsDeprecated() {
    return this.isDeprecated;
  }

  public void setIsDeprecated(Sort isDeprecated) {
    this.isDeprecated = isDeprecated;
  }

  public Sort getVersion() {
    return this.version;
  }

  public void setVersion(Sort version) {
    this.version = version;
  }

  public Sort getRealmId() {
    return this.realmId;
  }

  public void setRealmId(Sort realmId) {
    this.realmId = realmId;
  }

  public Sort getCreateUserId() {
    return this.createUserId;
  }

  public void setCreateUserId(Sort createUserId) {
    this.createUserId = createUserId;
  }

  public Sort getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Sort createTime) {
    this.createTime = createTime;
  }

  public Sort getUpdateUserId() {
    return this.updateUserId;
  }

  public void setUpdateUserId(Sort updateUserId) {
    this.updateUserId = updateUserId;
  }

  public Sort getUpdateTime() {
    return this.updateTime;
  }

  public void setUpdateTime(Sort updateTime) {
    this.updateTime = updateTime;
  }

  public Sort getCreateGroupId() {
    return this.createGroupId;
  }

  public void setCreateGroupId(Sort createGroupId) {
    this.createGroupId = createGroupId;
  }

  public Sort get__typename() {
    return this.__typename;
  }

  public void set__typename(Sort __typename) {
    this.__typename = __typename;
  }

  public Sort getSchemaId() {
    return this.schemaId;
  }

  public void setSchemaId(Sort schemaId) {
    this.schemaId = schemaId;
  }

  public Sort getIdCount() {
    return this.idCount;
  }

  public void setIdCount(Sort idCount) {
    this.idCount = idCount;
  }

  public Sort getIdMax() {
    return this.idMax;
  }

  public void setIdMax(Sort idMax) {
    this.idMax = idMax;
  }

  public Sort getIdMin() {
    return this.idMin;
  }

  public void setIdMin(Sort idMin) {
    this.idMin = idMin;
  }

  public Sort getNameCount() {
    return this.nameCount;
  }

  public void setNameCount(Sort nameCount) {
    this.nameCount = nameCount;
  }

  public Sort getNameMax() {
    return this.nameMax;
  }

  public void setNameMax(Sort nameMax) {
    this.nameMax = nameMax;
  }

  public Sort getNameMin() {
    return this.nameMin;
  }

  public void setNameMin(Sort nameMin) {
    this.nameMin = nameMin;
  }

  public Sort getDescriptionCount() {
    return this.descriptionCount;
  }

  public void setDescriptionCount(Sort descriptionCount) {
    this.descriptionCount = descriptionCount;
  }

  public Sort getDescriptionMax() {
    return this.descriptionMax;
  }

  public void setDescriptionMax(Sort descriptionMax) {
    this.descriptionMax = descriptionMax;
  }

  public Sort getDescriptionMin() {
    return this.descriptionMin;
  }

  public void setDescriptionMin(Sort descriptionMin) {
    this.descriptionMin = descriptionMin;
  }

  public Sort getSchemaIdCount() {
    return this.schemaIdCount;
  }

  public void setSchemaIdCount(Sort schemaIdCount) {
    this.schemaIdCount = schemaIdCount;
  }

  public Sort getSchemaIdSum() {
    return this.schemaIdSum;
  }

  public void setSchemaIdSum(Sort schemaIdSum) {
    this.schemaIdSum = schemaIdSum;
  }

  public Sort getSchemaIdAvg() {
    return this.schemaIdAvg;
  }

  public void setSchemaIdAvg(Sort schemaIdAvg) {
    this.schemaIdAvg = schemaIdAvg;
  }

  public Sort getSchemaIdMax() {
    return this.schemaIdMax;
  }

  public void setSchemaIdMax(Sort schemaIdMax) {
    this.schemaIdMax = schemaIdMax;
  }

  public Sort getSchemaIdMin() {
    return this.schemaIdMin;
  }

  public void setSchemaIdMin(Sort schemaIdMin) {
    this.schemaIdMin = schemaIdMin;
  }
}
