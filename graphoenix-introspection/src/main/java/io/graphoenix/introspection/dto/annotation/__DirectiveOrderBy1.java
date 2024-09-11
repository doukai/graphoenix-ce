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
public @interface __DirectiveOrderBy1 {
  Sort name() default Sort.ASC;

  __SchemaOrderBy2 ofSchema() default @__SchemaOrderBy2;

  Sort description() default Sort.ASC;

  Sort locations() default Sort.ASC;

  __InputValueOrderBy2 args() default @__InputValueOrderBy2;

  Sort isRepeatable() default Sort.ASC;

  Sort isDeprecated() default Sort.ASC;

  Sort version() default Sort.ASC;

  Sort realmId() default Sort.ASC;

  Sort createUserId() default Sort.ASC;

  Sort createTime() default Sort.ASC;

  Sort updateUserId() default Sort.ASC;

  Sort updateTime() default Sort.ASC;

  Sort createGroupId() default Sort.ASC;

  Sort __typename() default Sort.ASC;

  Sort schemaId() default Sort.ASC;

  __DirectiveLocationsRelationOrderBy2 __directiveLocationsRelation(
      ) default @__DirectiveLocationsRelationOrderBy2;

  __InputValueOrderBy2 argsAggregate() default @__InputValueOrderBy2;

  __DirectiveLocationsRelationOrderBy2 __directiveLocationsRelationAggregate(
      ) default @__DirectiveLocationsRelationOrderBy2;

  Sort nameCount() default Sort.ASC;

  Sort nameMax() default Sort.ASC;

  Sort nameMin() default Sort.ASC;

  Sort descriptionCount() default Sort.ASC;

  Sort descriptionMax() default Sort.ASC;

  Sort descriptionMin() default Sort.ASC;

  Sort schemaIdCount() default Sort.ASC;

  Sort schemaIdSum() default Sort.ASC;

  Sort schemaIdAvg() default Sort.ASC;

  Sort schemaIdMax() default Sort.ASC;

  Sort schemaIdMin() default Sort.ASC;

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

  String $argsAggregate() default "";

  String $__directiveLocationsRelationAggregate() default "";

  String $nameCount() default "";

  String $nameMax() default "";

  String $nameMin() default "";

  String $descriptionCount() default "";

  String $descriptionMax() default "";

  String $descriptionMin() default "";

  String $schemaIdCount() default "";

  String $schemaIdSum() default "";

  String $schemaIdAvg() default "";

  String $schemaIdMax() default "";

  String $schemaIdMin() default "";
}
