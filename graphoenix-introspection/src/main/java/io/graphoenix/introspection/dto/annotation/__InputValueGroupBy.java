package io.graphoenix.introspection.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Group Input for __InputValue
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Group Input for __InputValue")
public @interface __InputValueGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  String[] by() default {};

  /**
   * ofType
   */
  @Description("ofType")
  __TypeGroupBy1 ofType() default @__TypeGroupBy1;

  /**
   * type
   */
  @Description("type")
  __TypeGroupBy1 type() default @__TypeGroupBy1;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __InputValueGroupBy1[] gbs() default {};

  String $by() default "";

  String $ofType() default "";

  String $type() default "";

  String $gbs() default "";
}
