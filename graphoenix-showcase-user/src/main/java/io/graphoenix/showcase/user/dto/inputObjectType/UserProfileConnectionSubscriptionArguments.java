package io.graphoenix.showcase.user.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class UserProfileConnectionSubscriptionArguments implements MetaExpression {
  private StringExpression id;

  private UserExpression user;

  private StringExpression email;

  private StringExpression address;

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

  private UserUserProfileRelationExpression userUserProfileRelation;

  private UserProfileOrderBy orderBy;

  private Collection<String> groupBy;

  @DefaultValue("false")
  private Boolean not;

  @DefaultValue("AND")
  private Conditional cond;

  private Collection<UserProfileExpression> exs;

  private Integer first;

  private Integer last;

  private Integer offset;

  private String after;

  private String before;

  public StringExpression getId() {
    return this.id;
  }

  public void setId(StringExpression id) {
    this.id = id;
  }

  public UserExpression getUser() {
    return this.user;
  }

  public void setUser(UserExpression user) {
    this.user = user;
  }

  public StringExpression getEmail() {
    return this.email;
  }

  public void setEmail(StringExpression email) {
    this.email = email;
  }

  public StringExpression getAddress() {
    return this.address;
  }

  public void setAddress(StringExpression address) {
    this.address = address;
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

  public UserUserProfileRelationExpression getUserUserProfileRelation() {
    return this.userUserProfileRelation;
  }

  public void setUserUserProfileRelation(
      UserUserProfileRelationExpression userUserProfileRelation) {
    this.userUserProfileRelation = userUserProfileRelation;
  }

  public UserProfileOrderBy getOrderBy() {
    return this.orderBy;
  }

  public void setOrderBy(UserProfileOrderBy orderBy) {
    this.orderBy = orderBy;
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

  public Collection<UserProfileExpression> getExs() {
    return this.exs;
  }

  public void setExs(Collection<UserProfileExpression> exs) {
    this.exs = exs;
  }

  public Integer getFirst() {
    return this.first;
  }

  public void setFirst(Integer first) {
    this.first = first;
  }

  public Integer getLast() {
    return this.last;
  }

  public void setLast(Integer last) {
    this.last = last;
  }

  public Integer getOffset() {
    return this.offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public String getAfter() {
    return this.after;
  }

  public void setAfter(String after) {
    this.after = after;
  }

  public String getBefore() {
    return this.before;
  }

  public void setBefore(String before) {
    this.before = before;
  }
}
