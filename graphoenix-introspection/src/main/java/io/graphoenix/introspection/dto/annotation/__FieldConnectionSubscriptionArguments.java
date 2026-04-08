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
 * Subscription Arguments for __Field List
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Subscription Arguments for __Field List")
public @interface __FieldConnectionSubscriptionArguments {
  /**
   * id
   */
  @Description("id")
  StringExpression id() default @StringExpression;

  /**
   * name
   */
  @Description("name")
  StringExpression name() default @StringExpression;

  /**
   * ofType
   */
  @Description("ofType")
  __TypeExpression ofType() default @__TypeExpression;

  /**
   * description
   */
  @Description("description")
  StringExpression description() default @StringExpression;

  /**
   * args
   */
  @Description("args")
  __InputValueExpression args() default @__InputValueExpression;

  /**
   * type
   */
  @Description("type")
  __TypeExpression type() default @__TypeExpression;

  /**
   * deprecationReason
   */
  @Description("deprecationReason")
  StringExpression deprecationReason() default @StringExpression;

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
   * ofType Reference
   */
  @Description("ofType Reference")
  StringExpression ofTypeName() default @StringExpression;

  /**
   * type Reference
   */
  @Description("type Reference")
  StringExpression typeName() default @StringExpression;

  /**
   * Year of Create Time
   */
  @Description("Year of Create Time")
  IntExpression createTimeYear() default @IntExpression;

  /**
   * Month of Create Time
   */
  @Description("Month of Create Time")
  IntExpression createTimeMonth() default @IntExpression;

  /**
   * Day of Create Time
   */
  @Description("Day of Create Time")
  IntExpression createTimeDay() default @IntExpression;

  /**
   * Week of Create Time
   */
  @Description("Week of Create Time")
  IntExpression createTimeWeek() default @IntExpression;

  /**
   * Quarter of Create Time
   */
  @Description("Quarter of Create Time")
  IntExpression createTimeQuarter() default @IntExpression;

  /**
   * Year of Update Time
   */
  @Description("Year of Update Time")
  IntExpression updateTimeYear() default @IntExpression;

  /**
   * Month of Update Time
   */
  @Description("Month of Update Time")
  IntExpression updateTimeMonth() default @IntExpression;

  /**
   * Day of Update Time
   */
  @Description("Day of Update Time")
  IntExpression updateTimeDay() default @IntExpression;

  /**
   * Week of Update Time
   */
  @Description("Week of Update Time")
  IntExpression updateTimeWeek() default @IntExpression;

  /**
   * Quarter of Update Time
   */
  @Description("Quarter of Update Time")
  IntExpression updateTimeQuarter() default @IntExpression;

  /**
   * Order By
   */
  @Description("Order By")
  __FieldOrderBy orderBy() default @__FieldOrderBy;

  /**
   * Order By
   */
  @Description("Order By")
  __FieldGroupBy groupBy() default @__FieldGroupBy;

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
  __FieldExpression[] exs() default {};

  /**
   * First
   */
  @Description("First")
  int first() default 0;

  /**
   * Last
   */
  @Description("Last")
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

  String $id() default "";

  String $name() default "";

  String $ofType() default "";

  String $description() default "";

  String $args() default "";

  String $type() default "";

  String $deprecationReason() default "";

  String $includeDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $ofTypeName() default "";

  String $typeName() default "";

  String $createTimeYear() default "";

  String $createTimeMonth() default "";

  String $createTimeDay() default "";

  String $createTimeWeek() default "";

  String $createTimeQuarter() default "";

  String $updateTimeYear() default "";

  String $updateTimeMonth() default "";

  String $updateTimeDay() default "";

  String $updateTimeWeek() default "";

  String $updateTimeQuarter() default "";

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
