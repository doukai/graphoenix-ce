package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import io.graphoenix.introspection.dto.inputObjectType.__SchemaInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
public class __Schema implements Meta {
  @Id
  private String id;

  @NonNull
  private Collection<__Type> types;

  @NonNull
  private __Type queryType;

  private __Type mutationType;

  private __Type subscriptionType;

  @NonNull
  private Collection<__Directive> directives;

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
  private String __typename = "__Schema";

  /**
   * queryType Reference
   */
  @Description("queryType Reference")
  private String queryTypeName;

  /**
   * mutationType Reference
   */
  @Description("mutationType Reference")
  private String mutationTypeName;

  /**
   * subscriptionType Reference
   */
  @Description("subscriptionType Reference")
  private String subscriptionTypeName;

  /**
   * Aggregate Field for types
   */
  @Description("Aggregate Field for types")
  private __Type typesAggregate;

  /**
   * Connection Field for types
   */
  @Description("Connection Field for types")
  private __TypeConnection typesConnection;

  /**
   * Aggregate Field for directives
   */
  @Description("Aggregate Field for directives")
  private __Directive directivesAggregate;

  /**
   * Connection Field for directives
   */
  @Description("Connection Field for directives")
  private __DirectiveConnection directivesConnection;

  /**
   * Count of __Schema
   */
  @Description("Count of __Schema")
  private Integer idCount;

  /**
   * Count of queryType Reference
   */
  @Description("Count of queryType Reference")
  private Integer queryTypeNameCount;

  /**
   * Count of mutationType Reference
   */
  @Description("Count of mutationType Reference")
  private Integer mutationTypeNameCount;

  /**
   * Count of subscriptionType Reference
   */
  @Description("Count of subscriptionType Reference")
  private Integer subscriptionTypeNameCount;

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

  public Collection<__Type> getTypes() {
    return this.types;
  }

  public void setTypes(Collection<__Type> types) {
    this.types = types;
  }

  public __Type getQueryType() {
    return this.queryType;
  }

  public void setQueryType(__Type queryType) {
    this.queryType = queryType;
  }

  public __Type getMutationType() {
    return this.mutationType;
  }

  public void setMutationType(__Type mutationType) {
    this.mutationType = mutationType;
  }

  public __Type getSubscriptionType() {
    return this.subscriptionType;
  }

  public void setSubscriptionType(__Type subscriptionType) {
    this.subscriptionType = subscriptionType;
  }

  public Collection<__Directive> getDirectives() {
    return this.directives;
  }

  public void setDirectives(Collection<__Directive> directives) {
    this.directives = directives;
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

  public String getQueryTypeName() {
    return this.queryTypeName;
  }

  public void setQueryTypeName(String queryTypeName) {
    this.queryTypeName = queryTypeName;
  }

  public String getMutationTypeName() {
    return this.mutationTypeName;
  }

  public void setMutationTypeName(String mutationTypeName) {
    this.mutationTypeName = mutationTypeName;
  }

  public String getSubscriptionTypeName() {
    return this.subscriptionTypeName;
  }

  public void setSubscriptionTypeName(String subscriptionTypeName) {
    this.subscriptionTypeName = subscriptionTypeName;
  }

  public __Type getTypesAggregate() {
    return this.typesAggregate;
  }

  public void setTypesAggregate(__Type typesAggregate) {
    this.typesAggregate = typesAggregate;
  }

  public __TypeConnection getTypesConnection() {
    return this.typesConnection;
  }

  public void setTypesConnection(__TypeConnection typesConnection) {
    this.typesConnection = typesConnection;
  }

  public __Directive getDirectivesAggregate() {
    return this.directivesAggregate;
  }

  public void setDirectivesAggregate(__Directive directivesAggregate) {
    this.directivesAggregate = directivesAggregate;
  }

  public __DirectiveConnection getDirectivesConnection() {
    return this.directivesConnection;
  }

  public void setDirectivesConnection(__DirectiveConnection directivesConnection) {
    this.directivesConnection = directivesConnection;
  }

  public Integer getIdCount() {
    return this.idCount;
  }

  public void setIdCount(Integer idCount) {
    this.idCount = idCount;
  }

  public Integer getQueryTypeNameCount() {
    return this.queryTypeNameCount;
  }

  public void setQueryTypeNameCount(Integer queryTypeNameCount) {
    this.queryTypeNameCount = queryTypeNameCount;
  }

  public Integer getMutationTypeNameCount() {
    return this.mutationTypeNameCount;
  }

  public void setMutationTypeNameCount(Integer mutationTypeNameCount) {
    this.mutationTypeNameCount = mutationTypeNameCount;
  }

  public Integer getSubscriptionTypeNameCount() {
    return this.subscriptionTypeNameCount;
  }

  public void setSubscriptionTypeNameCount(Integer subscriptionTypeNameCount) {
    this.subscriptionTypeNameCount = subscriptionTypeNameCount;
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

  public __SchemaInput toInput() {
    __SchemaInput input = new __SchemaInput();
    input.setId(this.getId());
    if(getTypes() != null) {
      input.setTypes(this.getTypes().stream().map(item -> item.toInput()).collect(Collectors.toList()));
    }
    if(getQueryType() != null) {
      input.setQueryType(this.getQueryType().toInput());
    }
    if(getMutationType() != null) {
      input.setMutationType(this.getMutationType().toInput());
    }
    if(getSubscriptionType() != null) {
      input.setSubscriptionType(this.getSubscriptionType().toInput());
    }
    if(getDirectives() != null) {
      input.setDirectives(this.getDirectives().stream().map(item -> item.toInput()).collect(Collectors.toList()));
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
    input.setQueryTypeName(this.getQueryTypeName());
    input.setMutationTypeName(this.getMutationTypeName());
    input.setSubscriptionTypeName(this.getSubscriptionTypeName());
    return input;
  }
}
