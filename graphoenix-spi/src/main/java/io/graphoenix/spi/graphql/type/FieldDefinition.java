package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.error.GraphQLErrorType;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.common.ArrayValueWithVariable;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.*;
import static io.graphoenix.spi.utils.ElementUtil.*;

public class FieldDefinition extends AbstractDefinition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/FieldDefinition.stg");
    private Map<String, InputValue> argumentMap;
    private Type type;

    public FieldDefinition() {
        super();
    }

    public FieldDefinition(String name) {
        super(name);
    }

    public FieldDefinition(GraphqlParser.FieldDefinitionContext fieldDefinitionContext) {
        super(fieldDefinitionContext.name(), fieldDefinitionContext.description(), fieldDefinitionContext.directives());
        this.type = Type.of(fieldDefinitionContext.type());
        if (fieldDefinitionContext.argumentsDefinition() != null) {
            setArguments(
                    fieldDefinitionContext.argumentsDefinition().inputValueDefinition().stream()
                            .map(InputValue::new)
                            .collect(Collectors.toList())
            );
        }
    }

    public FieldDefinition(VariableElement variableElement, Types typeUtils) {
        super(variableElement);
        this.type = variableElementToTypeName(variableElement, typeUtils);
        getFormatDirective(variableElement).ifPresent(this::addDirective);
    }

    public FieldDefinition(ExecutableElement executableElement, Types typeUtils) {
        super(executableElement);
        this.type = executableElementToTypeName(executableElement, typeUtils);
        setArguments(executableElementParametersToInputValues(executableElement, typeUtils));
        getFormatDirective(executableElement).ifPresent(this::addDirective);
        addDirective(
                new Directive()
                        .setName(DIRECTIVE_INVOKE_NAME)
                        .addArgument(DIRECTIVE_INVOKE_ARGUMENT_CLASS_NAME_NAME, executableElement.getEnclosingElement().toString())
                        .addArgument(DIRECTIVE_INVOKE_ARGUMENT_METHOD_NAME_NAME, executableElement.getSimpleName().toString())
                        .addArgument(
                                DIRECTIVE_INVOKE_ARGUMENT_PARAMETER_NAME,
                                new ArrayValueWithVariable(
                                        executableElement.getParameters().stream()
                                                .map(parameter ->
                                                        Map.of(
                                                                INPUT_INVOKE_PARAMETER_INPUT_VALUE_NAME_NAME,
                                                                parameter.getSimpleName().toString(),
                                                                INPUT_INVOKE_PARAMETER_INPUT_VALUE_CLASS_NAME_NAME,
                                                                getTypeNameFromTypeMirror(parameter.asType(), typeUtils)
                                                        )
                                                )
                                                .collect(Collectors.toList())
                                )
                        )
                        .addArgument(DIRECTIVE_INVOKE_ARGUMENT_RETURN_CLASS_NAME_NAME, getTypeNameFromTypeMirror(executableElement.getReturnType(), typeUtils))
        );
    }

    public Collection<InputValue> getArguments() {
        return Optional.ofNullable(argumentMap).map(Map::values).orElse(null);
    }

    public InputValue getArgument(String name) {
        return Optional.ofNullable(argumentMap).map(stringInputValueMap -> stringInputValueMap.get(name)).orElse(null);
    }

    public FieldDefinition setArguments(Collection<InputValue> arguments) {
        this.argumentMap = arguments.stream()
                .collect(
                        Collectors.toMap(
                                InputValue::getName,
                                inputValue -> inputValue,
                                (x, y) -> y,
                                LinkedHashMap::new
                        )
                );
        return this;
    }

    public FieldDefinition addArguments(Collection<InputValue> arguments) {
        if (this.argumentMap == null) {
            this.argumentMap = new LinkedHashMap<>();
        }
        this.argumentMap.putAll(
                (Map<? extends String, ? extends InputValue>) arguments.stream()
                        .collect(
                                Collectors.toMap(
                                        InputValue::getName,
                                        inputValue -> inputValue,
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                        )
        );
        return this;
    }

    public FieldDefinition addArgument(InputValue argument) {
        if (this.argumentMap == null) {
            this.argumentMap = new LinkedHashMap<>();
        }
        this.argumentMap.put(argument.getName(), argument);
        return this;
    }

    public Type getType() {
        return type;
    }

    public FieldDefinition setType(Type type) {
        this.type = type;
        return this;
    }

    public FieldDefinition setType(String typeName) {
        this.type = Type.of(typeName);
        return this;
    }

    public boolean hesDataType() {
        return hasDirective(DIRECTIVE_TYPE_NAME);
    }

    public Optional<String> getTypeName() {
        return Optional.ofNullable(getDirective(DIRECTIVE_TYPE_NAME))
                .flatMap(directive -> Optional.ofNullable(directive.getArgument(DIRECTIVE_TYPE_ARGUMENT_NAME_NAME)))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getString());
    }

    public String getTypeNameWithoutID() {
        String fieldTypeName = getTypeName().orElseGet(() -> getType().getTypeName().getName());
        if (SCALA_ID_NAME.equals(fieldTypeName)) {
            return SCALA_STRING_NAME;
        }
        return fieldTypeName;
    }

    public boolean isInvokeField() {
        return hasDirective(DIRECTIVE_INVOKE_NAME);
    }

    public Optional<String> getInvokeClassName() {
        return Stream.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive ->
                        Stream.ofNullable(directive.getArgument(DIRECTIVE_INVOKE_ARGUMENT_CLASS_NAME_NAME))
                )
                .filter(ValueWithVariable::isString)
                .findFirst()
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getInvokeClassNameOrError() {
        return getInvokeClassName().orElseThrow(() -> new GraphQLErrors(CLASS_NAME_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getInvokeMethodName() {
        return Stream.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive ->
                        Stream.ofNullable(directive.getArgument(DIRECTIVE_INVOKE_ARGUMENT_METHOD_NAME_NAME))
                )
                .filter(ValueWithVariable::isString)
                .findFirst()
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getInvokeMethodNameOrError() {
        return getInvokeMethodName().orElseThrow(() -> new GraphQLErrors(METHOD_NAME_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Stream<Map.Entry<String, String>> getInvokeParameters() {
        return Stream.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive ->
                        Stream.ofNullable(directive.getArgument(DIRECTIVE_INVOKE_ARGUMENT_PARAMETER_NAME))
                )
                .filter(ValueWithVariable::isArray)
                .flatMap(valueWithVariable -> valueWithVariable.asArray().getValueWithVariables().stream())
                .filter(ValueWithVariable::isObject)
                .map(ValueWithVariable::asObject)
                .map(objectValueWithVariable ->
                        new AbstractMap.SimpleEntry<>(
                                objectValueWithVariable.getString(INPUT_INVOKE_PARAMETER_INPUT_VALUE_NAME_NAME),
                                objectValueWithVariable.getString(INPUT_INVOKE_PARAMETER_INPUT_VALUE_CLASS_NAME_NAME)
                        )
                );
    }

    public List<Map.Entry<String, String>> getInvokeParametersList() {
        return getInvokeParameters().collect(Collectors.toList());
    }

    public Optional<String> getInvokeReturnClassName() {
        return Stream.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive ->
                        Stream.ofNullable(directive.getArgument(DIRECTIVE_INVOKE_ARGUMENT_RETURN_CLASS_NAME_NAME))
                )
                .filter(ValueWithVariable::isString)
                .findFirst()
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getInvokeReturnClassNameOrError() {
        return getInvokeReturnClassName().orElseThrow(() -> new GraphQLErrors(RETURN_CLASS_NAME_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public boolean isFunctionField() {
        return hasDirective(DIRECTIVE_FUNC_NAME);
    }

    public boolean isConnectionField() {
        return hasDirective(DIRECTIVE_CONNECTION_NAME);
    }

    public Optional<String> getConnectionField() {
        return Optional.ofNullable(getDirective(DIRECTIVE_CONNECTION_NAME).getArgument(DIRECTIVE_CONNECTION_ARGUMENT_FIELD_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getConnectionFieldOrError() {
        return getConnectionField().orElseThrow(() -> new GraphQLErrors(CONNECTION_FIELD_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getConnectionAgg() {
        return Optional.ofNullable(getDirective(DIRECTIVE_CONNECTION_NAME).getArgument(DIRECTIVE_CONNECTION_ARGUMENT_AGG_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getConnectionAggOrError() {
        return getConnectionAgg().orElseThrow(() -> new GraphQLErrors(CONNECTION_AGG_FIELD_NOT_EXIST.bind(toString())));
    }

    public boolean isAggregateField() {
        return hasDirective(DIRECTIVE_AGGREGATE_NAME);
    }

    public boolean isMapField() {
        return hasDirective(DIRECTIVE_MAP_NAME);
    }

    public boolean isMapAnchor() {
        return hasDirective(DIRECTIVE_MAP_NAME) && getDirective(DIRECTIVE_MAP_NAME).hasArgument(DIRECTIVE_MAP_ARGUMENT_ANCHOR_NAME);
    }

    public boolean hasMapWith() {
        return hasDirective(DIRECTIVE_MAP_NAME) && getDirective(DIRECTIVE_MAP_NAME).hasArgument(DIRECTIVE_MAP_ARGUMENT_WITH_NAME);
    }

    public String getMapFrom() {
        return getDirective(DIRECTIVE_MAP_NAME).getArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME).asString().getString();
    }

    public Optional<String> getMapTo() {
        return Optional.ofNullable(getDirective(DIRECTIVE_MAP_NAME).getArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME))
                .map(valueWithVariable -> valueWithVariable.asString().getString());
    }

    public String getMapToOrError() {
        return getMapTo().orElseThrow(() -> new RuntimeException("map to argument not found: " + getName()));
    }

    public String getMapWithType() {
        return getDirective(DIRECTIVE_MAP_NAME)
                .getArgument(DIRECTIVE_MAP_ARGUMENT_WITH_NAME).asObject()
                .getValueWithVariable(INPUT_WITH_INPUT_VALUE_TYPE_NAME).asString()
                .getString();
    }

    public String getMapWithFrom() {
        return getDirective(DIRECTIVE_MAP_NAME)
                .getArgument(DIRECTIVE_MAP_ARGUMENT_WITH_NAME).asObject()
                .getValueWithVariable(DIRECTIVE_MAP_ARGUMENT_FROM_NAME).asString()
                .getString();
    }

    public String getMapWithTo() {
        return getDirective(DIRECTIVE_MAP_NAME)
                .getArgument(DIRECTIVE_MAP_ARGUMENT_WITH_NAME).asObject()
                .getValueWithVariable(DIRECTIVE_MAP_ARGUMENT_TO_NAME).asString()
                .getString();
    }

    public boolean isFetchField() {
        return hasDirective(DIRECTIVE_FETCH_NAME);
    }

    public boolean isFetchAnchor() {
        return hasDirective(DIRECTIVE_FETCH_NAME) && getDirective(DIRECTIVE_FETCH_NAME).hasArgument(DIRECTIVE_FETCH_ARGUMENT_ANCHOR_NAME);
    }

    public boolean hasFetchWith() {
        return hasDirective(DIRECTIVE_FETCH_NAME) && getDirective(DIRECTIVE_FETCH_NAME).hasArgument(DIRECTIVE_FETCH_ARGUMENT_WITH_NAME);
    }

    public String getFetchFrom() {
        return getDirective(DIRECTIVE_FETCH_NAME).getArgument(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME).asString().getString();
    }

    public Optional<String> getFetchTo() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FETCH_NAME).getArgument(DIRECTIVE_FETCH_ARGUMENT_TO_NAME))
                .map(valueWithVariable -> valueWithVariable.asString().getString());
    }

    public String getFetchToOrError() {
        return getFetchTo().orElseThrow(() -> new RuntimeException("fetch to argument not found: " + getName()));
    }

    public String getFetchWithType() {
        return getDirective(DIRECTIVE_FETCH_NAME)
                .getArgument(DIRECTIVE_FETCH_ARGUMENT_WITH_NAME).asObject()
                .getValueWithVariable(DIRECTIVE_FETCH_ARGUMENT_WITH_TYPE_NAME).asString()
                .getString();
    }

    public String getFetchWithFrom() {
        return getDirective(DIRECTIVE_FETCH_NAME)
                .getArgument(DIRECTIVE_FETCH_ARGUMENT_WITH_NAME).asObject()
                .getValueWithVariable(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME).asString()
                .getString();
    }

    public String getFetchWithTo() {
        return getDirective(DIRECTIVE_FETCH_NAME)
                .getArgument(DIRECTIVE_FETCH_ARGUMENT_WITH_NAME).asObject()
                .getValueWithVariable(DIRECTIVE_FETCH_ARGUMENT_TO_NAME).asString()
                .getString();
    }

    public String getFetchProtocol() {
        return getDirective(DIRECTIVE_FETCH_NAME).getArgument(DIRECTIVE_FETCH_ARGUMENT_PROTOCOL_NAME).asString().getString();
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("fieldDefinitionDefinition");
        st.add("filedDefinition", this);
        return st.render();
    }
}
