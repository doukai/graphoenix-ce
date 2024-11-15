package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Subscription Arguments for __Schema List
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Subscription Arguments for __Schema List")
public class __SchemaConnectionSubscriptionArguments implements MetaExpression {
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
   * Order By
   */
  @Description("Order By")
  private __SchemaOrderBy orderBy;

  /**
   * Order By
   */
  @Description("Order By")
  private Collection<String> groupBy;

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

  /**
   * First
   */
  @Description("First")
  private Integer first;

  /**
   * Last
   */
  @Description("Last")
  private Integer last;

  /**
   * Offset
   */
  @Description("Offset")
  private Integer offset;

  /**
   * After
   */
  @Description("After")
  private String after;

  /**
   * Before
   */
  @Description("Before")
  private String before;

  public StringExpression getId() {
    return this.id;
  }

  public void setId(StringExpression id) {
    this.id = id;
  }

  public __TypeExpression getTypes() {
    return this.types;
  }

  public void setTypes(__TypeExpression types) {
    this.types = types;
  }

  public __TypeExpression getQueryType() {
    return this.queryType;
  }

  public void setQueryType(__TypeExpression queryType) {
    this.queryType = queryType;
  }

  public __TypeExpression getMutationType() {
    return this.mutationType;
  }

  public void setMutationType(__TypeExpression mutationType) {
    this.mutationType = mutationType;
  }

  public __TypeExpression getSubscriptionType() {
    return this.subscriptionType;
  }

  public void setSubscriptionType(__TypeExpression subscriptionType) {
    this.subscriptionType = subscriptionType;
  }

  public __DirectiveExpression getDirectives() {
    return this.directives;
  }

  public void setDirectives(__DirectiveExpression directives) {
    this.directives = directives;
  }

  public Boolean getIncludeDeprecated() {
    return this.includeDeprecated;
  }

  public void setIncludeDeprecated(Boolean includeDeprecated) {
    this.includeDeprecated = includeDeprecated;
  }

  public IntExpression getVersion() {
    return this.version;
  }

  public void setVersion(IntExpression version) {
    this.version = version;
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

  public StringExpression get__typename() {
    return this.__typename;
  }

  public void set__typename(StringExpression __typename) {
    this.__typename = __typename;
  }

  public StringExpression getQueryTypeName() {
    return this.queryTypeName;
  }

  public void setQueryTypeName(StringExpression queryTypeName) {
    this.queryTypeName = queryTypeName;
  }

  public StringExpression getMutationTypeName() {
    return this.mutationTypeName;
  }

  public void setMutationTypeName(StringExpression mutationTypeName) {
    this.mutationTypeName = mutationTypeName;
  }

  public StringExpression getSubscriptionTypeName() {
    return this.subscriptionTypeName;
  }

  public void setSubscriptionTypeName(StringExpression subscriptionTypeName) {
    this.subscriptionTypeName = subscriptionTypeName;
  }

  public __SchemaOrderBy getOrderBy() {
    return this.orderBy;
  }

  public void setOrderBy(__SchemaOrderBy orderBy) {
    this.orderBy = orderBy;
  }

  public Collection<String> getGroupBy() {
    return this.groupBy;
  }

  public void setGroupBy(Collection<String> groupBy) {
    this.groupBy = groupBy;
  }

  public Boolean getNot() {
    return this.not;
  }

  public void setNot(Boolean not) {
    this.not = not;
  }

  public Conditional getCond() {
    return this.cond;
  }

  public void setCond(Conditional cond) {
    this.cond = cond;
  }

  public Collection<__SchemaExpression> getExs() {
    return this.exs;
  }

  public void setExs(Collection<__SchemaExpression> exs) {
    this.exs = exs;
  }

  public Integer getFirst() {
    return this.first;
  }

  public void setFirst(Integer first) {
    this.first = first;
  }

  public Integer getLast() {
    return this.last;
  }

  public void setLast(Integer last) {
    this.last = last;
  }

  public Integer getOffset() {
    return this.offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public String getAfter() {
    return this.after;
  }

  public void setAfter(String after) {
    this.after = after;
  }

  public String getBefore() {
    return this.before;
  }

  public void setBefore(String before) {
    this.before = before;
  }
}
