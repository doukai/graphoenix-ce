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
 * Order Input for __Field
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Order Input for __Field")
public @interface __FieldOrderBy1 {
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
   * ofType
   */
  @Description("ofType")
  __TypeOrderBy2 ofType() default @__TypeOrderBy2;

  /**
   * description
   */
  @Description("description")
  Sort description() default Sort.ASC;

  /**
   * args
   */
  @Description("args")
  __InputValueOrderBy2 args() default @__InputValueOrderBy2;

  /**
   * type
   */
  @Description("type")
  __TypeOrderBy2 type() default @__TypeOrderBy2;

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
   * type Reference
   */
  @Description("type Reference")
  Sort typeName() default Sort.ASC;

  /**
   * Aggregate Field for args
   */
  @Description("Aggregate Field for args")
  __InputValueOrderBy2 argsAggregate() default @__InputValueOrderBy2;

  /**
   * Count of __Field
   */
  @Description("Count of __Field")
  Sort idCount() default Sort.ASC;

  /**
   * Count of name
   */
  @Description("Count of name")
  Sort nameCount() default Sort.ASC;

  /**
   * Count of description
   */
  @Description("Count of description")
  Sort descriptionCount() default Sort.ASC;

  /**
   * Count of deprecationReason
   */
  @Description("Count of deprecationReason")
  Sort deprecationReasonCount() default Sort.ASC;

  /**
   * Count of ofType Reference
   */
  @Description("Count of ofType Reference")
  Sort ofTypeNameCount() default Sort.ASC;

  /**
   * Count of type Reference
   */
  @Description("Count of type Reference")
  Sort typeNameCount() default Sort.ASC;

  /**
   * Year of Create Time
   */
  @Description("Year of Create Time")
  Sort createTimeYear() default Sort.ASC;

  /**
   * Month of Create Time
   */
  @Description("Month of Create Time")
  Sort createTimeMonth() default Sort.ASC;

  /**
   * Day of Create Time
   */
  @Description("Day of Create Time")
  Sort createTimeDay() default Sort.ASC;

  /**
   * Week of Create Time
   */
  @Description("Week of Create Time")
  Sort createTimeWeek() default Sort.ASC;

  /**
   * Quarter of Create Time
   */
  @Description("Quarter of Create Time")
  Sort createTimeQuarter() default Sort.ASC;

  /**
   * Year of Update Time
   */
  @Description("Year of Update Time")
  Sort updateTimeYear() default Sort.ASC;

  /**
   * Month of Update Time
   */
  @Description("Month of Update Time")
  Sort updateTimeMonth() default Sort.ASC;

  /**
   * Day of Update Time
   */
  @Description("Day of Update Time")
  Sort updateTimeDay() default Sort.ASC;

  /**
   * Week of Update Time
   */
  @Description("Week of Update Time")
  Sort updateTimeWeek() default Sort.ASC;

  /**
   * Quarter of Update Time
   */
  @Description("Quarter of Update Time")
  Sort updateTimeQuarter() default Sort.ASC;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __FieldOrderBy2[] obs() default {};

  String $id() default "";

  String $name() default "";

  String $ofType() default "";

  String $description() default "";

  String $args() default "";

  String $type() default "";

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

  String $typeName() default "";

  String $argsAggregate() default "";

  String $idCount() default "";

  String $nameCount() default "";

  String $descriptionCount() default "";

  String $deprecationReasonCount() default "";

  String $ofTypeNameCount() default "";

  String $typeNameCount() default "";

  String $createTimeYear() default "";

  String $createTimeMonth() default "";

  String $createTimeDay() default "";

  String $createTimeWeek() default "";

  String $createTimeQuarter() default "";

  String $updateTimeYear() default "";

  String $updateTimeMonth() default "";

  String $updateTimeDay() default "";

  String $updateTimeWeek() default "";

  String $updateTimeQuarter() default "";

  String $obs() default "";
}
