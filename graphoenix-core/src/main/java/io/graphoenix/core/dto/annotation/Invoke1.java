package io.graphoenix.core.dto.annotation;

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
public @interface Invoke1 {
  String className() default "";

  String methodName() default "";

  InvokeParameter2[] parameters() default {};

  String returnClassName() default "";

  String[] thrownTypes() default {};

  String $className() default "";

  String $methodName() default "";

  String $parameters() default "";

  String $returnClassName() default "";

  String $thrownTypes() default "";
}
