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
 * Mutation Arguments for __Schema List
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation Arguments for __Schema List")
public @interface __SchemaListMutationArguments {
  /**
   * id
   */
  @Description("id")
  String id() default "";

  /**
   * types
   */
  @Description("types")
  __TypeInput[] types() default {};

  /**
   * queryType
   */
  @Description("queryType")
  __TypeInput queryType() default @__TypeInput;

  /**
   * mutationType
   */
  @Description("mutationType")
  __TypeInput mutationType() default @__TypeInput;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  __TypeInput subscriptionType() default @__TypeInput;

  /**
   * directives
   */
  @Description("directives")
  __DirectiveInput[] directives() default {};

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

  /**
   * Input List
   */
  @Description("Input List")
  __SchemaInput[] list() default {};

  /**
   * Where
   */
  @Description("Where")
  __SchemaExpression where() default @__SchemaExpression;

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

  String $list() default "";

  String $where() default "";
}
