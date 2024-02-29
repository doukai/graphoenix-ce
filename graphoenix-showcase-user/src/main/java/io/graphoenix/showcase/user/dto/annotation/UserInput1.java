package io.graphoenix.showcase.user.dto.annotation;

import io.graphoenix.showcase.user.dto.enumType.Sex;
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
public @interface UserInput1 {
  String id() default "";

  String login() default "";

  String password() default "";

  String name() default "";

  int age() default 0;

  boolean disabled() default false;

  Sex sex() default Sex.MAN;

  UserProfileInput2 userProfile() default @UserProfileInput2;

  OrganizationInput2 organization() default @OrganizationInput2;

  RoleInput2[] roles() default {};

  String[] mobileNumbers() default {};

  boolean isDeprecated() default false;

  int version() default 0;

  int realmId() default 0;

  String createUserId() default "";

  String createTime() default "";

  String updateUserId() default "";

  String updateTime() default "";

  String createGroupId() default "";

  String __typename() default "User";

  OrganizationUserRelationInput2[] organizationUserRelation() default {};

  RoleUserRelationInput2[] roleUserRelation() default {};

  UserMobileNumbersRelationInput2[] userMobileNumbersRelation() default {};

  UserExpression2 where() default @UserExpression2;

  String $id() default "";

  String $login() default "";

  String $password() default "";

  String $name() default "";

  String $age() default "";

  String $disabled() default "";

  String $sex() default "";

  String $userProfile() default "";

  String $organization() default "";

  String $roles() default "";

  String $mobileNumbers() default "";

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

  String $roleUserRelation() default "";

  String $userMobileNumbersRelation() default "";

  String $where() default "";
}
