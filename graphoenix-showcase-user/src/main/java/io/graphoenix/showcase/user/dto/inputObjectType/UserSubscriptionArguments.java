package io.graphoenix.showcase.user.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.BooleanExpression;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class UserSubscriptionArguments implements MetaExpression {
  private StringExpression id;

  private StringExpression login;

  private StringExpression password;

  private StringExpression name;

  private IntExpression age;

  private BooleanExpression disabled;

  private SexExpression sex;

  private UserProfileExpression userProfile;

  private OrganizationExpression organization;

  private RoleExpression roles;

  private StringExpression mobileNumbers;

  @DefaultValue("false")
  private Boolean includeDeprecated;

  private IntExpression version;

  private IntExpression realmId;

  private StringExpression createUserId;

  private StringExpression createTime;

  private StringExpression updateUserId;

  private StringExpression updateTime;

  private StringExpression createGroupId;

  private StringExpression __typename;

  private OrganizationUserRelationExpression organizationUserRelation;

  private RoleUserRelationExpression roleUserRelation;

  private UserMobileNumbersRelationExpression userMobileNumbersRelation;

  private Collection<String> groupBy;

  @DefaultValue("false")
  private Boolean not;

  @DefaultValue("AND")
  private Conditional cond;

  private Collection<UserExpression> exs;

  public StringExpression getId() {
    return this.id;
  }

  public void setId(StringExpression id) {
    this.id = id;
  }

  public StringExpression getLogin() {
    return this.login;
  }

  public void setLogin(StringExpression login) {
    this.login = login;
  }

  public StringExpression getPassword() {
    return this.password;
  }

  public void setPassword(StringExpression password) {
    this.password = password;
  }

  public StringExpression getName() {
    return this.name;
  }

  public void setName(StringExpression name) {
    this.name = name;
  }

  public IntExpression getAge() {
    return this.age;
  }

  public void setAge(IntExpression age) {
    this.age = age;
  }

  public BooleanExpression getDisabled() {
    return this.disabled;
  }

  public void setDisabled(BooleanExpression disabled) {
    this.disabled = disabled;
  }

  public SexExpression getSex() {
    return this.sex;
  }

  public void setSex(SexExpression sex) {
    this.sex = sex;
  }

  public UserProfileExpression getUserProfile() {
    return this.userProfile;
  }

  public void setUserProfile(UserProfileExpression userProfile) {
    this.userProfile = userProfile;
  }

  public OrganizationExpression getOrganization() {
    return this.organization;
  }

  public void setOrganization(OrganizationExpression organization) {
    this.organization = organization;
  }

  public RoleExpression getRoles() {
    return this.roles;
  }

  public void setRoles(RoleExpression roles) {
    this.roles = roles;
  }

  public StringExpression getMobileNumbers() {
    return this.mobileNumbers;
  }

  public void setMobileNumbers(StringExpression mobileNumbers) {
    this.mobileNumbers = mobileNumbers;
  }

  public Boolean getIncludeDeprecated() {
    return this.includeDeprecated;
  }

  public void setIncludeDeprecated(Boolean includeDeprecated) {
    this.includeDeprecated = includeDeprecated;
  }

  public IntExpression getVersion() {
    return this.version;
  }

  public void setVersion(IntExpression version) {
    this.version = version;
  }

  @Override
  public IntExpression getRealmId() {
    return this.realmId;
  }

  @Override
  public void setRealmId(IntExpression realmId) {
    this.realmId = realmId;
  }

  @Override
  public StringExpression getCreateUserId() {
    return this.createUserId;
  }

  @Override
  public void setCreateUserId(StringExpression createUserId) {
    this.createUserId = createUserId;
  }

  @Override
  public StringExpression getCreateTime() {
    return this.createTime;
  }

  @Override
  public void setCreateTime(StringExpression createTime) {
    this.createTime = createTime;
  }

  @Override
  public StringExpression getUpdateUserId() {
    return this.updateUserId;
  }

  @Override
  public void setUpdateUserId(StringExpression updateUserId) {
    this.updateUserId = updateUserId;
  }

  @Override
  public StringExpression getUpdateTime() {
    return this.updateTime;
  }

  @Override
  public void setUpdateTime(StringExpression updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public StringExpression getCreateGroupId() {
    return this.createGroupId;
  }

  @Override
  public void setCreateGroupId(StringExpression createGroupId) {
    this.createGroupId = createGroupId;
  }

  public StringExpression get__typename() {
    return this.__typename;
  }

  public void set__typename(StringExpression __typename) {
    this.__typename = __typename;
  }

  public OrganizationUserRelationExpression getOrganizationUserRelation() {
    return this.organizationUserRelation;
  }

  public void setOrganizationUserRelation(
      OrganizationUserRelationExpression organizationUserRelation) {
    this.organizationUserRelation = organizationUserRelation;
  }

  public RoleUserRelationExpression getRoleUserRelation() {
    return this.roleUserRelation;
  }

  public void setRoleUserRelation(RoleUserRelationExpression roleUserRelation) {
    this.roleUserRelation = roleUserRelation;
  }

  public UserMobileNumbersRelationExpression getUserMobileNumbersRelation() {
    return this.userMobileNumbersRelation;
  }

  public void setUserMobileNumbersRelation(
      UserMobileNumbersRelationExpression userMobileNumbersRelation) {
    this.userMobileNumbersRelation = userMobileNumbersRelation;
  }

  public Collection<String> getGroupBy() {
    return this.groupBy;
  }

  public void setGroupBy(Collection<String> groupBy) {
    this.groupBy = groupBy;
  }

  public Boolean getNot() {
    return this.not;
  }

  public void setNot(Boolean not) {
    this.not = not;
  }

  public Conditional getCond() {
    return this.cond;
  }

  public void setCond(Conditional cond) {
    this.cond = cond;
  }

  public Collection<UserExpression> getExs() {
    return this.exs;
  }

  public void setExs(Collection<UserExpression> exs) {
    this.exs = exs;
  }
}
