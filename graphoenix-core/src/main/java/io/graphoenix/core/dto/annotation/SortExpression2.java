package io.graphoenix.core.dto.annotation;

import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.core.dto.enumType.Sort;
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
public @interface SortExpression2 {
  Operator opr() default Operator.EQ;

  Sort val() default Sort.ASC;

  Sort[] arr() default {};
}
