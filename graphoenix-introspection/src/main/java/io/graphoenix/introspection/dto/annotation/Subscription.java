package io.graphoenix.introspection.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Subscription
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Subscription")
public @interface Subscription {
  __SchemaSubscriptionArguments __schema() default @__SchemaSubscriptionArguments;

  __SchemaListSubscriptionArguments __schemaList() default @__SchemaListSubscriptionArguments;

  __SchemaConnectionSubscriptionArguments __schemaConnection(
      ) default @__SchemaConnectionSubscriptionArguments;

  __TypeSubscriptionArguments __type() default @__TypeSubscriptionArguments;

  __TypeListSubscriptionArguments __typeList() default @__TypeListSubscriptionArguments;

  __TypeConnectionSubscriptionArguments __typeConnection(
      ) default @__TypeConnectionSubscriptionArguments;

  __FieldSubscriptionArguments __field() default @__FieldSubscriptionArguments;

  __FieldListSubscriptionArguments __fieldList() default @__FieldListSubscriptionArguments;

  __FieldConnectionSubscriptionArguments __fieldConnection(
      ) default @__FieldConnectionSubscriptionArguments;

  __InputValueSubscriptionArguments __inputValue() default @__InputValueSubscriptionArguments;

  __InputValueListSubscriptionArguments __inputValueList(
      ) default @__InputValueListSubscriptionArguments;

  __InputValueConnectionSubscriptionArguments __inputValueConnection(
      ) default @__InputValueConnectionSubscriptionArguments;

  __EnumValueSubscriptionArguments __enumValue() default @__EnumValueSubscriptionArguments;

  __EnumValueListSubscriptionArguments __enumValueList(
      ) default @__EnumValueListSubscriptionArguments;

  __EnumValueConnectionSubscriptionArguments __enumValueConnection(
      ) default @__EnumValueConnectionSubscriptionArguments;

  __DirectiveSubscriptionArguments __directive() default @__DirectiveSubscriptionArguments;

  __DirectiveListSubscriptionArguments __directiveList(
      ) default @__DirectiveListSubscriptionArguments;

  __DirectiveConnectionSubscriptionArguments __directiveConnection(
      ) default @__DirectiveConnectionSubscriptionArguments;

  __TypeInterfacesSubscriptionArguments __typeInterfaces(
      ) default @__TypeInterfacesSubscriptionArguments;

  __TypeInterfacesListSubscriptionArguments __typeInterfacesList(
      ) default @__TypeInterfacesListSubscriptionArguments;

  __TypeInterfacesConnectionSubscriptionArguments __typeInterfacesConnection(
      ) default @__TypeInterfacesConnectionSubscriptionArguments;

  __TypePossibleTypesSubscriptionArguments __typePossibleTypes(
      ) default @__TypePossibleTypesSubscriptionArguments;

  __TypePossibleTypesListSubscriptionArguments __typePossibleTypesList(
      ) default @__TypePossibleTypesListSubscriptionArguments;

  __TypePossibleTypesConnectionSubscriptionArguments __typePossibleTypesConnection(
      ) default @__TypePossibleTypesConnectionSubscriptionArguments;

  __DirectiveLocationsRelationSubscriptionArguments __directiveLocationsRelation(
      ) default @__DirectiveLocationsRelationSubscriptionArguments;

  __DirectiveLocationsRelationListSubscriptionArguments __directiveLocationsRelationList(
      ) default @__DirectiveLocationsRelationListSubscriptionArguments;

  __DirectiveLocationsRelationConnectionSubscriptionArguments __directiveLocationsRelationConnection(
      ) default @__DirectiveLocationsRelationConnectionSubscriptionArguments;
}
