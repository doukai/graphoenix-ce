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
 * Order Input for __Schema
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Order Input for __Schema")
public @interface __SchemaOrderBy {
  /**
   * id
   */
  @Description("id")
  Sort id() default Sort.ASC;

  /**
   * types
   */
  @Description("types")
  __TypeOrderBy1 types() default @__TypeOrderBy1;

  /**
   * queryType
   */
  @Description("queryType")
  __TypeOrderBy1 queryType() default @__TypeOrderBy1;

  /**
   * mutationType
   */
  @Description("mutationType")
  __TypeOrderBy1 mutationType() default @__TypeOrderBy1;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  __TypeOrderBy1 subscriptionType() default @__TypeOrderBy1;

  /**
   * directives
   */
  @Description("directives")
  __DirectiveOrderBy1 directives() default @__DirectiveOrderBy1;

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
   * queryType Reference
   */
  @Description("queryType Reference")
  Sort queryTypeName() default Sort.ASC;

  /**
   * mutationType Reference
   */
  @Description("mutationType Reference")
  Sort mutationTypeName() default Sort.ASC;

  /**
   * subscriptionType Reference
   */
  @Description("subscriptionType Reference")
  Sort subscriptionTypeName() default Sort.ASC;

  /**
   * Aggregate Field for types
   */
  @Description("Aggregate Field for types")
  __TypeOrderBy1 typesAggregate() default @__TypeOrderBy1;

  /**
   * Aggregate Field for directives
   */
  @Description("Aggregate Field for directives")
  __DirectiveOrderBy1 directivesAggregate() default @__DirectiveOrderBy1;

  /**
   * Count of __Schema
   */
  @Description("Count of __Schema")
  Sort idCount() default Sort.ASC;

  /**
   * Count of queryType Reference
   */
  @Description("Count of queryType Reference")
  Sort queryTypeNameCount() default Sort.ASC;

  /**
   * Count of mutationType Reference
   */
  @Description("Count of mutationType Reference")
  Sort mutationTypeNameCount() default Sort.ASC;

  /**
   * Count of subscriptionType Reference
   */
  @Description("Count of subscriptionType Reference")
  Sort subscriptionTypeNameCount() default Sort.ASC;

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
  __SchemaOrderBy1[] obs() default {};

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

  String $typesAggregate() default "";

  String $directivesAggregate() default "";

  String $idCount() default "";

  String $queryTypeNameCount() default "";

  String $mutationTypeNameCount() default "";

  String $subscriptionTypeNameCount() default "";

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
