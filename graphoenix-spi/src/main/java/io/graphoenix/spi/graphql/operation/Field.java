package io.graphoenix.spi.graphql.operation;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.common.Arguments;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.DocumentUtil.*;

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

    public static Field fromString(String graphQL) {
        return new Field(graphqlToSelection(graphQL.replaceAll("\\\\", "")));
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

    public Field(ExecutableElement executableElement, ExecutableElement fieldExecutableElement, AnnotationMirror fieldAnnotationMirror) {
        this.setName(fieldExecutableElement.getSimpleName().toString());
        this.setArguments(new Arguments(executableElement, fieldAnnotationMirror));
    }

    public Field mergeSelection(Collection<Field> fields) {
        setSelections(
                Stream.ofNullable(fields)
                        .flatMap(Collection::stream)
                        .reduce(
                                this.selections,
                                (pre, cur) ->
                                        Stream
                                                .concat(
                                                        Stream.ofNullable(pre)
                                                                .flatMap(Collection::stream)
                                                                .filter(Selection::isField)
                                                                .map(selection -> (Field) selection)
                                                                .map(original -> {
                                                                            if (Optional.ofNullable(original.getAlias()).orElseGet(original::getName)
                                                                                    .equals(Optional.ofNullable(cur.getAlias()).orElseGet(cur::getName)))
                                                                                if (cur.getFields() != null) {
                                                                                    return original.mergeSelection(cur.getFields());
                                                                                }
                                                                            return original;
                                                                        }
                                                                ),
                                                        Stream.ofNullable(cur)
                                                                .filter(field ->
                                                                        Stream.ofNullable(pre)
                                                                                .flatMap(Collection::stream)
                                                                                .filter(Selection::isField)
                                                                                .map(selection -> (Field) selection)
                                                                                .noneMatch(original ->
                                                                                        Optional.ofNullable(original.getAlias()).orElseGet(original::getName)
                                                                                                .equals(Optional.ofNullable(field.getAlias()).orElseGet(field::getName))
                                                                                )
                                                                )
                                                )
                                                .collect(Collectors.toCollection(LinkedHashSet::new)),
                                (x, y) -> y
                        )
        );
        return this;
    }

    public static List<Field> mergeFields(List<Field> originalFields, Collection<Field> fields) {
        return Stream.ofNullable(fields)
                .flatMap(Collection::stream)
                .reduce(
                        originalFields,
                        (pre, cur) ->
                                Stream
                                        .concat(
                                                Stream.ofNullable(pre)
                                                        .flatMap(Collection::stream)
                                                        .map(original -> {
                                                                    if (Optional.ofNullable(original.getAlias()).orElseGet(original::getName)
                                                                            .equals(Optional.ofNullable(cur.getAlias()).orElseGet(cur::getName)))
                                                                        if (cur.getFields() != null) {
                                                                            return original.mergeSelection(cur.getFields());
                                                                        }
                                                                    return original;
                                                                }
                                                        ),
                                                Stream.ofNullable(cur)
                                                        .filter(field ->
                                                                Stream.ofNullable(pre)
                                                                        .flatMap(Collection::stream)
                                                                        .noneMatch(original ->
                                                                                Optional.ofNullable(original.getAlias()).orElseGet(original::getName)
                                                                                        .equals(Optional.ofNullable(field.getAlias()).orElseGet(field::getName))
                                                                        )
                                                        )
                                        )
                                        .collect(Collectors.toList()),
                        (x, y) -> y
                );
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
        if (arguments != null) {
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

    public Field addArguments(Arguments arguments) {
        if (this.arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.putAll(arguments);
        return this;
    }

    public Field addArgument(String name, JsonValue jsonValue) {
        if (this.arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.put(name, jsonValue);
        return this;
    }

    public Field addArgument(String name, Object value) {
        if (this.arguments == null) {
            this.arguments = new Arguments();
        }
        this.arguments.put(name, value);
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
        return Optional.ofNullable(selections)
                .map(fields ->
                        fields.stream()
                                .filter(Selection::isField)
                                .map(Selection::asField)
                                .collect(Collectors.toList())
                )
                .orElse(null);
    }

    public Collection<Fragment> getFragments() {
        return Optional.ofNullable(selections)
                .map(fragments ->
                        fragments.stream()
                                .filter(Selection::isFragment)
                                .map(Selection::asFragment)
                                .collect(Collectors.toList())
                )
                .orElse(null);
    }

    public Field getField(String name) {
        return this.selections.stream()
                .filter(Selection::isField)
                .map(Selection::asField)
                .filter(field -> field.getAlias() != null && field.getAlias().equals(name) || field.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Field setSelections(Collection<? extends Selection> selections) {
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


    public Field setSelections(String selectionSet) {
        if (selectionSet != null) {
            this.selections = graphqlToSelectionSet(selectionSet).selection().stream()
                    .map(Field::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return this;
    }

    public Field addSelections(String selectionSet) {
        if (selectionSet != null) {
            addSelections(
                    graphqlToSelectionSet(selectionSet).selection().stream()
                            .map(Field::new)
                            .collect(Collectors.toList())
            );
        }
        return this;
    }

    public boolean hasArgument(String name) {
        return arguments != null && arguments.containsKey(name);
    }

    public boolean hasGroupBy() {
        return hasArgument(INPUT_VALUE_GROUP_BY_NAME);
    }

    public List<String> getGroupBy() {
        return arguments.get(INPUT_VALUE_GROUP_BY_NAME).asJsonArray().stream().map(JsonValue::toString).collect(Collectors.toList());
    }

    public boolean hasFormat() {
        return hasDirective(DIRECTIVE_FORMAT_NAME);
    }

    public Optional<String> getFormatValue() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FORMAT_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_FORMAT_ARGUMENT_VALUE_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getFormatValueOrNull() {
        return getFormatValue().orElse(null);
    }

    public Optional<String> getFormatLocale() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FORMAT_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_FORMAT_ARGUMENT_LOCALE_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getFormatLocaleOrNull() {
        return getFormatLocale().orElse(null);
    }

    public boolean isHide() {
        return hasDirective(DIRECTIVE_HIDE_NAME);
    }

    public boolean isMerge() {
        return hasDirective(DIRECTIVE_MERGE_NAME);
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
