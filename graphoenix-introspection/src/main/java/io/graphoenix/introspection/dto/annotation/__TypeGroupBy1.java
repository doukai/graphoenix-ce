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
 * Group Input for __Type
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Group Input for __Type")
public @interface __TypeGroupBy1 {
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
   * fields
   */
  @Description("fields")
  __FieldGroupBy2 fields() default @__FieldGroupBy2;

  /**
   * interfaces
   */
  @Description("interfaces")
  __TypeGroupBy2 interfaces() default @__TypeGroupBy2;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  __TypeGroupBy2 possibleTypes() default @__TypeGroupBy2;

  /**
   * enumValues
   */
  @Description("enumValues")
  __EnumValueGroupBy2 enumValues() default @__EnumValueGroupBy2;

  /**
   * inputFields
   */
  @Description("inputFields")
  __InputValueGroupBy2 inputFields() default @__InputValueGroupBy2;

  /**
   * ofType
   */
  @Description("ofType")
  __TypeGroupBy2 ofType() default @__TypeGroupBy2;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypeInterfacesGroupBy2 __typeInterfaces() default @__TypeInterfacesGroupBy2;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypePossibleTypesGroupBy2 __typePossibleTypes() default @__TypePossibleTypesGroupBy2;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __TypeGroupBy2[] gbs() default {};

  String $by() default "";

  String $ofSchema() default "";

  String $fields() default "";

  String $interfaces() default "";

  String $possibleTypes() default "";

  String $enumValues() default "";

  String $inputFields() default "";

  String $ofType() default "";

  String $__typeInterfaces() default "";

  String $__typePossibleTypes() default "";

  String $gbs() default "";
}
