package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.annotation.IntExpression1;
import io.graphoenix.core.dto.annotation.StringExpression1;
import io.graphoenix.core.dto.annotation.__TypeKindExpression1;
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
public @interface __TypeListQueryArguments {
  StringExpression1 name() default @StringExpression1;

  __SchemaExpression1 ofSchema() default @__SchemaExpression1;

  __TypeKindExpression1 kind() default @__TypeKindExpression1;

  StringExpression1 description() default @StringExpression1;

  __FieldExpression1 fields() default @__FieldExpression1;

  __TypeExpression1 interfaces() default @__TypeExpression1;

  __TypeExpression1 possibleTypes() default @__TypeExpression1;

  __EnumValueExpression1 enumValues() default @__EnumValueExpression1;

  __InputValueExpression1 inputFields() default @__InputValueExpression1;

  __TypeExpression1 ofType() default @__TypeExpression1;

  boolean includeDeprecated() default false;

  IntExpression1 version() default @IntExpression1;

  IntExpression1 realmId() default @IntExpression1;

  StringExpression1 createUserId() default @StringExpression1;

  StringExpression1 createTime() default @StringExpression1;

  StringExpression1 updateUserId() default @StringExpression1;

  StringExpression1 updateTime() default @StringExpression1;

  StringExpression1 createGroupId() default @StringExpression1;

  StringExpression1 __typename() default @StringExpression1;

  IntExpression1 schemaId() default @IntExpression1;

  StringExpression1 ofTypeName() default @StringExpression1;

  __TypeInterfacesExpression1 __typeInterfaces() default @__TypeInterfacesExpression1;

  __TypePossibleTypesExpression1 __typePossibleTypes() default @__TypePossibleTypesExpression1;

  __TypeOrderBy1 orderBy() default @__TypeOrderBy1;

  String[] groupBy() default {};

  boolean not() default false;

  Conditional cond() default Conditional.AND;

  __TypeExpression1[] exs() default {};

  int first() default 0;

  int last() default 0;

  int offset() default 0;

  String after() default "";

  String before() default "";

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

  String $orderBy() default "";

  String $groupBy() default "";

  String $not() default "";

  String $cond() default "";

  String $exs() default "";

  String $first() default "";

  String $last() default "";

  String $offset() default "";

  String $after() default "";

  String $before() default "";
}
