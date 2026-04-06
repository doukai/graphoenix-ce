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
public @interface __FieldGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  String[] fieldNames() default {};

  /**
   * ofType
   */
  @Description("ofType")
  __TypeGroupBy1 ofType() default @__TypeGroupBy1;

  /**
   * args
   */
  @Description("args")
  __InputValueGroupBy1 args() default @__InputValueGroupBy1;

  /**
   * type
   */
  @Description("type")
  __TypeGroupBy1 type() default @__TypeGroupBy1;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __FieldGroupBy1[] gbs() default {};

  String $fieldNames() default "";

  String $ofType() default "";

  String $args() default "";

  String $type() default "";

  String $gbs() default "";
}
