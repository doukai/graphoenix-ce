package io.graphoenix.graphql.common;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.error.GraphQLErrors;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static io.graphoenix.core.error.GraphQLErrorType.SELECTION_NOT_EXIST;

public class Field {

    private String name;
    private String alias;
    private io.graphoenix.core.operation.Arguments arguments;
    private Collection<io.graphoenix.core.operation.Directive> directives;
    private Collection<Field> fields;
    private String selections;

    public Field() {
    }

    public Field(String name) {
        this.name = name;
    }

    public Field(GraphqlParser.SelectionContext selectionContext) {
        this(selectionContext.field());
    }

    public Field(GraphqlParser.FieldContext fieldContext) {
        this.name = fieldContext.name().getText();
        if (fieldContext.alias() != null) {
            this.alias = fieldContext.alias().name().getText();
        }
        if (fieldContext.arguments() != null) {
            this.arguments = new io.graphoenix.core.operation.Arguments(fieldContext.arguments());
        }
        if (fieldContext.selectionSet() != null) {
            this.fields = fieldContext.selectionSet().selection().stream().map(Field::new).collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (fieldContext.directives() != null) {
            this.directives = fieldContext.directives().directive().stream().map(io.graphoenix.core.operation.Directive::new).collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    public static void mergeSelection(Collection<Field> originalSet, Collection<Field> fieldSet) {
        if (fieldSet != null) {
            fieldSet.forEach(
                    field -> {
                        if (originalSet.stream().map(Field::getName).noneMatch(name -> name.equals(field.getName()))) {
                            originalSet.add(field);
                        } else {
                            if (field.getFields() != null && field.getFields().size() > 0) {
                                mergeSelection(
                                        originalSet.stream()
                                                .filter(original -> original.getName().equals(field.getName()))
                                                .findFirst()
                                                .orElseThrow(() -> new GraphQLErrors(SELECTION_NOT_EXIST.bind(field.getName())))
                                                .getFields(),
                                        field.getFields()
                                );
                            }
                        }
                    }
            );
        }
    }

    public String getName() {
        return name;
    }

    public Field setName(String name) {
        this.name = name;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public Field setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public io.graphoenix.core.operation.Arguments getArguments() {
        return arguments;
    }

    public Field setArguments(io.graphoenix.core.operation.Arguments arguments) {
        this.arguments = arguments;
        return this;
    }

    public Field setArguments(JsonObject jsonObject) {
        this.arguments = new io.graphoenix.core.operation.Arguments(jsonObject);
        return this;
    }

    public Field updateArguments(JsonObject jsonObject) {
        this.arguments = new io.graphoenix.core.operation.Arguments(ValueWithVariable.updateJsonObject(this.arguments, jsonObject));
        return this;
    }

    public Field addArguments(io.graphoenix.core.operation.Arguments arguments) {
        if (this.arguments == null) {
            this.arguments = new io.graphoenix.core.operation.Arguments();
        }
        this.arguments.putAll(arguments);
        return this;
    }

    public Field addArguments(JsonObject jsonObject) {
        if (this.arguments == null) {
            this.arguments = new io.graphoenix.core.operation.Arguments();
        }
        this.arguments.putAll(jsonObject);
        return this;
    }

    public Field addArgument(String name, Object valueWithVariable) {
        if (this.arguments == null) {
            this.arguments = new io.graphoenix.core.operation.Arguments();
        }
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Field addArgument(String name, ValueWithVariable valueWithVariable) {
        if (this.arguments == null) {
            this.arguments = new io.graphoenix.core.operation.Arguments();
        }
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Field addArgument(String name, JsonValue valueWithVariable) {
        if (this.arguments == null) {
            this.arguments = new io.graphoenix.core.operation.Arguments();
        }
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Collection<io.graphoenix.core.operation.Directive> getDirectives() {
        return directives;
    }

    public Field setDirectives(Collection<io.graphoenix.core.operation.Directive> directives) {
        this.directives = directives;
        return this;
    }

    public Collection<Field> getFields() {
        return fields;
    }

    public Field getField(String name) {
        return this.fields.stream().filter(field -> field.getAlias() != null && field.getAlias().equals(name) || field.getName().equals(name)).findFirst().orElse(null);
    }

    public Field setFields(Collection<Field> fields) {
        this.fields = fields;
        return this;
    }

    public Field addField(Field field) {
        if (this.fields == null) {
            this.fields = new LinkedHashSet<>();
        }
        this.fields.add(field);
        return this;
    }

    public Field addFields(Collection<Field> fields) {
        if (this.fields == null) {
            this.fields = new LinkedHashSet<>();
        }
        this.fields.addAll(fields);
        return this;
    }

    public String getSelections() {
        return selections;
    }

    public Field setSelections(String selections) {
        this.selections = selections;
        return this;
    }

    @Override
    public String toString() {
        STGroupFile stGroupFile = new STGroupFile("stg/operation/Field.stg");
        ST st = stGroupFile.getInstanceOf("fieldDefinition");
        st.add("filed", this);
        String render = st.render();
        stGroupFile.unload();
        return render;
    }
}
