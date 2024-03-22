package io.graphoenix.jsonschema.dto.directive;

import io.graphoenix.core.dto.annotation.Invoke;
import io.graphoenix.spi.annotation.Directive;
import jakarta.annotation.Generated;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Directive("invokes")
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
public @interface Invokes {
  Invoke[] methods() default {};
}
