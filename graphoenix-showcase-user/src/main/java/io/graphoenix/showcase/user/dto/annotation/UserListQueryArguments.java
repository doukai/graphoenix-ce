package io.graphoenix.showcase.user.dto.annotation;

import io.graphoenix.core.dto.annotation.BooleanExpression1;
import io.graphoenix.core.dto.annotation.IntExpression1;
import io.graphoenix.core.dto.annotation.StringExpression1;
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
public @interface UserListQueryArguments {
  StringExpression1 id() default @StringExpression1;

  StringExpression1 login() default @StringExpression1;

  StringExpression1 password() default @StringExpression1;

  StringExpression1 name() default @StringExpression1;

  IntExpression1 age() default @IntExpression1;

  BooleanExpression1 disabled() default @BooleanExpression1;

  SexExpression1 sex() default @SexExpression1;

  UserProfileExpression1 userProfile() default @UserProfileExpression1;

  OrganizationExpression1 organization() default @OrganizationExpression1;

  RoleExpression1 roles() default @RoleExpression1;

  StringExpression1 mobileNumbers() default @StringExpression1;

  boolean includeDeprecated() default false;

  IntExpression1 version() default @IntExpression1;

  IntExpression1 realmId() default @IntExpression1;

  StringExpression1 createUserId() default @StringExpression1;

  StringExpression1 createTime() default @StringExpression1;

  StringExpression1 updateUserId() default @StringExpression1;

  StringExpression1 updateTime() default @StringExpression1;

  StringExpression1 createGroupId() default @StringExpression1;

  StringExpression1 __typename() default @StringExpression1;

  OrganizationUserRelationExpression1 organizationUserRelation(
      ) default @OrganizationUserRelationExpression1;

  RoleUserRelationExpression1 roleUserRelation() default @RoleUserRelationExpression1;

  UserMobileNumbersRelationExpression1 userMobileNumbersRelation(
      ) default @UserMobileNumbersRelationExpression1;

  UserOrderBy1 orderBy() default @UserOrderBy1;

  String[] groupBy() default {};

  boolean not() default false;

  Conditional cond() default Conditional.AND;

  UserExpression1[] exs() default {};

  int first() default 0;

  int last() default 0;

  int offset() default 0;

  String after() default "";

  String before() default "";

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

  String $organizationUserRelation() default "";

  String $roleUserRelation() default "";

  String $userMobileNumbersRelation() default "";

  String $orderBy() default "";

  String $groupBy() default "";

  String $not() default "";

  String $cond() default "";

  String $exs() default "";

  String $first() default "";

  String $last() default "";

  String $offset() default "";

  String $after() default "";

  String $before() default "";
}
