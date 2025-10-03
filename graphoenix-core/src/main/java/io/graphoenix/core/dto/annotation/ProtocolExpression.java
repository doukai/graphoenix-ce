package io.graphoenix.core.dto.annotation;

import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.core.dto.enumType.Protocol;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Query Expression Input for Protocol
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query Expression Input for Protocol")
public @interface ProtocolExpression {
  /**
   * Operators
   */
  @Description("Operators")
  Operator opr() default Operator.EQ;

  /**
   * Value
   */
  @Description("Value")
  Protocol val() default Protocol.LOCAL;

  /**
   * Array
   */
  @Description("Array")
  Protocol[] arr() default {};

  String $opr() default "";

  String $val() default "";

  String $arr() default "";
}
