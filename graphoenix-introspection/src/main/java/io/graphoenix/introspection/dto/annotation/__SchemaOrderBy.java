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
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
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
   * Count of queryType Reference
   */
  @Description("Count of queryType Reference")
  Sort queryTypeNameCount() default Sort.ASC;

  /**
   * Max of queryType Reference
   */
  @Description("Max of queryType Reference")
  Sort queryTypeNameMax() default Sort.ASC;

  /**
   * Min of queryType Reference
   */
  @Description("Min of queryType Reference")
  Sort queryTypeNameMin() default Sort.ASC;

  /**
   * Count of mutationType Reference
   */
  @Description("Count of mutationType Reference")
  Sort mutationTypeNameCount() default Sort.ASC;

  /**
   * Max of mutationType Reference
   */
  @Description("Max of mutationType Reference")
  Sort mutationTypeNameMax() default Sort.ASC;

  /**
   * Min of mutationType Reference
   */
  @Description("Min of mutationType Reference")
  Sort mutationTypeNameMin() default Sort.ASC;

  /**
   * Count of subscriptionType Reference
   */
  @Description("Count of subscriptionType Reference")
  Sort subscriptionTypeNameCount() default Sort.ASC;

  /**
   * Max of subscriptionType Reference
   */
  @Description("Max of subscriptionType Reference")
  Sort subscriptionTypeNameMax() default Sort.ASC;

  /**
   * Min of subscriptionType Reference
   */
  @Description("Min of subscriptionType Reference")
  Sort subscriptionTypeNameMin() default Sort.ASC;

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

  String $idMax() default "";

  String $idMin() default "";

  String $queryTypeNameCount() default "";

  String $queryTypeNameMax() default "";

  String $queryTypeNameMin() default "";

  String $mutationTypeNameCount() default "";

  String $mutationTypeNameMax() default "";

  String $mutationTypeNameMin() default "";

  String $subscriptionTypeNameCount() default "";

  String $subscriptionTypeNameMax() default "";

  String $subscriptionTypeNameMin() default "";
}
