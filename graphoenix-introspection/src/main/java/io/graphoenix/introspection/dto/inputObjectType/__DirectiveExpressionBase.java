package io.graphoenix.introspection.dto.inputObjectType;

import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.BooleanExpression;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Query Expression Input for __Directive
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for __Directive")
public interface __DirectiveExpressionBase extends MetaExpression {
  /**
   * name
   */
  @Description("name")
  StringExpression name = null;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaExpression ofSchema = null;

  /**
   * description
   */
  @Description("description")
  StringExpression description = null;

  /**
   * locations
   */
  @Description("locations")
  __DirectiveLocationExpression locations = null;

  /**
   * args
   */
  @Description("args")
  __InputValueExpression args = null;

  /**
   * isRepeatable
   */
  @Description("isRepeatable")
  BooleanExpression isRepeatable = null;

  /**
   * Include Deprecated
   */
  @Description("Include Deprecated")
  Boolean includeDeprecated = false;

  /**
   * Version
   */
  @Description("Version")
  IntExpression version = null;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  IntExpression realmId = null;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  StringExpression createUserId = null;

  /**
   * Create Time
   */
  @Description("Create Time")
  StringExpression createTime = null;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  StringExpression updateUserId = null;

  /**
   * Update Time
   */
  @Description("Update Time")
  StringExpression updateTime = null;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  StringExpression createGroupId = null;

  /**
   * Type Name
   */
  @Description("Type Name")
  StringExpression __typename = null;

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  IntExpression schemaId = null;

  /**
   * Relationship Object between __Directive and locations
   */
  @Description("Relationship Object between __Directive and locations")
  __DirectiveLocationsRelationExpression __directiveLocationsRelation = null;

  /**
   * Not
   */
  @Description("Not")
  Boolean not = false;

  /**
   * Condition
   */
  @Description("Condition")
  Conditional cond = Conditional.AND;

  /**
   * Expressions
   */
  @Description("Expressions")
  Collection<__DirectiveExpression> exs = null;

  default StringExpression getName() {
    return name;
  }

  void setName(StringExpression name);

  default __SchemaExpression getOfSchema() {
    return ofSchema;
  }

  void setOfSchema(__SchemaExpression ofSchema);

  default StringExpression getDescription() {
    return description;
  }

  void setDescription(StringExpression description);

  default __DirectiveLocationExpression getLocations() {
    return locations;
  }

  void setLocations(__DirectiveLocationExpression locations);

  default __InputValueExpression getArgs() {
    return args;
  }

  void setArgs(__InputValueExpression args);

  default BooleanExpression getIsRepeatable() {
    return isRepeatable;
  }

  void setIsRepeatable(BooleanExpression isRepeatable);

  default Boolean getIncludeDeprecated() {
    return includeDeprecated;
  }

  void setIncludeDeprecated(Boolean includeDeprecated);

  default IntExpression getVersion() {
    return version;
  }

  void setVersion(IntExpression version);

  default IntExpression getRealmId() {
    return realmId;
  }

  void setRealmId(IntExpression realmId);

  default StringExpression getCreateUserId() {
    return createUserId;
  }

  void setCreateUserId(StringExpression createUserId);

  default StringExpression getCreateTime() {
    return createTime;
  }

  void setCreateTime(StringExpression createTime);

  default StringExpression getUpdateUserId() {
    return updateUserId;
  }

  void setUpdateUserId(StringExpression updateUserId);

  default StringExpression getUpdateTime() {
    return updateTime;
  }

  void setUpdateTime(StringExpression updateTime);

  default StringExpression getCreateGroupId() {
    return createGroupId;
  }

  void setCreateGroupId(StringExpression createGroupId);

  default StringExpression get__typename() {
    return __typename;
  }

  void set__typename(StringExpression __typename);

  default IntExpression getSchemaId() {
    return schemaId;
  }

  void setSchemaId(IntExpression schemaId);

  default __DirectiveLocationsRelationExpression get__directiveLocationsRelation() {
    return __directiveLocationsRelation;
  }

  void set__directiveLocationsRelation(
      __DirectiveLocationsRelationExpression __directiveLocationsRelation);

  default Boolean getNot() {
    return not;
  }

  void setNot(Boolean not);

  default Conditional getCond() {
    return cond;
  }

  void setCond(Conditional cond);

  default Collection<__DirectiveExpression> getExs() {
    return exs;
  }

  void setExs(Collection<__DirectiveExpression> exs);
}
