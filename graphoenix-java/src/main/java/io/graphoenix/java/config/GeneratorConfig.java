package io.graphoenix.java.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "generator")
public class GeneratorConfig {
    @Optional
    private Integer annotationLevel = 3;

    public Integer getAnnotationLevel() {
        return annotationLevel;
    }

    public void setAnnotationLevel(Integer annotationLevel) {
        this.annotationLevel = annotationLevel;
    }
}
