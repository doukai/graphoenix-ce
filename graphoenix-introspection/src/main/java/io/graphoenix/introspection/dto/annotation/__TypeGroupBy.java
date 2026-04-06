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
public @interface __TypeGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  String[] fieldNames() default {};

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaGroupBy1 ofSchema() default @__SchemaGroupBy1;

  /**
   * fields
   */
  @Description("fields")
  __FieldGroupBy1 fields() default @__FieldGroupBy1;

  /**
   * interfaces
   */
  @Description("interfaces")
  __TypeGroupBy1 interfaces() default @__TypeGroupBy1;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  __TypeGroupBy1 possibleTypes() default @__TypeGroupBy1;

  /**
   * enumValues
   */
  @Description("enumValues")
  __EnumValueGroupBy1 enumValues() default @__EnumValueGroupBy1;

  /**
   * inputFields
   */
  @Description("inputFields")
  __InputValueGroupBy1 inputFields() default @__InputValueGroupBy1;

  /**
   * ofType
   */
  @Description("ofType")
  __TypeGroupBy1 ofType() default @__TypeGroupBy1;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypeInterfacesGroupBy1 __typeInterfaces() default @__TypeInterfacesGroupBy1;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypePossibleTypesGroupBy1 __typePossibleTypes() default @__TypePossibleTypesGroupBy1;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __TypeGroupBy1[] gbs() default {};

  String $fieldNames() default "";

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
