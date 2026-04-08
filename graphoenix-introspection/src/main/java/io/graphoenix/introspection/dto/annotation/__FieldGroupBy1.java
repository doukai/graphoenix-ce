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
 * Group Input for __Field
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Group Input for __Field")
public @interface __FieldGroupBy1 {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  String[] by() default {};

  /**
   * ofType
   */
  @Description("ofType")
  __TypeGroupBy2 ofType() default @__TypeGroupBy2;

  /**
   * args
   */
  @Description("args")
  __InputValueGroupBy2 args() default @__InputValueGroupBy2;

  /**
   * type
   */
  @Description("type")
  __TypeGroupBy2 type() default @__TypeGroupBy2;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __FieldGroupBy2[] gbs() default {};

  String $by() default "";

  String $ofType() default "";

  String $args() default "";

  String $type() default "";

  String $gbs() default "";
}
