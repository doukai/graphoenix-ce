package io.graphoenix.showcase.user.dto.annotation;

import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.showcase.user.dto.enumType.Sex;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface SexExpression1 {
  Operator opr() default Operator.EQ;

  Sex val() default Sex.MAN;

  Sex[] arr() default {};

  String $opr() default "";

  String $val() default "";

  String $arr() default "";
}
