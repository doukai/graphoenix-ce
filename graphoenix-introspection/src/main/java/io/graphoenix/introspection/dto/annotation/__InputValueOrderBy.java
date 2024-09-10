package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.enumType.Sort;
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
public @interface __InputValueOrderBy {
  Sort id() default Sort.ASC;

  Sort name() default Sort.ASC;

  __TypeOrderBy1 ofType() default @__TypeOrderBy1;

  Sort fieldId() default Sort.ASC;

  Sort directiveName() default Sort.ASC;

  Sort description() default Sort.ASC;

  __TypeOrderBy1 type() default @__TypeOrderBy1;

  Sort defaultValue() default Sort.ASC;

  Sort isDeprecated() default Sort.ASC;

  Sort version() default Sort.ASC;

  Sort realmId() default Sort.ASC;

  Sort createUserId() default Sort.ASC;

  Sort createTime() default Sort.ASC;

  Sort updateUserId() default Sort.ASC;

  Sort updateTime() default Sort.ASC;

  Sort createGroupId() default Sort.ASC;

  Sort __typename() default Sort.ASC;

  Sort ofTypeName() default Sort.ASC;

  Sort typeName() default Sort.ASC;

  Sort idCount() default Sort.ASC;

  Sort idMax() default Sort.ASC;

  Sort idMin() default Sort.ASC;

  Sort nameCount() default Sort.ASC;

  Sort nameMax() default Sort.ASC;

  Sort nameMin() default Sort.ASC;

  Sort directiveNameCount() default Sort.ASC;

  Sort directiveNameMax() default Sort.ASC;

  Sort directiveNameMin() default Sort.ASC;

  Sort descriptionCount() default Sort.ASC;

  Sort descriptionMax() default Sort.ASC;

  Sort descriptionMin() default Sort.ASC;

  Sort defaultValueCount() default Sort.ASC;

  Sort defaultValueMax() default Sort.ASC;

  Sort defaultValueMin() default Sort.ASC;

  Sort ofTypeNameCount() default Sort.ASC;

  Sort ofTypeNameMax() default Sort.ASC;

  Sort ofTypeNameMin() default Sort.ASC;

  Sort typeNameCount() default Sort.ASC;

  Sort typeNameMax() default Sort.ASC;

  Sort typeNameMin() default Sort.ASC;

  Sort fieldIdCount() default Sort.ASC;

  Sort fieldIdSum() default Sort.ASC;

  Sort fieldIdAvg() default Sort.ASC;

  Sort fieldIdMax() default Sort.ASC;

  Sort fieldIdMin() default Sort.ASC;

  String $id() default "";

  String $name() default "";

  String $ofType() default "";

  String $fieldId() default "";

  String $directiveName() default "";

  String $description() default "";

  String $type() default "";

  String $defaultValue() default "";

  String $isDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $ofTypeName() default "";

  String $typeName() default "";

  String $idCount() default "";

  String $idMax() default "";

  String $idMin() default "";

  String $nameCount() default "";

  String $nameMax() default "";

  String $nameMin() default "";

  String $directiveNameCount() default "";

  String $directiveNameMax() default "";

  String $directiveNameMin() default "";

  String $descriptionCount() default "";

  String $descriptionMax() default "";

  String $descriptionMin() default "";

  String $defaultValueCount() default "";

  String $defaultValueMax() default "";

  String $defaultValueMin() default "";

  String $ofTypeNameCount() default "";

  String $ofTypeNameMax() default "";

  String $ofTypeNameMin() default "";

  String $typeNameCount() default "";

  String $typeNameMax() default "";

  String $typeNameMin() default "";

  String $fieldIdCount() default "";

  String $fieldIdSum() default "";

  String $fieldIdAvg() default "";

  String $fieldIdMax() default "";

  String $fieldIdMin() default "";
}
