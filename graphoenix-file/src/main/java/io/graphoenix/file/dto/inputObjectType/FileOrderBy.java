package io.graphoenix.file.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for 文件
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Order Input for 文件")
public class FileOrderBy {
  /**
   * ID
   */
  @Description("ID")
  private Sort id;

  /**
   * 文件名
   */
  @Description("文件名")
  private Sort name;

  /**
   * 类型
   */
  @Description("类型")
  private Sort contentType;

  /**
   * 内容
   */
  @Description("内容")
  private Sort content;

  /**
   * URL
   */
  @Description("URL")
  private Sort url;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  private Sort isDeprecated;

  /**
   * Version
   */
  @Description("Version")
  private Sort version;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  private Sort realmId;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  private Sort createUserId;

  /**
   * Create Time
   */
  @Description("Create Time")
  private Sort createTime;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  private Sort updateUserId;

  /**
   * Update Time
   */
  @Description("Update Time")
  private Sort updateTime;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  private Sort createGroupId;

  /**
   * Type Name
   */
  @Description("Type Name")
  private Sort __typename;

  /**
   * Count of 文件
   */
  @Description("Count of 文件")
  private Sort idCount;

  /**
   * Count of 文件名
   */
  @Description("Count of 文件名")
  private Sort nameCount;

  /**
   * Count of 类型
   */
  @Description("Count of 类型")
  private Sort contentTypeCount;

  /**
   * Count of 内容
   */
  @Description("Count of 内容")
  private Sort contentCount;

  /**
   * Count of URL
   */
  @Description("Count of URL")
  private Sort urlCount;

  /**
   * Year of Create Time
   */
  @Description("Year of Create Time")
  private Sort createTimeYear;

  /**
   * Month of Create Time
   */
  @Description("Month of Create Time")
  private Sort createTimeMonth;

  /**
   * Day of Create Time
   */
  @Description("Day of Create Time")
  private Sort createTimeDay;

  /**
   * Week of Create Time
   */
  @Description("Week of Create Time")
  private Sort createTimeWeek;

  /**
   * Quarter of Create Time
   */
  @Description("Quarter of Create Time")
  private Sort createTimeQuarter;

  /**
   * Year of Update Time
   */
  @Description("Year of Update Time")
  private Sort updateTimeYear;

  /**
   * Month of Update Time
   */
  @Description("Month of Update Time")
  private Sort updateTimeMonth;

  /**
   * Day of Update Time
   */
  @Description("Day of Update Time")
  private Sort updateTimeDay;

  /**
   * Week of Update Time
   */
  @Description("Week of Update Time")
  private Sort updateTimeWeek;

  /**
   * Quarter of Update Time
   */
  @Description("Quarter of Update Time")
  private Sort updateTimeQuarter;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<FileOrderBy> obs;

  public Sort getId() {
    return this.id;
  }

  public void setId(Sort id) {
    this.id = id;
  }

  public Sort getName() {
    return this.name;
  }

  public void setName(Sort name) {
    this.name = name;
  }

  public Sort getContentType() {
    return this.contentType;
  }

  public void setContentType(Sort contentType) {
    this.contentType = contentType;
  }

  public Sort getContent() {
    return this.content;
  }

  public void setContent(Sort content) {
    this.content = content;
  }

  public Sort getUrl() {
    return this.url;
  }

  public void setUrl(Sort url) {
    this.url = url;
  }

  public Sort getIsDeprecated() {
    return this.isDeprecated;
  }

  public void setIsDeprecated(Sort isDeprecated) {
    this.isDeprecated = isDeprecated;
  }

  public Sort getVersion() {
    return this.version;
  }

  public void setVersion(Sort version) {
    this.version = version;
  }

  public Sort getRealmId() {
    return this.realmId;
  }

  public void setRealmId(Sort realmId) {
    this.realmId = realmId;
  }

  public Sort getCreateUserId() {
    return this.createUserId;
  }

  public void setCreateUserId(Sort createUserId) {
    this.createUserId = createUserId;
  }

  public Sort getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Sort createTime) {
    this.createTime = createTime;
  }

  public Sort getUpdateUserId() {
    return this.updateUserId;
  }

  public void setUpdateUserId(Sort updateUserId) {
    this.updateUserId = updateUserId;
  }

  public Sort getUpdateTime() {
    return this.updateTime;
  }

  public void setUpdateTime(Sort updateTime) {
    this.updateTime = updateTime;
  }

  public Sort getCreateGroupId() {
    return this.createGroupId;
  }

  public void setCreateGroupId(Sort createGroupId) {
    this.createGroupId = createGroupId;
  }

  public Sort get__typename() {
    return this.__typename;
  }

  public void set__typename(Sort __typename) {
    this.__typename = __typename;
  }

  public Sort getIdCount() {
    return this.idCount;
  }

  public void setIdCount(Sort idCount) {
    this.idCount = idCount;
  }

  public Sort getNameCount() {
    return this.nameCount;
  }

  public void setNameCount(Sort nameCount) {
    this.nameCount = nameCount;
  }

  public Sort getContentTypeCount() {
    return this.contentTypeCount;
  }

  public void setContentTypeCount(Sort contentTypeCount) {
    this.contentTypeCount = contentTypeCount;
  }

  public Sort getContentCount() {
    return this.contentCount;
  }

  public void setContentCount(Sort contentCount) {
    this.contentCount = contentCount;
  }

  public Sort getUrlCount() {
    return this.urlCount;
  }

  public void setUrlCount(Sort urlCount) {
    this.urlCount = urlCount;
  }

  public Sort getCreateTimeYear() {
    return this.createTimeYear;
  }

  public void setCreateTimeYear(Sort createTimeYear) {
    this.createTimeYear = createTimeYear;
  }

  public Sort getCreateTimeMonth() {
    return this.createTimeMonth;
  }

  public void setCreateTimeMonth(Sort createTimeMonth) {
    this.createTimeMonth = createTimeMonth;
  }

  public Sort getCreateTimeDay() {
    return this.createTimeDay;
  }

  public void setCreateTimeDay(Sort createTimeDay) {
    this.createTimeDay = createTimeDay;
  }

  public Sort getCreateTimeWeek() {
    return this.createTimeWeek;
  }

  public void setCreateTimeWeek(Sort createTimeWeek) {
    this.createTimeWeek = createTimeWeek;
  }

  public Sort getCreateTimeQuarter() {
    return this.createTimeQuarter;
  }

  public void setCreateTimeQuarter(Sort createTimeQuarter) {
    this.createTimeQuarter = createTimeQuarter;
  }

  public Sort getUpdateTimeYear() {
    return this.updateTimeYear;
  }

  public void setUpdateTimeYear(Sort updateTimeYear) {
    this.updateTimeYear = updateTimeYear;
  }

  public Sort getUpdateTimeMonth() {
    return this.updateTimeMonth;
  }

  public void setUpdateTimeMonth(Sort updateTimeMonth) {
    this.updateTimeMonth = updateTimeMonth;
  }

  public Sort getUpdateTimeDay() {
    return this.updateTimeDay;
  }

  public void setUpdateTimeDay(Sort updateTimeDay) {
    this.updateTimeDay = updateTimeDay;
  }

  public Sort getUpdateTimeWeek() {
    return this.updateTimeWeek;
  }

  public void setUpdateTimeWeek(Sort updateTimeWeek) {
    this.updateTimeWeek = updateTimeWeek;
  }

  public Sort getUpdateTimeQuarter() {
    return this.updateTimeQuarter;
  }

  public void setUpdateTimeQuarter(Sort updateTimeQuarter) {
    this.updateTimeQuarter = updateTimeQuarter;
  }

  public Collection<FileOrderBy> getObs() {
    return this.obs;
  }

  public void setObs(Collection<FileOrderBy> obs) {
    this.obs = obs;
  }
}
