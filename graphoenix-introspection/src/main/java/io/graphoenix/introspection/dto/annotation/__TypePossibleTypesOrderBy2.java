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
public @interface __TypePossibleTypesOrderBy2 {
  Sort id() default Sort.ASC;

  Sort typeRef() default Sort.ASC;

  Sort possibleTypeRef() default Sort.ASC;

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

  Sort typeRefCount() default Sort.ASC;

  Sort typeRefMax() default Sort.ASC;

  Sort typeRefMin() default Sort.ASC;

  Sort possibleTypeRefCount() default Sort.ASC;

  Sort possibleTypeRefMax() default Sort.ASC;

  Sort possibleTypeRefMin() default Sort.ASC;

  String $id() default "";

  String $typeRef() default "";

  String $possibleTypeRef() default "";

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

  String $typeRefCount() default "";

  String $typeRefMax() default "";

  String $typeRefMin() default "";

  String $possibleTypeRefCount() default "";

  String $possibleTypeRefMax() default "";

  String $possibleTypeRefMin() default "";
}
