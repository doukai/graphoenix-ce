package io.graphoenix.core.handler;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.*;
import io.nozdormu.spi.context.PublisherBeanContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonValue;
import jakarta.transaction.Transactional;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;

@ApplicationScoped
@Default
public class DefaultOperationHandler implements OperationHandler {

    private final List<OperationBeforeHandler> operationBeforeHandlerList;

    private final List<OperationAfterHandler> operationAfterHandlerList;

    private final QueryHandler queryHandler;

    private final MutationHandler mutationHandler;

    private final SubscriptionHandler subscriptionHandler;

    private final SubscriptionDataListener subscriptionDataListener;

    private final Provider<Mono<TransactionCompensator>> transactionCompensatorProvider;

    private final Provider<FetchHandler> fetchHandlerProvider;

    @Inject
    public DefaultOperationHandler(GraphQLConfig graphQLConfig, Instance<OperationBeforeHandler> operationBeforeHandlerInstance, Instance<OperationAfterHandler> operationAfterHandlerInstance, SubscriptionDataListener subscriptionDataListener, Provider<Mono<TransactionCompensator>> transactionCompensatorProvider, Provider<FetchHandler> fetchHandlerProvider) {
        this.operationBeforeHandlerList = operationBeforeHandlerInstance.stream().collect(Collectors.toList());
        this.operationAfterHandlerList = operationAfterHandlerInstance.stream().collect(Collectors.toList());
        this.queryHandler = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
                .map(name -> CDI.current().select(QueryHandler.class, NamedLiteral.of(name)).get())
                .orElseGet(() -> CDI.current().select(QueryHandler.class).get());
        this.mutationHandler = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
                .map(name -> CDI.current().select(MutationHandler.class, NamedLiteral.of(name)).get())
                .orElseGet(() -> CDI.current().select(MutationHandler.class).get());
        this.subscriptionHandler = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
                .map(name -> CDI.current().select(SubscriptionHandler.class, NamedLiteral.of(name)).get())
                .orElseGet(() -> CDI.current().select(SubscriptionHandler.class).get());
        this.subscriptionDataListener = subscriptionDataListener;
        this.transactionCompensatorProvider = transactionCompensatorProvider;
        this.fetchHandlerProvider = fetchHandlerProvider;
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
                        queryHandler.query(operationAfterHandler)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(operationAfterHandlerList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) -> pre.flatMap(result -> cur.query(operationAfterHandler, result))
                                                )
                                                .flatMap(operationMono -> operationMono)
                                )
                )
                .defaultIfEmpty(JsonValue.EMPTY_JSON_OBJECT);
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
                        mutationHandler.mutation(operationAfterHandler)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(operationAfterHandlerList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) ->
                                                                pre.flatMap(result ->
                                                                        cur.mutation(operationAfterHandler, result)
                                                                                .onErrorResume(throwable ->
                                                                                        transactionCompensatorProvider.get()
                                                                                                .flatMap(transactionCompensator ->
                                                                                                        Mono.justOrEmpty(transactionCompensator.compensating(result.asJsonObject()))
                                                                                                                .flatMap(fetchHandlerProvider.get()::request)
                                                                                                )
                                                                                                .then(Mono.error(throwable))
                                                                                )
                                                                )
                                                )
                                                .flatMap(operationMono -> operationMono)
                                )
                )
                .defaultIfEmpty(JsonValue.EMPTY_JSON_OBJECT);
    }

    public Flux<JsonValue> subscription(Operation operation, Map<String, JsonValue> variables, String token, String operationId) {
        return Flux.fromIterable(operationBeforeHandlerList)
                .reduce(
                        Mono.just(operation),
                        (pre, cur) -> pre.flatMap(result -> cur.subscription(result, variables))
                )
                .flatMap(operationMono -> operationMono)
                .flatMapMany(operationAfterHandler ->
                        subscriptionHandler.subscription(operationAfterHandler, token, operationId)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(operationAfterHandlerList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) -> pre.flatMap(result -> cur.subscription(operationAfterHandler, result))
                                                )
                                                .flatMap(operationMono -> operationMono)
                                )
                )
                .defaultIfEmpty(JsonValue.EMPTY_JSON_OBJECT)
                .contextWrite(PublisherBeanContext.of(SubscriptionDataListener.class, subscriptionDataListener));
    }
}
