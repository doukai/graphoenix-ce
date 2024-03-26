package io.graphoenix.introspection.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Query {
  __SchemaQueryArguments __schema() default @__SchemaQueryArguments;

  __SchemaListQueryArguments __schemaList() default @__SchemaListQueryArguments;

  __SchemaConnectionQueryArguments __schemaConnection() default @__SchemaConnectionQueryArguments;

  __TypeQueryArguments __type() default @__TypeQueryArguments;

  __TypeListQueryArguments __typeList() default @__TypeListQueryArguments;

  __TypeConnectionQueryArguments __typeConnection() default @__TypeConnectionQueryArguments;

  __FieldQueryArguments __field() default @__FieldQueryArguments;

  __FieldListQueryArguments __fieldList() default @__FieldListQueryArguments;

  __FieldConnectionQueryArguments __fieldConnection() default @__FieldConnectionQueryArguments;

  __InputValueQueryArguments __inputValue() default @__InputValueQueryArguments;

  __InputValueListQueryArguments __inputValueList() default @__InputValueListQueryArguments;

  __InputValueConnectionQueryArguments __inputValueConnection(
      ) default @__InputValueConnectionQueryArguments;

  __EnumValueQueryArguments __enumValue() default @__EnumValueQueryArguments;

  __EnumValueListQueryArguments __enumValueList() default @__EnumValueListQueryArguments;

  __EnumValueConnectionQueryArguments __enumValueConnection(
      ) default @__EnumValueConnectionQueryArguments;

  __DirectiveQueryArguments __directive() default @__DirectiveQueryArguments;

  __DirectiveListQueryArguments __directiveList() default @__DirectiveListQueryArguments;

  __DirectiveConnectionQueryArguments __directiveConnection(
      ) default @__DirectiveConnectionQueryArguments;

  __TypeInterfacesQueryArguments __typeInterfaces() default @__TypeInterfacesQueryArguments;

  __TypeInterfacesListQueryArguments __typeInterfacesList(
      ) default @__TypeInterfacesListQueryArguments;

  __TypeInterfacesConnectionQueryArguments __typeInterfacesConnection(
      ) default @__TypeInterfacesConnectionQueryArguments;

  __TypePossibleTypesQueryArguments __typePossibleTypes(
      ) default @__TypePossibleTypesQueryArguments;

  __TypePossibleTypesListQueryArguments __typePossibleTypesList(
      ) default @__TypePossibleTypesListQueryArguments;

  __TypePossibleTypesConnectionQueryArguments __typePossibleTypesConnection(
      ) default @__TypePossibleTypesConnectionQueryArguments;

  __DirectiveLocationsRelationQueryArguments __directiveLocationsRelation(
      ) default @__DirectiveLocationsRelationQueryArguments;

  __DirectiveLocationsRelationListQueryArguments __directiveLocationsRelationList(
      ) default @__DirectiveLocationsRelationListQueryArguments;

  __DirectiveLocationsRelationConnectionQueryArguments __directiveLocationsRelationConnection(
      ) default @__DirectiveLocationsRelationConnectionQueryArguments;
}
