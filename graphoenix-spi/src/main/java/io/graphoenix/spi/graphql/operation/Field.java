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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class Field extends AbstractDefinition implements Selection {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/Field.stg");
    private String alias;
    private Arguments arguments;
    private Collection<Selection> selections;

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
        this.selections = Stream
                .concat(
                        Stream.ofNullable(this.selections)
                                .flatMap(Collection::stream)
                                .filter(Selection::isField)
                                .map(selection -> (Field) selection),
                        Stream.ofNullable(fields)
                                .flatMap(Collection::stream)
                )
                .filter(distinctByKey(Field::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
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
        if (arguments != null && arguments.size() > 0) {
            this.arguments = new Arguments(arguments);
        }
        return this;
    }

    public Field setArguments(JsonObject jsonObject) {
        return setArguments(new Arguments(jsonObject));
    }

    public Field setArguments(Map<?, ?> objectValueWithVariableMap) {
        return setArguments(new Arguments(objectValueWithVariableMap));
    }

    public Field updateArguments(JsonObject jsonObject) {
        return setArguments(new Arguments(ValueWithVariable.updateJsonObject(this.arguments, jsonObject)));
    }

    public Field addArguments(Arguments arguments) {
        if (this.arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.putAll(arguments);
        return this;
    }

    public Field addArguments(JsonObject jsonObject) {
        return addArguments(new Arguments(jsonObject));
    }

    public Field putArgument(String name, Object object) {
        if (this.arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.put(name, object);
        return this;
    }

    public Field putArgument(String name, ValueWithVariable valueWithVariable) {
        if (this.arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.put(name, valueWithVariable);
        return this;
    }

    public Field putArgument(String name, JsonValue jsonValue) {
        if (this.arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.put(name, jsonValue);
        return this;
    }

    public Collection<Selection> getSelections() {
        return selections;
    }

    public Collection<Field> getFields() {
        return Stream.ofNullable(selections)
                .flatMap(Collection::stream)
                .filter(Selection::isField)
                .map(Selection::asField)
                .collect(Collectors.toList());
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
        this.selections = new LinkedHashSet<>(selections);
        return this;
    }

    public Field addSelection(Selection selection) {
        if (this.selections == null) {
            this.selections = new LinkedHashSet<>();
        }
        this.selections.add(selection);
        return this;
    }

    public Field addSelections(Collection<Selection> selections) {
        if (this.selections == null) {
            this.selections = new LinkedHashSet<>();
        }
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
