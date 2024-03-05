package io.graphoenix.showcase.user.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Query {
  private User user;

  private Collection<User> userList;

  private UserConnection userConnection;

  private UserProfile userProfile;

  private Collection<UserProfile> userProfileList;

  private UserProfileConnection userProfileConnection;

  private Role role;

  private Collection<Role> roleList;

  private RoleConnection roleConnection;

  private Organization organization;

  private Collection<Organization> organizationList;

  private OrganizationConnection organizationConnection;

  private OrganizationUserRelation organizationUserRelation;

  private Collection<OrganizationUserRelation> organizationUserRelationList;

  private OrganizationUserRelationConnection organizationUserRelationConnection;

  private RoleUserRelation roleUserRelation;

  private Collection<RoleUserRelation> roleUserRelationList;

  private RoleUserRelationConnection roleUserRelationConnection;

  private UserMobileNumbersRelation userMobileNumbersRelation;

  private Collection<UserMobileNumbersRelation> userMobileNumbersRelationList;

  private UserMobileNumbersRelationConnection userMobileNumbersRelationConnection;

  private UserUserProfileRelation userUserProfileRelation;

  private Collection<UserUserProfileRelation> userUserProfileRelationList;

  private UserUserProfileRelationConnection userUserProfileRelationConnection;

  private RoleRoleTypeRelation roleRoleTypeRelation;

  private Collection<RoleRoleTypeRelation> roleRoleTypeRelationList;

  private RoleRoleTypeRelationConnection roleRoleTypeRelationConnection;

  private LocalDateTime currentTime;

  private String appName;

  private String appName2;

  private Collection<String> loginName;

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

  public UserConnection getUserConnection() {
    return this.userConnection;
  }

  public void setUserConnection(UserConnection userConnection) {
    this.userConnection = userConnection;
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

  public UserProfileConnection getUserProfileConnection() {
    return this.userProfileConnection;
  }

  public void setUserProfileConnection(UserProfileConnection userProfileConnection) {
    this.userProfileConnection = userProfileConnection;
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

  public RoleConnection getRoleConnection() {
    return this.roleConnection;
  }

  public void setRoleConnection(RoleConnection roleConnection) {
    this.roleConnection = roleConnection;
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

  public OrganizationConnection getOrganizationConnection() {
    return this.organizationConnection;
  }

  public void setOrganizationConnection(OrganizationConnection organizationConnection) {
    this.organizationConnection = organizationConnection;
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

  public OrganizationUserRelationConnection getOrganizationUserRelationConnection() {
    return this.organizationUserRelationConnection;
  }

  public void setOrganizationUserRelationConnection(
      OrganizationUserRelationConnection organizationUserRelationConnection) {
    this.organizationUserRelationConnection = organizationUserRelationConnection;
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

  public RoleUserRelationConnection getRoleUserRelationConnection() {
    return this.roleUserRelationConnection;
  }

  public void setRoleUserRelationConnection(RoleUserRelationConnection roleUserRelationConnection) {
    this.roleUserRelationConnection = roleUserRelationConnection;
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

  public UserMobileNumbersRelationConnection getUserMobileNumbersRelationConnection() {
    return this.userMobileNumbersRelationConnection;
  }

  public void setUserMobileNumbersRelationConnection(
      UserMobileNumbersRelationConnection userMobileNumbersRelationConnection) {
    this.userMobileNumbersRelationConnection = userMobileNumbersRelationConnection;
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

  public UserUserProfileRelationConnection getUserUserProfileRelationConnection() {
    return this.userUserProfileRelationConnection;
  }

  public void setUserUserProfileRelationConnection(
      UserUserProfileRelationConnection userUserProfileRelationConnection) {
    this.userUserProfileRelationConnection = userUserProfileRelationConnection;
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

  public RoleRoleTypeRelationConnection getRoleRoleTypeRelationConnection() {
    return this.roleRoleTypeRelationConnection;
  }

  public void setRoleRoleTypeRelationConnection(
      RoleRoleTypeRelationConnection roleRoleTypeRelationConnection) {
    this.roleRoleTypeRelationConnection = roleRoleTypeRelationConnection;
  }

  public LocalDateTime getCurrentTime() {
    return this.currentTime;
  }

  public void setCurrentTime(LocalDateTime currentTime) {
    this.currentTime = currentTime;
  }

  public String getAppName() {
    return this.appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getAppName2() {
    return this.appName2;
  }

  public void setAppName2(String appName2) {
    this.appName2 = appName2;
  }

  public Collection<String> getLoginName() {
    return this.loginName;
  }

  public void setLoginName(Collection<String> loginName) {
    this.loginName = loginName;
  }
}
