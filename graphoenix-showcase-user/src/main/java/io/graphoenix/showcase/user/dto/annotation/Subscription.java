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
public @interface Subscription {
  UserSubscriptionArguments user() default @UserSubscriptionArguments;

  UserListSubscriptionArguments userList() default @UserListSubscriptionArguments;

  UserConnectionSubscriptionArguments userConnection() default @UserConnectionSubscriptionArguments;

  UserProfileSubscriptionArguments userProfile() default @UserProfileSubscriptionArguments;

  UserProfileListSubscriptionArguments userProfileList(
      ) default @UserProfileListSubscriptionArguments;

  UserProfileConnectionSubscriptionArguments userProfileConnection(
      ) default @UserProfileConnectionSubscriptionArguments;

  RoleSubscriptionArguments role() default @RoleSubscriptionArguments;

  RoleListSubscriptionArguments roleList() default @RoleListSubscriptionArguments;

  RoleConnectionSubscriptionArguments roleConnection() default @RoleConnectionSubscriptionArguments;

  OrganizationSubscriptionArguments organization() default @OrganizationSubscriptionArguments;

  OrganizationListSubscriptionArguments organizationList(
      ) default @OrganizationListSubscriptionArguments;

  OrganizationConnectionSubscriptionArguments organizationConnection(
      ) default @OrganizationConnectionSubscriptionArguments;

  UserUserProfileRelationSubscriptionArguments userUserProfileRelation(
      ) default @UserUserProfileRelationSubscriptionArguments;

  UserUserProfileRelationListSubscriptionArguments userUserProfileRelationList(
      ) default @UserUserProfileRelationListSubscriptionArguments;

  UserUserProfileRelationConnectionSubscriptionArguments userUserProfileRelationConnection(
      ) default @UserUserProfileRelationConnectionSubscriptionArguments;

  OrganizationUserRelationSubscriptionArguments organizationUserRelation(
      ) default @OrganizationUserRelationSubscriptionArguments;

  OrganizationUserRelationListSubscriptionArguments organizationUserRelationList(
      ) default @OrganizationUserRelationListSubscriptionArguments;

  OrganizationUserRelationConnectionSubscriptionArguments organizationUserRelationConnection(
      ) default @OrganizationUserRelationConnectionSubscriptionArguments;

  RoleUserRelationSubscriptionArguments roleUserRelation(
      ) default @RoleUserRelationSubscriptionArguments;

  RoleUserRelationListSubscriptionArguments roleUserRelationList(
      ) default @RoleUserRelationListSubscriptionArguments;

  RoleUserRelationConnectionSubscriptionArguments roleUserRelationConnection(
      ) default @RoleUserRelationConnectionSubscriptionArguments;

  MobileNumbersUserRelationSubscriptionArguments mobileNumbersUserRelation(
      ) default @MobileNumbersUserRelationSubscriptionArguments;

  MobileNumbersUserRelationListSubscriptionArguments mobileNumbersUserRelationList(
      ) default @MobileNumbersUserRelationListSubscriptionArguments;

  MobileNumbersUserRelationConnectionSubscriptionArguments mobileNumbersUserRelationConnection(
      ) default @MobileNumbersUserRelationConnectionSubscriptionArguments;

  RoleTypeRelationSubscriptionArguments roleTypeRelation(
      ) default @RoleTypeRelationSubscriptionArguments;

  RoleTypeRelationListSubscriptionArguments roleTypeRelationList(
      ) default @RoleTypeRelationListSubscriptionArguments;

  RoleTypeRelationConnectionSubscriptionArguments roleTypeRelationConnection(
      ) default @RoleTypeRelationConnectionSubscriptionArguments;

  OrganizationOrganizationRelationSubscriptionArguments organizationOrganizationRelation(
      ) default @OrganizationOrganizationRelationSubscriptionArguments;

  OrganizationOrganizationRelationListSubscriptionArguments organizationOrganizationRelationList(
      ) default @OrganizationOrganizationRelationListSubscriptionArguments;

  OrganizationOrganizationRelationConnectionSubscriptionArguments organizationOrganizationRelationConnection(
      ) default @OrganizationOrganizationRelationConnectionSubscriptionArguments;
}
