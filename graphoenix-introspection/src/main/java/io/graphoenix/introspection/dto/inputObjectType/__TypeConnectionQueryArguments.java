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
import org.eclipse.microprofile.graphql.Input;

@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __TypeConnectionQueryArguments implements MetaExpression {
  private StringExpression name;

  private __SchemaExpression ofSchema;

  private __TypeKindExpression kind;

  private StringExpression description;

  private __FieldExpression fields;

  private __TypeExpression interfaces;

  private __TypeExpression possibleTypes;

  private __EnumValueExpression enumValues;

  private __InputValueExpression inputFields;

  private __TypeExpression ofType;

  @DefaultValue("false")
  private Boolean includeDeprecated = false;

  private IntExpression version;

  private IntExpression realmId;

  private StringExpression createUserId;

  private StringExpression createTime;

  private StringExpression updateUserId;

  private StringExpression updateTime;

  private StringExpression createGroupId;

  private StringExpression __typename;

  private IntExpression schemaId;

  private StringExpression ofTypeName;

  private __TypeInterfacesExpression __typeInterfaces;

  private __TypePossibleTypesExpression __typePossibleTypes;

  private __TypeOrderBy orderBy;

  private Collection<String> groupBy;

  @DefaultValue("false")
  private Boolean not = false;

  @DefaultValue("AND")
  private Conditional cond = Conditional.AND;

  private Collection<__TypeExpression> exs;

  private Integer first;

  private Integer last;

  private Integer offset;

  private String after;

  private String before;

  public StringExpression getName() {
    return this.name;
  }

  public void setName(StringExpression name) {
    this.name = name;
  }

  public __SchemaExpression getOfSchema() {
    return this.ofSchema;
  }

  public void setOfSchema(__SchemaExpression ofSchema) {
    this.ofSchema = ofSchema;
  }

  public __TypeKindExpression getKind() {
    return this.kind;
  }

  public void setKind(__TypeKindExpression kind) {
    this.kind = kind;
  }

  public StringExpression getDescription() {
    return this.description;
  }

  public void setDescription(StringExpression description) {
    this.description = description;
  }

  public __FieldExpression getFields() {
    return this.fields;
  }

  public void setFields(__FieldExpression fields) {
    this.fields = fields;
  }

  public __TypeExpression getInterfaces() {
    return this.interfaces;
  }

  public void setInterfaces(__TypeExpression interfaces) {
    this.interfaces = interfaces;
  }

  public __TypeExpression getPossibleTypes() {
    return this.possibleTypes;
  }

  public void setPossibleTypes(__TypeExpression possibleTypes) {
    this.possibleTypes = possibleTypes;
  }

  public __EnumValueExpression getEnumValues() {
    return this.enumValues;
  }

  public void setEnumValues(__EnumValueExpression enumValues) {
    this.enumValues = enumValues;
  }

  public __InputValueExpression getInputFields() {
    return this.inputFields;
  }

  public void setInputFields(__InputValueExpression inputFields) {
    this.inputFields = inputFields;
  }

  public __TypeExpression getOfType() {
    return this.ofType;
  }

  public void setOfType(__TypeExpression ofType) {
    this.ofType = ofType;
  }

  public Boolean getIncludeDeprecated() {
    return this.includeDeprecated;
  }

  public void setIncludeDeprecated(Boolean includeDeprecated) {
    this.includeDeprecated = includeDeprecated;
  }

  public IntExpression getVersion() {
    return this.version;
  }

  public void setVersion(IntExpression version) {
    this.version = version;
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

  public StringExpression get__typename() {
    return this.__typename;
  }

  public void set__typename(StringExpression __typename) {
    this.__typename = __typename;
  }

  public IntExpression getSchemaId() {
    return this.schemaId;
  }

  public void setSchemaId(IntExpression schemaId) {
    this.schemaId = schemaId;
  }

  public StringExpression getOfTypeName() {
    return this.ofTypeName;
  }

  public void setOfTypeName(StringExpression ofTypeName) {
    this.ofTypeName = ofTypeName;
  }

  public __TypeInterfacesExpression get__typeInterfaces() {
    return this.__typeInterfaces;
  }

  public void set__typeInterfaces(__TypeInterfacesExpression __typeInterfaces) {
    this.__typeInterfaces = __typeInterfaces;
  }

  public __TypePossibleTypesExpression get__typePossibleTypes() {
    return this.__typePossibleTypes;
  }

  public void set__typePossibleTypes(__TypePossibleTypesExpression __typePossibleTypes) {
    this.__typePossibleTypes = __typePossibleTypes;
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

  public Boolean getNot() {
    return this.not;
  }

  public void setNot(Boolean not) {
    this.not = not;
  }

  public Conditional getCond() {
    return this.cond;
  }

  public void setCond(Conditional cond) {
    this.cond = cond;
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
