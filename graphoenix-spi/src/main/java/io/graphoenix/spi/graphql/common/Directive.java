package io.graphoenix.spi.graphql.common;

import graphql.parser.antlr.GraphqlParser;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.AnnotationMirror;
import java.util.Map;
import java.util.Optional;

public class Directive {

    private final STGroupFile stGroupFile = new STGroupFile("stg/common/Directive.stg");
    private String name;
    private Arguments arguments;

    public String getName() {
        return name;
    }

    public Directive() {
    }

    public Directive(GraphqlParser.DirectiveContext directiveContext) {
        this.name = directiveContext.name().getText();
        if (directiveContext.arguments() != null) {
            setArguments(new Arguments(directiveContext.arguments()));
        }
    }

    public Directive(AnnotationMirror annotationMirror) {
        this.name = annotationMirror.getAnnotationType().asElement().getAnnotation(io.graphoenix.spi.annotation.Directive.class).value();
        if (annotationMirror.getElementValues() != null) {
            setArguments(new Arguments(annotationMirror));
        }
    }

    public Directive(String name) {
        this.name = name;
    }

    public Directive setName(String name) {
        this.name = name;
        return this;
    }

    public Arguments getArguments() {
        return arguments;
    }

    public ValueWithVariable getArgument(String name) {
        return arguments.getArgument(name);
    }

    public Optional<ValueWithVariable> getArgumentOrEmpty(String name) {
        return Optional.ofNullable(getArgument(name));
    }

    public boolean hasArgument(String name) {
        return arguments.containsKey(name);
    }

    public Directive setArguments(GraphqlParser.ArgumentsContext argumentsContext) {
        return setArguments(new Arguments(argumentsContext));
    }

    public Directive setArguments(Arguments arguments) {
        if (arguments != null) {
            this.arguments = arguments;
        }
        return this;
    }

    public Directive setArguments(JsonObject jsonObject) {
        return setArguments(new Arguments(jsonObject));
    }

    public Directive setArguments(Map<?, ?> objectValueWithVariableMap) {
        return setArguments(new Arguments(objectValueWithVariableMap));
    }

    public Directive addArguments(Arguments arguments) {
        if (arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.putAll(arguments);
        return this;
    }

    public Directive addArguments(JsonObject jsonObject) {
        if (arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.putAll(jsonObject);
        return this;
    }

    public Directive addArgument(String name, Object valueWithVariable) {
        if (arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Directive addArgument(String name, ValueWithVariable valueWithVariable) {
        if (arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Directive addArgument(String name, JsonValue valueWithVariable) {
        if (arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("directiveDefinition");
        st.add("directive", this);
        return st.render();
    }
}
