package io.graphoenix.introspection.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Mutation Arguments for __EnumValue
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation Arguments for __EnumValue")
public @interface __EnumValueMutationArguments {
  /**
   * id
   */
  @Description("id")
  String id() default "";

  /**
   * name
   */
  @Description("name")
  String name() default "";

  /**
   * ofType
   */
  @Description("ofType")
  __TypeInput ofType() default @__TypeInput;

  /**
   * description
   */
  @Description("description")
  String description() default "";

  /**
   * deprecationReason
   */
  @Description("deprecationReason")
  String deprecationReason() default "";

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  boolean isDeprecated() default false;

  /**
   * Version
   */
  @Description("Version")
  int version() default 0;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  int realmId() default 0;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  String createUserId() default "";

  /**
   * Create Time
   */
  @Description("Create Time")
  String createTime() default "";

  /**
   * Update User ID
   */
  @Description("Update User ID")
  String updateUserId() default "";

  /**
   * Update Time
   */
  @Description("Update Time")
  String updateTime() default "";

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  String createGroupId() default "";

  /**
   * Type Name
   */
  @Description("Type Name")
  String __typename() default "__EnumValue";

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  String ofTypeName() default "";

  /**
   * Input
   */
  @Description("Input")
  __EnumValueInput input() default @__EnumValueInput;

  /**
   * Where
   */
  @Description("Where")
  __EnumValueExpression where() default @__EnumValueExpression;

  String $id() default "";

  String $name() default "";

  String $ofType() default "";

  String $description() default "";

  String $deprecationReason() default "";

  String $isDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $ofTypeName() default "";

  String $input() default "";

  String $where() default "";
}
