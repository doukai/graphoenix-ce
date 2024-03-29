package io.graphoenix.core.dto.annotation;

import io.graphoenix.core.dto.enumType.Func;
import io.graphoenix.core.dto.enumType.Operator;
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
public @interface FuncExpression2 {
  Operator opr() default Operator.EQ;

  Func val() default Func.COUNT;

  Func[] arr() default {};

  String $opr() default "";

  String $val() default "";

  String $arr() default "";
}
