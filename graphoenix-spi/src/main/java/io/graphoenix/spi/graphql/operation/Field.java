package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.common.Arguments;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class Field extends AbstractDefinition implements Selection {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/Field.stg");
    private String alias;
    private final Arguments arguments = new Arguments();
    private final Collection<Selection> selections = new LinkedHashSet<>();

    public Field() {
        super();
    }

    public Field(String name) {
        super(name);
    }

    public Field(GraphqlParser.SelectionContext selectionContext) {
        this(selectionContext.field());
    }

    public Field(GraphqlParser.FieldContext fieldContext) {
        super(fieldContext.name(), null, fieldContext.directives());
        if (fieldContext.alias() != null) {
            this.alias = fieldContext.alias().name().getText();
        }
        if (fieldContext.arguments() != null) {
            setArguments(new Arguments(fieldContext.arguments()));
        }
        if (fieldContext.selectionSet() != null) {
            setSelections(
                    fieldContext.selectionSet().selection().stream()
                            .map(Selection::of)
                            .collect(Collectors.toList())
            );
        }
    }

    public Field mergeSelection(Collection<Field> fields) {
        this.selections.addAll(
                Stream
                        .concat(
                                Stream.ofNullable(this.selections)
                                        .flatMap(Collection::stream)
                                        .filter(Selection::isField)
                                        .map(selection -> (Field) selection),
                                Stream.ofNullable(fields)
                                        .flatMap(Collection::stream)
                        )
                        .filter(distinctByKey(Field::getName))
                        .collect(Collectors.toList())
        );
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public Field setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public Arguments getArguments() {
        return arguments;
    }

    public Field setArguments(Arguments arguments) {
        this.arguments.clear();
        this.arguments.putAll(arguments);
        return this;
    }

    public Field setArguments(JsonObject jsonObject) {
        return setArguments(new Arguments(jsonObject));
    }

    public Field updateArguments(JsonObject jsonObject) {
        return setArguments(new Arguments(ValueWithVariable.updateJsonObject(this.arguments, jsonObject)));
    }

    public Field addArguments(Arguments arguments) {
        this.arguments.putAll(arguments);
        return this;
    }

    public Field addArguments(JsonObject jsonObject) {
        this.arguments.putAll(jsonObject);
        return this;
    }

    public Field addArgument(String name, Object valueWithVariable) {
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Field addArgument(String name, ValueWithVariable valueWithVariable) {
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Field addArgument(String name, JsonValue valueWithVariable) {
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Collection<Selection> getSelections() {
        return selections;
    }

    public Field getField(String name) {
        return this.selections.stream()
                .filter(Selection::isField)
                .map(selection -> (Field) selection)
                .filter(field -> field.getAlias() != null && field.getAlias().equals(name) || field.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Field setSelections(Collection<Selection> selections) {
        this.selections.clear();
        this.selections.addAll(selections);
        return this;
    }

    public Field addSelection(Selection selection) {
        this.selections.add(selection);
        return this;
    }

    public Field addSelections(Collection<Selection> selections) {
        this.selections.addAll(selections);
        return this;
    }

    @Override
    public boolean isField() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("fieldDefinition");
        st.add("filed", this);
        return st.render();
    }
}
