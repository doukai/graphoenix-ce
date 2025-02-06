package io.graphoenix.structure.dto.annotation;

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
 * Query Expression Input for TreeStruct
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query Expression Input for TreeStruct")
public @interface TreeStructExpressionBase1 {
  /**
   * name
   */
  @Description("name")
  StringExpression name() default @StringExpression;

  /**
   * path
   */
  @Description("path")
  StringExpression path() default @StringExpression;

  /**
   * deep
   */
  @Description("deep")
  IntExpression deep() default @IntExpression;

  /**
   * parentId
   */
  @Description("parentId")
  StringExpression parentId() default @StringExpression;

  /**
   * Include Deprecated
   */
  @Description("Include Deprecated")
  boolean includeDeprecated() default false;

  /**
   * version
   */
  @Description("version")
  IntExpression version() default @IntExpression;

  /**
   * realmId
   */
  @Description("realmId")
  IntExpression realmId() default @IntExpression;

  /**
   * createUserId
   */
  @Description("createUserId")
  StringExpression createUserId() default @StringExpression;

  /**
   * createTime
   */
  @Description("createTime")
  StringExpression createTime() default @StringExpression;

  /**
   * updateUserId
   */
  @Description("updateUserId")
  StringExpression updateUserId() default @StringExpression;

  /**
   * updateTime
   */
  @Description("updateTime")
  StringExpression updateTime() default @StringExpression;

  /**
   * createGroupId
   */
  @Description("createGroupId")
  StringExpression createGroupId() default @StringExpression;

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

  String $name() default "";

  String $path() default "";

  String $deep() default "";

  String $parentId() default "";

  String $includeDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $not() default "";

  String $cond() default "";
}
