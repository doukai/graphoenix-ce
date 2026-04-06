package io.graphoenix.file.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Group Input for 文件
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Group Input for 文件")
public @interface FileGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  String[] fieldNames() default {};

  /**
   * OrderByes
   */
  @Description("OrderByes")
  FileGroupBy1[] gbs() default {};

  String $fieldNames() default "";

  String $gbs() default "";
}
