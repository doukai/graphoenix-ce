package io.graphoenix.spi.graphql.operation;

import com.google.common.collect.Iterators;
import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.type.VariableDefinition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.*;
import static io.graphoenix.spi.utils.DocumentUtil.graphqlToOperation;

public class Operation extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/operation/Operation.stg");
    private String operationType;
    private Map<String, VariableDefinition> variableDefinitionMap;
    private final Collection<Selection> selections = new LinkedHashSet<>();

    public Operation() {
        super();
    }

    public Operation(String name) {
        super(name);
    }

    public static Operation fromString(String graphQL) {
        return new Operation(graphqlToOperation(graphQL));
    }

    public Operation(GraphqlParser.OperationDefinitionContext operationDefinitionContext) {
        super(operationDefinitionContext.name(), null, operationDefinitionContext.directives());
        if (operationDefinitionContext.operationType() != null) {
            this.operationType = operationDefinitionContext.operationType().getText();
        } else {
            this.operationType = OPERATION_QUERY_NAME;
        }
        if (operationDefinitionContext.variableDefinitions() != null) {
            setVariableDefinitions(
                    operationDefinitionContext.variableDefinitions().variableDefinition().stream()
                            .map(VariableDefinition::new)
                            .collect(Collectors.toList())
            );
        }
        setSelections(
                operationDefinitionContext.selectionSet().selection().stream()
                        .map(Selection::of)
                        .collect(Collectors.toList())
        );
    }

    public String getOperationType() {
        return operationType;
    }

    public Operation setOperationType(String operationType) {
        this.operationType = operationType;
        return this;
    }

    public VariableDefinition getVariableDefinition(String name) {
        return Optional.ofNullable(variableDefinitionMap).map(stringVariableDefinitionMap -> stringVariableDefinitionMap.get(name)).orElse(null);
    }

    public Collection<VariableDefinition> getVariableDefinitions() {
        return Optional.ofNullable(variableDefinitionMap).map(Map::values).orElse(null);
    }

    public Operation setVariableDefinitions(Collection<VariableDefinition> variableDefinitions) {
        if (variableDefinitions != null) {
            this.variableDefinitionMap = variableDefinitions.stream()
                    .collect(
                            Collectors.toMap(
                                    variableDefinition -> variableDefinition.getVariable().getName(),
                                    variableDefinition -> variableDefinition,
                                    (x, y) -> y,
                                    LinkedHashMap::new
                            )
                    );
        }
        return this;
    }

    public Operation addVariableDefinitions(Collection<VariableDefinition> variableDefinitions) {
        if (this.variableDefinitionMap == null) {
            this.variableDefinitionMap = new LinkedHashMap<>();
        }
        this.variableDefinitionMap.putAll(
                (Map<? extends String, ? extends VariableDefinition>) variableDefinitions.stream()
                        .collect(
                                Collectors.toMap(
                                        variableDefinition -> variableDefinition.getVariable().getName(),
                                        variableDefinition -> variableDefinition,
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                        )
        );
        return this;
    }

    public Operation addVariableDefinition(VariableDefinition variableDefinition) {
        if (this.variableDefinitionMap == null) {
            this.variableDefinitionMap = new LinkedHashMap<>();
        }
        this.variableDefinitionMap.put(variableDefinition.getVariable().getName(), variableDefinition);
        return this;
    }

    public Collection<Selection> getSelections() {
        return selections.stream().filter(Selection::isInclude).collect(Collectors.toList());
    }

    public Collection<Field> getFields() {
        return getSelections().stream()
                .filter(Selection::isField)
                .map(Selection::asField)
                .collect(Collectors.toList());
    }

    public Selection getSelection(int index) {
        return Iterators.get(getSelections().iterator(), index);
    }

    public Collection<Fragment> getFragments() {
        return getSelections().stream()
                .filter(Selection::isFragment)
                .map(Selection::asFragment)
                .collect(Collectors.toList());
    }

    public Operation setSelections(Collection<? extends Selection> selections) {
        this.selections.clear();
        this.selections.addAll(selections);
        return this;
    }

    public Operation addSelections(Collection<? extends Selection> selections) {
        this.selections.addAll(selections);
        return this;
    }

    public Field getField(String name) {
        return getSelections().stream()
                .filter(Selection::isField)
                .map(selection -> (Field) selection)
                .filter(field -> field.getAlias() != null && field.getAlias().equals(name) || field.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Fragment getFragment(String name) {
        return getSelections().stream()
                .filter(Selection::isFragment)
                .map(selection -> (Fragment) selection)
                .filter(fragment ->fragment.getFragmentName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Operation addSelection(Selection selection) {
        this.selections.add(selection);
        return this;
    }

    public Operation mergeSelection(Collection<Field> fields) {
        setSelections(
                Stream.ofNullable(fields)
                        .flatMap(Collection::stream)
                        .reduce(
                                getSelections(),
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

    public Optional<Integer> getInvokeIndexName() {
        return Optional.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_INVOKE_ARGUMENT_METHOD_INDEX_NAME))
                .filter(ValueWithVariable::isInt)
                .map(valueWithVariable -> valueWithVariable.asInt().getIntegerValue());
    }

    public Integer getInvokeMethodIndexOrError() {
        return getInvokeIndexName().orElseThrow(() -> new GraphQLErrors(METHOD_NAME_ARGUMENT_NOT_EXIST.bind(toString())));
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

    public Stream<String> getInvokeThrownTypes() {
        return Stream.ofNullable(getDirective(DIRECTIVE_INVOKE_NAME))
                .flatMap(directive -> directive.getArgumentOrEmpty(DIRECTIVE_INVOKE_ARGUMENT_THROWN_TYPES_NAME).stream())
                .filter(ValueWithVariable::isArray)
                .map(ValueWithVariable::asArray)
                .flatMap(arrayValueWithVariable -> arrayValueWithVariable.getValueWithVariables().stream())
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> valueWithVariable.asString().getValue());
    }

    public boolean isQuery() {
        return OPERATION_QUERY_NAME.equals(operationType);
    }

    public boolean isMutation() {
        return OPERATION_MUTATION_NAME.equals(operationType);
    }

    public boolean isSubscription() {
        return OPERATION_SUBSCRIPTION_NAME.equals(operationType);
    }

    @Override
    public boolean isOperation() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("operationDefinition");
        st.add("operation", this);
        return st.render();
    }
}
