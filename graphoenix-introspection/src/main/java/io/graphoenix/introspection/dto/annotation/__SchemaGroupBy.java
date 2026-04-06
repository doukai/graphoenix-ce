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
 * Group Input for __Schema
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Group Input for __Schema")
public @interface __SchemaGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  String[] fieldNames() default {};

  /**
   * types
   */
  @Description("types")
  __TypeGroupBy1 types() default @__TypeGroupBy1;

  /**
   * queryType
   */
  @Description("queryType")
  __TypeGroupBy1 queryType() default @__TypeGroupBy1;

  /**
   * mutationType
   */
  @Description("mutationType")
  __TypeGroupBy1 mutationType() default @__TypeGroupBy1;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  __TypeGroupBy1 subscriptionType() default @__TypeGroupBy1;

  /**
   * directives
   */
  @Description("directives")
  __DirectiveGroupBy1 directives() default @__DirectiveGroupBy1;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __SchemaGroupBy1[] gbs() default {};

  String $fieldNames() default "";

  String $types() default "";

  String $queryType() default "";

  String $mutationType() default "";

  String $subscriptionType() default "";

  String $directives() default "";

  String $gbs() default "";
}
