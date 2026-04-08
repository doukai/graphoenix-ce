package io.graphoenix.introspection.dto.inputObjectType;

import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Query Expression Input for __Type
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Query Expression Input for __Type")
public interface __TypeExpressionBase extends MetaExpression {
  /**
   * id
   */
  @Description("id")
  StringExpression id = null;

  /**
   * name
   */
  @Description("name")
  StringExpression name = null;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaExpression ofSchema = null;

  /**
   * kind
   */
  @Description("kind")
  __TypeKindExpression kind = null;

  /**
   * description
   */
  @Description("description")
  StringExpression description = null;

  /**
   * fields
   */
  @Description("fields")
  __FieldExpression fields = null;

  /**
   * interfaces
   */
  @Description("interfaces")
  __TypeExpression interfaces = null;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  __TypeExpression possibleTypes = null;

  /**
   * enumValues
   */
  @Description("enumValues")
  __EnumValueExpression enumValues = null;

  /**
   * inputFields
   */
  @Description("inputFields")
  __InputValueExpression inputFields = null;

  /**
   * ofType
   */
  @Description("ofType")
  __TypeExpression ofType = null;

  /**
   * Include Deprecated
   */
  @Description("Include Deprecated")
  Boolean includeDeprecated = false;

  /**
   * Version
   */
  @Description("Version")
  IntExpression version = null;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  IntExpression realmId = null;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  StringExpression createUserId = null;

  /**
   * Create Time
   */
  @Description("Create Time")
  StringExpression createTime = null;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  StringExpression updateUserId = null;

  /**
   * Update Time
   */
  @Description("Update Time")
  StringExpression updateTime = null;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  StringExpression createGroupId = null;

  /**
   * Type Name
   */
  @Description("Type Name")
  StringExpression __typename = null;

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  IntExpression schemaId = null;

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  StringExpression ofTypeName = null;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypeInterfacesExpression __typeInterfaces = null;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypePossibleTypesExpression __typePossibleTypes = null;

  /**
   * Year of Create Time
   */
  @Description("Year of Create Time")
  IntExpression createTimeYear = null;

  /**
   * Month of Create Time
   */
  @Description("Month of Create Time")
  IntExpression createTimeMonth = null;

  /**
   * Day of Create Time
   */
  @Description("Day of Create Time")
  IntExpression createTimeDay = null;

  /**
   * Week of Create Time
   */
  @Description("Week of Create Time")
  IntExpression createTimeWeek = null;

  /**
   * Quarter of Create Time
   */
  @Description("Quarter of Create Time")
  IntExpression createTimeQuarter = null;

  /**
   * Year of Update Time
   */
  @Description("Year of Update Time")
  IntExpression updateTimeYear = null;

  /**
   * Month of Update Time
   */
  @Description("Month of Update Time")
  IntExpression updateTimeMonth = null;

  /**
   * Day of Update Time
   */
  @Description("Day of Update Time")
  IntExpression updateTimeDay = null;

  /**
   * Week of Update Time
   */
  @Description("Week of Update Time")
  IntExpression updateTimeWeek = null;

  /**
   * Quarter of Update Time
   */
  @Description("Quarter of Update Time")
  IntExpression updateTimeQuarter = null;

  /**
   * Not
   */
  @Description("Not")
  Boolean not = false;

  /**
   * Condition
   */
  @Description("Condition")
  Conditional cond = Conditional.AND;

  /**
   * Expressions
   */
  @Description("Expressions")
  Collection<__TypeExpression> exs = null;

  default StringExpression getId() {
    return id;
  }

  void setId(StringExpression id);

  default StringExpression getName() {
    return name;
  }

  void setName(StringExpression name);

  default __SchemaExpression getOfSchema() {
    return ofSchema;
  }

  void setOfSchema(__SchemaExpression ofSchema);

  default __TypeKindExpression getKind() {
    return kind;
  }

  void setKind(__TypeKindExpression kind);

  default StringExpression getDescription() {
    return description;
  }

  void setDescription(StringExpression description);

  default __FieldExpression getFields() {
    return fields;
  }

  void setFields(__FieldExpression fields);

  default __TypeExpression getInterfaces() {
    return interfaces;
  }

  void setInterfaces(__TypeExpression interfaces);

  default __TypeExpression getPossibleTypes() {
    return possibleTypes;
  }

  void setPossibleTypes(__TypeExpression possibleTypes);

  default __EnumValueExpression getEnumValues() {
    return enumValues;
  }

  void setEnumValues(__EnumValueExpression enumValues);

  default __InputValueExpression getInputFields() {
    return inputFields;
  }

  void setInputFields(__InputValueExpression inputFields);

  default __TypeExpression getOfType() {
    return ofType;
  }

  void setOfType(__TypeExpression ofType);

  default Boolean getIncludeDeprecated() {
    return includeDeprecated;
  }

  void setIncludeDeprecated(Boolean includeDeprecated);

  default IntExpression getVersion() {
    return version;
  }

  void setVersion(IntExpression version);

  default IntExpression getRealmId() {
    return realmId;
  }

  void setRealmId(IntExpression realmId);

  default StringExpression getCreateUserId() {
    return createUserId;
  }

  void setCreateUserId(StringExpression createUserId);

  default StringExpression getCreateTime() {
    return createTime;
  }

  void setCreateTime(StringExpression createTime);

  default StringExpression getUpdateUserId() {
    return updateUserId;
  }

  void setUpdateUserId(StringExpression updateUserId);

  default StringExpression getUpdateTime() {
    return updateTime;
  }

  void setUpdateTime(StringExpression updateTime);

  default StringExpression getCreateGroupId() {
    return createGroupId;
  }

  void setCreateGroupId(StringExpression createGroupId);

  default StringExpression get__typename() {
    return __typename;
  }

  void set__typename(StringExpression __typename);

  default IntExpression getSchemaId() {
    return schemaId;
  }

  void setSchemaId(IntExpression schemaId);

  default StringExpression getOfTypeName() {
    return ofTypeName;
  }

  void setOfTypeName(StringExpression ofTypeName);

  default __TypeInterfacesExpression get__typeInterfaces() {
    return __typeInterfaces;
  }

  void set__typeInterfaces(__TypeInterfacesExpression __typeInterfaces);

  default __TypePossibleTypesExpression get__typePossibleTypes() {
    return __typePossibleTypes;
  }

  void set__typePossibleTypes(__TypePossibleTypesExpression __typePossibleTypes);

  default IntExpression getCreateTimeYear() {
    return createTimeYear;
  }

  void setCreateTimeYear(IntExpression createTimeYear);

  default IntExpression getCreateTimeMonth() {
    return createTimeMonth;
  }

  void setCreateTimeMonth(IntExpression createTimeMonth);

  default IntExpression getCreateTimeDay() {
    return createTimeDay;
  }

  void setCreateTimeDay(IntExpression createTimeDay);

  default IntExpression getCreateTimeWeek() {
    return createTimeWeek;
  }

  void setCreateTimeWeek(IntExpression createTimeWeek);

  default IntExpression getCreateTimeQuarter() {
    return createTimeQuarter;
  }

  void setCreateTimeQuarter(IntExpression createTimeQuarter);

  default IntExpression getUpdateTimeYear() {
    return updateTimeYear;
  }

  void setUpdateTimeYear(IntExpression updateTimeYear);

  default IntExpression getUpdateTimeMonth() {
    return updateTimeMonth;
  }

  void setUpdateTimeMonth(IntExpression updateTimeMonth);

  default IntExpression getUpdateTimeDay() {
    return updateTimeDay;
  }

  void setUpdateTimeDay(IntExpression updateTimeDay);

  default IntExpression getUpdateTimeWeek() {
    return updateTimeWeek;
  }

  void setUpdateTimeWeek(IntExpression updateTimeWeek);

  default IntExpression getUpdateTimeQuarter() {
    return updateTimeQuarter;
  }

  void setUpdateTimeQuarter(IntExpression updateTimeQuarter);

  default Boolean getNot() {
    return not;
  }

  void setNot(Boolean not);

  default Conditional getCond() {
    return cond;
  }

  void setCond(Conditional cond);

  default Collection<__TypeExpression> getExs() {
    return exs;
  }

  void setExs(Collection<__TypeExpression> exs);
}
