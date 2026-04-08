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
 * Group Input for __Directive
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Group Input for __Directive")
public @interface __DirectiveGroupBy1 {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  String[] by() default {};

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaGroupBy2 ofSchema() default @__SchemaGroupBy2;

  /**
   * args
   */
  @Description("args")
  __InputValueGroupBy2 args() default @__InputValueGroupBy2;

  /**
   * Relationship Object between __Directive and locations
   */
  @Description("Relationship Object between __Directive and locations")
  __DirectiveLocationsRelationGroupBy2 __directiveLocationsRelation(
      ) default @__DirectiveLocationsRelationGroupBy2;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __DirectiveGroupBy2[] gbs() default {};

  String $by() default "";

  String $ofSchema() default "";

  String $args() default "";

  String $__directiveLocationsRelation() default "";

  String $gbs() default "";
}
