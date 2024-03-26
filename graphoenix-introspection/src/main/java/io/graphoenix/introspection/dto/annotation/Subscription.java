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

  __Type__TypeRelationSubscriptionArguments __typeTypeRelation(
      ) default @__Type__TypeRelationSubscriptionArguments;

  __Type__TypeRelationListSubscriptionArguments __typeTypeRelationList(
      ) default @__Type__TypeRelationListSubscriptionArguments;

  __Type__TypeRelationConnectionSubscriptionArguments __typeTypeRelationConnection(
      ) default @__Type__TypeRelationConnectionSubscriptionArguments;
}
