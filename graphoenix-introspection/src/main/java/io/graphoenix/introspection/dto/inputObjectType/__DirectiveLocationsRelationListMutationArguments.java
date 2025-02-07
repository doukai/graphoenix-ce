package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.__DirectiveLocation;
import io.graphoenix.core.dto.inputObjectType.MetaInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Mutation Arguments for Relationship Object between __Directive and locations List
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Arguments for Relationship Object between __Directive and locations List")
public class __DirectiveLocationsRelationListMutationArguments implements MetaInput, __DirectiveLocationsRelationInputBase {
  /**
   * ID
   */
  @Description("ID")
  private String id;

  /**
   * __Directive Reference
   */
  @Description("__Directive Reference")
  private String __directiveRef;

  /**
   * __Directive
   */
  @Description("__Directive")
  private __DirectiveInput __directive;

  /**
   * locations Reference
   */
  @Description("locations Reference")
  private __DirectiveLocation locationsRef;

  /**
   * Is Deprecated
   */
  @DefaultValue("false")
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
  @DefaultValue("__DirectiveLocationsRelation")
  @Description("Type Name")
  private String __typename = "__DirectiveLocationsRelation";

  /**
   * Input List
   */
  @Description("Input List")
  private Collection<__DirectiveLocationsRelationInput> list;

  /**
   * Where
   */
  @Description("Where")
  private __DirectiveLocationsRelationExpression where;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = (String)id;
  }

  @Override
  public String get__directiveRef() {
    return this.__directiveRef;
  }

  @Override
  public void set__directiveRef(String __directiveRef) {
    this.__directiveRef = (String)__directiveRef;
  }

  @Override
  public __DirectiveInput get__directive() {
    return this.__directive;
  }

  @Override
  public void set__directive(__DirectiveInput __directive) {
    this.__directive = (__DirectiveInput)__directive;
  }

  @Override
  public __DirectiveLocation getLocationsRef() {
    return this.locationsRef;
  }

  @Override
  public void setLocationsRef(__DirectiveLocation locationsRef) {
    this.locationsRef = (__DirectiveLocation)locationsRef;
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

  @Override
  public String get__typename() {
    return this.__typename;
  }

  @Override
  public void set__typename(String __typename) {
    this.__typename = (String)__typename;
  }

  public Collection<__DirectiveLocationsRelationInput> getList() {
    return this.list;
  }

  public void setList(Collection<__DirectiveLocationsRelationInput> list) {
    this.list = list;
  }

  @Override
  public __DirectiveLocationsRelationExpression getWhere() {
    return this.where;
  }

  @Override
  public void setWhere(__DirectiveLocationsRelationExpression where) {
    this.where = (__DirectiveLocationsRelationExpression)where;
  }
}
