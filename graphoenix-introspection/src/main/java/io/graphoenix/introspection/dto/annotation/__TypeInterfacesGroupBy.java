package io.graphoenix.introspection.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Name;

/**
 * Group Input for Relationship Object between __Type and __Type
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Group Input for Relationship Object between __Type and __Type")
public @interface __TypeInterfacesGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  String[] fieldNames() default {};

  /**
   * __Type
   */
  @Description("__Type")
  __TypeGroupBy1 type() default @__TypeGroupBy1;

  /**
   * __Type
   */
  @Name("interface")
  @Description("__Type")
  __TypeGroupBy1 _interface() default @__TypeGroupBy1;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __TypeInterfacesGroupBy1[] gbs() default {};

  String $fieldNames() default "";

  String $type() default "";

  String $interface() default "";

  String $gbs() default "";
}
