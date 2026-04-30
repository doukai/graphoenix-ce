package io.graphoenix.introspection.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Mutation
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation")
public @interface Mutation {
  __SchemaInput __schema() default @__SchemaInput;

  __SchemaInput __schemaList() default @__SchemaInput;

  __TypeInput __type() default @__TypeInput;

  __TypeInput __typeList() default @__TypeInput;

  __FieldInput __field() default @__FieldInput;

  __FieldInput __fieldList() default @__FieldInput;

  __InputValueInput __inputValue() default @__InputValueInput;

  __InputValueInput __inputValueList() default @__InputValueInput;

  __EnumValueInput __enumValue() default @__EnumValueInput;

  __EnumValueInput __enumValueList() default @__EnumValueInput;

  __DirectiveInput __directive() default @__DirectiveInput;

  __DirectiveInput __directiveList() default @__DirectiveInput;

  __TypeInterfacesInput __typeInterfaces() default @__TypeInterfacesInput;

  __TypeInterfacesInput __typeInterfacesList() default @__TypeInterfacesInput;

  __TypePossibleTypesInput __typePossibleTypes() default @__TypePossibleTypesInput;

  __TypePossibleTypesInput __typePossibleTypesList() default @__TypePossibleTypesInput;

  __DirectiveLocationsRelationInput __directiveLocationsRelation(
      ) default @__DirectiveLocationsRelationInput;

  __DirectiveLocationsRelationInput __directiveLocationsRelationList(
      ) default @__DirectiveLocationsRelationInput;
}
