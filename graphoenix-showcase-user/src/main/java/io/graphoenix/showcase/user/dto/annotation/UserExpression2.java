package io.graphoenix.showcase.user.dto.annotation;

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
public @interface UserExpression2 {
  boolean includeDeprecated() default false;

  boolean not() default false;

  Conditional cond() default Conditional.AND;
}
