package io.graphoenix.structure.dto.inputObjectType;

import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Query Expression Input for NamedStruct
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for NamedStruct")
public interface NamedStructExpression extends MetaExpression {
  /**
   * name
   */
  @Description("name")
  StringExpression name = null;

  /**
   * description
   */
  @Description("description")
  StringExpression description = null;

  /**
   * id
   */
  @Description("id")
  StringExpression id = null;

  /**
   * Include Deprecated
   */
  @Description("Include Deprecated")
  Boolean includeDeprecated = false;

  /**
   * version
   */
  @Description("version")
  IntExpression version = null;

  /**
   * realmId
   */
  @Description("realmId")
  IntExpression realmId = null;

  /**
   * createUserId
   */
  @Description("createUserId")
  StringExpression createUserId = null;

  /**
   * createTime
   */
  @Description("createTime")
  StringExpression createTime = null;

  /**
   * updateUserId
   */
  @Description("updateUserId")
  StringExpression updateUserId = null;

  /**
   * updateTime
   */
  @Description("updateTime")
  StringExpression updateTime = null;

  /**
   * createGroupId
   */
  @Description("createGroupId")
  StringExpression createGroupId = null;

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

  default StringExpression getName() {
    return name;
  }

  void setName(StringExpression name);

  default StringExpression getDescription() {
    return description;
  }

  void setDescription(StringExpression description);

  default StringExpression getId() {
    return id;
  }

  void setId(StringExpression id);

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

  default Boolean getNot() {
    return not;
  }

  void setNot(Boolean not);

  default Conditional getCond() {
    return cond;
  }

  void setCond(Conditional cond);
}
