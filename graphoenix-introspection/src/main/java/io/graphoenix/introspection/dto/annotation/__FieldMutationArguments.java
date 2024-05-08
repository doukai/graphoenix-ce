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
public @interface __FieldMutationArguments {
  String id() default "";

  String name() default "";

  __TypeInput1 ofType() default @__TypeInput1;

  String description() default "";

  __InputValueInput1[] args() default {};

  __TypeInput1 type() default @__TypeInput1;

  String deprecationReason() default "";

  boolean isDeprecated() default false;

  int version() default 0;

  int realmId() default 0;

  String createUserId() default "";

  String createTime() default "";

  String updateUserId() default "";

  String updateTime() default "";

  String createGroupId() default "";

  String __typename() default "__Field";

  String ofTypeName() default "";

  String typeName() default "";

  __FieldInput1 input() default @__FieldInput1;

  __FieldExpression1 where() default @__FieldExpression1;

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

  String $input() default "";

  String $where() default "";
}
