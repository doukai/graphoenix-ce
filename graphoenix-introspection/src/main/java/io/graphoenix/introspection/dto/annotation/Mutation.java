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
public @interface Mutation {
  __SchemaMutationArguments __schema() default @__SchemaMutationArguments;

  __SchemaListMutationArguments __schemaList() default @__SchemaListMutationArguments;

  __TypeMutationArguments __type() default @__TypeMutationArguments;

  __TypeListMutationArguments __typeList() default @__TypeListMutationArguments;

  __FieldMutationArguments __field() default @__FieldMutationArguments;

  __FieldListMutationArguments __fieldList() default @__FieldListMutationArguments;

  __InputValueMutationArguments __inputValue() default @__InputValueMutationArguments;

  __InputValueListMutationArguments __inputValueList() default @__InputValueListMutationArguments;

  __EnumValueMutationArguments __enumValue() default @__EnumValueMutationArguments;

  __EnumValueListMutationArguments __enumValueList() default @__EnumValueListMutationArguments;

  __DirectiveMutationArguments __directive() default @__DirectiveMutationArguments;

  __DirectiveListMutationArguments __directiveList() default @__DirectiveListMutationArguments;

  __Type__TypeRelationMutationArguments __typeTypeRelation(
      ) default @__Type__TypeRelationMutationArguments;

  __Type__TypeRelationListMutationArguments __typeTypeRelationList(
      ) default @__Type__TypeRelationListMutationArguments;
}
