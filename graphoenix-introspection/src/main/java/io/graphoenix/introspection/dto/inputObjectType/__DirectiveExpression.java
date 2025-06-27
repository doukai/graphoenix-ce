package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.BooleanExpression;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Override;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Query Expression Input for __Directive
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for __Directive")
public class __DirectiveExpression implements MetaExpression, __DirectiveExpressionBase {
  /**
   * name
   */
  @Description("name")
  private StringExpression name;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  private __SchemaExpression ofSchema;

  /**
   * description
   */
  @Description("description")
  private StringExpression description;

  /**
   * locations
   */
  @Description("locations")
  private __DirectiveLocationExpression locations;

  /**
   * args
   */
  @Description("args")
  private __InputValueExpression args;

  /**
   * isRepeatable
   */
  @Description("isRepeatable")
  private BooleanExpression isRepeatable;

  /**
   * Include Deprecated
   */
  @DefaultValue("false")
  @Description("Include Deprecated")
  private Boolean includeDeprecated = false;

  /**
   * Version
   */
  @Description("Version")
  private IntExpression version;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  private IntExpression realmId;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  private StringExpression createUserId;

  /**
   * Create Time
   */
  @Description("Create Time")
  private StringExpression createTime;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  private StringExpression updateUserId;

  /**
   * Update Time
   */
  @Description("Update Time")
  private StringExpression updateTime;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  private StringExpression createGroupId;

  /**
   * Type Name
   */
  @Description("Type Name")
  private StringExpression __typename;

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  private IntExpression schemaId;

  /**
   * Relationship Object between __Directive and locations
   */
  @Description("Relationship Object between __Directive and locations")
  private __DirectiveLocationsRelationExpression __directiveLocationsRelation;

  /**
   * Not
   */
  @DefaultValue("false")
  @Description("Not")
  private Boolean not = false;

  /**
   * Condition
   */
  @DefaultValue("AND")
  @Description("Condition")
  private Conditional cond = Conditional.AND;

  /**
   * Expressions
   */
  @Description("Expressions")
  private Collection<__DirectiveExpression> exs;

  @Override
  public StringExpression getName() {
    return this.name;
  }

  @Override
  public void setName(StringExpression name) {
    this.name = (StringExpression)name;
  }

  @Override
  public __SchemaExpression getOfSchema() {
    return this.ofSchema;
  }

  @Override
  public void setOfSchema(__SchemaExpression ofSchema) {
    this.ofSchema = (__SchemaExpression)ofSchema;
  }

  @Override
  public StringExpression getDescription() {
    return this.description;
  }

  @Override
  public void setDescription(StringExpression description) {
    this.description = (StringExpression)description;
  }

  @Override
  public __DirectiveLocationExpression getLocations() {
    return this.locations;
  }

  @Override
  public void setLocations(__DirectiveLocationExpression locations) {
    this.locations = (__DirectiveLocationExpression)locations;
  }

  @Override
  public __InputValueExpression getArgs() {
    return this.args;
  }

  @Override
  public void setArgs(__InputValueExpression args) {
    this.args = (__InputValueExpression)args;
  }

  @Override
  public BooleanExpression getIsRepeatable() {
    return this.isRepeatable;
  }

  @Override
  public void setIsRepeatable(BooleanExpression isRepeatable) {
    this.isRepeatable = (BooleanExpression)isRepeatable;
  }

  @Override
  public Boolean getIncludeDeprecated() {
    return this.includeDeprecated;
  }

  @Override
  public void setIncludeDeprecated(Boolean includeDeprecated) {
    this.includeDeprecated = (Boolean)includeDeprecated;
  }

  @Override
  public IntExpression getVersion() {
    return this.version;
  }

  @Override
  public void setVersion(IntExpression version) {
    this.version = (IntExpression)version;
  }

  @Override
  public IntExpression getRealmId() {
    return this.realmId;
  }

  @Override
  public void setRealmId(IntExpression realmId) {
    this.realmId = (IntExpression)realmId;
  }

  @Override
  public StringExpression getCreateUserId() {
    return this.createUserId;
  }

  @Override
  public void setCreateUserId(StringExpression createUserId) {
    this.createUserId = (StringExpression)createUserId;
  }

  @Override
  public StringExpression getCreateTime() {
    return this.createTime;
  }

  @Override
  public void setCreateTime(StringExpression createTime) {
    this.createTime = (StringExpression)createTime;
  }

  @Override
  public StringExpression getUpdateUserId() {
    return this.updateUserId;
  }

  @Override
  public void setUpdateUserId(StringExpression updateUserId) {
    this.updateUserId = (StringExpression)updateUserId;
  }

  @Override
  public StringExpression getUpdateTime() {
    return this.updateTime;
  }

  @Override
  public void setUpdateTime(StringExpression updateTime) {
    this.updateTime = (StringExpression)updateTime;
  }

  @Override
  public StringExpression getCreateGroupId() {
    return this.createGroupId;
  }

  @Override
  public void setCreateGroupId(StringExpression createGroupId) {
    this.createGroupId = (StringExpression)createGroupId;
  }

  @Override
  public StringExpression get__typename() {
    return this.__typename;
  }

  @Override
  public void set__typename(StringExpression __typename) {
    this.__typename = (StringExpression)__typename;
  }

  @Override
  public IntExpression getSchemaId() {
    return this.schemaId;
  }

  @Override
  public void setSchemaId(IntExpression schemaId) {
    this.schemaId = (IntExpression)schemaId;
  }

  @Override
  public __DirectiveLocationsRelationExpression get__directiveLocationsRelation() {
    return this.__directiveLocationsRelation;
  }

  @Override
  public void set__directiveLocationsRelation(
      __DirectiveLocationsRelationExpression __directiveLocationsRelation) {
    this.__directiveLocationsRelation = (__DirectiveLocationsRelationExpression)__directiveLocationsRelation;
  }

  @Override
  public Boolean getNot() {
    return this.not;
  }

  @Override
  public void setNot(Boolean not) {
    this.not = (Boolean)not;
  }

  @Override
  public Conditional getCond() {
    return this.cond;
  }

  @Override
  public void setCond(Conditional cond) {
    this.cond = (Conditional)cond;
  }

  @Override
  public Collection<__DirectiveExpression> getExs() {
    return this.exs;
  }

  @Override
  public void setExs(Collection<__DirectiveExpression> exs) {
    this.exs = (Collection<__DirectiveExpression>)exs;
  }
}
