package io.graphoenix.file.dto.annotation;

import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Order Input for 文件
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Order Input for 文件")
public @interface FileOrderBy {
  /**
   * ID
   */
  @Description("ID")
  Sort id() default Sort.ASC;

  /**
   * 文件名
   */
  @Description("文件名")
  Sort name() default Sort.ASC;

  /**
   * 类型
   */
  @Description("类型")
  Sort contentType() default Sort.ASC;

  /**
   * 内容
   */
  @Description("内容")
  Sort content() default Sort.ASC;

  /**
   * URL
   */
  @Description("URL")
  Sort url() default Sort.ASC;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  Sort isDeprecated() default Sort.ASC;

  /**
   * Version
   */
  @Description("Version")
  Sort version() default Sort.ASC;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  Sort realmId() default Sort.ASC;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  Sort createUserId() default Sort.ASC;

  /**
   * Create Time
   */
  @Description("Create Time")
  Sort createTime() default Sort.ASC;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  Sort updateUserId() default Sort.ASC;

  /**
   * Update Time
   */
  @Description("Update Time")
  Sort updateTime() default Sort.ASC;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  Sort createGroupId() default Sort.ASC;

  /**
   * Type Name
   */
  @Description("Type Name")
  Sort __typename() default Sort.ASC;

  /**
   * Count of 文件
   */
  @Description("Count of 文件")
  Sort idCount() default Sort.ASC;

  /**
   * Max of ID
   */
  @Description("Max of ID")
  Sort idMax() default Sort.ASC;

  /**
   * Min of ID
   */
  @Description("Min of ID")
  Sort idMin() default Sort.ASC;

  /**
   * Count of 文件名
   */
  @Description("Count of 文件名")
  Sort nameCount() default Sort.ASC;

  /**
   * Max of 文件名
   */
  @Description("Max of 文件名")
  Sort nameMax() default Sort.ASC;

  /**
   * Min of 文件名
   */
  @Description("Min of 文件名")
  Sort nameMin() default Sort.ASC;

  /**
   * Count of 类型
   */
  @Description("Count of 类型")
  Sort contentTypeCount() default Sort.ASC;

  /**
   * Max of 类型
   */
  @Description("Max of 类型")
  Sort contentTypeMax() default Sort.ASC;

  /**
   * Min of 类型
   */
  @Description("Min of 类型")
  Sort contentTypeMin() default Sort.ASC;

  /**
   * Count of 内容
   */
  @Description("Count of 内容")
  Sort contentCount() default Sort.ASC;

  /**
   * Max of 内容
   */
  @Description("Max of 内容")
  Sort contentMax() default Sort.ASC;

  /**
   * Min of 内容
   */
  @Description("Min of 内容")
  Sort contentMin() default Sort.ASC;

  /**
   * Count of URL
   */
  @Description("Count of URL")
  Sort urlCount() default Sort.ASC;

  /**
   * Max of URL
   */
  @Description("Max of URL")
  Sort urlMax() default Sort.ASC;

  /**
   * Min of URL
   */
  @Description("Min of URL")
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
