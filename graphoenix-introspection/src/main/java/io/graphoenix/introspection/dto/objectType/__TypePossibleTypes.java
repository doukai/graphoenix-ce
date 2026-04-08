package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import io.graphoenix.introspection.dto.inputObjectType.__TypePossibleTypesInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Type;

/**
 * Relationship Object between __Type and __Type
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Relationship Object between __Type and __Type")
public class __TypePossibleTypes implements Meta {
  /**
   * ID
   */
  @Id
  @Description("ID")
  private String id;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  private String typeRef;

  /**
   * __Type
   */
  @Description("__Type")
  private __Type type;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  private String possibleTypeRef;

  /**
   * __Type
   */
  @Description("__Type")
  private __Type possibleType;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  private Boolean isDeprecated = false;

  /**
   * Version
   */
  @Description("Version")
  private Integer version;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  private Integer realmId;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  private String createUserId;

  /**
   * Create Time
   */
  @Description("Create Time")
  private LocalDateTime createTime;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  private String updateUserId;

  /**
   * Update Time
   */
  @Description("Update Time")
  private LocalDateTime updateTime;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  private String createGroupId;

  /**
   * Type Name
   */
  @Description("Type Name")
  private String __typename = "__TypePossibleTypes";

  /**
   * Count of Relationship Object between __Type and __Type
   */
  @Description("Count of Relationship Object between __Type and __Type")
  private Integer idCount;

  /**
   * Count of __Type Reference
   */
  @Description("Count of __Type Reference")
  private Integer typeRefCount;

  /**
   * Count of __Type Reference
   */
  @Description("Count of __Type Reference")
  private Integer possibleTypeRefCount;

  /**
   * Year of Create Time
   */
  @Description("Year of Create Time")
  private Integer createTimeYear;

  /**
   * Month of Create Time
   */
  @Description("Month of Create Time")
  private Integer createTimeMonth;

  /**
   * Day of Create Time
   */
  @Description("Day of Create Time")
  private Integer createTimeDay;

  /**
   * Week of Create Time
   */
  @Description("Week of Create Time")
  private Integer createTimeWeek;

  /**
   * Quarter of Create Time
   */
  @Description("Quarter of Create Time")
  private Integer createTimeQuarter;

  /**
   * Year of Update Time
   */
  @Description("Year of Update Time")
  private Integer updateTimeYear;

  /**
   * Month of Update Time
   */
  @Description("Month of Update Time")
  private Integer updateTimeMonth;

  /**
   * Day of Update Time
   */
  @Description("Day of Update Time")
  private Integer updateTimeDay;

  /**
   * Week of Update Time
   */
  @Description("Week of Update Time")
  private Integer updateTimeWeek;

  /**
   * Quarter of Update Time
   */
  @Description("Quarter of Update Time")
  private Integer updateTimeQuarter;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = (String)id;
  }

  public String getTypeRef() {
    return this.typeRef;
  }

  public void setTypeRef(String typeRef) {
    this.typeRef = typeRef;
  }

  public __Type getType() {
    return this.type;
  }

  public void setType(__Type type) {
    this.type = type;
  }

  public String getPossibleTypeRef() {
    return this.possibleTypeRef;
  }

  public void setPossibleTypeRef(String possibleTypeRef) {
    this.possibleTypeRef = possibleTypeRef;
  }

  public __Type getPossibleType() {
    return this.possibleType;
  }

  public void setPossibleType(__Type possibleType) {
    this.possibleType = possibleType;
  }

  @Override
  public Boolean getIsDeprecated() {
    return this.isDeprecated;
  }

  @Override
  public void setIsDeprecated(Boolean isDeprecated) {
    this.isDeprecated = (Boolean)isDeprecated;
  }

  @Override
  public Integer getVersion() {
    return this.version;
  }

  @Override
  public void setVersion(Integer version) {
    this.version = (Integer)version;
  }

  @Override
  public Integer getRealmId() {
    return this.realmId;
  }

  @Override
  public void setRealmId(Integer realmId) {
    this.realmId = (Integer)realmId;
  }

  @Override
  public String getCreateUserId() {
    return this.createUserId;
  }

  @Override
  public void setCreateUserId(String createUserId) {
    this.createUserId = (String)createUserId;
  }

  @Override
  public LocalDateTime getCreateTime() {
    return this.createTime;
  }

  @Override
  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = (LocalDateTime)createTime;
  }

  @Override
  public String getUpdateUserId() {
    return this.updateUserId;
  }

  @Override
  public void setUpdateUserId(String updateUserId) {
    this.updateUserId = (String)updateUserId;
  }

  @Override
  public LocalDateTime getUpdateTime() {
    return this.updateTime;
  }

  @Override
  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = (LocalDateTime)updateTime;
  }

  @Override
  public String getCreateGroupId() {
    return this.createGroupId;
  }

  @Override
  public void setCreateGroupId(String createGroupId) {
    this.createGroupId = (String)createGroupId;
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

  public Integer getTypeRefCount() {
    return this.typeRefCount;
  }

  public void setTypeRefCount(Integer typeRefCount) {
    this.typeRefCount = typeRefCount;
  }

  public Integer getPossibleTypeRefCount() {
    return this.possibleTypeRefCount;
  }

  public void setPossibleTypeRefCount(Integer possibleTypeRefCount) {
    this.possibleTypeRefCount = possibleTypeRefCount;
  }

  public Integer getCreateTimeYear() {
    return this.createTimeYear;
  }

  public void setCreateTimeYear(Integer createTimeYear) {
    this.createTimeYear = createTimeYear;
  }

  public Integer getCreateTimeMonth() {
    return this.createTimeMonth;
  }

  public void setCreateTimeMonth(Integer createTimeMonth) {
    this.createTimeMonth = createTimeMonth;
  }

  public Integer getCreateTimeDay() {
    return this.createTimeDay;
  }

  public void setCreateTimeDay(Integer createTimeDay) {
    this.createTimeDay = createTimeDay;
  }

  public Integer getCreateTimeWeek() {
    return this.createTimeWeek;
  }

  public void setCreateTimeWeek(Integer createTimeWeek) {
    this.createTimeWeek = createTimeWeek;
  }

  public Integer getCreateTimeQuarter() {
    return this.createTimeQuarter;
  }

  public void setCreateTimeQuarter(Integer createTimeQuarter) {
    this.createTimeQuarter = createTimeQuarter;
  }

  public Integer getUpdateTimeYear() {
    return this.updateTimeYear;
  }

  public void setUpdateTimeYear(Integer updateTimeYear) {
    this.updateTimeYear = updateTimeYear;
  }

  public Integer getUpdateTimeMonth() {
    return this.updateTimeMonth;
  }

  public void setUpdateTimeMonth(Integer updateTimeMonth) {
    this.updateTimeMonth = updateTimeMonth;
  }

  public Integer getUpdateTimeDay() {
    return this.updateTimeDay;
  }

  public void setUpdateTimeDay(Integer updateTimeDay) {
    this.updateTimeDay = updateTimeDay;
  }

  public Integer getUpdateTimeWeek() {
    return this.updateTimeWeek;
  }

  public void setUpdateTimeWeek(Integer updateTimeWeek) {
    this.updateTimeWeek = updateTimeWeek;
  }

  public Integer getUpdateTimeQuarter() {
    return this.updateTimeQuarter;
  }

  public void setUpdateTimeQuarter(Integer updateTimeQuarter) {
    this.updateTimeQuarter = updateTimeQuarter;
  }

  public __TypePossibleTypesInput toInput() {
    __TypePossibleTypesInput input = new __TypePossibleTypesInput();
    input.setId(this.getId());
    input.setTypeRef(this.getTypeRef());
    if(getType() != null) {
      input.setType(this.getType().toInput());
    }
    input.setPossibleTypeRef(this.getPossibleTypeRef());
    if(getPossibleType() != null) {
      input.setPossibleType(this.getPossibleType().toInput());
    }
    input.setIsDeprecated(this.getIsDeprecated());
    input.setVersion(this.getVersion());
    input.setRealmId(this.getRealmId());
    input.setCreateUserId(this.getCreateUserId());
    input.setCreateTime(this.getCreateTime());
    input.setUpdateUserId(this.getUpdateUserId());
    input.setUpdateTime(this.getUpdateTime());
    input.setCreateGroupId(this.getCreateGroupId());
    input.set__typename(this.get__typename());
    return input;
  }
}
