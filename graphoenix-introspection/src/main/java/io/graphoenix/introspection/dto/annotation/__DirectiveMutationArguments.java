package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.enumType.__DirectiveLocation;
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
public @interface __DirectiveMutationArguments {
  String name() default "";

  __SchemaInput ofSchema() default @__SchemaInput;

  String description() default "";

  __DirectiveLocation[] locations() default {};

  __InputValueInput[] args() default {};

  boolean isRepeatable() default false;

  boolean isDeprecated() default false;

  int version() default 0;

  int realmId() default 0;

  String createUserId() default "";

  String createTime() default "";

  String updateUserId() default "";

  String updateTime() default "";

  String createGroupId() default "";

  String __typename() default "__Directive";

  int schemaId() default 0;

  __DirectiveLocationsRelationInput[] __directiveLocationsRelation() default {};

  __DirectiveInput input() default @__DirectiveInput;

  __DirectiveExpression where() default @__DirectiveExpression;

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

  String $input() default "";

  String $where() default "";
}
