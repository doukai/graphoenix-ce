package io.graphoenix.core.dto.annotation;

import io.graphoenix.core.dto.enumType.Conditional;
import jakarta.annotation.Generated;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface MetaExpression1 {
  boolean includeDeprecated() default false;

  IntExpression2 version();

  IntExpression2 realmId();

  StringExpression2 createUserId();

  StringExpression2 createTime();

  StringExpression2 updateUserId();

  StringExpression2 updateTime();

  StringExpression2 createGroupId();

  boolean not() default false;

  Conditional cond() default AND;

  MetaExpression2[] exs();
}
