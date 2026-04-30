package io.graphoenix.introspection.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Query
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query")
public @interface Query {
  __SchemaExpression __schema() default @__SchemaExpression;

  __SchemaExpression __schemaList() default @__SchemaExpression;

  __SchemaExpression __schemaConnection() default @__SchemaExpression;

  __TypeExpression __type() default @__TypeExpression;

  __TypeExpression __typeList() default @__TypeExpression;

  __TypeExpression __typeConnection() default @__TypeExpression;

  __FieldExpression __field() default @__FieldExpression;

  __FieldExpression __fieldList() default @__FieldExpression;

  __FieldExpression __fieldConnection() default @__FieldExpression;

  __InputValueExpression __inputValue() default @__InputValueExpression;

  __InputValueExpression __inputValueList() default @__InputValueExpression;

  __InputValueExpression __inputValueConnection() default @__InputValueExpression;

  __EnumValueExpression __enumValue() default @__EnumValueExpression;

  __EnumValueExpression __enumValueList() default @__EnumValueExpression;

  __EnumValueExpression __enumValueConnection() default @__EnumValueExpression;

  __DirectiveExpression __directive() default @__DirectiveExpression;

  __DirectiveExpression __directiveList() default @__DirectiveExpression;

  __DirectiveExpression __directiveConnection() default @__DirectiveExpression;

  __TypeInterfacesExpression __typeInterfaces() default @__TypeInterfacesExpression;

  __TypeInterfacesExpression __typeInterfacesList() default @__TypeInterfacesExpression;

  __TypeInterfacesExpression __typeInterfacesConnection() default @__TypeInterfacesExpression;

  __TypePossibleTypesExpression __typePossibleTypes() default @__TypePossibleTypesExpression;

  __TypePossibleTypesExpression __typePossibleTypesList() default @__TypePossibleTypesExpression;

  __TypePossibleTypesExpression __typePossibleTypesConnection(
      ) default @__TypePossibleTypesExpression;

  __DirectiveLocationsRelationExpression __directiveLocationsRelation(
      ) default @__DirectiveLocationsRelationExpression;

  __DirectiveLocationsRelationExpression __directiveLocationsRelationList(
      ) default @__DirectiveLocationsRelationExpression;

  __DirectiveLocationsRelationExpression __directiveLocationsRelationConnection(
      ) default @__DirectiveLocationsRelationExpression;
}
