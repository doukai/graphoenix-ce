package io.graphoenix.spi.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface SelectionSet {

    String value() default "";

    int layers() default 0;
}
