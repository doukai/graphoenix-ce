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

  __SchemaInput ofSchema() default @__SchemaInput;

  __TypeKind kind() default __TypeKind.SCALAR;

  String description() default "";

  __FieldInput[] fields() default {};

  __TypeInput[] interfaces() default {};

  __TypeInput[] possibleTypes() default {};

  __EnumValueInput[] enumValues() default {};

  __InputValueInput[] inputFields() default {};

  __TypeInput ofType() default @__TypeInput;

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

  __TypeInterfacesInput[] __typeInterfaces() default {};

  __TypePossibleTypesInput[] __typePossibleTypes() default {};

  __TypeInput input() default @__TypeInput;

  __TypeExpression where() default @__TypeExpression;

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

  String $__typeInterfaces() default "";

  String $__typePossibleTypes() default "";

  String $input() default "";

  String $where() default "";
}
