package io.graphoenix.core.handler;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.*;
import io.nozdormu.spi.context.BeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Provider;
import jakarta.json.JsonValue;
import jakarta.transaction.Transactional;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;

@ApplicationScoped
@Default
public class DefaultOperationHandler implements OperationHandler {

    private static final GraphQLConfig graphQLConfig = BeanContext.get(GraphQLConfig.class);

    private static final List<OperationBeforeHandler> operationBeforeHandlerList = BeanContext.getMap(OperationBeforeHandler.class).values().stream()
            .sorted(Comparator.comparing(handler -> getPriority(handler.getClass())))
            .collect(Collectors.toList());

    private static final List<OperationAfterHandler> operationAfterHandlerList = BeanContext.getMap(OperationAfterHandler.class).values().stream()
            .sorted(Comparator.comparing(handler -> getPriority(handler.getClass())))
            .collect(Collectors.toList());

    private static final List<QueryBeforeHandler> queryBeforeHandlerList = BeanContext.getMap(QueryBeforeHandler.class).values().stream()
            .sorted(Comparator.comparing(handler -> getPriority(handler.getClass())))
            .collect(Collectors.toList());

    private static final List<QueryAfterHandler> queryAfterHandlerList = BeanContext.getMap(QueryAfterHandler.class).values().stream()
            .sorted(Comparator.comparing(handler -> getPriority(handler.getClass())))
            .collect(Collectors.toList());

    private static final List<MutationBeforeHandler> mutationBeforeHandlerList = BeanContext.getMap(MutationBeforeHandler.class).values().stream()
            .sorted(Comparator.comparing(handler -> getPriority(handler.getClass())))
            .collect(Collectors.toList());

    private static final List<MutationAfterHandler> mutationAfterHandlerList = BeanContext.getMap(MutationAfterHandler.class).values().stream()
            .sorted(Comparator.comparing(handler -> getPriority(handler.getClass())))
            .collect(Collectors.toList());

    private static final QueryHandler queryHandler = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
            .map(name -> BeanContext.get(QueryHandler.class, name))
            .orElseGet(() -> BeanContext.get(QueryHandler.class));

    private static final Provider<MutationHandler> mutationHandler = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
            .map(name -> BeanContext.getProvider(MutationHandler.class, name))
            .orElseGet(() -> BeanContext.getProvider(MutationHandler.class));

    private static int getPriority(Class<?> type) {
        return Optional.ofNullable(type.getAnnotation(Priority.class)).map(Priority::value).orElse(Integer.MAX_VALUE);
    }

    @Override
    public Publisher<JsonValue> handle(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables, null, null);
    }

    @Override
    public Publisher<JsonValue> handle(Operation operation, Map<String, JsonValue> variables, String token, String operationId) {
        switch (operation.getOperationType()) {
            case OPERATION_QUERY_NAME:
                return query(operation, variables);
            case OPERATION_MUTATION_NAME:
                return mutation(operation, variables);
            case OPERATION_SUBSCRIPTION_NAME:
                return subscription(operation, variables, token, operationId);
            default:
                throw new GraphQLErrors(UNSUPPORTED_OPERATION_TYPE.bind(operation.getOperationType()));
        }
    }

    public Mono<JsonValue> query(Operation operation, Map<String, JsonValue> variables) {
        return Flux.fromIterable(operationBeforeHandlerList)
                .reduce(
                        Mono.just(operation),
                        (pre, cur) -> pre.flatMap(result -> cur.query(result, variables))
                )
                .flatMap(operationMono -> operationMono)
                .flatMap(operationAfterHandler ->
                        Flux.fromIterable(queryBeforeHandlerList)
                                .reduce(
                                        Mono.just(operationAfterHandler),
                                        (pre, cur) -> pre.flatMap(result -> cur.query(result, variables))
                                )
                                .flatMap(operationMono -> operationMono)
                )
                .flatMap(operationAfterHandler ->
                        queryHandler.query(operationAfterHandler)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(queryAfterHandlerList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) -> pre.flatMap(result -> cur.query(operationAfterHandler, jsonValue))
                                                )
                                                .flatMap(jsonValueMono -> jsonValueMono)
                                                .flatMap(jsonValueAfterHandler ->
                                                        Flux.fromIterable(operationAfterHandlerList)
                                                                .reduce(
                                                                        Mono.just(jsonValueAfterHandler),
                                                                        (pre, cur) -> pre.flatMap(result -> cur.query(operationAfterHandler, jsonValueAfterHandler))
                                                                )
                                                                .flatMap(operationMono -> operationMono)
                                                )
                                )
                );
    }

    @Transactional
    public Mono<JsonValue> mutation(Operation operation, Map<String, JsonValue> variables) {
        return Flux.fromIterable(operationBeforeHandlerList)
                .reduce(
                        Mono.just(operation),
                        (pre, cur) -> pre.flatMap(result -> cur.mutation(result, variables))
                )
                .flatMap(operationMono -> operationMono)
                .flatMap(operationAfterHandler ->
                        Flux.fromIterable(mutationBeforeHandlerList)
                                .reduce(
                                        Mono.just(operationAfterHandler),
                                        (pre, cur) -> pre.flatMap(result -> cur.mutation(result, variables))
                                )
                                .flatMap(operationMono -> operationMono)
                )
                .flatMap(operationAfterHandler ->
                        mutationHandler.get().mutation(operationAfterHandler)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(mutationAfterHandlerList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) -> pre.flatMap(result -> cur.mutation(operationAfterHandler, jsonValue))
                                                )
                                                .flatMap(jsonValueMono -> jsonValueMono)
                                                .flatMap(jsonValueAfterHandler ->
                                                        Flux.fromIterable(operationAfterHandlerList)
                                                                .reduce(
                                                                        Mono.just(jsonValueAfterHandler),
                                                                        (pre, cur) -> pre.flatMap(result -> cur.mutation(operationAfterHandler, jsonValueAfterHandler))
                                                                )
                                                                .flatMap(operationMono -> operationMono)
                                                )
                                )
                );
    }

    public Flux<JsonValue> subscription(Operation operation, Map<String, JsonValue> variables, String token, String operationId) {
        return null;
    }
}
