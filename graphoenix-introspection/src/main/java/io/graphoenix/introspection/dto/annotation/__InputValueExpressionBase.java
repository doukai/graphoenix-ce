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
 * Query Expression Input for __InputValue
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query Expression Input for __InputValue")
public @interface __InputValueExpressionBase {
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
  __TypeExpression1 ofType() default @__TypeExpression1;

  /**
   * fieldId
   */
  @Description("fieldId")
  IntExpression fieldId() default @IntExpression;

  /**
   * directiveName
   */
  @Description("directiveName")
  StringExpression directiveName() default @StringExpression;

  /**
   * description
   */
  @Description("description")
  StringExpression description() default @StringExpression;

  /**
   * type
   */
  @Description("type")
  __TypeExpression1 type() default @__TypeExpression1;

  /**
   * defaultValue
   */
  @Description("defaultValue")
  StringExpression defaultValue() default @StringExpression;

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
   * Not
   */
  @Description("Not")
  boolean not() default false;

  /**
   * Condition
   */
  @Description("Condition")
  Conditional cond() default Conditional.AND;

  String $id() default "";

  String $name() default "";

  String $ofType() default "";

  String $fieldId() default "";

  String $directiveName() default "";

  String $description() default "";

  String $type() default "";

  String $defaultValue() default "";

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

  String $not() default "";

  String $cond() default "";
}
