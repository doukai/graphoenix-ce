package io.graphoenix.showcase.user.dto.annotation;

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
public @interface RoleQueryArguments {
  StringExpression1 id() default @StringExpression1;

  StringExpression1 name() default @StringExpression1;

  RoleTypeExpression1 type() default @RoleTypeExpression1;

  UserExpression1 users() default @UserExpression1;

  boolean includeDeprecated() default false;

  IntExpression1 version() default @IntExpression1;

  IntExpression1 realmId() default @IntExpression1;

  StringExpression1 createUserId() default @StringExpression1;

  StringExpression1 createTime() default @StringExpression1;

  StringExpression1 updateUserId() default @StringExpression1;

  StringExpression1 updateTime() default @StringExpression1;

  StringExpression1 createGroupId() default @StringExpression1;

  StringExpression1 __typename() default @StringExpression1;

  RoleTypeRelationExpression1 roleTypeRelation() default @RoleTypeRelationExpression1;

  RoleUserRelationExpression1 roleUserRelation() default @RoleUserRelationExpression1;

  String[] groupBy() default {};

  boolean not() default false;

  Conditional cond() default Conditional.AND;

  RoleExpression1[] exs() default {};

  String $id() default "";

  String $name() default "";

  String $type() default "";

  String $users() default "";

  String $includeDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $roleTypeRelation() default "";

  String $roleUserRelation() default "";

  String $groupBy() default "";

  String $not() default "";

  String $cond() default "";

  String $exs() default "";
}
