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
 * Group Input for Relationship Object between __Type and __Type
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Group Input for Relationship Object between __Type and __Type")
public @interface __TypePossibleTypesGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  String[] by() default {};

  /**
   * __Type
   */
  @Description("__Type")
  __TypeGroupBy1 type() default @__TypeGroupBy1;

  /**
   * __Type
   */
  @Description("__Type")
  __TypeGroupBy1 possibleType() default @__TypeGroupBy1;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __TypePossibleTypesGroupBy1[] gbs() default {};

  String $by() default "";

  String $type() default "";

  String $possibleType() default "";

  String $gbs() default "";
}
