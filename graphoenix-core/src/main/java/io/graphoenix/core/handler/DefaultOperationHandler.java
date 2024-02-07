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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;

@ApplicationScoped
@Default
public class DefaultOperationHandler implements OperationHandler {

    private static final GraphQLConfig graphQLConfig = BeanContext.get(GraphQLConfig.class);

    private static final List<Provider<OperationBeforeHandler>> operationBeforeHandlerProviderList = BeanContext.getPriorityProviderList(OperationBeforeHandler.class);

    private static final List<Provider<OperationAfterHandler>> operationAfterHandlerProviderList = BeanContext.getPriorityProviderList(OperationAfterHandler.class);

    private static final List<Provider<QueryBeforeHandler>> queryBeforeHandlerProviderList = BeanContext.getPriorityProviderList(QueryBeforeHandler.class);

    private static final List<Provider<QueryAfterHandler>> queryAfterHandlerProviderList = BeanContext.getPriorityProviderList(QueryAfterHandler.class);

    private static final List<Provider<MutationBeforeHandler>> mutationBeforeHandlerProviderList = BeanContext.getPriorityProviderList(MutationBeforeHandler.class);

    private static final List<Provider<MutationAfterHandler>> mutationAfterHandlerProviderList = BeanContext.getPriorityProviderList(MutationAfterHandler.class);

    private static final Provider<QueryHandler> queryHandlerProvider = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
            .map(name -> BeanContext.getProvider(QueryHandler.class, name))
            .orElseGet(() -> BeanContext.getProvider(QueryHandler.class));

    private static final Provider<MutationHandler> mutationHandlerProvider = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
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
        return Flux.fromIterable(operationBeforeHandlerProviderList)
                .reduce(
                        Mono.just(operation),
                        (pre, cur) -> pre.flatMap(result -> cur.get().query(result, variables))
                )
                .flatMap(operationMono -> operationMono)
                .flatMap(operationAfterHandler ->
                        Flux.fromIterable(queryBeforeHandlerProviderList)
                                .reduce(
                                        Mono.just(operationAfterHandler),
                                        (pre, cur) -> pre.flatMap(result -> cur.get().query(result, variables))
                                )
                                .flatMap(operationMono -> operationMono)
                )
                .flatMap(operationAfterHandler ->
                        queryHandlerProvider.get().query(operationAfterHandler)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(queryAfterHandlerProviderList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) -> pre.flatMap(result -> cur.get().query(operationAfterHandler, result))
                                                )
                                                .flatMap(jsonValueMono -> jsonValueMono)
                                                .flatMap(jsonValueAfterHandler ->
                                                        Flux.fromIterable(operationAfterHandlerProviderList)
                                                                .reduce(
                                                                        Mono.just(jsonValueAfterHandler),
                                                                        (pre, cur) -> pre.flatMap(result -> cur.get().query(operationAfterHandler, result))
                                                                )
                                                                .flatMap(operationMono -> operationMono)
                                                )
                                )
                );
    }

    @Transactional
    public Mono<JsonValue> mutation(Operation operation, Map<String, JsonValue> variables) {
        return Flux.fromIterable(operationBeforeHandlerProviderList)
                .reduce(
                        Mono.just(operation),
                        (pre, cur) -> pre.flatMap(result -> cur.get().mutation(result, variables))
                )
                .flatMap(operationMono -> operationMono)
                .flatMap(operationAfterHandler ->
                        Flux.fromIterable(mutationBeforeHandlerProviderList)
                                .reduce(
                                        Mono.just(operationAfterHandler),
                                        (pre, cur) -> pre.flatMap(result -> cur.get().mutation(result, variables))
                                )
                                .flatMap(operationMono -> operationMono)
                )
                .flatMap(operationAfterHandler ->
                        mutationHandlerProvider.get().mutation(operationAfterHandler)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(mutationAfterHandlerProviderList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) -> pre.flatMap(result -> cur.get().mutation(operationAfterHandler, result))
                                                )
                                                .flatMap(jsonValueMono -> jsonValueMono)
                                                .flatMap(jsonValueAfterHandler ->
                                                        Flux.fromIterable(operationAfterHandlerProviderList)
                                                                .reduce(
                                                                        Mono.just(jsonValueAfterHandler),
                                                                        (pre, cur) -> pre.flatMap(result -> cur.get().mutation(operationAfterHandler, result))
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
