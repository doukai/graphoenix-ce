package io.graphoenix.r2dbc.handler;

import io.graphoenix.r2dbc.executor.TableManager;
import io.graphoenix.spi.handler.TypeEmptyHandler;
import io.graphoenix.sql.translator.TypeTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;

@ApplicationScoped
public class R2DBCTypeEmptyHandler implements TypeEmptyHandler {

    private final TypeTranslator typeTranslator;
    private final TableManager tableManager;

    @Inject
    public R2DBCTypeEmptyHandler(TypeTranslator typeTranslator, TableManager tableManager) {
        this.typeTranslator = typeTranslator;
        this.tableManager = tableManager;
    }

    @Override
    public Mono<Void> empty(String... typeNames) {
        return tableManager.mergeTable(Arrays.stream(typeNames).map(typeTranslator::truncateTableSQL));
    }

    @Override
    public Mono<Void> empty(Collection<String> typeNames) {
        return tableManager.mergeTable(typeNames.stream().map(typeTranslator::truncateTableSQL));
    }
}
