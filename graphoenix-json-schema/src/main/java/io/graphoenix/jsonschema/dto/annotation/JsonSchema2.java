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
public @interface JsonSchema2 {
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

  int minItems() default 0;

  int maxItems() default 0;

  boolean uniqueItems() default false;
}
