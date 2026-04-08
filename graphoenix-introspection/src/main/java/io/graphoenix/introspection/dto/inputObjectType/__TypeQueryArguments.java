package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Override;
import java.util.Collection;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Query Arguments for __Type
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Query Arguments for __Type")
public class __TypeQueryArguments implements MetaExpression, __TypeExpressionBase {
  /**
   * id
   */
  @Description("id")
  private StringExpression id;

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
   * Year of Create Time
   */
  @Description("Year of Create Time")
  private IntExpression createTimeYear;

  /**
   * Month of Create Time
   */
  @Description("Month of Create Time")
  private IntExpression createTimeMonth;

  /**
   * Day of Create Time
   */
  @Description("Day of Create Time")
  private IntExpression createTimeDay;

  /**
   * Week of Create Time
   */
  @Description("Week of Create Time")
  private IntExpression createTimeWeek;

  /**
   * Quarter of Create Time
   */
  @Description("Quarter of Create Time")
  private IntExpression createTimeQuarter;

  /**
   * Year of Update Time
   */
  @Description("Year of Update Time")
  private IntExpression updateTimeYear;

  /**
   * Month of Update Time
   */
  @Description("Month of Update Time")
  private IntExpression updateTimeMonth;

  /**
   * Day of Update Time
   */
  @Description("Day of Update Time")
  private IntExpression updateTimeDay;

  /**
   * Week of Update Time
   */
  @Description("Week of Update Time")
  private IntExpression updateTimeWeek;

  /**
   * Quarter of Update Time
   */
  @Description("Quarter of Update Time")
  private IntExpression updateTimeQuarter;

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

  @Override
  public StringExpression getId() {
    return this.id;
  }

  @Override
  public void setId(StringExpression id) {
    this.id = (StringExpression)id;
  }

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

  @Override
  public IntExpression getCreateTimeYear() {
    return this.createTimeYear;
  }

  @Override
  public void setCreateTimeYear(IntExpression createTimeYear) {
    this.createTimeYear = (IntExpression)createTimeYear;
  }

  @Override
  public IntExpression getCreateTimeMonth() {
    return this.createTimeMonth;
  }

  @Override
  public void setCreateTimeMonth(IntExpression createTimeMonth) {
    this.createTimeMonth = (IntExpression)createTimeMonth;
  }

  @Override
  public IntExpression getCreateTimeDay() {
    return this.createTimeDay;
  }

  @Override
  public void setCreateTimeDay(IntExpression createTimeDay) {
    this.createTimeDay = (IntExpression)createTimeDay;
  }

  @Override
  public IntExpression getCreateTimeWeek() {
    return this.createTimeWeek;
  }

  @Override
  public void setCreateTimeWeek(IntExpression createTimeWeek) {
    this.createTimeWeek = (IntExpression)createTimeWeek;
  }

  @Override
  public IntExpression getCreateTimeQuarter() {
    return this.createTimeQuarter;
  }

  @Override
  public void setCreateTimeQuarter(IntExpression createTimeQuarter) {
    this.createTimeQuarter = (IntExpression)createTimeQuarter;
  }

  @Override
  public IntExpression getUpdateTimeYear() {
    return this.updateTimeYear;
  }

  @Override
  public void setUpdateTimeYear(IntExpression updateTimeYear) {
    this.updateTimeYear = (IntExpression)updateTimeYear;
  }

  @Override
  public IntExpression getUpdateTimeMonth() {
    return this.updateTimeMonth;
  }

  @Override
  public void setUpdateTimeMonth(IntExpression updateTimeMonth) {
    this.updateTimeMonth = (IntExpression)updateTimeMonth;
  }

  @Override
  public IntExpression getUpdateTimeDay() {
    return this.updateTimeDay;
  }

  @Override
  public void setUpdateTimeDay(IntExpression updateTimeDay) {
    this.updateTimeDay = (IntExpression)updateTimeDay;
  }

  @Override
  public IntExpression getUpdateTimeWeek() {
    return this.updateTimeWeek;
  }

  @Override
  public void setUpdateTimeWeek(IntExpression updateTimeWeek) {
    this.updateTimeWeek = (IntExpression)updateTimeWeek;
  }

  @Override
  public IntExpression getUpdateTimeQuarter() {
    return this.updateTimeQuarter;
  }

  @Override
  public void setUpdateTimeQuarter(IntExpression updateTimeQuarter) {
    this.updateTimeQuarter = (IntExpression)updateTimeQuarter;
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

  @Override
  public Collection<__TypeExpression> getExs() {
    return this.exs;
  }

  @Override
  public void setExs(Collection<__TypeExpression> exs) {
    this.exs = (Collection<__TypeExpression>)exs;
  }
}
