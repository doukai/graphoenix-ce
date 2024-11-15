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
 * Order Input for __EnumValue
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Order Input for __EnumValue")
public @interface __EnumValueOrderBy2 {
  /**
   * id
   */
  @Description("id")
  Sort id() default Sort.ASC;

  /**
   * name
   */
  @Description("name")
  Sort name() default Sort.ASC;

  /**
   * description
   */
  @Description("description")
  Sort description() default Sort.ASC;

  /**
   * deprecationReason
   */
  @Description("deprecationReason")
  Sort deprecationReason() default Sort.ASC;

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
   * ofType Reference
   */
  @Description("ofType Reference")
  Sort ofTypeName() default Sort.ASC;

  /**
   * Count of __EnumValue
   */
  @Description("Count of __EnumValue")
  Sort idCount() default Sort.ASC;

  /**
   * Max of id
   */
  @Description("Max of id")
  Sort idMax() default Sort.ASC;

  /**
   * Min of id
   */
  @Description("Min of id")
  Sort idMin() default Sort.ASC;

  /**
   * Count of name
   */
  @Description("Count of name")
  Sort nameCount() default Sort.ASC;

  /**
   * Max of name
   */
  @Description("Max of name")
  Sort nameMax() default Sort.ASC;

  /**
   * Min of name
   */
  @Description("Min of name")
  Sort nameMin() default Sort.ASC;

  /**
   * Count of description
   */
  @Description("Count of description")
  Sort descriptionCount() default Sort.ASC;

  /**
   * Max of description
   */
  @Description("Max of description")
  Sort descriptionMax() default Sort.ASC;

  /**
   * Min of description
   */
  @Description("Min of description")
  Sort descriptionMin() default Sort.ASC;

  /**
   * Count of deprecationReason
   */
  @Description("Count of deprecationReason")
  Sort deprecationReasonCount() default Sort.ASC;

  /**
   * Max of deprecationReason
   */
  @Description("Max of deprecationReason")
  Sort deprecationReasonMax() default Sort.ASC;

  /**
   * Min of deprecationReason
   */
  @Description("Min of deprecationReason")
  Sort deprecationReasonMin() default Sort.ASC;

  /**
   * Count of ofType Reference
   */
  @Description("Count of ofType Reference")
  Sort ofTypeNameCount() default Sort.ASC;

  /**
   * Max of ofType Reference
   */
  @Description("Max of ofType Reference")
  Sort ofTypeNameMax() default Sort.ASC;

  /**
   * Min of ofType Reference
   */
  @Description("Min of ofType Reference")
  Sort ofTypeNameMin() default Sort.ASC;

  String $id() default "";

  String $name() default "";

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

  String $idCount() default "";

  String $idMax() default "";

  String $idMin() default "";

  String $nameCount() default "";

  String $nameMax() default "";

  String $nameMin() default "";

  String $descriptionCount() default "";

  String $descriptionMax() default "";

  String $descriptionMin() default "";

  String $deprecationReasonCount() default "";

  String $deprecationReasonMax() default "";

  String $deprecationReasonMin() default "";

  String $ofTypeNameCount() default "";

  String $ofTypeNameMax() default "";

  String $ofTypeNameMin() default "";
}
