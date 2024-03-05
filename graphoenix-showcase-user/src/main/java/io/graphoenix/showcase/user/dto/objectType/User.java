package io.graphoenix.showcase.user.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import io.graphoenix.showcase.user.dto.enumType.Sex;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class User implements Meta {
  @Id
  private String id;

  @NonNull
  private String login;

  @NonNull
  private String password;

  @NonNull
  private String name;

  private Integer age;

  private Boolean disabled;

  private Sex sex;

  private UserProfile userProfile;

  private Organization organization;

  private Collection<Role> roles;

  private Collection<String> mobileNumbers;

  private Boolean isDeprecated;

  private Integer version;

  private Integer realmId;

  private String createUserId;

  private LocalDateTime createTime;

  private String updateUserId;

  private LocalDateTime updateTime;

  private String createGroupId;

  private String __typename;

  private Collection<OrganizationUserRelation> organizationUserRelation;

  private Collection<RoleUserRelation> roleUserRelation;

  private Collection<UserMobileNumbersRelation> userMobileNumbersRelation;

  private Role rolesAggregate;

  private RoleConnection rolesConnection;

  private OrganizationUserRelation organizationUserRelationAggregate;

  private OrganizationUserRelationConnection organizationUserRelationConnection;

  private RoleUserRelation roleUserRelationAggregate;

  private RoleUserRelationConnection roleUserRelationConnection;

  private UserMobileNumbersRelation userMobileNumbersRelationAggregate;

  private UserMobileNumbersRelationConnection userMobileNumbersRelationConnection;

  private Integer idCount;

  private Integer idMax;

  private Integer idMin;

  private Integer loginCount;

  private String loginMax;

  private String loginMin;

  private Integer passwordCount;

  private String passwordMax;

  private String passwordMin;

  private Integer nameCount;

  private String nameMax;

  private String nameMin;

  private Integer ageCount;

  private Integer ageSum;

  private Integer ageAvg;

  private Integer ageMax;

  private Integer ageMin;

  private String nickName;

  private Integer age2;

  private Collection<String> address;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLogin() {
    return this.login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return this.age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Boolean getDisabled() {
    return this.disabled;
  }

  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
  }

  public Sex getSex() {
    return this.sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public UserProfile getUserProfile() {
    return this.userProfile;
  }

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  public Organization getOrganization() {
    return this.organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  public Collection<Role> getRoles() {
    return this.roles;
  }

  public void setRoles(Collection<Role> roles) {
    this.roles = roles;
  }

  public Collection<String> getMobileNumbers() {
    return this.mobileNumbers;
  }

  public void setMobileNumbers(Collection<String> mobileNumbers) {
    this.mobileNumbers = mobileNumbers;
  }

  @Override
  public Boolean getIsDeprecated() {
    return this.isDeprecated;
  }

  @Override
  public void setIsDeprecated(Boolean isDeprecated) {
    this.isDeprecated = isDeprecated;
  }

  @Override
  public Integer getVersion() {
    return this.version;
  }

  @Override
  public void setVersion(Integer version) {
    this.version = version;
  }

  @Override
  public Integer getRealmId() {
    return this.realmId;
  }

  @Override
  public void setRealmId(Integer realmId) {
    this.realmId = realmId;
  }

  @Override
  public String getCreateUserId() {
    return this.createUserId;
  }

  @Override
  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }

  @Override
  public LocalDateTime getCreateTime() {
    return this.createTime;
  }

  @Override
  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  @Override
  public String getUpdateUserId() {
    return this.updateUserId;
  }

  @Override
  public void setUpdateUserId(String updateUserId) {
    this.updateUserId = updateUserId;
  }

  @Override
  public LocalDateTime getUpdateTime() {
    return this.updateTime;
  }

  @Override
  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public String getCreateGroupId() {
    return this.createGroupId;
  }

  @Override
  public void setCreateGroupId(String createGroupId) {
    this.createGroupId = createGroupId;
  }

  public String get__typename() {
    return this.__typename;
  }

  public void set__typename(String __typename) {
    this.__typename = __typename;
  }

  public Collection<OrganizationUserRelation> getOrganizationUserRelation() {
    return this.organizationUserRelation;
  }

  public void setOrganizationUserRelation(
      Collection<OrganizationUserRelation> organizationUserRelation) {
    this.organizationUserRelation = organizationUserRelation;
  }

  public Collection<RoleUserRelation> getRoleUserRelation() {
    return this.roleUserRelation;
  }

  public void setRoleUserRelation(Collection<RoleUserRelation> roleUserRelation) {
    this.roleUserRelation = roleUserRelation;
  }

  public Collection<UserMobileNumbersRelation> getUserMobileNumbersRelation() {
    return this.userMobileNumbersRelation;
  }

  public void setUserMobileNumbersRelation(
      Collection<UserMobileNumbersRelation> userMobileNumbersRelation) {
    this.userMobileNumbersRelation = userMobileNumbersRelation;
  }

  public Role getRolesAggregate() {
    return this.rolesAggregate;
  }

  public void setRolesAggregate(Role rolesAggregate) {
    this.rolesAggregate = rolesAggregate;
  }

  public RoleConnection getRolesConnection() {
    return this.rolesConnection;
  }

  public void setRolesConnection(RoleConnection rolesConnection) {
    this.rolesConnection = rolesConnection;
  }

  public OrganizationUserRelation getOrganizationUserRelationAggregate() {
    return this.organizationUserRelationAggregate;
  }

  public void setOrganizationUserRelationAggregate(
      OrganizationUserRelation organizationUserRelationAggregate) {
    this.organizationUserRelationAggregate = organizationUserRelationAggregate;
  }

  public OrganizationUserRelationConnection getOrganizationUserRelationConnection() {
    return this.organizationUserRelationConnection;
  }

  public void setOrganizationUserRelationConnection(
      OrganizationUserRelationConnection organizationUserRelationConnection) {
    this.organizationUserRelationConnection = organizationUserRelationConnection;
  }

  public RoleUserRelation getRoleUserRelationAggregate() {
    return this.roleUserRelationAggregate;
  }

  public void setRoleUserRelationAggregate(RoleUserRelation roleUserRelationAggregate) {
    this.roleUserRelationAggregate = roleUserRelationAggregate;
  }

  public RoleUserRelationConnection getRoleUserRelationConnection() {
    return this.roleUserRelationConnection;
  }

  public void setRoleUserRelationConnection(RoleUserRelationConnection roleUserRelationConnection) {
    this.roleUserRelationConnection = roleUserRelationConnection;
  }

  public UserMobileNumbersRelation getUserMobileNumbersRelationAggregate() {
    return this.userMobileNumbersRelationAggregate;
  }

  public void setUserMobileNumbersRelationAggregate(
      UserMobileNumbersRelation userMobileNumbersRelationAggregate) {
    this.userMobileNumbersRelationAggregate = userMobileNumbersRelationAggregate;
  }

  public UserMobileNumbersRelationConnection getUserMobileNumbersRelationConnection() {
    return this.userMobileNumbersRelationConnection;
  }

  public void setUserMobileNumbersRelationConnection(
      UserMobileNumbersRelationConnection userMobileNumbersRelationConnection) {
    this.userMobileNumbersRelationConnection = userMobileNumbersRelationConnection;
  }

  public Integer getIdCount() {
    return this.idCount;
  }

  public void setIdCount(Integer idCount) {
    this.idCount = idCount;
  }

  public Integer getIdMax() {
    return this.idMax;
  }

  public void setIdMax(Integer idMax) {
    this.idMax = idMax;
  }

  public Integer getIdMin() {
    return this.idMin;
  }

  public void setIdMin(Integer idMin) {
    this.idMin = idMin;
  }

  public Integer getLoginCount() {
    return this.loginCount;
  }

  public void setLoginCount(Integer loginCount) {
    this.loginCount = loginCount;
  }

  public String getLoginMax() {
    return this.loginMax;
  }

  public void setLoginMax(String loginMax) {
    this.loginMax = loginMax;
  }

  public String getLoginMin() {
    return this.loginMin;
  }

  public void setLoginMin(String loginMin) {
    this.loginMin = loginMin;
  }

  public Integer getPasswordCount() {
    return this.passwordCount;
  }

  public void setPasswordCount(Integer passwordCount) {
    this.passwordCount = passwordCount;
  }

  public String getPasswordMax() {
    return this.passwordMax;
  }

  public void setPasswordMax(String passwordMax) {
    this.passwordMax = passwordMax;
  }

  public String getPasswordMin() {
    return this.passwordMin;
  }

  public void setPasswordMin(String passwordMin) {
    this.passwordMin = passwordMin;
  }

  public Integer getNameCount() {
    return this.nameCount;
  }

  public void setNameCount(Integer nameCount) {
    this.nameCount = nameCount;
  }

  public String getNameMax() {
    return this.nameMax;
  }

  public void setNameMax(String nameMax) {
    this.nameMax = nameMax;
  }

  public String getNameMin() {
    return this.nameMin;
  }

  public void setNameMin(String nameMin) {
    this.nameMin = nameMin;
  }

  public Integer getAgeCount() {
    return this.ageCount;
  }

  public void setAgeCount(Integer ageCount) {
    this.ageCount = ageCount;
  }

  public Integer getAgeSum() {
    return this.ageSum;
  }

  public void setAgeSum(Integer ageSum) {
    this.ageSum = ageSum;
  }

  public Integer getAgeAvg() {
    return this.ageAvg;
  }

  public void setAgeAvg(Integer ageAvg) {
    this.ageAvg = ageAvg;
  }

  public Integer getAgeMax() {
    return this.ageMax;
  }

  public void setAgeMax(Integer ageMax) {
    this.ageMax = ageMax;
  }

  public Integer getAgeMin() {
    return this.ageMin;
  }

  public void setAgeMin(Integer ageMin) {
    this.ageMin = ageMin;
  }

  public String getNickName() {
    return this.nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public Integer getAge2() {
    return this.age2;
  }

  public void setAge2(Integer age2) {
    this.age2 = age2;
  }

  public Collection<String> getAddress() {
    return this.address;
  }

  public void setAddress(Collection<String> address) {
    this.address = address;
  }
}
