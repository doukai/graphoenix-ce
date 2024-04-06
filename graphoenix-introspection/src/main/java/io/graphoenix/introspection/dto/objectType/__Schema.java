package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
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

  private Boolean isDeprecated;

  private Integer version;

  private Integer realmId;

  private String createUserId;

  private LocalDateTime createTime;

  private String updateUserId;

  private LocalDateTime updateTime;

  private String createGroupId;

  private String __typename;

  private String queryTypeName;

  private String mutationTypeName;

  private String subscriptionTypeName;

  private __Type typesAggregate;

  private __TypeConnection typesConnection;

  private __Directive directivesAggregate;

  private __DirectiveConnection directivesConnection;

  private Integer idCount;

  private Integer idMax;

  private Integer idMin;

  private Integer queryTypeNameCount;

  private String queryTypeNameMax;

  private String queryTypeNameMin;

  private Integer mutationTypeNameCount;

  private String mutationTypeNameMax;

  private String mutationTypeNameMin;

  private Integer subscriptionTypeNameCount;

  private String subscriptionTypeNameMax;

  private String subscriptionTypeNameMin;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
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

  public Integer getQueryTypeNameCount() {
    return this.queryTypeNameCount;
  }

  public void setQueryTypeNameCount(Integer queryTypeNameCount) {
    this.queryTypeNameCount = queryTypeNameCount;
  }

  public String getQueryTypeNameMax() {
    return this.queryTypeNameMax;
  }

  public void setQueryTypeNameMax(String queryTypeNameMax) {
    this.queryTypeNameMax = queryTypeNameMax;
  }

  public String getQueryTypeNameMin() {
    return this.queryTypeNameMin;
  }

  public void setQueryTypeNameMin(String queryTypeNameMin) {
    this.queryTypeNameMin = queryTypeNameMin;
  }

  public Integer getMutationTypeNameCount() {
    return this.mutationTypeNameCount;
  }

  public void setMutationTypeNameCount(Integer mutationTypeNameCount) {
    this.mutationTypeNameCount = mutationTypeNameCount;
  }

  public String getMutationTypeNameMax() {
    return this.mutationTypeNameMax;
  }

  public void setMutationTypeNameMax(String mutationTypeNameMax) {
    this.mutationTypeNameMax = mutationTypeNameMax;
  }

  public String getMutationTypeNameMin() {
    return this.mutationTypeNameMin;
  }

  public void setMutationTypeNameMin(String mutationTypeNameMin) {
    this.mutationTypeNameMin = mutationTypeNameMin;
  }

  public Integer getSubscriptionTypeNameCount() {
    return this.subscriptionTypeNameCount;
  }

  public void setSubscriptionTypeNameCount(Integer subscriptionTypeNameCount) {
    this.subscriptionTypeNameCount = subscriptionTypeNameCount;
  }

  public String getSubscriptionTypeNameMax() {
    return this.subscriptionTypeNameMax;
  }

  public void setSubscriptionTypeNameMax(String subscriptionTypeNameMax) {
    this.subscriptionTypeNameMax = subscriptionTypeNameMax;
  }

  public String getSubscriptionTypeNameMin() {
    return this.subscriptionTypeNameMin;
  }

  public void setSubscriptionTypeNameMin(String subscriptionTypeNameMin) {
    this.subscriptionTypeNameMin = subscriptionTypeNameMin;
  }
}
