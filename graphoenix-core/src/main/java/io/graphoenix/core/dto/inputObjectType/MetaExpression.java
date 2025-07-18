package io.graphoenix.core.dto.inputObjectType;

import jakarta.annotation.Generated;
import org.eclipse.microprofile.graphql.Input;

@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public interface MetaExpression {
  StringExpression id = null;

  IntExpression realmId = null;

  StringExpression createUserId = null;

  StringExpression createTime = null;

  StringExpression updateUserId = null;

  StringExpression updateTime = null;

  StringExpression createGroupId = null;

  default StringExpression getId() {
    return id;
  }

  void setId(StringExpression id);

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
}
