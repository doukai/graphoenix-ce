package io.graphoenix.file.dto.directive;

import io.graphoenix.spi.annotation.Directive;
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
@Directive("func")
@Target({ElementType.FIELD})
public @interface Func {
  io.graphoenix.core.dto.enumType.Func name() default io.graphoenix.core.dto.enumType.Func.COUNT;

  String field() default "";
}
