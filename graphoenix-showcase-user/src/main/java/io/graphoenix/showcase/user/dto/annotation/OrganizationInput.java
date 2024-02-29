package io.graphoenix.showcase.user.dto.annotation;

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
public @interface OrganizationInput {
  String id() default "";

  int parentId() default 0;

  String name() default "";

  OrganizationInput1 above() default @OrganizationInput1;

  UserInput1[] users() default {};

  boolean isDeprecated() default false;

  int version() default 0;

  int realmId() default 0;

  String createUserId() default "";

  String createTime() default "";

  String updateUserId() default "";

  String updateTime() default "";

  String createGroupId() default "";

  String __typename() default "Organization";

  OrganizationUserRelationInput1[] organizationUserRelation() default {};

  OrganizationExpression1 where() default @OrganizationExpression1;

  String $id() default "";

  String $parentId() default "";

  String $name() default "";

  String $above() default "";

  String $users() default "";

  String $isDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $organizationUserRelation() default "";

  String $where() default "";
}
