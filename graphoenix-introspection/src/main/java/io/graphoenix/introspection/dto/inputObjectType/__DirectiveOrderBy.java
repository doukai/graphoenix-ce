package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __DirectiveOrderBy {
  private Sort name;

  private __SchemaOrderBy ofSchema;

  private Sort description;

  private Sort locations;

  private __InputValueOrderBy args;

  private Sort isRepeatable;

  private Sort isDeprecated;

  private Sort version;

  private Sort realmId;

  private Sort createUserId;

  private Sort createTime;

  private Sort updateUserId;

  private Sort updateTime;

  private Sort createGroupId;

  private Sort __typename;

  private Sort schemaId;

  private __DirectiveLocationsRelationOrderBy __directiveLocationsRelation;

  private Sort nameCount;

  private Sort nameMax;

  private Sort nameMin;

  private Sort descriptionCount;

  private Sort descriptionMax;

  private Sort descriptionMin;

  private Sort schemaIdCount;

  private Sort schemaIdSum;

  private Sort schemaIdAvg;

  private Sort schemaIdMax;

  private Sort schemaIdMin;

  public Sort getName() {
    return this.name;
  }

  public void setName(Sort name) {
    this.name = name;
  }

  public __SchemaOrderBy getOfSchema() {
    return this.ofSchema;
  }

  public void setOfSchema(__SchemaOrderBy ofSchema) {
    this.ofSchema = ofSchema;
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

  public __InputValueOrderBy getArgs() {
    return this.args;
  }

  public void setArgs(__InputValueOrderBy args) {
    this.args = args;
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

  public __DirectiveLocationsRelationOrderBy get__directiveLocationsRelation() {
    return this.__directiveLocationsRelation;
  }

  public void set__directiveLocationsRelation(
      __DirectiveLocationsRelationOrderBy __directiveLocationsRelation) {
    this.__directiveLocationsRelation = __directiveLocationsRelation;
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
