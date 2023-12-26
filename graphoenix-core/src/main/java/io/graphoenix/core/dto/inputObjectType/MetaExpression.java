package io.graphoenix.core.dto.inputObjectType;

import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Input;

@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public interface MetaExpression {
  IntExpression realmId = null;

  StringExpression createUserId = null;

  StringExpression createTime = null;

  StringExpression updateUserId = null;

  StringExpression updateTime = null;

  StringExpression createGroupId = null;

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
}
