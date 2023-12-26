package io.graphoenix.core.dto.annotation;

import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.core.dto.enumType.Protocol;
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
public @interface ProtocolExpression2 {
  Operator opr() default Operator.EQ;

  Protocol val() default Protocol.LOCAL;

  Protocol[] arr() default {};
}
