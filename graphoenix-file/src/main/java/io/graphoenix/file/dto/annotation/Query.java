package io.graphoenix.file.dto.annotation;

import jakarta.annotation.Generated;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Query
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query")
public @interface Query {
  FileQueryArguments file() default @FileQueryArguments;

  FileListQueryArguments fileList() default @FileListQueryArguments;

  FileConnectionQueryArguments fileConnection() default @FileConnectionQueryArguments;
}
