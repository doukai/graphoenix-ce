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
public @interface Mutation {
  UserMutationArguments user() default @UserMutationArguments;

  UserListMutationArguments userList() default @UserListMutationArguments;

  UserProfileMutationArguments userProfile() default @UserProfileMutationArguments;

  UserProfileListMutationArguments userProfileList() default @UserProfileListMutationArguments;

  RoleMutationArguments role() default @RoleMutationArguments;

  RoleListMutationArguments roleList() default @RoleListMutationArguments;

  OrganizationMutationArguments organization() default @OrganizationMutationArguments;

  OrganizationListMutationArguments organizationList() default @OrganizationListMutationArguments;

  UserUserProfileRelationMutationArguments userUserProfileRelation(
      ) default @UserUserProfileRelationMutationArguments;

  UserUserProfileRelationListMutationArguments userUserProfileRelationList(
      ) default @UserUserProfileRelationListMutationArguments;

  OrganizationUserRelationMutationArguments organizationUserRelation(
      ) default @OrganizationUserRelationMutationArguments;

  OrganizationUserRelationListMutationArguments organizationUserRelationList(
      ) default @OrganizationUserRelationListMutationArguments;

  RoleUserRelationMutationArguments roleUserRelation() default @RoleUserRelationMutationArguments;

  RoleUserRelationListMutationArguments roleUserRelationList(
      ) default @RoleUserRelationListMutationArguments;

  MobileNumbersUserRelationMutationArguments mobileNumbersUserRelation(
      ) default @MobileNumbersUserRelationMutationArguments;

  MobileNumbersUserRelationListMutationArguments mobileNumbersUserRelationList(
      ) default @MobileNumbersUserRelationListMutationArguments;

  RoleTypeRelationMutationArguments roleTypeRelation() default @RoleTypeRelationMutationArguments;

  RoleTypeRelationListMutationArguments roleTypeRelationList(
      ) default @RoleTypeRelationListMutationArguments;

  OrganizationOrganizationRelationMutationArguments organizationOrganizationRelation(
      ) default @OrganizationOrganizationRelationMutationArguments;

  OrganizationOrganizationRelationListMutationArguments organizationOrganizationRelationList(
      ) default @OrganizationOrganizationRelationListMutationArguments;
}
