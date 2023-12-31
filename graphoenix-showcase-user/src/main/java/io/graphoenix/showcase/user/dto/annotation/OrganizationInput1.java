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
public @interface OrganizationInput1 {
  String id() default "";

  String name() default "";

  OrganizationInput2 above() default @OrganizationInput2;

  UserInput2[] users() default {};

  boolean isDeprecated() default false;

  int version() default 0;

  int realmId() default 0;

  String createUserId() default "";

  String createTime() default "";

  String updateUserId() default "";

  String updateTime() default "";

  String createGroupId() default "";

  String __typename() default "Organization";

  OrganizationOrganizationRelationInput2[] organizationOrganizationRelation() default {};

  OrganizationUserRelationInput2[] organizationUserRelation() default {};

  OrganizationExpression2 where() default @OrganizationExpression2;

  String $id() default "";

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

  String $organizationOrganizationRelation() default "";

  String $organizationUserRelation() default "";

  String $where() default "";
}
