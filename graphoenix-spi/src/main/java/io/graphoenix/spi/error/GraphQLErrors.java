package io.graphoenix.spi.error;

import org.eclipse.microprofile.graphql.GraphQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GraphQLErrors extends RuntimeException {

    private Object data;

    private List<GraphQLError> errors = new ArrayList<>();

    public GraphQLErrors() {
    }

    public GraphQLErrors(GraphQLErrorType graphQLErrorType) {
        this.errors.add(new GraphQLError(graphQLErrorType.toString()));
    }

    public GraphQLErrors(GraphQLErrorType graphQLErrorType, int line, int column) {
        this.errors.add(new GraphQLError(graphQLErrorType.toString(), line, column));
    }

    public GraphQLErrors(Object data, GraphQLErrorType graphQLErrorType) {
        this.data = data;
        this.errors.add(new GraphQLError(graphQLErrorType.toString()));
    }

    public GraphQLErrors(Object data, GraphQLErrorType graphQLErrorType, int line, int column) {
        this.data = data;
        this.errors.add(new GraphQLError(graphQLErrorType.toString(), line, column));
    }

    public GraphQLErrors(GraphQLErrors graphQLErrors) {
        this.data = graphQLErrors.getData();
        this.errors = graphQLErrors.getErrors();
    }

    public GraphQLErrors(GraphQLException graphQLException) {
        this.data = graphQLException.getPartialResults();
        this.errors.add(new GraphQLError(graphQLException.getMessage()));
    }

    public GraphQLErrors(Throwable throwable) {
        addThrowable(throwable);
    }

    public GraphQLErrors(Integer code, String message) {
        this.errors.add(new GraphQLError(code, message));
    }

    public GraphQLErrors(String message) {
        this.errors.add(new GraphQLError(message));
    }

    public void addThrowable(Throwable throwable) {
        if (throwable.getCause() != null) {
            addThrowable(throwable.getCause());
        } else if (throwable instanceof GraphQLErrors) {
            this.data = ((GraphQLErrors) throwable).getData();
            this.errors = ((GraphQLErrors) throwable).getErrors();
        } else if (throwable instanceof GraphQLException) {
            this.data = ((GraphQLException) throwable).getPartialResults();
            this.errors.add(new GraphQLError(throwable));
        } else {
            this.errors.add(new GraphQLError(throwable));
        }
    }

    public GraphQLErrors add(GraphQLErrorType graphQLErrorType) {
        this.errors.add(new GraphQLError(graphQLErrorType.toString()));
        return this;
    }

    public GraphQLErrors add(GraphQLErrorType graphQLErrorType, List<GraphQLLocation> locations, List<String> path) {
        this.errors.add(new GraphQLError(graphQLErrorType.toString(), locations, path));
        return this;
    }

    public GraphQLErrors add(GraphQLErrorType graphQLErrorType, List<GraphQLLocation> locations) {
        this.errors.add(new GraphQLError(graphQLErrorType.toString(), locations));
        return this;
    }

    public GraphQLErrors add(GraphQLErrorType graphQLErrorType, int line, int column) {
        this.errors.add(new GraphQLError(graphQLErrorType.toString(), line, column));
        return this;
    }

    public GraphQLErrors add(GraphQLError graphQLError) {
        this.errors.add(graphQLError);
        return this;
    }

    public GraphQLErrors addAll(Collection<GraphQLError> graphQLErrors) {
        this.errors.addAll(graphQLErrors);
        return this;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<GraphQLError> getErrors() {
        return errors;
    }

    @Override
    public String getMessage() {
        return this.errors.stream().map(GraphQLError::getMessage).collect(Collectors.joining("\r\n"));
    }

    @Override
    public String toString() {
        return "GraphQLErrors{" +
                "data=" + data +
                ", errors=" + errors +
                '}';
    }
}
