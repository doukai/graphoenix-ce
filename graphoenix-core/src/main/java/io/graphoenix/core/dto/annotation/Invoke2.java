package io.graphoenix.core.dto.annotation;

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
public @interface Invoke2 {
  @Name("package")
  String _package() default "";

  String className() default "";

  String methodName() default "";

  String returnClassName() default "";

  String[] thrownTypes() default {};

  boolean async() default false;

  String directiveName() default "";

  boolean onField() default false;

  boolean onInputValue() default false;

  boolean onExpression() default false;

  String $package() default "";

  String $className() default "";

  String $methodName() default "";

  String $returnClassName() default "";

  String $thrownTypes() default "";

  String $async() default "";

  String $directiveName() default "";

  String $onField() default "";

  String $onInputValue() default "";

  String $onExpression() default "";
}
