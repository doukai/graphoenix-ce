package io.graphoenix.sql.translator;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import static io.graphoenix.sql.utils.DBNameUtil.graphqlTypeToTable;

@ApplicationScoped
public class TypeTranslator {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;

    @Inject
    public TypeTranslator(DocumentManager documentManager, PackageManager packageManager) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
    }


    protected CreateTable createTable(ObjectType objectType) {
        return new CreateTable()
                .withTable(graphqlTypeToTable(objectType.getName()))
                .withIfNotExists(true)
                ;
    }
}
