package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import io.graphoenix.core.dto.inputObjectType.__TypeKindExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Subscription Arguments for __Type List
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Subscription Arguments for __Type List")
public class __TypeListSubscriptionArguments implements MetaExpression, __TypeExpressionBase {
  /**
   * name
   */
  @Description("name")
  private StringExpression name;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  private __SchemaExpression ofSchema;

  /**
   * kind
   */
  @Description("kind")
  private __TypeKindExpression kind;

  /**
   * description
   */
  @Description("description")
  private StringExpression description;

  /**
   * fields
   */
  @Description("fields")
  private __FieldExpression fields;

  /**
   * interfaces
   */
  @Description("interfaces")
  private __TypeExpression interfaces;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  private __TypeExpression possibleTypes;

  /**
   * enumValues
   */
  @Description("enumValues")
  private __EnumValueExpression enumValues;

  /**
   * inputFields
   */
  @Description("inputFields")
  private __InputValueExpression inputFields;

  /**
   * ofType
   */
  @Description("ofType")
  private __TypeExpression ofType;

  /**
   * Include Deprecated
   */
  @DefaultValue("false")
  @Description("Include Deprecated")
  private Boolean includeDeprecated = false;

  /**
   * Version
   */
  @Description("Version")
  private IntExpression version;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  private IntExpression realmId;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  private StringExpression createUserId;

  /**
   * Create Time
   */
  @Description("Create Time")
  private StringExpression createTime;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  private StringExpression updateUserId;

  /**
   * Update Time
   */
  @Description("Update Time")
  private StringExpression updateTime;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  private StringExpression createGroupId;

  /**
   * Type Name
   */
  @Description("Type Name")
  private StringExpression __typename;

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  private IntExpression schemaId;

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  private StringExpression ofTypeName;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  private __TypeInterfacesExpression __typeInterfaces;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  private __TypePossibleTypesExpression __typePossibleTypes;

  /**
   * Order By
   */
  @Description("Order By")
  private __TypeOrderBy orderBy;

  /**
   * Group By
   */
  @Description("Group By")
  private Collection<String> groupBy;

  /**
   * Not
   */
  @DefaultValue("false")
  @Description("Not")
  private Boolean not = false;

  /**
   * Condition
   */
  @DefaultValue("AND")
  @Description("Condition")
  private Conditional cond = Conditional.AND;

  /**
   * Expressions
   */
  @Description("Expressions")
  private Collection<__TypeExpression> exs;

  /**
   * First
   */
  @Description("First")
  private Integer first;

  /**
   * Input List
   */
  @Description("Input List")
  private Integer last;

  /**
   * Offset
   */
  @Description("Offset")
  private Integer offset;

  /**
   * After
   */
  @Description("After")
  private String after;

  /**
   * Before
   */
  @Description("Before")
  private String before;

  @Override
  public StringExpression getName() {
    return this.name;
  }

  @Override
  public void setName(StringExpression name) {
    this.name = (StringExpression)name;
  }

  @Override
  public __SchemaExpression getOfSchema() {
    return this.ofSchema;
  }

  @Override
  public void setOfSchema(__SchemaExpression ofSchema) {
    this.ofSchema = (__SchemaExpression)ofSchema;
  }

  @Override
  public __TypeKindExpression getKind() {
    return this.kind;
  }

  @Override
  public void setKind(__TypeKindExpression kind) {
    this.kind = (__TypeKindExpression)kind;
  }

  @Override
  public StringExpression getDescription() {
    return this.description;
  }

  @Override
  public void setDescription(StringExpression description) {
    this.description = (StringExpression)description;
  }

  @Override
  public __FieldExpression getFields() {
    return this.fields;
  }

  @Override
  public void setFields(__FieldExpression fields) {
    this.fields = (__FieldExpression)fields;
  }

  @Override
  public __TypeExpression getInterfaces() {
    return this.interfaces;
  }

  @Override
  public void setInterfaces(__TypeExpression interfaces) {
    this.interfaces = (__TypeExpression)interfaces;
  }

  @Override
  public __TypeExpression getPossibleTypes() {
    return this.possibleTypes;
  }

  @Override
  public void setPossibleTypes(__TypeExpression possibleTypes) {
    this.possibleTypes = (__TypeExpression)possibleTypes;
  }

  @Override
  public __EnumValueExpression getEnumValues() {
    return this.enumValues;
  }

  @Override
  public void setEnumValues(__EnumValueExpression enumValues) {
    this.enumValues = (__EnumValueExpression)enumValues;
  }

  @Override
  public __InputValueExpression getInputFields() {
    return this.inputFields;
  }

  @Override
  public void setInputFields(__InputValueExpression inputFields) {
    this.inputFields = (__InputValueExpression)inputFields;
  }

  @Override
  public __TypeExpression getOfType() {
    return this.ofType;
  }

  @Override
  public void setOfType(__TypeExpression ofType) {
    this.ofType = (__TypeExpression)ofType;
  }

  @Override
  public Boolean getIncludeDeprecated() {
    return this.includeDeprecated;
  }

  @Override
  public void setIncludeDeprecated(Boolean includeDeprecated) {
    this.includeDeprecated = (Boolean)includeDeprecated;
  }

  @Override
  public IntExpression getVersion() {
    return this.version;
  }

  @Override
  public void setVersion(IntExpression version) {
    this.version = (IntExpression)version;
  }

  @Override
  public IntExpression getRealmId() {
    return this.realmId;
  }

  @Override
  public void setRealmId(IntExpression realmId) {
    this.realmId = (IntExpression)realmId;
  }

  @Override
  public StringExpression getCreateUserId() {
    return this.createUserId;
  }

  @Override
  public void setCreateUserId(StringExpression createUserId) {
    this.createUserId = (StringExpression)createUserId;
  }

  @Override
  public StringExpression getCreateTime() {
    return this.createTime;
  }

  @Override
  public void setCreateTime(StringExpression createTime) {
    this.createTime = (StringExpression)createTime;
  }

  @Override
  public StringExpression getUpdateUserId() {
    return this.updateUserId;
  }

  @Override
  public void setUpdateUserId(StringExpression updateUserId) {
    this.updateUserId = (StringExpression)updateUserId;
  }

  @Override
  public StringExpression getUpdateTime() {
    return this.updateTime;
  }

  @Override
  public void setUpdateTime(StringExpression updateTime) {
    this.updateTime = (StringExpression)updateTime;
  }

  @Override
  public StringExpression getCreateGroupId() {
    return this.createGroupId;
  }

  @Override
  public void setCreateGroupId(StringExpression createGroupId) {
    this.createGroupId = (StringExpression)createGroupId;
  }

  @Override
  public StringExpression get__typename() {
    return this.__typename;
  }

  @Override
  public void set__typename(StringExpression __typename) {
    this.__typename = (StringExpression)__typename;
  }

  @Override
  public IntExpression getSchemaId() {
    return this.schemaId;
  }

  @Override
  public void setSchemaId(IntExpression schemaId) {
    this.schemaId = (IntExpression)schemaId;
  }

  @Override
  public StringExpression getOfTypeName() {
    return this.ofTypeName;
  }

  @Override
  public void setOfTypeName(StringExpression ofTypeName) {
    this.ofTypeName = (StringExpression)ofTypeName;
  }

  @Override
  public __TypeInterfacesExpression get__typeInterfaces() {
    return this.__typeInterfaces;
  }

  @Override
  public void set__typeInterfaces(__TypeInterfacesExpression __typeInterfaces) {
    this.__typeInterfaces = (__TypeInterfacesExpression)__typeInterfaces;
  }

  @Override
  public __TypePossibleTypesExpression get__typePossibleTypes() {
    return this.__typePossibleTypes;
  }

  @Override
  public void set__typePossibleTypes(__TypePossibleTypesExpression __typePossibleTypes) {
    this.__typePossibleTypes = (__TypePossibleTypesExpression)__typePossibleTypes;
  }

  public __TypeOrderBy getOrderBy() {
    return this.orderBy;
  }

  public void setOrderBy(__TypeOrderBy orderBy) {
    this.orderBy = orderBy;
  }

  public Collection<String> getGroupBy() {
    return this.groupBy;
  }

  public void setGroupBy(Collection<String> groupBy) {
    this.groupBy = groupBy;
  }

  @Override
  public Boolean getNot() {
    return this.not;
  }

  @Override
  public void setNot(Boolean not) {
    this.not = (Boolean)not;
  }

  @Override
  public Conditional getCond() {
    return this.cond;
  }

  @Override
  public void setCond(Conditional cond) {
    this.cond = (Conditional)cond;
  }

  public Collection<__TypeExpression> getExs() {
    return this.exs;
  }

  public void setExs(Collection<__TypeExpression> exs) {
    this.exs = exs;
  }

  public Integer getFirst() {
    return this.first;
  }

  public void setFirst(Integer first) {
    this.first = first;
  }

  public Integer getLast() {
    return this.last;
  }

  public void setLast(Integer last) {
    this.last = last;
  }

  public Integer getOffset() {
    return this.offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public String getAfter() {
    return this.after;
  }

  public void setAfter(String after) {
    this.after = after;
  }

  public String getBefore() {
    return this.before;
  }

  public void setBefore(String before) {
    this.before = before;
  }
}
