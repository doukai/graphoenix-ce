package io.graphoenix.introspection.dto.directive;

import io.graphoenix.core.dto.enumType.Protocol;
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
@Directive("options")
@Target({ElementType.FIELD})
public @interface Options {
  String type() default "";

  @Name("default")
  String _default() default "";

  int length() default 0;

  int decimals() default 0;

  boolean autoIncrement() default false;

  Protocol protocol() default Protocol.GRPC;
}
