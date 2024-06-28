package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.annotation.BooleanExpression;
import io.graphoenix.core.dto.annotation.IntExpression;
import io.graphoenix.core.dto.annotation.StringExpression;
import io.graphoenix.core.dto.annotation.__DirectiveLocationExpression;
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
public @interface __DirectiveExpression {
  StringExpression name() default @StringExpression;

  __SchemaExpression1 ofSchema() default @__SchemaExpression1;

  StringExpression description() default @StringExpression;

  __DirectiveLocationExpression locations() default @__DirectiveLocationExpression;

  __InputValueExpression1 args() default @__InputValueExpression1;

  BooleanExpression isRepeatable() default @BooleanExpression;

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

  __DirectiveLocationsRelationExpression1 __directiveLocationsRelation(
      ) default @__DirectiveLocationsRelationExpression1;

  boolean not() default false;

  Conditional cond() default Conditional.AND;

  __DirectiveExpression1[] exs() default {};

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
