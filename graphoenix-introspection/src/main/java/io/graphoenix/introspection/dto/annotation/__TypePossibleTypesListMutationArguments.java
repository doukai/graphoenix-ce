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
 * Mutation Arguments for Relationship Object between __Type and __Type List
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation Arguments for Relationship Object between __Type and __Type List")
public @interface __TypePossibleTypesListMutationArguments {
  /**
   * ID
   */
  @Description("ID")
  String id() default "";

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  String typeRef() default "";

  /**
   * __Type
   */
  @Description("__Type")
  __TypeInput type() default @__TypeInput;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  String possibleTypeRef() default "";

  /**
   * __Type
   */
  @Description("__Type")
  __TypeInput possibleType() default @__TypeInput;

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
  String __typename() default "\"__TypePossibleTypes\"";

  /**
   * Input List
   */
  @Description("Input List")
  __TypePossibleTypesInput[] list() default {};

  /**
   * Where
   */
  @Description("Where")
  __TypePossibleTypesExpression where() default @__TypePossibleTypesExpression;

  String $id() default "";

  String $typeRef() default "";

  String $type() default "";

  String $possibleTypeRef() default "";

  String $possibleType() default "";

  String $isDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $list() default "";

  String $where() default "";
}
