package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.annotation.BooleanExpression;
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
 * Subscription Arguments for __Directive List
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Subscription Arguments for __Directive List")
public @interface __DirectiveListSubscriptionArguments {
  /**
   * name
   */
  @Description("name")
  StringExpression name() default @StringExpression;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaExpression ofSchema() default @__SchemaExpression;

  /**
   * description
   */
  @Description("description")
  StringExpression description() default @StringExpression;

  /**
   * locations
   */
  @Description("locations")
  __DirectiveLocationExpression locations() default @__DirectiveLocationExpression;

  /**
   * args
   */
  @Description("args")
  __InputValueExpression args() default @__InputValueExpression;

  /**
   * isRepeatable
   */
  @Description("isRepeatable")
  BooleanExpression isRepeatable() default @BooleanExpression;

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
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  IntExpression schemaId() default @IntExpression;

  /**
   * Relationship Object between __Directive and locations
   */
  @Description("Relationship Object between __Directive and locations")
  __DirectiveLocationsRelationExpression __directiveLocationsRelation(
      ) default @__DirectiveLocationsRelationExpression;

  /**
   * Order By
   */
  @Description("Order By")
  __DirectiveOrderBy orderBy() default @__DirectiveOrderBy;

  /**
   * Group By
   */
  @Description("Group By")
  String[] groupBy() default {};

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
  __DirectiveExpression[] exs() default {};

  /**
   * First
   */
  @Description("First")
  int first() default 0;

  /**
   * Input List
   */
  @Description("Input List")
  int last() default 0;

  /**
   * Offset
   */
  @Description("Offset")
  int offset() default 0;

  /**
   * After
   */
  @Description("After")
  String after() default "";

  /**
   * Before
   */
  @Description("Before")
  String before() default "";

  String $name() default "";

  String $ofSchema() default "";

  String $description() default "";

  String $locations() default "";

  String $args() default "";

  String $isRepeatable() default "";

  String $includeDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $schemaId() default "";

  String $__directiveLocationsRelation() default "";

  String $orderBy() default "";

  String $groupBy() default "";

  String $not() default "";

  String $cond() default "";

  String $exs() default "";

  String $first() default "";

  String $last() default "";

  String $offset() default "";

  String $after() default "";

  String $before() default "";
}
