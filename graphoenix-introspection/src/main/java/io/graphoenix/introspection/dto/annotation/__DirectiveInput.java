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
 * Mutation Input for __Directive
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation Input for __Directive")
public @interface __DirectiveInput {
  /**
   * name
   */
  @Description("name")
  String name() default "";

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaInput1 ofSchema() default @__SchemaInput1;

  /**
   * description
   */
  @Description("description")
  String description() default "";

  /**
   * locations
   */
  @Description("locations")
  __DirectiveLocation[] locations() default {};

  /**
   * args
   */
  @Description("args")
  __InputValueInput1[] args() default {};

  /**
   * isRepeatable
   */
  @Description("isRepeatable")
  boolean isRepeatable() default false;

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
  String __typename() default "__Directive";

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  int schemaId() default 0;

  /**
   * Relationship Object between __Directive and locations
   */
  @Description("Relationship Object between __Directive and locations")
  __DirectiveLocationsRelationInput1[] __directiveLocationsRelation() default {};

  /**
   * Where
   */
  @Description("Where")
  __DirectiveExpression1 where() default @__DirectiveExpression1;

  String $name() default "";

  String $ofSchema() default "";

  String $description() default "";

  String $locations() default "";

  String $args() default "";

  String $isRepeatable() default "";

  String $isDeprecated() default "";

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

  String $where() default "";
}
