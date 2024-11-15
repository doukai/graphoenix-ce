package io.graphoenix.core.dto.annotation;

import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.core.dto.enumType.__DirectiveLocation;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Query Expression Input for __DirectiveLocation
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query Expression Input for __DirectiveLocation")
public @interface __DirectiveLocationExpression {
  /**
   * Operators
   */
  @Description("Operators")
  Operator opr() default Operator.EQ;

  /**
   * Value
   */
  @Description("Value")
  __DirectiveLocation val() default __DirectiveLocation.QUERY;

  /**
   * Array
   */
  @Description("Array")
  __DirectiveLocation[] arr() default {};

  String $opr() default "";

  String $val() default "";

  String $arr() default "";
}
