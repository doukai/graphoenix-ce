package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.annotation.IntExpression2;
import io.graphoenix.core.dto.annotation.StringExpression2;
import io.graphoenix.core.dto.annotation.__TypeKindExpression2;
import io.graphoenix.core.dto.enumType.Conditional;
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
public @interface __TypeExpression1 {
  StringExpression2 name() default @StringExpression2;

  __SchemaExpression2 ofSchema() default @__SchemaExpression2;

  __TypeKindExpression2 kind() default @__TypeKindExpression2;

  StringExpression2 description() default @StringExpression2;

  __FieldExpression2 fields() default @__FieldExpression2;

  __TypeExpression2 interfaces() default @__TypeExpression2;

  __TypeExpression2 possibleTypes() default @__TypeExpression2;

  __EnumValueExpression2 enumValues() default @__EnumValueExpression2;

  __InputValueExpression2 inputFields() default @__InputValueExpression2;

  __TypeExpression2 ofType() default @__TypeExpression2;

  boolean includeDeprecated() default false;

  IntExpression2 version() default @IntExpression2;

  IntExpression2 realmId() default @IntExpression2;

  StringExpression2 createUserId() default @StringExpression2;

  StringExpression2 createTime() default @StringExpression2;

  StringExpression2 updateUserId() default @StringExpression2;

  StringExpression2 updateTime() default @StringExpression2;

  StringExpression2 createGroupId() default @StringExpression2;

  StringExpression2 __typename() default @StringExpression2;

  IntExpression2 schemaId() default @IntExpression2;

  StringExpression2 ofTypeName() default @StringExpression2;

  __TypeInterfacesExpression2 __typeInterfaces() default @__TypeInterfacesExpression2;

  __TypePossibleTypesExpression2 __typePossibleTypes() default @__TypePossibleTypesExpression2;

  boolean not() default false;

  Conditional cond() default Conditional.AND;

  __TypeExpression2[] exs() default {};

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

  String $includeDeprecated() default "";

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

  String $not() default "";

  String $cond() default "";

  String $exs() default "";
}
