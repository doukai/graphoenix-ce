package io.graphoenix.structure.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Mutation Input for TreeStruct
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation Input for TreeStruct")
public @interface TreeStructInput {
  /**
   * parentId
   */
  @Description("parentId")
  String parentId() default "";

  /**
   * name
   */
  @Description("name")
  String name() default "";

  /**
   * id
   */
  @Description("id")
  String id() default "";

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  boolean isDeprecated() default false;

  /**
   * version
   */
  @Description("version")
  int version() default 0;

  /**
   * realmId
   */
  @Description("realmId")
  int realmId() default 0;

  /**
   * createUserId
   */
  @Description("createUserId")
  String createUserId() default "";

  /**
   * createTime
   */
  @Description("createTime")
  String createTime() default "";

  /**
   * updateUserId
   */
  @Description("updateUserId")
  String updateUserId() default "";

  /**
   * updateTime
   */
  @Description("updateTime")
  String updateTime() default "";

  /**
   * createGroupId
   */
  @Description("createGroupId")
  String createGroupId() default "";

  String $parentId() default "";

  String $name() default "";

  String $id() default "";

  String $isDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";
}
