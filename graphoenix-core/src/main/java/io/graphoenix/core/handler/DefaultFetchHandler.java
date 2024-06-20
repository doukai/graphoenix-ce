package io.graphoenix.core.handler;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import jakarta.transaction.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;

@ApplicationScoped
@Default
public class DefaultFetchHandler implements FetchHandler {

    private final List<FetchBeforeHandler> fetchBeforeHandlerList;

    private final List<FetchAfterHandler> fetchAfterHandlerList;

    private final QueryHandler queryHandler;

    private final MutationHandler mutationHandler;

    @Inject
    public DefaultFetchHandler(GraphQLConfig graphQLConfig, Instance<FetchBeforeHandler> operationBeforeHandlerInstance, Instance<FetchAfterHandler> operationAfterHandlerInstance, SubscriptionDataListener subscriptionDataListener) {
        this.fetchBeforeHandlerList = operationBeforeHandlerInstance.stream().collect(Collectors.toList());
        this.fetchAfterHandlerList = operationAfterHandlerInstance.stream().collect(Collectors.toList());
        this.queryHandler = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
                .map(name -> CDI.current().select(QueryHandler.class, NamedLiteral.of(name)).get())
                .orElseGet(() -> CDI.current().select(QueryHandler.class).get());
        this.mutationHandler = Optional.ofNullable(graphQLConfig.getDefaultOperationHandlerName())
                .map(name -> CDI.current().select(MutationHandler.class, NamedLiteral.of(name)).get())
                .orElseGet(() -> CDI.current().select(MutationHandler.class).get());
    }

    @Override
    public Mono<JsonValue> request(Operation operation) {
        switch (operation.getOperationType()) {
            case OPERATION_QUERY_NAME:
                return query(operation);
            case OPERATION_MUTATION_NAME:
                return mutation(operation);
            default:
                throw new GraphQLErrors(UNSUPPORTED_OPERATION_TYPE.bind(operation.getOperationType()));
        }
    }

    public Mono<JsonValue> query(Operation operation) {
        return Flux.fromIterable(fetchBeforeHandlerList)
                .reduce(
                        Mono.just(operation),
                        (pre, cur) -> pre.flatMap(cur::query)
                )
                .flatMap(operationMono -> operationMono)
                .flatMap(operationAfterHandler ->
                        queryHandler.query(operationAfterHandler)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(fetchAfterHandlerList)
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
    public Mono<JsonValue> mutation(Operation operation) {
        return Flux.fromIterable(fetchBeforeHandlerList)
                .reduce(
                        Mono.just(operation),
                        (pre, cur) -> pre.flatMap(cur::mutation)
                )
                .flatMap(operationMono -> operationMono)
                .flatMap(operationAfterHandler ->
                        mutationHandler.mutation(operationAfterHandler)
                                .flatMap(jsonValue ->
                                        Flux.fromIterable(fetchAfterHandlerList)
                                                .reduce(
                                                        Mono.just(jsonValue),
                                                        (pre, cur) ->
                                                                pre.flatMap(result -> cur.mutation(operationAfterHandler, result))
                                                )
                                                .flatMap(operationMono -> operationMono)
                                )
                )
                .defaultIfEmpty(JsonValue.EMPTY_JSON_OBJECT);
    }
}
