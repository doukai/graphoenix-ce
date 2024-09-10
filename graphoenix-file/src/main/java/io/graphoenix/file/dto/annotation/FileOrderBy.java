package io.graphoenix.file.dto.annotation;

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
public @interface FileOrderBy {
  Sort id() default Sort.ASC;

  Sort name() default Sort.ASC;

  Sort contentType() default Sort.ASC;

  Sort content() default Sort.ASC;

  Sort url() default Sort.ASC;

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

  Sort nameCount() default Sort.ASC;

  Sort nameMax() default Sort.ASC;

  Sort nameMin() default Sort.ASC;

  Sort contentTypeCount() default Sort.ASC;

  Sort contentTypeMax() default Sort.ASC;

  Sort contentTypeMin() default Sort.ASC;

  Sort contentCount() default Sort.ASC;

  Sort contentMax() default Sort.ASC;

  Sort contentMin() default Sort.ASC;

  Sort urlCount() default Sort.ASC;

  Sort urlMax() default Sort.ASC;

  Sort urlMin() default Sort.ASC;

  String $id() default "";

  String $name() default "";

  String $contentType() default "";

  String $content() default "";

  String $url() default "";

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

  String $nameCount() default "";

  String $nameMax() default "";

  String $nameMin() default "";

  String $contentTypeCount() default "";

  String $contentTypeMax() default "";

  String $contentTypeMin() default "";

  String $contentCount() default "";

  String $contentMax() default "";

  String $contentMin() default "";

  String $urlCount() default "";

  String $urlMax() default "";

  String $urlMin() default "";
}
