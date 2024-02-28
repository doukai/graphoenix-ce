package io.graphoenix.showcase.user.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Query {
  UserQueryArguments user() default @UserQueryArguments;

  UserListQueryArguments userList() default @UserListQueryArguments;

  UserConnectionQueryArguments userConnection() default @UserConnectionQueryArguments;

  UserProfileQueryArguments userProfile() default @UserProfileQueryArguments;

  UserProfileListQueryArguments userProfileList() default @UserProfileListQueryArguments;

  UserProfileConnectionQueryArguments userProfileConnection(
      ) default @UserProfileConnectionQueryArguments;

  RoleQueryArguments role() default @RoleQueryArguments;

  RoleListQueryArguments roleList() default @RoleListQueryArguments;

  RoleConnectionQueryArguments roleConnection() default @RoleConnectionQueryArguments;

  OrganizationQueryArguments organization() default @OrganizationQueryArguments;

  OrganizationListQueryArguments organizationList() default @OrganizationListQueryArguments;

  OrganizationConnectionQueryArguments organizationConnection(
      ) default @OrganizationConnectionQueryArguments;

  RoleUserRelationQueryArguments roleUserRelation() default @RoleUserRelationQueryArguments;

  RoleUserRelationListQueryArguments roleUserRelationList(
      ) default @RoleUserRelationListQueryArguments;

  RoleUserRelationConnectionQueryArguments roleUserRelationConnection(
      ) default @RoleUserRelationConnectionQueryArguments;

  UserMobileNumbersRelationQueryArguments userMobileNumbersRelation(
      ) default @UserMobileNumbersRelationQueryArguments;

  UserMobileNumbersRelationListQueryArguments userMobileNumbersRelationList(
      ) default @UserMobileNumbersRelationListQueryArguments;

  UserMobileNumbersRelationConnectionQueryArguments userMobileNumbersRelationConnection(
      ) default @UserMobileNumbersRelationConnectionQueryArguments;

  UserUserProfileRelationQueryArguments userUserProfileRelation(
      ) default @UserUserProfileRelationQueryArguments;

  UserUserProfileRelationListQueryArguments userUserProfileRelationList(
      ) default @UserUserProfileRelationListQueryArguments;

  UserUserProfileRelationConnectionQueryArguments userUserProfileRelationConnection(
      ) default @UserUserProfileRelationConnectionQueryArguments;

  RoleRoleTypeRelationQueryArguments roleRoleTypeRelation(
      ) default @RoleRoleTypeRelationQueryArguments;

  RoleRoleTypeRelationListQueryArguments roleRoleTypeRelationList(
      ) default @RoleRoleTypeRelationListQueryArguments;

  RoleRoleTypeRelationConnectionQueryArguments roleRoleTypeRelationConnection(
      ) default @RoleRoleTypeRelationConnectionQueryArguments;

  OrganizationOrganizationRelationQueryArguments organizationOrganizationRelation(
      ) default @OrganizationOrganizationRelationQueryArguments;

  OrganizationOrganizationRelationListQueryArguments organizationOrganizationRelationList(
      ) default @OrganizationOrganizationRelationListQueryArguments;

  OrganizationOrganizationRelationConnectionQueryArguments organizationOrganizationRelationConnection(
      ) default @OrganizationOrganizationRelationConnectionQueryArguments;
}
