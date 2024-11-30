package io.graphoenix.jsonschema.dto.directive;

import io.graphoenix.jsonschema.dto.annotation.Property;
import io.graphoenix.spi.annotation.Directive;
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
@Directive("jsonSchema")
@Target({ElementType.FIELD,ElementType.TYPE,ElementType.PARAMETER})
public @interface JsonSchema {
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

  io.graphoenix.jsonschema.dto.annotation.JsonSchema items(
      ) default @io.graphoenix.jsonschema.dto.annotation.JsonSchema;

  int minItems() default 0;

  int maxItems() default 0;

  boolean uniqueItems() default false;

  io.graphoenix.jsonschema.dto.annotation.JsonSchema[] allOf() default {};

  io.graphoenix.jsonschema.dto.annotation.JsonSchema[] anyOf() default {};

  io.graphoenix.jsonschema.dto.annotation.JsonSchema[] oneOf() default {};

  io.graphoenix.jsonschema.dto.annotation.JsonSchema not(
      ) default @io.graphoenix.jsonschema.dto.annotation.JsonSchema;

  Property[] properties() default {};

  @Name("if")
  io.graphoenix.jsonschema.dto.annotation.JsonSchema _if(
      ) default @io.graphoenix.jsonschema.dto.annotation.JsonSchema;

  io.graphoenix.jsonschema.dto.annotation.JsonSchema then(
      ) default @io.graphoenix.jsonschema.dto.annotation.JsonSchema;

  @Name("else")
  io.graphoenix.jsonschema.dto.annotation.JsonSchema _else(
      ) default @io.graphoenix.jsonschema.dto.annotation.JsonSchema;

  Property[] dependentRequired() default {};

  @Name("package")
  String _package() default "io.graphoenix.jsonschema";
}
