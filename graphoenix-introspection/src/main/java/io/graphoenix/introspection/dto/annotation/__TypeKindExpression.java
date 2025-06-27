package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.introspection.dto.enumType.__TypeKind;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Query Expression Input for __TypeKind
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query Expression Input for __TypeKind")
public @interface __TypeKindExpression {
  /**
   * Operators
   */
  @Description("Operators")
  Operator opr() default Operator.EQ;

  /**
   * Value
   */
  @Description("Value")
  __TypeKind val() default __TypeKind.SCALAR;

  /**
   * Array
   */
  @Description("Array")
  __TypeKind[] arr() default {};

  String $opr() default "";

  String $val() default "";

  String $arr() default "";
}
