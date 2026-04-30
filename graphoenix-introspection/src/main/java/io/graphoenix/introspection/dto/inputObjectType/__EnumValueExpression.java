package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
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
 * Query Expression Input for __EnumValue
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Query Expression Input for __EnumValue")
public class __EnumValueExpression implements MetaExpression {
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
   * ofType
   */
  @Description("ofType")
  private __TypeExpression ofType;

  /**
   * description
   */
  @Description("description")
  private StringExpression description;

  /**
   * deprecationReason
   */
  @Description("deprecationReason")
  private StringExpression deprecationReason;

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
   * ofType Reference
   */
  @Description("ofType Reference")
  private StringExpression ofTypeName;

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
  private Collection<__EnumValueExpression> exs;

  /**
   * Order By
   */
  @Description("Order By")
  private __EnumValueOrderBy orderBy;

  /**
   * Group By
   */
  @Description("Group By")
  private __EnumValueGroupBy groupBy;

  /**
   * First
   */
  @Description("First")
  private Integer first;

  /**
   * Last
   */
  @Description("Last")
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
  public StringExpression getId() {
    return this.id;
  }

  @Override
  public void setId(StringExpression id) {
    this.id = (StringExpression)id;
  }

  public StringExpression getName() {
    return this.name;
  }

  public void setName(StringExpression name) {
    this.name = name;
  }

  public __TypeExpression getOfType() {
    return this.ofType;
  }

  public void setOfType(__TypeExpression ofType) {
    this.ofType = ofType;
  }

  public StringExpression getDescription() {
    return this.description;
  }

  public void setDescription(StringExpression description) {
    this.description = description;
  }

  public StringExpression getDeprecationReason() {
    return this.deprecationReason;
  }

  public void setDeprecationReason(StringExpression deprecationReason) {
    this.deprecationReason = deprecationReason;
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

  public StringExpression getOfTypeName() {
    return this.ofTypeName;
  }

  public void setOfTypeName(StringExpression ofTypeName) {
    this.ofTypeName = ofTypeName;
  }

  public IntExpression getCreateTimeYear() {
    return this.createTimeYear;
  }

  public void setCreateTimeYear(IntExpression createTimeYear) {
    this.createTimeYear = createTimeYear;
  }

  public IntExpression getCreateTimeMonth() {
    return this.createTimeMonth;
  }

  public void setCreateTimeMonth(IntExpression createTimeMonth) {
    this.createTimeMonth = createTimeMonth;
  }

  public IntExpression getCreateTimeDay() {
    return this.createTimeDay;
  }

  public void setCreateTimeDay(IntExpression createTimeDay) {
    this.createTimeDay = createTimeDay;
  }

  public IntExpression getCreateTimeWeek() {
    return this.createTimeWeek;
  }

  public void setCreateTimeWeek(IntExpression createTimeWeek) {
    this.createTimeWeek = createTimeWeek;
  }

  public IntExpression getCreateTimeQuarter() {
    return this.createTimeQuarter;
  }

  public void setCreateTimeQuarter(IntExpression createTimeQuarter) {
    this.createTimeQuarter = createTimeQuarter;
  }

  public IntExpression getUpdateTimeYear() {
    return this.updateTimeYear;
  }

  public void setUpdateTimeYear(IntExpression updateTimeYear) {
    this.updateTimeYear = updateTimeYear;
  }

  public IntExpression getUpdateTimeMonth() {
    return this.updateTimeMonth;
  }

  public void setUpdateTimeMonth(IntExpression updateTimeMonth) {
    this.updateTimeMonth = updateTimeMonth;
  }

  public IntExpression getUpdateTimeDay() {
    return this.updateTimeDay;
  }

  public void setUpdateTimeDay(IntExpression updateTimeDay) {
    this.updateTimeDay = updateTimeDay;
  }

  public IntExpression getUpdateTimeWeek() {
    return this.updateTimeWeek;
  }

  public void setUpdateTimeWeek(IntExpression updateTimeWeek) {
    this.updateTimeWeek = updateTimeWeek;
  }

  public IntExpression getUpdateTimeQuarter() {
    return this.updateTimeQuarter;
  }

  public void setUpdateTimeQuarter(IntExpression updateTimeQuarter) {
    this.updateTimeQuarter = updateTimeQuarter;
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

  public Collection<__EnumValueExpression> getExs() {
    return this.exs;
  }

  public void setExs(Collection<__EnumValueExpression> exs) {
    this.exs = exs;
  }

  public __EnumValueOrderBy getOrderBy() {
    return this.orderBy;
  }

  public void setOrderBy(__EnumValueOrderBy orderBy) {
    this.orderBy = orderBy;
  }

  public __EnumValueGroupBy getGroupBy() {
    return this.groupBy;
  }

  public void setGroupBy(__EnumValueGroupBy groupBy) {
    this.groupBy = groupBy;
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
