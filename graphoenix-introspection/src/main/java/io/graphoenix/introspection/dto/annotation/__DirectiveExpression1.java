package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.annotation.BooleanExpression2;
import io.graphoenix.core.dto.annotation.IntExpression2;
import io.graphoenix.core.dto.annotation.StringExpression2;
import io.graphoenix.core.dto.annotation.__DirectiveLocationExpression2;
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
public @interface __DirectiveExpression1 {
  StringExpression2 name() default @StringExpression2;

  __SchemaExpression2 ofSchema() default @__SchemaExpression2;

  StringExpression2 description() default @StringExpression2;

  __DirectiveLocationExpression2 locations() default @__DirectiveLocationExpression2;

  __InputValueExpression2 args() default @__InputValueExpression2;

  BooleanExpression2 isRepeatable() default @BooleanExpression2;

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

  __DirectiveLocationsRelationExpression2 __directiveLocationsRelation(
      ) default @__DirectiveLocationsRelationExpression2;

  boolean not() default false;

  Conditional cond() default Conditional.AND;

  __DirectiveExpression2[] exs() default {};

  String $name() default "";

  String $ofSchema() default "";

  String $description() default "";

  String $locations() default "";

  String $args() default "";

  String $isRepeatable() default "";

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

  String $__directiveLocationsRelation() default "";

  String $not() default "";

  String $cond() default "";

  String $exs() default "";
}
