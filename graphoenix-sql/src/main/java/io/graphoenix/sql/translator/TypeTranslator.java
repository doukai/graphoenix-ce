package io.graphoenix.sql.translator;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.alter.AlterOperation;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_FIELD_TYPE;
import static io.graphoenix.sql.utils.DBNameUtil.*;

@ApplicationScoped
public class TypeTranslator {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;

    @Inject
    public TypeTranslator(DocumentManager documentManager, PackageManager packageManager) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
    }

    public String createTablesSQL() {
        return createTablesSQLStream().collect(Collectors.joining(";"));
    }

    public Stream<String> createTablesSQLStream() {
        return documentManager.getDocument().getObjectTypes()
                .filter(packageManager::isLocalPackage)
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .filter(objectType -> !objectType.isContainer())
                .map(this::createTable)
                .map(CreateTable::toString);
    }

    public Stream<String> mergeTablesSQL(List<Tuple2<String, String>> existsColumnNameList) {
        return documentManager.getDocument().getObjectTypes()
                .filter(packageManager::isLocalPackage)
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .filter(objectType -> !objectType.isContainer())
                .map(objectType -> {
                            if (existsColumnNameList.stream()
                                    .noneMatch(tuple2 -> nameToDBEscape(tuple2.getT1()).equals(graphqlTypeNameToTableName(objectType.getName())))) {
                                return createTable(objectType).toString();
                            } else {
                                return alterTable(objectType, existsColumnNameList).toString();
                            }
                        }
                );
    }

    protected CreateTable createTable(ObjectType objectType) {
        return new CreateTable()
                .withTable(graphqlTypeToTable(objectType.getName()))
                .withColumnDefinitions(
                        objectType.getFields().stream()
                                .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                                .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                                .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                                .filter(fieldDefinition -> !documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                .map(this::createColumn)
                                .collect(Collectors.toList())
                )
                .withIfNotExists(true)
                .withTableOptionsStrings(createTableOption(objectType));
    }

    protected Alter alterTable(ObjectType objectType, List<Tuple2<String, String>> existsColumnNameList) {
        Table table = graphqlTypeToTable(objectType.getName());
        return new Alter()
                .withTable(table)
                .withAlterExpressions(
                        objectType.getFields().stream()
                                .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                                .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                                .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                                .filter(fieldDefinition -> !documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                .filter(fieldDefinition -> !fieldDefinition.getType().getTypeName().getName().equals(SCALA_ID_NAME))
                                .map(this::createColumn)
                                .map(columnDefinition ->
                                        alterColumn(
                                                columnDefinition,
                                                existsColumnNameList.stream()
                                                        .anyMatch(tuple2 ->
                                                                nameToDBEscape(tuple2.getT1()).equals(table.getName()) &&
                                                                        nameToDBEscape(tuple2.getT2()).equals(columnDefinition.getColumnName())
                                                        )
                                        )
                                )
                                .collect(Collectors.toList())
                );
    }

    protected AlterExpression alterColumn(ColumnDefinition columnDefinition, boolean exists) {
        AlterExpression alterExpression = new AlterExpression()
                .withOperation(exists ? AlterOperation.MODIFY : AlterOperation.ADD);
        alterExpression.addColDataType(
                new AlterExpression
                        .ColumnDataType(false)
                        .withColumnName(columnDefinition.getColumnName())
                        .withColDataType(columnDefinition.getColDataType())
                        .withColumnSpecs(columnDefinition.getColumnSpecs())
        );
        return alterExpression;
    }

    protected ColumnDefinition createColumn(FieldDefinition fieldDefinition) {
        List<String> columnSpecs = new ArrayList<>();
        String fieldTypeName = fieldDefinition.getType().getTypeName().getName();
        if (fieldTypeName.equals(SCALA_ID_NAME)) {
            columnSpecs.add("PRIMARY KEY");
        }
        if (fieldDefinition.getType().isNonNull()) {
            columnSpecs.add("NOT NULL");
        } else {
            if (fieldTypeName.equals(SCALA_DATE_NAME) ||
                    fieldTypeName.equals(SCALA_TIME_NAME) ||
                    fieldTypeName.equals(SCALA_DATE_TIME_NAME) ||
                    fieldTypeName.equals(SCALA_TIMESTAMP_NAME)) {
                columnSpecs.add("NULL");
            }
        }

        fieldDefinition.getDefault()
                .ifPresentOrElse(
                        (defaultValue) -> {
                            if (fieldTypeName.equals(SCALA_ID_NAME) ||
                                    fieldTypeName.equals(SCALA_STRING_NAME) ||
                                    fieldTypeName.equals(SCALA_DATE_NAME) ||
                                    fieldTypeName.equals(SCALA_TIME_NAME) ||
                                    fieldTypeName.equals(SCALA_DATE_TIME_NAME) ||
                                    fieldTypeName.equals(SCALA_TIMESTAMP_NAME)) {
                                columnSpecs.add("DEFAULT  " + stringValueToDBVarchar(defaultValue));
                            } else {
                                columnSpecs.add("DEFAULT  " + defaultValue);
                            }
                        },
                        () -> {
                            if (fieldTypeName.equals(SCALA_DATE_NAME) ||
                                    fieldTypeName.equals(SCALA_TIME_NAME) ||
                                    fieldTypeName.equals(SCALA_DATE_TIME_NAME) ||
                                    fieldTypeName.equals(SCALA_TIMESTAMP_NAME)) {
                                if (!fieldDefinition.getType().isNonNull()) {
                                    columnSpecs.add("DEFAULT NULL");
                                }
                            }
                        }
                );

        if (fieldDefinition.isAutoIncrement()) {
            columnSpecs.add("AUTO_INCREMENT");
        }

        if (fieldDefinition.getDescription() != null) {
            columnSpecs.add("COMMENT " + graphqlDescriptionToDBComment(fieldDefinition.getDescription()));
        }

        return new ColumnDefinition()
                .withColumnName(graphqlFieldNameToColumnName(fieldDefinition.getName()))
                .withColDataType(createColDataType(fieldDefinition))
                .withColumnSpecs(columnSpecs);
    }

    protected ColDataType createColDataType(FieldDefinition fieldDefinition) {
        return createColDataType(fieldDefinition, false);
    }

    protected ColDataType createColDataType(FieldDefinition fieldDefinition, boolean enumToString) {
        ColDataType colDataType = new ColDataType();
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isEnum()) {
            if (enumToString) {
                return colDataType.withDataType("VARCHAR")
                        .withArgumentsStringList(Collections.singletonList(String.valueOf(fieldDefinition.getLength().orElse(255))));
            } else {
                return colDataType
                        .withDataType("ENUM")
                        .withArgumentsStringList(
                                fieldTypeDefinition.asEnum().getEnumValues().stream()
                                        .map(enumValueDefinition -> stringValueToDBVarchar(enumValueDefinition.getName()))
                                        .collect(Collectors.toList())
                        );
            }
        } else if (fieldTypeDefinition.isScalar()) {
            List<String> argumentsStringList = new ArrayList<>();
            String fieldTypeName = fieldDefinition.getTypeName().orElseGet(() -> fieldDefinition.getType().getTypeName().getName());
            switch (fieldTypeName) {
                case SCALA_ID_NAME:
                case SCALA_STRING_NAME:
                    colDataType.setDataType("VARCHAR");
                    argumentsStringList.add(String.valueOf(fieldDefinition.getLength().orElse(255)));
                    break;
                case SCALA_BOOLEAN_NAME:
                    colDataType.setDataType("BOOL");
                    fieldDefinition.getLength().ifPresent(length -> argumentsStringList.add(String.valueOf(length)));
                    break;
                case SCALA_INT_NAME:
                    colDataType.setDataType("INT");
                    fieldDefinition.getLength().ifPresent(length -> argumentsStringList.add(String.valueOf(length)));
                    break;
                case SCALA_FLOAT_NAME:
                    colDataType.setDataType("FLOAT");
                    argumentsStringList.add(String.valueOf(fieldDefinition.getLength().orElse(11)));
                    argumentsStringList.add(String.valueOf(fieldDefinition.getDecimals().orElse(2)));
                    break;
                case SCALA_BIG_INTEGER_NAME:
                    colDataType.setDataType("BIGINT");
                    fieldDefinition.getLength().ifPresent(length -> argumentsStringList.add(String.valueOf(length)));
                    break;
                case SCALA_BIG_DECIMAL_NAME:
                    colDataType.setDataType("DECIMAL");
                    argumentsStringList.add(String.valueOf(fieldDefinition.getLength().orElse(11)));
                    argumentsStringList.add(String.valueOf(fieldDefinition.getDecimals().orElse(2)));
                    break;
                case SCALA_DATE_NAME:
                    colDataType.setDataType("DATE");
                    break;
                case SCALA_TIME_NAME:
                    colDataType.setDataType("TIME");
                    break;
                case SCALA_DATE_TIME_NAME:
                    colDataType.setDataType("DATETIME");
                    break;
                case SCALA_TIMESTAMP_NAME:
                    colDataType.setDataType("TIMESTAMP");
                    break;
                default:
                    throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(fieldDefinition.toString()));
            }
            if (!argumentsStringList.isEmpty()) {
                colDataType.setArgumentsStringList(argumentsStringList);
            }
            return colDataType;
        } else {
            throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(fieldDefinition.toString()));
        }
    }

    protected List<String> createTableOption(ObjectType objectType) {
        List<String> tableOptionsList = new ArrayList<>();
        if (objectType.getDescription() != null) {
            tableOptionsList.add("COMMENT " + graphqlDescriptionToDBComment(objectType.getDescription()));
        }
        return tableOptionsList;
    }

    public String selectColumnsSQL() {
        return selectColumns().toString();
    }

    public Select selectColumns() {
        return new PlainSelect()
                .addSelectItems(
                        new Column("table_name"),
                        new Column("column_name")
                )
                .withFromItem(new Table("COLUMNS").withSchemaName("information_schema"))
                .withWhere(
                        new EqualsTo()
                                .withLeftExpression(new Column("table_schema"))
                                .withRightExpression(new Function().withName("DATABASE"))
                );
    }

    public String selectTablesSQL() {
        return selectTables().toString();
    }

    public Select selectTables() {
        return new PlainSelect()
                .addSelectItems(
                        new Column("table_name")
                )
                .withFromItem(new Table("TABLES").withSchemaName("information_schema"))
                .withWhere(
                        new EqualsTo()
                                .withLeftExpression(new Column("table_schema"))
                                .withRightExpression(new Function().withName("DATABASE"))
                );
    }

    public String truncateTableSQL(String tableName) {
        return truncateTable(tableName).toString();
    }

    public Truncate truncateTable(String tableName) {
        return new Truncate().withTable(new Table(tableName));
    }
}
