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
 * Group Input for Relationship Object between __Directive and locations
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Group Input for Relationship Object between __Directive and locations")
public @interface __DirectiveLocationsRelationGroupBy1 {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  String[] by() default {};

  /**
   * __Directive
   */
  @Description("__Directive")
  __DirectiveGroupBy2 __directive() default @__DirectiveGroupBy2;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __DirectiveLocationsRelationGroupBy2[] gbs() default {};

  String $by() default "";

  String $__directive() default "";

  String $gbs() default "";
}
