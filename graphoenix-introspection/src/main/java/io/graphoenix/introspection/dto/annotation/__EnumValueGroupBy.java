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
 * Group Input for __EnumValue
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Group Input for __EnumValue")
public @interface __EnumValueGroupBy {
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
   * OrderByes
   */
  @Description("OrderByes")
  __EnumValueGroupBy1[] gbs() default {};

  String $fieldNames() default "";

  String $ofType() default "";

  String $gbs() default "";
}
