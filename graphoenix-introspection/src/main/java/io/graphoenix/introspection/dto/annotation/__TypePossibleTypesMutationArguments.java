package io.graphoenix.introspection.dto.annotation;

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
public @interface __TypePossibleTypesMutationArguments {
  String id() default "";

  String typeName() default "";

  __TypeInput1 typeNameType() default @__TypeInput1;

  String possibleTypeName() default "";

  __TypeInput1 possibleTypeNameType() default @__TypeInput1;

  boolean isDeprecated() default false;

  int version() default 0;

  int realmId() default 0;

  String createUserId() default "";

  String createTime() default "";

  String updateUserId() default "";

  String updateTime() default "";

  String createGroupId() default "";

  String __typename() default "__TypePossibleTypes";

  __TypePossibleTypesExpression1 where() default @__TypePossibleTypesExpression1;

  String $id() default "";

  String $typeName() default "";

  String $typeNameType() default "";

  String $possibleTypeName() default "";

  String $possibleTypeNameType() default "";

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