package io.graphoenix.spi.graphql.common;

import graphql.parser.antlr.GraphqlParser;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.AnnotationMirror;

public class Directive {

    private final STGroupFile stGroupFile = new STGroupFile("stg/common/Directive.stg");
    private String name;
    private final Arguments arguments = new Arguments();

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
        this.name = annotationMirror.getAnnotationType().getAnnotation(io.graphoenix.spi.annotation.Directive.class).value();
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

    public boolean hasArgument(String name) {
        return arguments.containsKey(name);
    }

    public Directive setArguments(GraphqlParser.ArgumentsContext argumentsContext) {
        return setArguments(new Arguments(argumentsContext));
    }

    public Directive setArguments(Arguments arguments) {
        this.arguments.clear();
        this.arguments.putAll(arguments);
        return this;
    }

    public Directive setArguments(JsonObject jsonObject) {
        return setArguments(new Arguments(jsonObject));
    }

    public Directive addArguments(Arguments arguments) {
        this.arguments.putAll(arguments);
        return this;
    }

    public Directive addArguments(JsonObject jsonObject) {
        this.arguments.putAll(jsonObject);
        return this;
    }

    public Directive addArgument(String name, Object valueWithVariable) {
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Directive addArgument(String name, ValueWithVariable valueWithVariable) {
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Directive addArgument(String name, JsonValue valueWithVariable) {
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
