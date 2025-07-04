package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.introspection.dto.enumType.__DirectiveLocation;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Mutation Input for Relationship Object between __Directive and locations
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation Input for Relationship Object between __Directive and locations")
public @interface __DirectiveLocationsRelationInput {
  /**
   * ID
   */
  @Description("ID")
  String id() default "";

  /**
   * __Directive Reference
   */
  @Description("__Directive Reference")
  String __directiveRef() default "";

  /**
   * __Directive
   */
  @Description("__Directive")
  __DirectiveInput1 __directive() default @__DirectiveInput1;

  /**
   * locations Reference
   */
  @Description("locations Reference")
  __DirectiveLocation locationsRef() default __DirectiveLocation.QUERY;

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
  String __typename() default "__DirectiveLocationsRelation";

  /**
   * Where
   */
  @Description("Where")
  __DirectiveLocationsRelationExpression1 where() default @__DirectiveLocationsRelationExpression1;

  String $id() default "";

  String $__directiveRef() default "";

  String $__directive() default "";

  String $locationsRef() default "";

  String $isDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $where() default "";
}
