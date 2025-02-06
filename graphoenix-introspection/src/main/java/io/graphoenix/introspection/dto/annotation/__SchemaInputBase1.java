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
 * Mutation Input for __Schema
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation Input for __Schema")
public @interface __SchemaInputBase1 {
  /**
   * id
   */
  @Description("id")
  String id() default "";

  /**
   * types
   */
  @Description("types")
  __TypeInput2[] types() default {};

  /**
   * queryType
   */
  @Description("queryType")
  __TypeInput2 queryType() default @__TypeInput2;

  /**
   * mutationType
   */
  @Description("mutationType")
  __TypeInput2 mutationType() default @__TypeInput2;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  __TypeInput2 subscriptionType() default @__TypeInput2;

  /**
   * directives
   */
  @Description("directives")
  __DirectiveInput2[] directives() default {};

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
  String __typename() default "__Schema";

  /**
   * queryType Reference
   */
  @Description("queryType Reference")
  String queryTypeName() default "";

  /**
   * mutationType Reference
   */
  @Description("mutationType Reference")
  String mutationTypeName() default "";

  /**
   * subscriptionType Reference
   */
  @Description("subscriptionType Reference")
  String subscriptionTypeName() default "";

  String $id() default "";

  String $types() default "";

  String $queryType() default "";

  String $mutationType() default "";

  String $subscriptionType() default "";

  String $directives() default "";

  String $isDeprecated() default "";

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
}
