package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.enumType.Sort;
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
public @interface __TypeInterfacesOrderBy1 {
  Sort id() default Sort.ASC;

  Sort typeRef() default Sort.ASC;

  __TypeOrderBy2 type() default @__TypeOrderBy2;

  Sort interfaceRef() default Sort.ASC;

  @Name("interface")
  __TypeOrderBy2 _interface() default @__TypeOrderBy2;

  Sort isDeprecated() default Sort.ASC;

  Sort version() default Sort.ASC;

  Sort realmId() default Sort.ASC;

  Sort createUserId() default Sort.ASC;

  Sort createTime() default Sort.ASC;

  Sort updateUserId() default Sort.ASC;

  Sort updateTime() default Sort.ASC;

  Sort createGroupId() default Sort.ASC;

  Sort __typename() default Sort.ASC;

  Sort idCount() default Sort.ASC;

  Sort idMax() default Sort.ASC;

  Sort idMin() default Sort.ASC;

  Sort typeRefCount() default Sort.ASC;

  Sort typeRefMax() default Sort.ASC;

  Sort typeRefMin() default Sort.ASC;

  Sort interfaceRefCount() default Sort.ASC;

  Sort interfaceRefMax() default Sort.ASC;

  Sort interfaceRefMin() default Sort.ASC;

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

  String $idCount() default "";

  String $idMax() default "";

  String $idMin() default "";

  String $typeRefCount() default "";

  String $typeRefMax() default "";

  String $typeRefMin() default "";

  String $interfaceRefCount() default "";

  String $interfaceRefMax() default "";

  String $interfaceRefMin() default "";
}
