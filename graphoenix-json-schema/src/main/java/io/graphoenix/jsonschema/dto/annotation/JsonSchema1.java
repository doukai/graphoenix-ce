package io.graphoenix.jsonschema.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Name;

@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface JsonSchema1 {
  int minLength() default 0;

  int maxLength() default 0;

  String pattern() default "";

  String format() default "";

  String contentMediaType() default "";

  String contentEncoding() default "";

  float minimum() default 0;

  float exclusiveMinimum() default 0;

  float maximum() default 0;

  float exclusiveMaximum() default 0;

  float multipleOf() default 0;

  @Name("const")
  String _const() default "";

  @Name("enum")
  String[] _enum() default {};

  JsonSchema2 items() default @JsonSchema2;

  int minItems() default 0;

  int maxItems() default 0;

  boolean uniqueItems() default false;

  JsonSchema2[] allOf() default {};

  JsonSchema2[] anyOf() default {};

  JsonSchema2[] oneOf() default {};

  JsonSchema2 not() default @JsonSchema2;

  Property2[] properties() default {};

  @Name("if")
  JsonSchema2 _if() default @JsonSchema2;

  JsonSchema2 then() default @JsonSchema2;

  @Name("else")
  JsonSchema2 _else() default @JsonSchema2;

  Property2[] dependentRequired() default {};

  String $minLength() default "";

  String $maxLength() default "";

  String $pattern() default "";

  String $format() default "";

  String $contentMediaType() default "";

  String $contentEncoding() default "";

  String $minimum() default "";

  String $exclusiveMinimum() default "";

  String $maximum() default "";

  String $exclusiveMaximum() default "";

  String $multipleOf() default "";

  String $const() default "";

  String $enum() default "";

  String $items() default "";

  String $minItems() default "";

  String $maxItems() default "";

  String $uniqueItems() default "";

  String $allOf() default "";

  String $anyOf() default "";

  String $oneOf() default "";

  String $not() default "";

  String $properties() default "";

  String $if() default "";

  String $then() default "";

  String $else() default "";

  String $dependentRequired() default "";
}
