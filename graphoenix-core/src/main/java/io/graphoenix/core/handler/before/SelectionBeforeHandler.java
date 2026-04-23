package io.graphoenix.core.handler.before;

import static io.graphoenix.core.handler.before.FragmentHandler.FRAGMENT_HANDLER_PRIORITY;

import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.operation.Selection;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonValue;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;

@ApplicationScoped
@Priority(SelectionBeforeHandler.SELECTION_BEFORE_HANDLER_PRIORITY)
public class SelectionBeforeHandler implements OperationBeforeHandler {

  public static final int SELECTION_BEFORE_HANDLER_PRIORITY = FRAGMENT_HANDLER_PRIORITY + 50;

  @Override
  public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
    return Mono.just(
        operation.setSelections(
            filterSelections(operation.getSelections()).collect(Collectors.toList())));
  }

  public Stream<Selection> filterSelections(Collection<Selection> selections) {
    return Stream.ofNullable(selections)
        .flatMap(Collection::stream)
        .filter(Selection::isInclude)
        .map(
            selection -> {
              if (selection.isField()) {
                if (selection.asField().getSelections() == null) {
                  return selection;
                }
                return selection
                    .asField()
                    .setSelections(
                        filterSelections(selection.asField().getSelections())
                            .collect(Collectors.toList()));
              }
              return selection;
            });
  }
}
