package io.graphoenix.core.dto.annotation;

import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Query Expression Input for Sort
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query Expression Input for Sort")
public @interface SortExpression {
  /**
   * Operators
   */
  @Description("Operators")
  Operator opr() default Operator.EQ;

  /**
   * Value
   */
  @Description("Value")
  Sort val() default Sort.ASC;

  /**
   * Array
   */
  @Description("Array")
  Sort[] arr() default {};

  String $opr() default "";

  String $val() default "";

  String $arr() default "";
}
