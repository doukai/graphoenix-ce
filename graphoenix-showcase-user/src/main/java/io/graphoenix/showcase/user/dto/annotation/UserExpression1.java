package io.graphoenix.showcase.user.dto.annotation;

import io.graphoenix.core.dto.annotation.BooleanExpression2;
import io.graphoenix.core.dto.annotation.IntExpression2;
import io.graphoenix.core.dto.annotation.StringExpression2;
import io.graphoenix.core.dto.enumType.Conditional;
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
public @interface UserExpression1 {
  StringExpression2 id() default @StringExpression2;

  StringExpression2 login() default @StringExpression2;

  StringExpression2 password() default @StringExpression2;

  StringExpression2 name() default @StringExpression2;

  IntExpression2 age() default @IntExpression2;

  BooleanExpression2 disabled() default @BooleanExpression2;

  SexExpression2 sex() default @SexExpression2;

  UserProfileExpression2 userProfile() default @UserProfileExpression2;

  OrganizationExpression2 organization() default @OrganizationExpression2;

  RoleExpression2 roles() default @RoleExpression2;

  StringExpression2 mobileNumbers() default @StringExpression2;

  boolean includeDeprecated() default false;

  IntExpression2 version() default @IntExpression2;

  IntExpression2 realmId() default @IntExpression2;

  StringExpression2 createUserId() default @StringExpression2;

  StringExpression2 createTime() default @StringExpression2;

  StringExpression2 updateUserId() default @StringExpression2;

  StringExpression2 updateTime() default @StringExpression2;

  StringExpression2 createGroupId() default @StringExpression2;

  StringExpression2 __typename() default @StringExpression2;

  UserUserProfileRelationExpression2 userUserProfileRelation(
      ) default @UserUserProfileRelationExpression2;

  OrganizationUserRelationExpression2 organizationUserRelation(
      ) default @OrganizationUserRelationExpression2;

  RoleUserRelationExpression2 roleUserRelation() default @RoleUserRelationExpression2;

  UserMobileNumbersRelationExpression2 userMobileNumbersRelation(
      ) default @UserMobileNumbersRelationExpression2;

  boolean not() default false;

  Conditional cond() default Conditional.AND;

  UserExpression2[] exs() default {};

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

  String $includeDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $userUserProfileRelation() default "";

  String $organizationUserRelation() default "";

  String $roleUserRelation() default "";

  String $userMobileNumbersRelation() default "";

  String $not() default "";

  String $cond() default "";

  String $exs() default "";
}
