package io.graphoenix.sql.utils;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import static io.graphoenix.spi.constant.Hammurabi.PREFIX_INTROSPECTION;

public final class DBNameUtil {

    public static String graphqlTypeNameToTableName(String graphqlTypeName) {
        if (graphqlTypeName.startsWith(PREFIX_INTROSPECTION)) {
            return nameToDBEscape(PREFIX_INTROSPECTION + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, graphqlTypeName.replaceFirst(PREFIX_INTROSPECTION, "")));
        }
        return nameToDBEscape(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, graphqlTypeName));
    }

    public static String graphqlTypeNameToTableAliaName(String graphqlTypeName, int level) {
        if (graphqlTypeName.startsWith(PREFIX_INTROSPECTION)) {
            return PREFIX_INTROSPECTION + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, graphqlTypeName.replaceFirst(PREFIX_INTROSPECTION, "")) + "_" + level;
        }
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, graphqlTypeName) + "_" + level;
    }

    public static String graphqlFieldNameToColumnName(String graphqlFieldName) {
        return nameToDBEscape(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, graphqlFieldName));
    }

    public static String graphqlFieldNameToVariableName(String graphqlTypeName, String graphqlFieldName) {
        return String.join("_", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, graphqlTypeName), CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, graphqlFieldName));
    }

    public static String graphqlTypeToDBType(String graphqlType) {
        return nameToDBEscape(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, graphqlType));
    }

    public static String directiveToTableOption(String argumentName, String argumentValue) {
        return String.format("%s=%s", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, argumentName), stringValueToDBVarchar(CharMatcher.anyOf("\"").trimFrom(argumentValue)));
    }

    public static String directiveToColumnDefinition(String argumentName, String argumentValue) {
        return String.format("%s %s", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, argumentName), stringValueToDBVarchar(CharMatcher.anyOf("\"").trimFrom(argumentValue)));
    }

    public static String booleanDirectiveTocColumnDefinition(String argumentName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, argumentName);
    }

    public static String graphqlDescriptionToDBComment(String description) {
        return stringValueToDBVarchar(CharMatcher.anyOf("\"").or(CharMatcher.anyOf("\"\"\"")).trimFrom(description));
    }

    public static String graphqlStringValueToDBOption(String argumentName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, CharMatcher.anyOf("\"").trimFrom(argumentName));
    }

    public static String stringValueToDBVarchar(String stringValue) {
        return String.format("'%s'", stringValue);
    }

    public static String nameToDBEscape(String stringValue) {
        return String.format("`%s`", stringValue);
    }

    public static Table dualTable() {
        return new Table("dual");
    }

    public static Table graphqlTypeToTable(String typeName) {
        return new Table(graphqlTypeNameToTableName(typeName));
    }

    public static Table graphqlTypeToTable(String typeName, int level) {
        Table table = new Table(graphqlTypeNameToTableName(typeName));
        table.setAlias(new Alias(graphqlTypeNameToTableAliaName(typeName, level)));
        return table;
    }

    public static Column graphqlFieldToColumn(Table table, String fieldName) {
        return new Column(table, graphqlFieldNameToColumnName(fieldName));
    }

    public static Column graphqlFieldToColumn(String typeName, String fieldName, int level) {
        return new Column(graphqlTypeToTable(typeName, level), graphqlFieldNameToColumnName(fieldName));
    }
}
