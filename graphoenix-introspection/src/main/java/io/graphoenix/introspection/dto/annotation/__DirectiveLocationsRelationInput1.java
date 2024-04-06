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
public @interface __DirectiveLocationsRelationInput1 {
  String id() default "";

  String __directiveRef() default "";

  __DirectiveInput2 __directive() default @__DirectiveInput2;

  __DirectiveLocation locationsRef() default __DirectiveLocation.QUERY;

  boolean isDeprecated() default false;

  int version() default 0;

  int realmId() default 0;

  String createUserId() default "";

  String createTime() default "";

  String updateUserId() default "";

  String updateTime() default "";

  String createGroupId() default "";

  String __typename() default "__DirectiveLocationsRelation";

  __DirectiveLocationsRelationExpression2 where() default @__DirectiveLocationsRelationExpression2;

  String $id() default "";

  String $__directiveRef() default "";

  String $__directive() default "";

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

  String $where() default "";
}
