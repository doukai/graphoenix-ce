package io.graphoenix.introspection.dto.annotation;

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
public @interface __TypeInterfacesListMutationArguments {
  String id() default "";

  String typeRef() default "";

  __TypeInput1 type() default @__TypeInput1;

  String interfaceRef() default "";

  @Name("interface")
  __TypeInput1 _interface() default @__TypeInput1;

  boolean isDeprecated() default false;

  int version() default 0;

  int realmId() default 0;

  String createUserId() default "";

  String createTime() default "";

  String updateUserId() default "";

  String updateTime() default "";

  String createGroupId() default "";

  String __typename() default "__TypeInterfaces";

  __TypeInterfacesInput1[] list() default {};

  __TypeInterfacesExpression1 where() default @__TypeInterfacesExpression1;

  String $id() default "";

  String $typeRef() default "";

  String $type() default "";

  String $interfaceRef() default "";

  String $interface() default "";

  String $isDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $list() default "";

  String $where() default "";
}
