package io.graphoenix.showcase.user.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Mutation {
  private User user;

  private Collection<User> userList;

  private UserProfile userProfile;

  private Collection<UserProfile> userProfileList;

  private Role role;

  private Collection<Role> roleList;

  private Organization organization;

  private Collection<Organization> organizationList;

  private UserUserProfileRelation userUserProfileRelation;

  private Collection<UserUserProfileRelation> userUserProfileRelationList;

  private OrganizationUserRelation organizationUserRelation;

  private Collection<OrganizationUserRelation> organizationUserRelationList;

  private RoleUserRelation roleUserRelation;

  private Collection<RoleUserRelation> roleUserRelationList;

  private UserMobileNumbersRelation userMobileNumbersRelation;

  private Collection<UserMobileNumbersRelation> userMobileNumbersRelationList;

  private RoleRoleTypeRelation roleRoleTypeRelation;

  private Collection<RoleRoleTypeRelation> roleRoleTypeRelationList;

  private OrganizationOrganizationRelation organizationOrganizationRelation;

  private Collection<OrganizationOrganizationRelation> organizationOrganizationRelationList;

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Collection<User> getUserList() {
    return this.userList;
  }

  public void setUserList(Collection<User> userList) {
    this.userList = userList;
  }

  public UserProfile getUserProfile() {
    return this.userProfile;
  }

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  public Collection<UserProfile> getUserProfileList() {
    return this.userProfileList;
  }

  public void setUserProfileList(Collection<UserProfile> userProfileList) {
    this.userProfileList = userProfileList;
  }

  public Role getRole() {
    return this.role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Collection<Role> getRoleList() {
    return this.roleList;
  }

  public void setRoleList(Collection<Role> roleList) {
    this.roleList = roleList;
  }

  public Organization getOrganization() {
    return this.organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  public Collection<Organization> getOrganizationList() {
    return this.organizationList;
  }

  public void setOrganizationList(Collection<Organization> organizationList) {
    this.organizationList = organizationList;
  }

  public UserUserProfileRelation getUserUserProfileRelation() {
    return this.userUserProfileRelation;
  }

  public void setUserUserProfileRelation(UserUserProfileRelation userUserProfileRelation) {
    this.userUserProfileRelation = userUserProfileRelation;
  }

  public Collection<UserUserProfileRelation> getUserUserProfileRelationList() {
    return this.userUserProfileRelationList;
  }

  public void setUserUserProfileRelationList(
      Collection<UserUserProfileRelation> userUserProfileRelationList) {
    this.userUserProfileRelationList = userUserProfileRelationList;
  }

  public OrganizationUserRelation getOrganizationUserRelation() {
    return this.organizationUserRelation;
  }

  public void setOrganizationUserRelation(OrganizationUserRelation organizationUserRelation) {
    this.organizationUserRelation = organizationUserRelation;
  }

  public Collection<OrganizationUserRelation> getOrganizationUserRelationList() {
    return this.organizationUserRelationList;
  }

  public void setOrganizationUserRelationList(
      Collection<OrganizationUserRelation> organizationUserRelationList) {
    this.organizationUserRelationList = organizationUserRelationList;
  }

  public RoleUserRelation getRoleUserRelation() {
    return this.roleUserRelation;
  }

  public void setRoleUserRelation(RoleUserRelation roleUserRelation) {
    this.roleUserRelation = roleUserRelation;
  }

  public Collection<RoleUserRelation> getRoleUserRelationList() {
    return this.roleUserRelationList;
  }

  public void setRoleUserRelationList(Collection<RoleUserRelation> roleUserRelationList) {
    this.roleUserRelationList = roleUserRelationList;
  }

  public UserMobileNumbersRelation getUserMobileNumbersRelation() {
    return this.userMobileNumbersRelation;
  }

  public void setUserMobileNumbersRelation(UserMobileNumbersRelation userMobileNumbersRelation) {
    this.userMobileNumbersRelation = userMobileNumbersRelation;
  }

  public Collection<UserMobileNumbersRelation> getUserMobileNumbersRelationList() {
    return this.userMobileNumbersRelationList;
  }

  public void setUserMobileNumbersRelationList(
      Collection<UserMobileNumbersRelation> userMobileNumbersRelationList) {
    this.userMobileNumbersRelationList = userMobileNumbersRelationList;
  }

  public RoleRoleTypeRelation getRoleRoleTypeRelation() {
    return this.roleRoleTypeRelation;
  }

  public void setRoleRoleTypeRelation(RoleRoleTypeRelation roleRoleTypeRelation) {
    this.roleRoleTypeRelation = roleRoleTypeRelation;
  }

  public Collection<RoleRoleTypeRelation> getRoleRoleTypeRelationList() {
    return this.roleRoleTypeRelationList;
  }

  public void setRoleRoleTypeRelationList(
      Collection<RoleRoleTypeRelation> roleRoleTypeRelationList) {
    this.roleRoleTypeRelationList = roleRoleTypeRelationList;
  }

  public OrganizationOrganizationRelation getOrganizationOrganizationRelation() {
    return this.organizationOrganizationRelation;
  }

  public void setOrganizationOrganizationRelation(
      OrganizationOrganizationRelation organizationOrganizationRelation) {
    this.organizationOrganizationRelation = organizationOrganizationRelation;
  }

  public Collection<OrganizationOrganizationRelation> getOrganizationOrganizationRelationList() {
    return this.organizationOrganizationRelationList;
  }

  public void setOrganizationOrganizationRelationList(
      Collection<OrganizationOrganizationRelation> organizationOrganizationRelationList) {
    this.organizationOrganizationRelationList = organizationOrganizationRelationList;
  }
}
