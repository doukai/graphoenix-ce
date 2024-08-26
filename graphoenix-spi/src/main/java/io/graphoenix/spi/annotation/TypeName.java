package io.graphoenix.spi.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface TypeName {
    String value();
}
