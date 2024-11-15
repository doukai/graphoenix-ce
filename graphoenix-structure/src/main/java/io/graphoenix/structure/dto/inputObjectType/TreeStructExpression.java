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
 * Query Expression Input for TreeStruct
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for TreeStruct")
public interface TreeStructExpression extends MetaExpression {
  /**
   * name
   */
  @Description("name")
  StringExpression name = null;

  /**
   * path
   */
  @Description("path")
  StringExpression path = null;

  /**
   * deep
   */
  @Description("deep")
  IntExpression deep = null;

  /**
   * parentId
   */
  @Description("parentId")
  StringExpression parentId = null;

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

  StringExpression getName();

  void setName(StringExpression name);

  StringExpression getPath();

  void setPath(StringExpression path);

  IntExpression getDeep();

  void setDeep(IntExpression deep);

  StringExpression getParentId();

  void setParentId(StringExpression parentId);

  Boolean getIncludeDeprecated();

  void setIncludeDeprecated(Boolean includeDeprecated);

  IntExpression getVersion();

  void setVersion(IntExpression version);

  IntExpression getRealmId();

  void setRealmId(IntExpression realmId);

  StringExpression getCreateUserId();

  void setCreateUserId(StringExpression createUserId);

  StringExpression getCreateTime();

  void setCreateTime(StringExpression createTime);

  StringExpression getUpdateUserId();

  void setUpdateUserId(StringExpression updateUserId);

  StringExpression getUpdateTime();

  void setUpdateTime(StringExpression updateTime);

  StringExpression getCreateGroupId();

  void setCreateGroupId(StringExpression createGroupId);

  Boolean getNot();

  void setNot(Boolean not);

  Conditional getCond();

  void setCond(Conditional cond);
}
