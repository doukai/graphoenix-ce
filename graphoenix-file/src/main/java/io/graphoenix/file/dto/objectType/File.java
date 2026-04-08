package io.graphoenix.file.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import io.graphoenix.file.dto.inputObjectType.FileInput;
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
 * 文件
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("文件")
public class File implements Meta {
  /**
   * ID
   */
  @Id
  @Description("ID")
  private String id;

  /**
   * 文件名
   */
  @Description("文件名")
  private String name;

  /**
   * 类型
   */
  @Description("类型")
  private String contentType;

  /**
   * 内容
   */
  @Description("内容")
  private String content;

  /**
   * URL
   */
  @Description("URL")
  private String url;

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
  private String __typename = "File";

  /**
   * Count of 文件
   */
  @Description("Count of 文件")
  private Integer idCount;

  /**
   * Count of 文件名
   */
  @Description("Count of 文件名")
  private Integer nameCount;

  /**
   * Count of 类型
   */
  @Description("Count of 类型")
  private Integer contentTypeCount;

  /**
   * Count of 内容
   */
  @Description("Count of 内容")
  private Integer contentCount;

  /**
   * Count of URL
   */
  @Description("Count of URL")
  private Integer urlCount;

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

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContentType() {
    return this.contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
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

  public Integer getNameCount() {
    return this.nameCount;
  }

  public void setNameCount(Integer nameCount) {
    this.nameCount = nameCount;
  }

  public Integer getContentTypeCount() {
    return this.contentTypeCount;
  }

  public void setContentTypeCount(Integer contentTypeCount) {
    this.contentTypeCount = contentTypeCount;
  }

  public Integer getContentCount() {
    return this.contentCount;
  }

  public void setContentCount(Integer contentCount) {
    this.contentCount = contentCount;
  }

  public Integer getUrlCount() {
    return this.urlCount;
  }

  public void setUrlCount(Integer urlCount) {
    this.urlCount = urlCount;
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

  public FileInput toInput() {
    FileInput input = new FileInput();
    input.setId(this.getId());
    input.setName(this.getName());
    input.setContentType(this.getContentType());
    input.setContent(this.getContent());
    input.setUrl(this.getUrl());
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
