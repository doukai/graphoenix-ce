package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.enumType.__TypeKind;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface __TypeMutationArguments {
  String name() default "";

  __SchemaInput1 ofSchema() default @__SchemaInput1;

  __TypeKind kind() default __TypeKind.SCALAR;

  String description() default "";

  __FieldInput1[] fields() default {};

  __TypeInput1[] interfaces() default {};

  __TypeInput1[] possibleTypes() default {};

  __EnumValueInput1[] enumValues() default {};

  __InputValueInput1[] inputFields() default {};

  __TypeInput1 ofType() default @__TypeInput1;

  boolean isDeprecated() default false;

  int version() default 0;

  int realmId() default 0;

  String createUserId() default "";

  String createTime() default "";

  String updateUserId() default "";

  String updateTime() default "";

  String createGroupId() default "";

  String __typename() default "__Type";

  int schemaId() default 0;

  String ofTypeName() default "";

  __Type__TypeRelationInput1[] __typeTypeRelation() default {};

  __TypeExpression1 where() default @__TypeExpression1;

  String $name() default "";

  String $ofSchema() default "";

  String $kind() default "";

  String $description() default "";

  String $fields() default "";

  String $interfaces() default "";

  String $possibleTypes() default "";

  String $enumValues() default "";

  String $inputFields() default "";

  String $ofType() default "";

  String $isDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $schemaId() default "";

  String $ofTypeName() default "";

  String $__typeTypeRelation() default "";

  String $where() default "";
}
