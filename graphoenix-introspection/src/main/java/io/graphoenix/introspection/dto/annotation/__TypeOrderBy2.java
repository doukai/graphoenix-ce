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
 * Order Input for __Type
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Order Input for __Type")
public @interface __TypeOrderBy2 {
  /**
   * name
   */
  @Description("name")
  Sort name() default Sort.ASC;

  /**
   * kind
   */
  @Description("kind")
  Sort kind() default Sort.ASC;

  /**
   * description
   */
  @Description("description")
  Sort description() default Sort.ASC;

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
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  Sort schemaId() default Sort.ASC;

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  Sort ofTypeName() default Sort.ASC;

  /**
   * Count of __Type
   */
  @Description("Count of __Type")
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
   * Count of kind
   */
  @Description("Count of kind")
  Sort kindCount() default Sort.ASC;

  /**
   * Max of kind
   */
  @Description("Max of kind")
  Sort kindMax() default Sort.ASC;

  /**
   * Min of kind
   */
  @Description("Min of kind")
  Sort kindMin() default Sort.ASC;

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

  /**
   * Count of ofSchema Reference
   */
  @Description("Count of ofSchema Reference")
  Sort schemaIdCount() default Sort.ASC;

  /**
   * Sum of ofSchema Reference
   */
  @Description("Sum of ofSchema Reference")
  Sort schemaIdSum() default Sort.ASC;

  /**
   * Avg of ofSchema Reference
   */
  @Description("Avg of ofSchema Reference")
  Sort schemaIdAvg() default Sort.ASC;

  /**
   * Max of ofSchema Reference
   */
  @Description("Max of ofSchema Reference")
  Sort schemaIdMax() default Sort.ASC;

  /**
   * Min of ofSchema Reference
   */
  @Description("Min of ofSchema Reference")
  Sort schemaIdMin() default Sort.ASC;

  String $name() default "";

  String $kind() default "";

  String $description() default "";

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

  String $ofTypeName() default "";

  String $nameCount() default "";

  String $nameMax() default "";

  String $nameMin() default "";

  String $kindCount() default "";

  String $kindMax() default "";

  String $kindMin() default "";

  String $descriptionCount() default "";

  String $descriptionMax() default "";

  String $descriptionMin() default "";

  String $ofTypeNameCount() default "";

  String $ofTypeNameMax() default "";

  String $ofTypeNameMin() default "";

  String $schemaIdCount() default "";

  String $schemaIdSum() default "";

  String $schemaIdAvg() default "";

  String $schemaIdMax() default "";

  String $schemaIdMin() default "";
}
