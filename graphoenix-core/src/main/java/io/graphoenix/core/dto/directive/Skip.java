package io.graphoenix.core.dto.directive;

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
@Directive("skip")
@Target({ElementType.CONSTRUCTOR,ElementType.METHOD,ElementType.PARAMETER})
public @interface Skip {
  @Name("if")
  boolean _if() default true;

  @Name("package")
  String _package() default "io.graphoenix.core";
}
