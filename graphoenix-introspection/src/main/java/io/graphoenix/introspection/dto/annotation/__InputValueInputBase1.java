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
 * Mutation Input for __InputValue
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation Input for __InputValue")
public @interface __InputValueInputBase1 {
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
  __TypeInput2 ofType() default @__TypeInput2;

  /**
   * fieldId
   */
  @Description("fieldId")
  int fieldId() default 0;

  /**
   * directiveName
   */
  @Description("directiveName")
  String directiveName() default "";

  /**
   * description
   */
  @Description("description")
  String description() default "";

  /**
   * type
   */
  @Description("type")
  __TypeInput2 type() default @__TypeInput2;

  /**
   * defaultValue
   */
  @Description("defaultValue")
  String defaultValue() default "";

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
  String __typename() default "__InputValue";

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  String ofTypeName() default "";

  /**
   * type Reference
   */
  @Description("type Reference")
  String typeName() default "";

  String $id() default "";

  String $name() default "";

  String $ofType() default "";

  String $fieldId() default "";

  String $directiveName() default "";

  String $description() default "";

  String $type() default "";

  String $defaultValue() default "";

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

  String $typeName() default "";
}
