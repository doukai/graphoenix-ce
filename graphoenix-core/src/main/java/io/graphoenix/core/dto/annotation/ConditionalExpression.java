package io.graphoenix.core.dto.annotation;

import io.graphoenix.core.dto.enumType.Conditional;
import io.graphoenix.core.dto.enumType.Operator;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Query Expression Input for Conditional
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query Expression Input for Conditional")
public @interface ConditionalExpression {
  /**
   * Operators
   */
  @Description("Operators")
  Operator opr() default Operator.EQ;

  /**
   * Value
   */
  @Description("Value")
  Conditional val() default Conditional.AND;

  /**
   * Array
   */
  @Description("Array")
  Conditional[] arr() default {};

  String $opr() default "";

  String $val() default "";

  String $arr() default "";
}
