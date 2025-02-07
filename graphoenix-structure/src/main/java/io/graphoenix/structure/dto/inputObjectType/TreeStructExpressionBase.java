package io.graphoenix.structure.dto.inputObjectType;

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
 * Query Expression Input for TreeStruct
 */
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query Expression Input for TreeStruct")
public interface TreeStructExpressionBase extends MetaExpression {
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

  /**
   * Expressions
   */
  @Description("Expressions")
  Collection<? extends TreeStructExpression> exs = null;

  default StringExpression getName() {
    return name;
  }

  void setName(StringExpression name);

  default StringExpression getPath() {
    return path;
  }

  void setPath(StringExpression path);

  default IntExpression getDeep() {
    return deep;
  }

  void setDeep(IntExpression deep);

  default StringExpression getParentId() {
    return parentId;
  }

  void setParentId(StringExpression parentId);

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

  default Collection<? extends TreeStructExpression> getExs() {
    return exs;
  }

  void setExs(Collection<? extends TreeStructExpression> exs);
}
