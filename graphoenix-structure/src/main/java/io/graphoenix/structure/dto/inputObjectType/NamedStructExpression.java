package io.graphoenix.structure.dto.inputObjectType;

import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.inputObjectType.IntExpression;
import io.graphoenix.core.dto.inputObjectType.MetaExpression;
import io.graphoenix.core.dto.inputObjectType.StringExpression;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import org.eclipse.microprofile.graphql.Input;

@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public interface NamedStructExpression extends MetaExpression {
  StringExpression name = null;

  StringExpression description = null;

  Boolean includeDeprecated = null;

  IntExpression version = null;

  IntExpression realmId = null;

  StringExpression createUserId = null;

  StringExpression createTime = null;

  StringExpression updateUserId = null;

  StringExpression updateTime = null;

  StringExpression createGroupId = null;

  Boolean not = null;

  Conditional cond = null;

  StringExpression getName();

  void setName(StringExpression name);

  StringExpression getDescription();

  void setDescription(StringExpression description);

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
