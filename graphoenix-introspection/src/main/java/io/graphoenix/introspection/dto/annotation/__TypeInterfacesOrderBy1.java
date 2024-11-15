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
import org.eclipse.microprofile.graphql.Name;

/**
 * Order Input for Relationship Object between __Type and __Type
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Order Input for Relationship Object between __Type and __Type")
public @interface __TypeInterfacesOrderBy1 {
  /**
   * ID
   */
  @Description("ID")
  Sort id() default Sort.ASC;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  Sort typeRef() default Sort.ASC;

  /**
   * __Type
   */
  @Description("__Type")
  __TypeOrderBy2 type() default @__TypeOrderBy2;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  Sort interfaceRef() default Sort.ASC;

  /**
   * __Type
   */
  @Name("interface")
  @Description("__Type")
  __TypeOrderBy2 _interface() default @__TypeOrderBy2;

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
   * Count of Relationship Object between __Type and __Type
   */
  @Description("Count of Relationship Object between __Type and __Type")
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
   * Count of __Type Reference
   */
  @Description("Count of __Type Reference")
  Sort typeRefCount() default Sort.ASC;

  /**
   * Max of __Type Reference
   */
  @Description("Max of __Type Reference")
  Sort typeRefMax() default Sort.ASC;

  /**
   * Min of __Type Reference
   */
  @Description("Min of __Type Reference")
  Sort typeRefMin() default Sort.ASC;

  /**
   * Count of __Type Reference
   */
  @Description("Count of __Type Reference")
  Sort interfaceRefCount() default Sort.ASC;

  /**
   * Max of __Type Reference
   */
  @Description("Max of __Type Reference")
  Sort interfaceRefMax() default Sort.ASC;

  /**
   * Min of __Type Reference
   */
  @Description("Min of __Type Reference")
  Sort interfaceRefMin() default Sort.ASC;

  String $id() default "";

  String $typeRef() default "";

  String $type() default "";

  String $interfaceRef() default "";

  String $interface() default "";

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

  String $typeRefCount() default "";

  String $typeRefMax() default "";

  String $typeRefMin() default "";

  String $interfaceRefCount() default "";

  String $interfaceRefMax() default "";

  String $interfaceRefMin() default "";
}
