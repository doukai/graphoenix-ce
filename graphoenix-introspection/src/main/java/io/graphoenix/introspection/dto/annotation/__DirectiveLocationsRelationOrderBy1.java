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
public @interface __DirectiveLocationsRelationOrderBy1 {
  Sort id() default Sort.ASC;

  Sort __directiveRef() default Sort.ASC;

  __DirectiveOrderBy2 __directive() default @__DirectiveOrderBy2;

  Sort locationsRef() default Sort.ASC;

  Sort isDeprecated() default Sort.ASC;

  Sort version() default Sort.ASC;

  Sort realmId() default Sort.ASC;

  Sort createUserId() default Sort.ASC;

  Sort createTime() default Sort.ASC;

  Sort updateUserId() default Sort.ASC;

  Sort updateTime() default Sort.ASC;

  Sort createGroupId() default Sort.ASC;

  Sort __typename() default Sort.ASC;

  Sort idCount() default Sort.ASC;

  Sort idMax() default Sort.ASC;

  Sort idMin() default Sort.ASC;

  Sort __directiveRefCount() default Sort.ASC;

  Sort __directiveRefMax() default Sort.ASC;

  Sort __directiveRefMin() default Sort.ASC;

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

  String $idCount() default "";

  String $idMax() default "";

  String $idMin() default "";

  String $__directiveRefCount() default "";

  String $__directiveRefMax() default "";

  String $__directiveRefMin() default "";
}