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
public @interface OrganizationUserRelationInput {
  String id() default "";

  String userRef() default "";

  UserInput1 user() default @UserInput1;

  String organizationRef() default "";

  OrganizationInput1 organization() default @OrganizationInput1;

  boolean isDeprecated() default false;

  int version() default 0;

  int realmId() default 0;

  String createUserId() default "";

  String createTime() default "";

  String updateUserId() default "";

  String updateTime() default "";

  String createGroupId() default "";

  String __typename() default "OrganizationUserRelation";

  OrganizationUserRelationExpression1 where() default @OrganizationUserRelationExpression1;

  String $id() default "";

  String $userRef() default "";

  String $user() default "";

  String $organizationRef() default "";

  String $organization() default "";

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
