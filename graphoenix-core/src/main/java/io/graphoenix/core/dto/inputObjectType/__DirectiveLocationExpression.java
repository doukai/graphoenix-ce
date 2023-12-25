package io.graphoenix.core.dto.inputObjectType;

import io.graphoenix.core.dto.enumType.Operator;
import io.graphoenix.core.dto.enumType.__DirectiveLocation;
import jakarta.annotation.Generated;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface __DirectiveLocationExpression {
  Operator opr() default EQ;

  __DirectiveLocation val();

  __DirectiveLocation[] arr();
}
