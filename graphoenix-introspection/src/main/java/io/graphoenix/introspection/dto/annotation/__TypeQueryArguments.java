package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.annotation.IntExpression;
import io.graphoenix.core.dto.annotation.StringExpression;
import io.graphoenix.core.dto.annotation.__TypeKindExpression;
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
public @interface __TypeQueryArguments {
  StringExpression name() default @StringExpression;

  __SchemaExpression ofSchema() default @__SchemaExpression;

  __TypeKindExpression kind() default @__TypeKindExpression;

  StringExpression description() default @StringExpression;

  __FieldExpression fields() default @__FieldExpression;

  __TypeExpression interfaces() default @__TypeExpression;

  __TypeExpression possibleTypes() default @__TypeExpression;

  __EnumValueExpression enumValues() default @__EnumValueExpression;

  __InputValueExpression inputFields() default @__InputValueExpression;

  __TypeExpression ofType() default @__TypeExpression;

  boolean includeDeprecated() default false;

  IntExpression version() default @IntExpression;

  IntExpression realmId() default @IntExpression;

  StringExpression createUserId() default @StringExpression;

  StringExpression createTime() default @StringExpression;

  StringExpression updateUserId() default @StringExpression;

  StringExpression updateTime() default @StringExpression;

  StringExpression createGroupId() default @StringExpression;

  StringExpression __typename() default @StringExpression;

  IntExpression schemaId() default @IntExpression;

  StringExpression ofTypeName() default @StringExpression;

  __TypeInterfacesExpression __typeInterfaces() default @__TypeInterfacesExpression;

  __TypePossibleTypesExpression __typePossibleTypes() default @__TypePossibleTypesExpression;

  String[] groupBy() default {};

  boolean not() default false;

  Conditional cond() default Conditional.AND;

  __TypeExpression[] exs() default {};

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

  String $groupBy() default "";

  String $not() default "";

  String $cond() default "";

  String $exs() default "";
}
