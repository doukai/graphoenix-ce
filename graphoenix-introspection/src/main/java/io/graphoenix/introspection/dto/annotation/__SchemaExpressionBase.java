package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.annotation.IntExpression;
import io.graphoenix.core.dto.annotation.StringExpression;
import io.graphoenix.core.dto.enumType.Conditional;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Query Expression Input for __Schema
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query Expression Input for __Schema")
public @interface __SchemaExpressionBase {
  /**
   * id
   */
  @Description("id")
  StringExpression id() default @StringExpression;

  /**
   * types
   */
  @Description("types")
  __TypeExpression1 types() default @__TypeExpression1;

  /**
   * queryType
   */
  @Description("queryType")
  __TypeExpression1 queryType() default @__TypeExpression1;

  /**
   * mutationType
   */
  @Description("mutationType")
  __TypeExpression1 mutationType() default @__TypeExpression1;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  __TypeExpression1 subscriptionType() default @__TypeExpression1;

  /**
   * directives
   */
  @Description("directives")
  __DirectiveExpression1 directives() default @__DirectiveExpression1;

  /**
   * Include Deprecated
   */
  @Description("Include Deprecated")
  boolean includeDeprecated() default false;

  /**
   * Version
   */
  @Description("Version")
  IntExpression version() default @IntExpression;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  IntExpression realmId() default @IntExpression;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  StringExpression createUserId() default @StringExpression;

  /**
   * Create Time
   */
  @Description("Create Time")
  StringExpression createTime() default @StringExpression;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  StringExpression updateUserId() default @StringExpression;

  /**
   * Update Time
   */
  @Description("Update Time")
  StringExpression updateTime() default @StringExpression;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  StringExpression createGroupId() default @StringExpression;

  /**
   * Type Name
   */
  @Description("Type Name")
  StringExpression __typename() default @StringExpression;

  /**
   * queryType Reference
   */
  @Description("queryType Reference")
  StringExpression queryTypeName() default @StringExpression;

  /**
   * mutationType Reference
   */
  @Description("mutationType Reference")
  StringExpression mutationTypeName() default @StringExpression;

  /**
   * subscriptionType Reference
   */
  @Description("subscriptionType Reference")
  StringExpression subscriptionTypeName() default @StringExpression;

  /**
   * Not
   */
  @Description("Not")
  boolean not() default false;

  /**
   * Condition
   */
  @Description("Condition")
  Conditional cond() default Conditional.AND;

  /**
   * Expressions
   */
  @Description("Expressions")
  __SchemaExpression1[] exs() default {};

  String $id() default "";

  String $types() default "";

  String $queryType() default "";

  String $mutationType() default "";

  String $subscriptionType() default "";

  String $directives() default "";

  String $includeDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $queryTypeName() default "";

  String $mutationTypeName() default "";

  String $subscriptionTypeName() default "";

  String $not() default "";

  String $cond() default "";

  String $exs() default "";
}
