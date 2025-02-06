package io.graphoenix.introspection.dto.inputObjectType;

import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Query Expression Input for __Schema
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for __Schema")
public interface __SchemaExpressionBase extends MetaExpression {
  /**
   * id
   */
  @Description("id")
  StringExpression id = null;

  /**
   * types
   */
  @Description("types")
  __TypeExpression types = null;

  /**
   * queryType
   */
  @Description("queryType")
  __TypeExpression queryType = null;

  /**
   * mutationType
   */
  @Description("mutationType")
  __TypeExpression mutationType = null;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  __TypeExpression subscriptionType = null;

  /**
   * directives
   */
  @Description("directives")
  __DirectiveExpression directives = null;

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
   * queryType Reference
   */
  @Description("queryType Reference")
  StringExpression queryTypeName = null;

  /**
   * mutationType Reference
   */
  @Description("mutationType Reference")
  StringExpression mutationTypeName = null;

  /**
   * subscriptionType Reference
   */
  @Description("subscriptionType Reference")
  StringExpression subscriptionTypeName = null;

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

  default StringExpression getId() {
    return id;
  }

  void setId(StringExpression id);

  default __TypeExpression getTypes() {
    return types;
  }

  void setTypes(__TypeExpression types);

  default __TypeExpression getQueryType() {
    return queryType;
  }

  void setQueryType(__TypeExpression queryType);

  default __TypeExpression getMutationType() {
    return mutationType;
  }

  void setMutationType(__TypeExpression mutationType);

  default __TypeExpression getSubscriptionType() {
    return subscriptionType;
  }

  void setSubscriptionType(__TypeExpression subscriptionType);

  default __DirectiveExpression getDirectives() {
    return directives;
  }

  void setDirectives(__DirectiveExpression directives);

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

  default StringExpression getQueryTypeName() {
    return queryTypeName;
  }

  void setQueryTypeName(StringExpression queryTypeName);

  default StringExpression getMutationTypeName() {
    return mutationTypeName;
  }

  void setMutationTypeName(StringExpression mutationTypeName);

  default StringExpression getSubscriptionTypeName() {
    return subscriptionTypeName;
  }

  void setSubscriptionTypeName(StringExpression subscriptionTypeName);

  default Boolean getNot() {
    return not;
  }

  void setNot(Boolean not);

  default Conditional getCond() {
    return cond;
  }

  void setCond(Conditional cond);
}
