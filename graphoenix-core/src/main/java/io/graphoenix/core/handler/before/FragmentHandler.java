package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Fragment;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.operation.Selection;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

import static io.graphoenix.core.handler.before.VariableHandler.VARIABLE_HANDLER_PRIORITY;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_SELECTION;

@ApplicationScoped
@Priority(FragmentHandler.FRAGMENT_HANDLER_PRIORITY)
public class FragmentHandler implements OperationBeforeHandler {

    public static final int FRAGMENT_HANDLER_PRIORITY = VARIABLE_HANDLER_PRIORITY + 100;

    private final DocumentManager documentManager;

    @Inject
    public FragmentHandler(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Override
    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation.getSelections())
                .collectList()
                .map(operation::setSelections);
    }

    public Flux<Field> handle(Collection<Selection> selections) {
        return Mono.justOrEmpty(selections)
                .flatMapMany(Flux::fromIterable)
                .flatMap(selection -> {
                            if (selection.isField()) {
                                return handle(selection.asField().getSelections())
                                        .collectList()
                                        .map(selection.asField()::setSelections);
                            } else if (selection.isFragment()) {
                                return fragmentToFields(selection.asFragment())
                                        .collectList()
                                        .flatMapMany(this::handle);
                            } else {
                                return Flux.error(new GraphQLErrors(UNSUPPORTED_SELECTION.bind(selection.toString())));
                            }
                        }
                );
    }

    public Flux<Selection> fragmentToFields(Fragment fragment) {
        return Mono.just(documentManager.getDocument().getFragmentDefinitionOrError(fragment.getFragmentName()))
                .flatMapMany(fragmentDefinition -> Flux.fromIterable(fragmentDefinition.getSelections()));
    }
}
