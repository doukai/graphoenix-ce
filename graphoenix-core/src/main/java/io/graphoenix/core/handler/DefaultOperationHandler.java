package io.graphoenix.core.handler;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.*;
import io.nozdormu.spi.context.BeanContext;
import io.nozdormu.spi.context.PublisherBeanContext;
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

    private final GraphQLConfig graphQLConfig = BeanContext.get(GraphQLConfig.class);

    private final List<Provider<OperationBeforeHandler>> operationBeforeHandlerProviderList = BeanContext.getPriorityProviderList(OperationBeforeHandler.class);

    private final List<Provider<OperationAfterHandler>> operationAfterHandlerProviderList = BeanContext.getPriorityProviderList(OperationAfterHandler.class);

    private final Provider<QueryHandler> queryHandlerProvider = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
            .map(name -> BeanContext.getProvider(QueryHandler.class, name))
            .orElseGet(() -> BeanContext.getProvider(QueryHandler.class));

    private final Provider<MutationHandler> mutationHandlerProvider = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
            .map(name -> BeanContext.getProvider(MutationHandler.class, name))
            .orElseGet(() -> BeanContext.getProvider(MutationHandler.class));

    private final Provider<SubscriptionHandler> subscriptionHandlerProvider = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
            .map(name -> BeanContext.getProvider(SubscriptionHandler.class, name))
            .orElseGet(() -> BeanContext.getProvider(SubscriptionHandler.class));

    private final Provider<SubscriptionDataListener> subscriptionDataListenerProvider = BeanContext.getProvider(SubscriptionDataListener.class);

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
                        queryHandlerProvider.get().query(operationAfterHandler)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(operationAfterHandlerProviderList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) -> pre.flatMap(result -> cur.get().query(operationAfterHandler, result))
                                                )
                                                .flatMap(operationMono -> operationMono)
                                )
                )
                .defaultIfEmpty(JsonValue.EMPTY_JSON_OBJECT);
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
                        mutationHandlerProvider.get().mutation(operationAfterHandler)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(operationAfterHandlerProviderList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) -> pre.flatMap(result -> cur.get().mutation(operationAfterHandler, result))
                                                )
                                                .flatMap(operationMono -> operationMono)
                                )
                )
                .defaultIfEmpty(JsonValue.EMPTY_JSON_OBJECT);
    }

    public Flux<JsonValue> subscription(Operation operation, Map<String, JsonValue> variables, String token, String operationId) {
        return Flux.fromIterable(operationBeforeHandlerProviderList)
                .reduce(
                        Mono.just(operation),
                        (pre, cur) -> pre.flatMap(result -> cur.get().query(result, variables))
                )
                .flatMap(operationMono -> operationMono)
                .flatMapMany(operationAfterHandler ->
                        subscriptionHandlerProvider.get().subscription(operationAfterHandler, token, operationId)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(operationAfterHandlerProviderList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) -> pre.flatMap(result -> cur.get().query(operationAfterHandler, result))
                                                )
                                                .flatMap(operationMono -> operationMono)
                                )
                )
                .defaultIfEmpty(JsonValue.EMPTY_JSON_OBJECT)
                .contextWrite(PublisherBeanContext.of(SubscriptionDataListener.class, subscriptionDataListenerProvider.get()));
    }
}
