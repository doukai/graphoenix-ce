package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Order Input for Relationship Object between __Directive and locations
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Order Input for Relationship Object between __Directive and locations")
public @interface __DirectiveLocationsRelationOrderBy2 {
  /**
   * ID
   */
  @Description("ID")
  Sort id() default Sort.ASC;

  /**
   * __Directive Reference
   */
  @Description("__Directive Reference")
  Sort __directiveRef() default Sort.ASC;

  /**
   * locations Reference
   */
  @Description("locations Reference")
  Sort locationsRef() default Sort.ASC;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  Sort isDeprecated() default Sort.ASC;

  /**
   * Version
   */
  @Description("Version")
  Sort version() default Sort.ASC;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  Sort realmId() default Sort.ASC;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  Sort createUserId() default Sort.ASC;

  /**
   * Create Time
   */
  @Description("Create Time")
  Sort createTime() default Sort.ASC;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  Sort updateUserId() default Sort.ASC;

  /**
   * Update Time
   */
  @Description("Update Time")
  Sort updateTime() default Sort.ASC;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  Sort createGroupId() default Sort.ASC;

  /**
   * Type Name
   */
  @Description("Type Name")
  Sort __typename() default Sort.ASC;

  /**
   * Count of Relationship Object between __Directive and locations
   */
  @Description("Count of Relationship Object between __Directive and locations")
  Sort idCount() default Sort.ASC;

  /**
   * Max of ID
   */
  @Description("Max of ID")
  Sort idMax() default Sort.ASC;

  /**
   * Min of ID
   */
  @Description("Min of ID")
  Sort idMin() default Sort.ASC;

  /**
   * Count of __Directive Reference
   */
  @Description("Count of __Directive Reference")
  Sort __directiveRefCount() default Sort.ASC;

  /**
   * Max of __Directive Reference
   */
  @Description("Max of __Directive Reference")
  Sort __directiveRefMax() default Sort.ASC;

  /**
   * Min of __Directive Reference
   */
  @Description("Min of __Directive Reference")
  Sort __directiveRefMin() default Sort.ASC;

  /**
   * Count of locations Reference
   */
  @Description("Count of locations Reference")
  Sort locationsRefCount() default Sort.ASC;

  /**
   * Max of locations Reference
   */
  @Description("Max of locations Reference")
  Sort locationsRefMax() default Sort.ASC;

  /**
   * Min of locations Reference
   */
  @Description("Min of locations Reference")
  Sort locationsRefMin() default Sort.ASC;

  String $id() default "";

  String $__directiveRef() default "";

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

  String $idCount() default "";

  String $idMax() default "";

  String $idMin() default "";

  String $__directiveRefCount() default "";

  String $__directiveRefMax() default "";

  String $__directiveRefMin() default "";

  String $locationsRefCount() default "";

  String $locationsRefMax() default "";

  String $locationsRefMin() default "";
}
