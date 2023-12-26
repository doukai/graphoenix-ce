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
public @interface MetaExpression {
  boolean includeDeprecated() default false;

  IntExpression1 version();

  IntExpression1 realmId();

  StringExpression1 createUserId();

  StringExpression1 createTime();

  StringExpression1 updateUserId();

  StringExpression1 updateTime();

  StringExpression1 createGroupId();

  boolean not() default false;

  Conditional cond() default AND;

  MetaExpression1[] exs();
}
