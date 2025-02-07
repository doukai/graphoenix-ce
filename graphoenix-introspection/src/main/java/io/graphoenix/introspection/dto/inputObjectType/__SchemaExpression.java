package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Conditional;
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
 * Query Expression Input for __Schema
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for __Schema")
public class __SchemaExpression implements MetaExpression, __SchemaExpressionBase {
  /**
   * id
   */
  @Description("id")
  private StringExpression id;

  /**
   * types
   */
  @Description("types")
  private __TypeExpression types;

  /**
   * queryType
   */
  @Description("queryType")
  private __TypeExpression queryType;

  /**
   * mutationType
   */
  @Description("mutationType")
  private __TypeExpression mutationType;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  private __TypeExpression subscriptionType;

  /**
   * directives
   */
  @Description("directives")
  private __DirectiveExpression directives;

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
   * queryType Reference
   */
  @Description("queryType Reference")
  private StringExpression queryTypeName;

  /**
   * mutationType Reference
   */
  @Description("mutationType Reference")
  private StringExpression mutationTypeName;

  /**
   * subscriptionType Reference
   */
  @Description("subscriptionType Reference")
  private StringExpression subscriptionTypeName;

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
  private Collection<__SchemaExpression> exs;

  @Override
  public StringExpression getId() {
    return this.id;
  }

  @Override
  public void setId(StringExpression id) {
    this.id = (StringExpression)id;
  }

  @Override
  public __TypeExpression getTypes() {
    return this.types;
  }

  @Override
  public void setTypes(__TypeExpression types) {
    this.types = (__TypeExpression)types;
  }

  @Override
  public __TypeExpression getQueryType() {
    return this.queryType;
  }

  @Override
  public void setQueryType(__TypeExpression queryType) {
    this.queryType = (__TypeExpression)queryType;
  }

  @Override
  public __TypeExpression getMutationType() {
    return this.mutationType;
  }

  @Override
  public void setMutationType(__TypeExpression mutationType) {
    this.mutationType = (__TypeExpression)mutationType;
  }

  @Override
  public __TypeExpression getSubscriptionType() {
    return this.subscriptionType;
  }

  @Override
  public void setSubscriptionType(__TypeExpression subscriptionType) {
    this.subscriptionType = (__TypeExpression)subscriptionType;
  }

  @Override
  public __DirectiveExpression getDirectives() {
    return this.directives;
  }

  @Override
  public void setDirectives(__DirectiveExpression directives) {
    this.directives = (__DirectiveExpression)directives;
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
  public StringExpression getQueryTypeName() {
    return this.queryTypeName;
  }

  @Override
  public void setQueryTypeName(StringExpression queryTypeName) {
    this.queryTypeName = (StringExpression)queryTypeName;
  }

  @Override
  public StringExpression getMutationTypeName() {
    return this.mutationTypeName;
  }

  @Override
  public void setMutationTypeName(StringExpression mutationTypeName) {
    this.mutationTypeName = (StringExpression)mutationTypeName;
  }

  @Override
  public StringExpression getSubscriptionTypeName() {
    return this.subscriptionTypeName;
  }

  @Override
  public void setSubscriptionTypeName(StringExpression subscriptionTypeName) {
    this.subscriptionTypeName = (StringExpression)subscriptionTypeName;
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
  public Collection<__SchemaExpression> getExs() {
    return this.exs;
  }

  @Override
  public void setExs(Collection<__SchemaExpression> exs) {
    this.exs = (Collection<__SchemaExpression>)exs;
  }
}
