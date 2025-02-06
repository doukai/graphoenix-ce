package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
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
import org.eclipse.microprofile.graphql.Name;

/**
 * Mutation Arguments for Relationship Object between __Type and __Type List
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation Arguments for Relationship Object between __Type and __Type List")
public class __TypeInterfacesListMutationArguments implements MetaInput, __TypeInterfacesInputBase {
  /**
   * ID
   */
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
  private __TypeInput type;

  /**
   * __Type Reference
   */
  @Description("__Type Reference")
  private String interfaceRef;

  /**
   * __Type
   */
  @Name("interface")
  @Description("__Type")
  private __TypeInput _interface;

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
  @DefaultValue("__TypeInterfaces")
  @Description("Type Name")
  private String __typename = "__TypeInterfaces";

  /**
   * Input List
   */
  @Description("Input List")
  private Collection<__TypeInterfacesInput> list;

  /**
   * Where
   */
  @Description("Where")
  private __TypeInterfacesExpression where;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = (String)id;
  }

  @Override
  public String getTypeRef() {
    return this.typeRef;
  }

  @Override
  public void setTypeRef(String typeRef) {
    this.typeRef = (String)typeRef;
  }

  @Override
  public __TypeInput getType() {
    return this.type;
  }

  @Override
  public void setType(__TypeInput type) {
    this.type = (__TypeInput)type;
  }

  @Override
  public String getInterfaceRef() {
    return this.interfaceRef;
  }

  @Override
  public void setInterfaceRef(String interfaceRef) {
    this.interfaceRef = (String)interfaceRef;
  }

  public __TypeInput get_interface() {
    return this._interface;
  }

  public void set_interface(__TypeInput _interface) {
    this._interface = _interface;
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

  public Collection<__TypeInterfacesInput> getList() {
    return this.list;
  }

  public void setList(Collection<__TypeInterfacesInput> list) {
    this.list = list;
  }

  public __TypeInterfacesExpression getWhere() {
    return this.where;
  }

  public void setWhere(__TypeInterfacesExpression where) {
    this.where = where;
  }
}
