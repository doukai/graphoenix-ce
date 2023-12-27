package io.graphoenix.showcase.user.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class UserUserProfileRelation implements Meta {
  @Id
  private String id;

  private String userRef;

  private User user;

  private String userProfileRef;

  private UserProfile userProfile;

  private Boolean isDeprecated;

  private Integer version;

  private Integer realmId;

  private String createUserId;

  private LocalDateTime createTime;

  private String updateUserId;

  private LocalDateTime updateTime;

  private String createGroupId;

  private String __typename;

  private Integer idCount;

  private Integer idMax;

  private Integer idMin;

  private Integer userRefCount;

  private String userRefMax;

  private String userRefMin;

  private Integer userProfileRefCount;

  private String userProfileRefMax;

  private String userProfileRefMin;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserRef() {
    return this.userRef;
  }

  public void setUserRef(String userRef) {
    this.userRef = userRef;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getUserProfileRef() {
    return this.userProfileRef;
  }

  public void setUserProfileRef(String userProfileRef) {
    this.userProfileRef = userProfileRef;
  }

  public UserProfile getUserProfile() {
    return this.userProfile;
  }

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
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

  public Integer getUserRefCount() {
    return this.userRefCount;
  }

  public void setUserRefCount(Integer userRefCount) {
    this.userRefCount = userRefCount;
  }

  public String getUserRefMax() {
    return this.userRefMax;
  }

  public void setUserRefMax(String userRefMax) {
    this.userRefMax = userRefMax;
  }

  public String getUserRefMin() {
    return this.userRefMin;
  }

  public void setUserRefMin(String userRefMin) {
    this.userRefMin = userRefMin;
  }

  public Integer getUserProfileRefCount() {
    return this.userProfileRefCount;
  }

  public void setUserProfileRefCount(Integer userProfileRefCount) {
    this.userProfileRefCount = userProfileRefCount;
  }

  public String getUserProfileRefMax() {
    return this.userProfileRefMax;
  }

  public void setUserProfileRefMax(String userProfileRefMax) {
    this.userProfileRefMax = userProfileRefMax;
  }

  public String getUserProfileRefMin() {
    return this.userProfileRefMin;
  }

  public void setUserProfileRefMin(String userProfileRefMin) {
    this.userProfileRefMin = userProfileRefMin;
  }
}
