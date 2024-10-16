package io.graphoenix.file.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
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
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
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

  private Boolean isDeprecated = false;

  private Integer version;

  private Integer realmId;

  private String createUserId;

  private LocalDateTime createTime;

  private String updateUserId;

  private LocalDateTime updateTime;

  private String createGroupId;

  private String __typename = "File";

  private Integer idCount;

  private String idMax;

  private String idMin;

  private Integer nameCount;

  private String nameMax;

  private String nameMin;

  private Integer contentTypeCount;

  private String contentTypeMax;

  private String contentTypeMin;

  private Integer contentCount;

  private String contentMax;

  private String contentMin;

  private Integer urlCount;

  private String urlMax;

  private String urlMin;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getIdMax() {
    return this.idMax;
  }

  public void setIdMax(String idMax) {
    this.idMax = idMax;
  }

  public String getIdMin() {
    return this.idMin;
  }

  public void setIdMin(String idMin) {
    this.idMin = idMin;
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

  public Integer getContentTypeCount() {
    return this.contentTypeCount;
  }

  public void setContentTypeCount(Integer contentTypeCount) {
    this.contentTypeCount = contentTypeCount;
  }

  public String getContentTypeMax() {
    return this.contentTypeMax;
  }

  public void setContentTypeMax(String contentTypeMax) {
    this.contentTypeMax = contentTypeMax;
  }

  public String getContentTypeMin() {
    return this.contentTypeMin;
  }

  public void setContentTypeMin(String contentTypeMin) {
    this.contentTypeMin = contentTypeMin;
  }

  public Integer getContentCount() {
    return this.contentCount;
  }

  public void setContentCount(Integer contentCount) {
    this.contentCount = contentCount;
  }

  public String getContentMax() {
    return this.contentMax;
  }

  public void setContentMax(String contentMax) {
    this.contentMax = contentMax;
  }

  public String getContentMin() {
    return this.contentMin;
  }

  public void setContentMin(String contentMin) {
    this.contentMin = contentMin;
  }

  public Integer getUrlCount() {
    return this.urlCount;
  }

  public void setUrlCount(Integer urlCount) {
    this.urlCount = urlCount;
  }

  public String getUrlMax() {
    return this.urlMax;
  }

  public void setUrlMax(String urlMax) {
    this.urlMax = urlMax;
  }

  public String getUrlMin() {
    return this.urlMin;
  }

  public void setUrlMin(String urlMin) {
    this.urlMin = urlMin;
  }
}
