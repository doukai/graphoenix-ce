package io.graphoenix.spi.graphql.type;

import com.google.common.collect.Iterators;
import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.common.*;
import io.nozdormu.spi.async.Async;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

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

    public FieldDefinition(VariableElement variableElement, Types types) {
        super(variableElement);
        this.type = variableElementToTypeName(variableElement, types);
        getFormatDirective(variableElement).ifPresent(this::addDirective);
    }

    public FieldDefinition(ExecutableElement executableElement, Types types) {
        super(executableElement);
        this.type = executableElementToTypeName(executableElement, types);
        setArguments(executableElementParametersToInputValues(executableElement, types));
        getFormatDirective(executableElement).ifPresent(this::addDirective);
        boolean async = executableElement.getAnnotation(Async.class) != null;
        addDirective(
                new Directive()
                        .setName(DIRECTIVE_INVOKE_NAME)
                        .addArgument(DIRECTIVE_INVOKE_ARGUMENT_CLASS_NAME_NAME, executableElement.getEnclosingElement().toString())
                        .addArgument(DIRECTIVE_INVOKE_ARGUMENT_METHOD_NAME_NAME, async ? getAsyncMethodName(executableElement, types) : executableElement.getSimpleName().toString())
                        .addArgument(
                                DIRECTIVE_INVOKE_ARGUMENT_PARAMETER_NAME,
                                new ArrayValueWithVariable(
                                        executableElement.getParameters().stream()
                                                .map(parameter ->
                                                        Map.of(
                                                                INPUT_INVOKE_PARAMETER_INPUT_VALUE_NAME_NAME,
                                                                parameter.getSimpleName().toString(),
                                                                INPUT_INVOKE_PARAMETER_INPUT_VALUE_CLASS_NAME_NAME,
                                                                getTypeWithArgumentsName(parameter.asType(), types)
                                                        )
                                                )
                                                .collect(Collectors.toList())
                                )
                        )
                        .addArgument(DIRECTIVE_INVOKE_ARGUMENT_RETURN_CLASS_NAME_NAME, getTypeWithArgumentsName(executableElement.getReturnType(), types))
                        .addArgument(DIRECTIVE_INVOKE_ASYNC_NAME, async)
        );

        if (executableElement.getAnnotation(PermitAll.class) != null) {
            addDirective(new Directive(DIRECTIVE_PERMIT_ALL));
        }
        if (executableElement.getAnnotation(DenyAll.class) != null) {
            addDirective(new Directive(DIRECTIVE_DENY_ALL));
        }
        if (executableElement.getAnnotation(RolesAllowed.class) != null) {
            Directive directive = new Directive()
                    .setName(DIRECTIVE_ROLES_ALLOWED)
                    .addArgument("roles", executableElement.getAnnotation(RolesAllowed.class).value());
            addDirective(directive);
        }
    }

    public Collection<InputValue> getArguments() {
        return Optional.ofNullable(argumentMap).map(Map::values).orElse(null);
    }

    public InputValue getArgument(int index) {
        return Iterators.get(argumentMap.values().iterator(), index);
    }

    public InputValue getArgument(String name) {
        return getArgumentOrEmpty(name).orElse(null);
    }

    public Optional<InputValue> getArgumentOrEmpty(String name) {
        return Optional.ofNullable(argumentMap).map(arguments -> arguments.get(name));
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

    public boolean hasOptions() {
        return hasDirective(DIRECTIVE_OPTIONS_NAME);
    }

    public Optional<String> getTypeName() {
        return Optional.ofNullable(getDirective(DIRECTIVE_OPTIONS_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_OPTIONS_ARGUMENT_TYPE_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public Optional<String> getDefault() {
        return Optional.ofNullable(getDirective(DIRECTIVE_OPTIONS_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_OPTIONS_ARGUMENT_DEFAULT_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getString());
    }

    public Optional<Integer> getLength() {
        return Optional.ofNullable(getDirective(DIRECTIVE_OPTIONS_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_OPTIONS_ARGUMENT_LENGTH_NAME))
                .filter(ValueWithVariable::isInt)
                .map(valueWithVariable -> valueWithVariable.asInt().getIntegerValue());
    }

    public Optional<Integer> getDecimals() {
        return Optional.ofNullable(getDirective(DIRECTIVE_OPTIONS_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_OPTIONS_ARGUMENT_DECIMALS_NAME))
                .filter(ValueWithVariable::isInt)
                .map(valueWithVariable -> valueWithVariable.asInt().getIntegerValue());
    }

    public Optional<EnumValue> getProtocol() {
        return Optional.ofNullable(getDirective(DIRECTIVE_OPTIONS_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_OPTIONS_ARGUMENT_PROTOCOL_NAME))
                .filter(ValueWithVariable::isEnum)
                .map(ValueWithVariable::asEnum);
    }

    public boolean isAutoIncrement() {
        return Optional.ofNullable(getDirective(DIRECTIVE_OPTIONS_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_OPTIONS_ARGUMENT_AUTO_INCREMENT_NAME))
                .filter(ValueWithVariable::isBoolean)
                .map(valueWithVariable -> valueWithVariable.asBoolean().getValue())
                .orElse(false);
    }

    public boolean isUnique() {
        return Optional.ofNullable(getDirective(DIRECTIVE_OPTIONS_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_OPTIONS_ARGUMENT_UNIQUE_NAME))
                .filter(ValueWithVariable::isBoolean)
                .map(valueWithVariable -> valueWithVariable.asBoolean().getValue())
                .orElse(false);
    }

    public String getTypeNameWithoutID() {
        String fieldTypeName = getTypeName().orElseGet(() -> getType().getTypeName().getName());
        if (SCALA_ID_NAME.equals(fieldTypeName)) {
            return SCALA_STRING_NAME;
        }
        return fieldTypeName;
    }

    public String getCursorTypeNameWithoutID() {
        String fieldTypeName = getType().getTypeName().getName();
        if (SCALA_ID_NAME.equals(fieldTypeName)) {
            return SCALA_STRING_NAME;
        }
        return fieldTypeName;
    }

    public boolean isInvokeField() {
        return hasDirective(DIRECTIVE_INVOKE_NAME);
    }

    public Optional<String> getInvokeClassName() {
        return Optional.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_INVOKE_ARGUMENT_CLASS_NAME_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getInvokeClassNameOrError() {
        return getInvokeClassName().orElseThrow(() -> new GraphQLErrors(CLASS_NAME_DIRECTIVE_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getInvokeMethodName() {
        return Optional.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_INVOKE_ARGUMENT_METHOD_NAME_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getInvokeMethodNameOrError() {
        return getInvokeMethodName().orElseThrow(() -> new GraphQLErrors(METHOD_NAME_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Stream<Map.Entry<String, String>> getInvokeParameters() {
        return Stream.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_INVOKE_ARGUMENT_PARAMETER_NAME).stream())
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
        return Optional.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_INVOKE_ARGUMENT_RETURN_CLASS_NAME_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getInvokeReturnClassNameOrError() {
        return getInvokeReturnClassName().orElseThrow(() -> new GraphQLErrors(RETURN_CLASS_NAME_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public boolean isAsyncInvoke() {
        return Optional.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_INVOKE_ASYNC_NAME))
                .filter(ValueWithVariable::isBoolean)
                .map(valueWithVariable -> valueWithVariable.asBoolean().getValue())
                .orElse(false);
    }

    public boolean isFunctionField() {
        return hasDirective(DIRECTIVE_FUNC_NAME);
    }

    public boolean isGroupFunctionField() {
        return hasDirective(DIRECTIVE_FUNC_NAME) &&
                Stream.of(SUFFIX_COUNT, SUFFIX_SUM, SUFFIX_AVG, SUFFIX_MAX, SUFFIX_MIN)
                        .anyMatch(name -> name.toUpperCase().equals(getFunctionNameOrError()));
    }

    public Optional<String> getFunctionName() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FUNC_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_FUNC_ARGUMENT_NAME_NAME))
                .filter(ValueWithVariable::isEnum)
                .map(valueWithVariable -> valueWithVariable.asEnum().getValue());
    }

    public String getFunctionNameOrError() {
        return getFunctionName().orElseThrow(() -> new GraphQLErrors(FUNC_NAME_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getFunctionField() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FUNC_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_FUNC_ARGUMENT_FIELD_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getFunctionFieldOrError() {
        return getFunctionField().orElseThrow(() -> new GraphQLErrors(FUNC_FIELD_NOT_EXIST.bind(toString())));
    }

    public boolean isConnectionField() {
        return hasDirective(DIRECTIVE_CONNECTION_NAME);
    }

    public Optional<String> getConnectionField() {
        return Optional.ofNullable(getDirective(DIRECTIVE_CONNECTION_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_CONNECTION_ARGUMENT_FIELD_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getConnectionFieldOrError() {
        return getConnectionField().orElseThrow(() -> new GraphQLErrors(CONNECTION_FIELD_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getConnectionAgg() {
        return Optional.ofNullable(getDirective(DIRECTIVE_CONNECTION_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_CONNECTION_ARGUMENT_AGG_NAME))
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

    public Optional<String> getMapFrom() {
        return Optional.ofNullable(getDirective(DIRECTIVE_MAP_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_MAP_ARGUMENT_FROM_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getMapFromOrError() {
        return getMapFrom().orElseThrow(() -> new GraphQLErrors(MAP_FROM_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getMapTo() {
        return Optional.ofNullable(getDirective(DIRECTIVE_MAP_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_MAP_ARGUMENT_TO_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getMapToOrError() {
        return getMapTo().orElseThrow(() -> new GraphQLErrors(MAP_TO_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getMapWithType() {
        return Optional.ofNullable(getDirective(DIRECTIVE_MAP_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_MAP_ARGUMENT_WITH_NAME))
                .filter(ValueWithVariable::isObject)
                .flatMap(valueWithVariable -> valueWithVariable.asObject().getValueWithVariableOrEmpty(INPUT_WITH_INPUT_VALUE_TYPE_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getMapWithTypeOrError() {
        return getMapWithType().orElseThrow(() -> new GraphQLErrors(MAP_WITH_TYPE_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getMapWithFrom() {
        return Optional.ofNullable(getDirective(DIRECTIVE_MAP_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_MAP_ARGUMENT_WITH_NAME))
                .filter(ValueWithVariable::isObject)
                .flatMap(valueWithVariable -> valueWithVariable.asObject().getValueWithVariableOrEmpty(DIRECTIVE_MAP_ARGUMENT_FROM_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getMapWithFromOrError() {
        return getMapWithFrom().orElseThrow(() -> new GraphQLErrors(MAP_WITH_FROM_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getMapWithTo() {
        return Optional.ofNullable(getDirective(DIRECTIVE_MAP_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_MAP_ARGUMENT_WITH_NAME))
                .filter(ValueWithVariable::isObject)
                .flatMap(valueWithVariable -> valueWithVariable.asObject().getValueWithVariableOrEmpty(DIRECTIVE_MAP_ARGUMENT_TO_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getMapWithToOrError() {
        return getMapWithTo().orElseThrow(() -> new GraphQLErrors(MAP_WITH_TO_ARGUMENT_NOT_EXIST.bind(toString())));
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

    public Optional<String> getFetchFrom() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FETCH_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getFetchFromOrError() {
        return getFetchFrom().orElseThrow(() -> new GraphQLErrors(FETCH_FROM_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getFetchTo() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FETCH_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_FETCH_ARGUMENT_TO_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getFetchToOrError() {
        return getFetchTo().orElseThrow(() -> new GraphQLErrors(FETCH_TO_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getFetchWithType() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FETCH_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_FETCH_ARGUMENT_WITH_NAME))
                .filter(ValueWithVariable::isObject)
                .flatMap(valueWithVariable -> valueWithVariable.asObject().getValueWithVariableOrEmpty(DIRECTIVE_FETCH_ARGUMENT_WITH_TYPE_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getFetchWithTypeOrError() {
        return getFetchWithType().orElseThrow(() -> new GraphQLErrors(FETCH_WITH_TYPE_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getFetchWithFrom() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FETCH_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_FETCH_ARGUMENT_WITH_NAME))
                .filter(ValueWithVariable::isObject)
                .flatMap(valueWithVariable -> valueWithVariable.asObject().getValueWithVariableOrEmpty(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getFetchWithFromOrError() {
        return getFetchWithFrom().orElseThrow(() -> new GraphQLErrors(FETCH_WITH_FROM_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Optional<String> getFetchWithTo() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FETCH_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_FETCH_ARGUMENT_WITH_NAME))
                .filter(ValueWithVariable::isObject)
                .flatMap(valueWithVariable -> valueWithVariable.asObject().getValueWithVariableOrEmpty(DIRECTIVE_FETCH_ARGUMENT_TO_NAME))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public String getFetchWithToOrError() {
        return getFetchWithTo().orElseThrow(() -> new GraphQLErrors(FETCH_WITH_TO_ARGUMENT_NOT_EXIST.bind(toString())));
    }

    public Optional<EnumValue> getFetchProtocol() {
        return Optional.ofNullable(getDirective(DIRECTIVE_FETCH_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_FETCH_ARGUMENT_PROTOCOL_NAME))
                .map(ValueWithVariable::asEnum);
    }

    public EnumValue getFetchProtocolOrError() {
        return getFetchProtocol().orElseThrow(() -> new GraphQLErrors(FETCH_PROTOCOL_ARGUMENT_NOT_EXIST.bind(toString())));
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

    public boolean isPermitAll() {
        return hasDirective(DIRECTIVE_PERMIT_ALL);
    }

    public boolean isDenyAll() {
        return hasDirective(DIRECTIVE_DENY_ALL);
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("fieldDefinitionDefinition");
        st.add("fieldDefinition", this);
        return st.render();
    }
}
