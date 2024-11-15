package io.graphoenix.file.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Order Input for 文件
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
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
   * Max of ID
   */
  @Description("Max of ID")
  private Sort idMax;

  /**
   * Min of ID
   */
  @Description("Min of ID")
  private Sort idMin;

  /**
   * Count of 文件名
   */
  @Description("Count of 文件名")
  private Sort nameCount;

  /**
   * Max of 文件名
   */
  @Description("Max of 文件名")
  private Sort nameMax;

  /**
   * Min of 文件名
   */
  @Description("Min of 文件名")
  private Sort nameMin;

  /**
   * Count of 类型
   */
  @Description("Count of 类型")
  private Sort contentTypeCount;

  /**
   * Max of 类型
   */
  @Description("Max of 类型")
  private Sort contentTypeMax;

  /**
   * Min of 类型
   */
  @Description("Min of 类型")
  private Sort contentTypeMin;

  /**
   * Count of 内容
   */
  @Description("Count of 内容")
  private Sort contentCount;

  /**
   * Max of 内容
   */
  @Description("Max of 内容")
  private Sort contentMax;

  /**
   * Min of 内容
   */
  @Description("Min of 内容")
  private Sort contentMin;

  /**
   * Count of URL
   */
  @Description("Count of URL")
  private Sort urlCount;

  /**
   * Max of URL
   */
  @Description("Max of URL")
  private Sort urlMax;

  /**
   * Min of URL
   */
  @Description("Min of URL")
  private Sort urlMin;

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

  public Sort getIdMax() {
    return this.idMax;
  }

  public void setIdMax(Sort idMax) {
    this.idMax = idMax;
  }

  public Sort getIdMin() {
    return this.idMin;
  }

  public void setIdMin(Sort idMin) {
    this.idMin = idMin;
  }

  public Sort getNameCount() {
    return this.nameCount;
  }

  public void setNameCount(Sort nameCount) {
    this.nameCount = nameCount;
  }

  public Sort getNameMax() {
    return this.nameMax;
  }

  public void setNameMax(Sort nameMax) {
    this.nameMax = nameMax;
  }

  public Sort getNameMin() {
    return this.nameMin;
  }

  public void setNameMin(Sort nameMin) {
    this.nameMin = nameMin;
  }

  public Sort getContentTypeCount() {
    return this.contentTypeCount;
  }

  public void setContentTypeCount(Sort contentTypeCount) {
    this.contentTypeCount = contentTypeCount;
  }

  public Sort getContentTypeMax() {
    return this.contentTypeMax;
  }

  public void setContentTypeMax(Sort contentTypeMax) {
    this.contentTypeMax = contentTypeMax;
  }

  public Sort getContentTypeMin() {
    return this.contentTypeMin;
  }

  public void setContentTypeMin(Sort contentTypeMin) {
    this.contentTypeMin = contentTypeMin;
  }

  public Sort getContentCount() {
    return this.contentCount;
  }

  public void setContentCount(Sort contentCount) {
    this.contentCount = contentCount;
  }

  public Sort getContentMax() {
    return this.contentMax;
  }

  public void setContentMax(Sort contentMax) {
    this.contentMax = contentMax;
  }

  public Sort getContentMin() {
    return this.contentMin;
  }

  public void setContentMin(Sort contentMin) {
    this.contentMin = contentMin;
  }

  public Sort getUrlCount() {
    return this.urlCount;
  }

  public void setUrlCount(Sort urlCount) {
    this.urlCount = urlCount;
  }

  public Sort getUrlMax() {
    return this.urlMax;
  }

  public void setUrlMax(Sort urlMax) {
    this.urlMax = urlMax;
  }

  public Sort getUrlMin() {
    return this.urlMin;
  }

  public void setUrlMin(Sort urlMin) {
    this.urlMin = urlMin;
  }
}
