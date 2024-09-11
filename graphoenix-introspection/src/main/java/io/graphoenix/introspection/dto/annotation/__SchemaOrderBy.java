package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface __SchemaOrderBy {
  Sort id() default Sort.ASC;

  __TypeOrderBy1 types() default @__TypeOrderBy1;

  __TypeOrderBy1 queryType() default @__TypeOrderBy1;

  __TypeOrderBy1 mutationType() default @__TypeOrderBy1;

  __TypeOrderBy1 subscriptionType() default @__TypeOrderBy1;

  __DirectiveOrderBy1 directives() default @__DirectiveOrderBy1;

  Sort isDeprecated() default Sort.ASC;

  Sort version() default Sort.ASC;

  Sort realmId() default Sort.ASC;

  Sort createUserId() default Sort.ASC;

  Sort createTime() default Sort.ASC;

  Sort updateUserId() default Sort.ASC;

  Sort updateTime() default Sort.ASC;

  Sort createGroupId() default Sort.ASC;

  Sort __typename() default Sort.ASC;

  Sort queryTypeName() default Sort.ASC;

  Sort mutationTypeName() default Sort.ASC;

  Sort subscriptionTypeName() default Sort.ASC;

  __TypeOrderBy1 typesAggregate() default @__TypeOrderBy1;

  __DirectiveOrderBy1 directivesAggregate() default @__DirectiveOrderBy1;

  Sort idCount() default Sort.ASC;

  Sort idMax() default Sort.ASC;

  Sort idMin() default Sort.ASC;

  Sort queryTypeNameCount() default Sort.ASC;

  Sort queryTypeNameMax() default Sort.ASC;

  Sort queryTypeNameMin() default Sort.ASC;

  Sort mutationTypeNameCount() default Sort.ASC;

  Sort mutationTypeNameMax() default Sort.ASC;

  Sort mutationTypeNameMin() default Sort.ASC;

  Sort subscriptionTypeNameCount() default Sort.ASC;

  Sort subscriptionTypeNameMax() default Sort.ASC;

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
