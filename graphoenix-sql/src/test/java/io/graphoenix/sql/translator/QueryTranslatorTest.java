package io.graphoenix.sql.translator;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import java.io.IOException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.select.Limit;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import static org.junit.jupiter.api.Assertions.*;

class QueryTranslatorTest {

  private static final String TEST_SCHEMA =
      lines(
          "type Query {",
          "  user(id: IDExpression): User",
          "  users(",
          "    id: IDExpression,",
          "    name: StringExpression,",
          "    active: BooleanExpression,",
          "    profile: ProfileExpression,",
          "    permissions: PermissionExpression,",
          "    tags: StringExpression,",
          "    tagCount: IntExpression,",
          "    includeDeprecated: Boolean,",
          "    orderBy: UserOrderBy,",
          "    groupBy: UserGroupBy,",
          "    first: Int,",
          "    last: Int,",
          "    offset: Int,",
          "    after: String,",
          "    before: String",
          "  ): [User]",
          "  skippedFetch: User @fetch(from: \"id\")",
          "  skippedInvoke: User @invoke(className: \"Example\", methodName: \"load\")",
          "  skippedConnection: User @connection(field: \"users\")",
          "}",
          "",
          "type Mutation {",
          "  createUser(input: UserInput): User",
          "  createUsers(list: [UserInput]): [User]",
          "  updateUser(where: UserExpression): User",
          "  deleteUser(id: ID): User",
          "}",
          "",
          "type User {",
          "  id: ID",
          "  name: String @cursor",
          "  active: Boolean",
          "  isDeprecated: Boolean",
          "  profileId: ID",
          "  profile: Profile @map(from: \"profileId\", to: \"id\")",
          "  permissions(where: PermissionExpression, orderBy: PermissionOrderBy, first: Int, offset: Int): [Permission] @map(from: \"id\", with: {type: \"UserPermission\", from: \"userId\", to: \"permissionId\"}, to: \"id\")",
          "  tags(sort: Sort = ASC, first: Int, offset: Int): [String] @map(from: \"id\", with: {type: \"UserTag\", from: \"userId\", to: \"tag\"})",
          "  tagCount: [Int] @map(from: \"id\", with: {type: \"UserTag\", from: \"userId\", to: \"tag\"}) @func(name: COUNT)",
          "  totalCount: Int @func(name: COUNT, field: \"id\")",
          "  nameMax: String @func(name: MAX, field: \"name\")",
          "}",
          "",
          "type Profile {",
          "  id: ID",
          "  bio: String",
          "  isDeprecated: Boolean",
          "}",
          "",
          "type Permission {",
          "  id: ID",
          "  code: String",
          "  isDeprecated: Boolean",
          "  codeCount: Int @func(name: COUNT, field: \"id\")",
          "}",
          "",
          "type UserPermission {",
          "  id: ID",
          "  userId: ID",
          "  permissionId: ID",
          "  isDeprecated: Boolean",
          "}",
          "",
          "type UserTag {",
          "  id: ID",
          "  userId: ID",
          "  tag: String",
          "  isDeprecated: Boolean",
          "}",
          "",
          "input UserExpression {",
          "  id: IDExpression",
          "  name: StringExpression",
          "  active: BooleanExpression",
          "  profile: ProfileExpression",
          "  permissions: PermissionExpression",
          "  tags: StringExpression",
          "  tagCount: IntExpression",
          "  includeDeprecated: Boolean",
          "  not: Boolean",
          "  cond: Conditional",
          "  exs: [UserExpression]",
          "}",
          "",
          "input ProfileExpression {",
          "  id: IDExpression",
          "  bio: StringExpression",
          "  includeDeprecated: Boolean",
          "}",
          "",
          "input PermissionExpression {",
          "  id: IDExpression",
          "  code: StringExpression",
          "  includeDeprecated: Boolean",
          "}",
          "",
          "input UserOrderBy {",
          "  id: Sort",
          "  name: Sort",
          "  profile: ProfileOrderBy",
          "  permissions: PermissionOrderBy",
          "  totalCount: Sort",
          "  obs: [UserOrderBy]",
          "}",
          "",
          "input ProfileOrderBy {",
          "  bio: Sort",
          "}",
          "",
          "input PermissionOrderBy {",
          "  code: Sort",
          "  codeCount: Sort",
          "}",
          "",
          "input UserGroupBy {",
          "  by: [String]",
          "  gbs: [UserGroupBy]",
          "  profile: ProfileGroupBy",
          "  permissions: PermissionGroupBy",
          "}",
          "",
          "input ProfileGroupBy {",
          "  by: [String]",
          "}",
          "",
          "input PermissionGroupBy {",
          "  by: [String]",
          "}",
          "",
          "input UserInput {",
          "  id: ID",
          "  name: String",
          "  where: UserExpression",
          "}");

  private final TestQueryTranslator translator = newTranslator();

  @Test
  void singleObjectQueryBuildsJsonObjectWithWhereAndLimit() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  selectedUser: user(id: {val: \"u1\"}) {",
                        "    id",
                        "    name",
                        "    active",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "JSON_OBJECT( 'selectedUser', (SELECT JSON_EXTRACT(JSON_OBJECT");
    assertContains(sql, "'id', CONVERT(user_1.`id`, CHAR)");
    assertContains(sql, "'active', IF(user_1.`active`, TRUE, FALSE)");
    assertContains(sql, "FROM `user` AS user_1");
    assertContains(sql, "user_1.`id` = 'u1'");
    assertContains(sql, "user_1.`is_deprecated` <> 1");
    assertContains(sql, "LIMIT 0, 1");
  }

  @Test
  void rootListQueryPushesWhereOrderAndLimitIntoInnerSelect() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(",
                        "    name: {opr: LK, val: \"A%\"},",
                        "    active: {val: true},",
                        "    orderBy: {name: DESC},",
                        "    first: 2,",
                        "    offset: 1",
                        "  ) {",
                        "    id",
                        "    name",
                        "    active",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "FROM (SELECT JSON_OBJECT");
    assertContains(sql, "user_1.`name` LIKE 'A%'");
    assertContains(sql, "user_1.`active` = 1");
    assertContains(sql, "user_1.`is_deprecated` <> 1");
    assertContains(sql, "ORDER BY user_1.`name` DESC");
    assertContains(sql, "LIMIT 1, 2");
    assertContains(sql, "CONVERT(user_1.`id`, CHAR)");
    assertContains(sql, "IF(user_1.`active`, TRUE, FALSE)");
  }

  @Test
  void rootListLastAndOffsetRenderDescendingCursorLimit() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(lines("query {", "  users(last: 3, offset: 2) { id }", "}")))
            .orElseThrow();

    assertContains(sql, "FROM (SELECT JSON_OBJECT");
    assertContains(sql, "ORDER BY user_1.`name` DESC");
    assertContains(sql, "LIMIT 2, 3");
    assertContains(sql, "JSON_ARRAYAGG( user_1.`json_object` )");
  }

  @Test
  void listQueryWithAggregateFieldBuildsGroupedAggregateJson() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users {",
                        "    totalCount",
                        "    nameMax",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "JSON_ARRAYAGG( user_1.`json_object` )");
    assertContains(sql, "'totalCount', CONVERT(COUNT(user_1.`id`), INT)");
    assertContains(sql, "'nameMax', MAX(user_1.`name`)");
    assertContains(sql, "FROM `user` AS user_1");
  }

  @Test
  void nestedObjectListAndLeafListUseRelationPredicates() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users {",
                        "    id",
                        "    profile { bio }",
                        "    permissions(orderBy: {code: ASC}, first: 3) { code }",
                        "    tags(sort: DESC, first: 2)",
                        "    tagCount",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "profile_2.`id` = user_1.`profile_id`");
    assertContains(sql, "LEFT JOIN `user_permission` AS user_permission_2");
    assertContains(sql, "user_permission_2.`user_id` = user_1.`id`");
    assertContains(sql, "user_permission_2.`permission_id` = permission_2.`id`");
    assertContains(sql, "JSON_ARRAYAGG( JSON_OBJECT( 'code', permission_2.`code` )  ORDER BY permission_2.`code` LIMIT 3)");
    assertContains(sql, "FROM `user_tag` AS user_tag_1");
    assertContains(sql, "user_tag_1.`user_id` = user_1.`id`");
    assertContains(sql, "ORDER BY user_tag_1.`tag` DESC");
    assertContains(sql, "CONVERT(COUNT(user_tag_1.`tag`), INT)");
  }

  @Test
  void groupByWrapsListResultAndProjectsGroupedJson() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(groupBy: {by: [\"name\"]}, orderBy: {name: ASC}) {",
                        "    name",
                        "    totalCount",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "JSON_ARRAYAGG( user_1.`json_object` )");
    assertContains(sql, "JSON_OBJECT( 'name', user_1.`name`, 'totalCount', CONVERT(COUNT(user_1.`id`), INT) )");
    assertContains(sql, "GROUP BY user_1.`name`");
    assertContains(sql, "ORDER BY user_1.`name`");
  }

  @Test
  void groupByCountOrderAndLimitStayInsideGroupedSubselect() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(groupBy: {by: [\"name\"]}, orderBy: {totalCount: DESC}, first: 10, offset: 5) {",
                        "    name",
                        "    totalCount",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "JSON_ARRAYAGG( user_1.`json_object` )");
    assertContains(sql, "JSON_OBJECT( 'name', user_1.`name`, 'totalCount', CONVERT(COUNT(user_1.`id`), INT) )");
    assertContains(sql, "GROUP BY user_1.`name`");
    assertContains(sql, "ORDER BY COUNT(user_1.`id`) DESC");
    assertContains(sql, "LIMIT 5, 10");
  }

  @Test
  void groupByWithoutOrderByDoesNotUseDefaultCursorOrder() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(groupBy: {by: [\"active\"]}) {",
                        "    active",
                        "    totalCount",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "GROUP BY user_1.`active`");
    assertDoesNotContain(sql, "ORDER BY user_1.`name`");
  }

  @Test
  void groupByDoesNotJoinSelectedListFieldsIntoRootAggregate() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(groupBy: {by: [\"name\"]}) {",
                        "    name",
                        "    totalCount",
                        "    permissions { code }",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "COUNT(user_1.`id`)");
    assertContains(sql, "GROUP BY user_1.`name`");
    assertDoesNotContain(sql, "FROM `user` AS user_1 LEFT JOIN `user_permission`");
  }

  @Test
  void groupByRecursesThroughGbsEntries() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(groupBy: {gbs: [{by: [\"name\"]}, {by: [\"active\"]}]}) {",
                        "    name",
                        "    active",
                        "    totalCount",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "JSON_ARRAYAGG( user_1.`json_object` )");
    assertContains(sql, "GROUP BY user_1.`name`, user_1.`active`");
    assertContains(sql, "'active', IF(user_1.`active`, TRUE, FALSE)");
    assertContains(sql, "'totalCount', CONVERT(COUNT(user_1.`id`), INT)");
  }

  @Test
  void nestedGroupByBuildsJoinExpressionsForObjectAndMappedList() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(groupBy: {profile: {by: [\"bio\"]}, permissions: {by: [\"code\"]}}) {",
                        "    profile { bio }",
                        "    permissions { code }",
                        "  }",
                        "}")))
            .orElseThrow();

    assertDoesNotContain(sql, "FROM `user` AS user_1 LEFT JOIN");
    assertContains(sql, "FROM `profile` AS profile_2 WHERE (profile_2.`id` = user_1.`profile_id`");
    assertContains(sql, "FROM `permission` AS permission_2 LEFT JOIN `user_permission` AS user_permission_2");
    assertContains(sql, "user_permission_2.`user_id` = user_1.`id`");
    assertContains(sql, "user_permission_2.`permission_id` = permission_2.`id`");
    assertContains(sql, "GROUP BY (SELECT profile_2.`bio`");
    assertContains(sql, "(SELECT MIN(permission_2.`code`)");
  }

  @Test
  void nestedOrderByUsesScalarSubqueryAndListAggregate() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(orderBy: {profile: {bio: ASC}, permissions: {code: DESC}}) {",
                        "    id",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "ORDER BY (SELECT profile_2.`bio` FROM `profile` AS profile_2");
    assertContains(sql, "profile_2.`id` = user_1.`profile_id`");
    assertContains(sql, "(SELECT MAX(permission_2.`code`) FROM `permission` AS permission_2");
    assertContains(sql, "user_permission_2.`permission_id` = permission_2.`id`");
  }

  @Test
  void orderByObsAndFunctionFieldBuildMultipleOrderExpressions() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(orderBy: {obs: [{totalCount: DESC}, {name: ASC}]}) {",
                        "    id",
                        "  }",
                        "}")))
            .orElseThrow();

    assertContains(sql, "ORDER BY COUNT(user_1.`id`) DESC, user_1.`name`");
  }

  @Test
  void afterBeforeAndLastAffectCursorFilterAndDefaultOrdering() {
    String afterSql =
        translator
            .operationToSelectSQL(Operation.fromString(lines("query {", "  users(after: \"A\") { id }", "}")))
            .orElseThrow();
    String lastSql =
        translator
            .operationToSelectSQL(Operation.fromString(lines("query {", "  users(last: 4, before: \"Z\") { id }", "}")))
            .orElseThrow();

    assertContains(afterSql, "user_1.`name` > 'A'");
    assertContains(afterSql, "ORDER BY user_1.`name`");
    assertContains(lastSql, "user_1.`name` < 'Z'");
    assertContains(lastSql, "ORDER BY user_1.`name` DESC");
    assertContains(lastSql, "LIMIT 4");
  }

  @Test
  void includeDeprecatedAndNotOrExpressionsAffectWhereSql() {
    String includeDeprecatedSql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(includeDeprecated: true, name: {val: \"A\"}) { id }",
                        "}")))
            .orElseThrow();
    String notOrSql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "query {",
                        "  users(cond: OR, not: true, name: {val: \"A\"}, active: {val: false}) { id }",
                        "}")))
            .orElseThrow();

    assertContains(includeDeprecatedSql, "user_1.`name` = 'A'");
    assertDoesNotContain(includeDeprecatedSql, "user_1.`is_deprecated` <> 1");
    assertContains(notOrSql, "NOT (");
    assertContains(notOrSql, "user_1.`active` = 0");
    assertContains(notOrSql, "user_1.`name` = 'A'");
    assertContains(notOrSql, "user_1.`is_deprecated` <> 1");
  }

  @Test
  void mutationInputAndListArgumentsResolveAffectedRows() {
    String inputSql =
        translator
            .operationToSelectSQL(Operation.fromString(lines("mutation {", "  createUser(input: {name: \"A\"}) { id name }", "}")))
            .orElseThrow();
    String listSql =
        translator
            .operationToSelectSQL(
                Operation.fromString(
                    lines(
                        "mutation {",
                        "  createUsers(list: [{id: \"u1\"}, {where: {name: {val: \"B\"}}}, {name: \"C\"}]) { id }",
                        "}")))
            .orElseThrow();

    assertContains(inputSql, "user_1.`id` = @user_id_0_0");
    assertContains(inputSql, "LIMIT 0, 1");
    assertContains(listSql, "user_1.`id` IN ('u1', @user_id_0_2)");
    assertContains(listSql, "OR (user_1.`name` = 'B'");
    assertContains(listSql, "ORDER BY FIELD(user_1.`id`, 'u1', @user_id_0_2)");
  }

  @Test
  void mutationVariableInputFallsBackToLastInsertId() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(lines("mutation($input: UserInput) {", "  createUser(input: $input) { id }", "}")))
            .orElseThrow();

    assertContains(sql, "user_1.`id` = IFNULL((SELECT `id` FROM JSON_TABLE(CONCAT('[', :input, ']')");
    assertContains(sql, "LAST_INSERT_ID())");
    assertContains(sql, "LIMIT 0, 1");
  }

  @Test
  void mutationVariableListFallsBackToLastInsertId() {
    String sql =
        translator
            .operationToSelectSQL(
                Operation.fromString(lines("mutation($list: [UserInput]) {", "  createUsers(list: $list) { id }", "}")))
            .orElseThrow();

    assertContains(sql, "user_1.`id` >= IFNULL((SELECT `id` FROM JSON_TABLE(:list");
    assertContains(sql, "LAST_INSERT_ID())");
  }

  @Test
  void mutationWhereAndIdArgumentsAreTranslated() {
    String whereSql =
        translator
            .operationToSelectSQL(
                Operation.fromString(lines("mutation {", "  updateUser(where: {name: {opr: NEQ, val: \"old\"}}) { id }", "}")))
            .orElseThrow();
    String idSql =
        translator
            .operationToSelectSQL(Operation.fromString(lines("mutation {", "  deleteUser(id: \"u2\") { id }", "}")))
            .orElseThrow();

    assertContains(whereSql, "user_1.`name` <> 'old'");
    assertContains(idSql, "user_1.`id` = 'u2'");
  }

  @Test
  void groupAndOrderHelpersReturnOriginalExpressionForNonObjectFields() {
    ObjectType userType = translator.objectType("User");
    FieldDefinition nameField = userType.getFieldOrError("name");
    Column expression = new Column("name");

    assertSame(
        expression,
        translator.exposeObjectFieldToOrderByExpression(userType, nameField, expression, 1));
    assertSame(
        expression,
        translator.exposeListFieldToOrderByExpression(userType, nameField, expression, true, 1));
    assertSame(
        expression,
        translator.exposeListFieldToGroupByExpression(userType, nameField, expression, 1));
  }

  @Test
  void objectSelectionIsRequired() {
    assertThrows(
        GraphQLErrors.class,
        () -> translator.operationToSelectSQL(Operation.fromString(lines("query {", "  users", "}"))));
  }

  @Test
  void unsupportedFunctionFieldsInNestedListGroupByAndOrderByThrow() {
    assertThrows(
        GraphQLErrors.class,
        () ->
            translator.operationToSelectSQL(
                Operation.fromString(lines("query {", "  users(groupBy: {permissions: {by: [\"codeCount\"]}}) { id }", "}"))));

    assertThrows(
        GraphQLErrors.class,
        () ->
            translator.operationToSelectSQL(
                Operation.fromString(lines("query {", "  users(orderBy: {permissions: {codeCount: ASC}}) { id }", "}"))));
  }

  @Test
  void protectedHelpersCoverNullDefaultsAndInvalidValues() {
    ObjectType userType = translator.objectType("User");
    FieldDefinition usersField = translator.objectType("Query").getFieldOrError("users");
    FieldDefinition idField = userType.getFieldOrError("id");

    assertNull(translator.exposeArgumentsToLimit(usersField, new Field("users")));
    assertTrue(translator.exposeArgumentsToOrderByStream(usersField, new Field("users"), 1).findAny().isPresent());
    assertTrue(
        translator
            .exposeValueWithVariableToGroupByExpressionStream(
                userType, ValueWithVariable.of((Object) null), 1)
            .findAny()
            .isEmpty());
    assertTrue(
        translator
            .exposeValueWithVariableToOrderByStream(
                userType, ValueWithVariable.of((Object) null), 1)
            .findAny()
            .isEmpty());
    assertFalse(translator.exposeIsValidOrderByEnum(ValueWithVariable.of("ASC")));
    assertFalse(
        translator.exposeHasFunctionFieldInGroupBy(userType, ValueWithVariable.of((Object) null)));
    assertFalse(
        translator.exposeHasFunctionFieldInOrderBy(userType, ValueWithVariable.of((Object) null)));
    assertSame(idField, translator.objectType("User").getFieldOrError("id"));

    Function function =
        new Function().withName("COUNT").withParameters(new ExpressionList<>(new LongValue(1)));
    assertThrows(
        GraphQLErrors.class,
        () ->
            translator.exposeListFieldToGroupByExpression(
                userType, userType.getFieldOrError("permissions"), function, 1));
  }

  private static TestQueryTranslator newTranslator() {
    try {
      PackageManager packageManager = allocateWithoutConstructor(PackageManager.class);
      DocumentManager documentManager =
          new DocumentManager(packageManager)
              .setDocument(
                  new Document()
                      .addDefinitionsByFileName("graphql/core.gql")
                      .addDefinitions(TEST_SCHEMA));
      ArgumentsTranslator argumentsTranslator = new ArgumentsTranslator(documentManager);
      return new TestQueryTranslator(documentManager, packageManager, argumentsTranslator);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> T allocateWithoutConstructor(Class<T> type) {
    try {
      java.lang.reflect.Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      return (T) ((Unsafe) field.get(null)).allocateInstance(type);
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }

  private static String lines(String... lines) {
    return String.join("\n", lines);
  }

  private static void assertContains(String actual, String expected) {
    assertTrue(
        actual.contains(expected),
        () -> "Expected SQL to contain:\n" + expected + "\n\nActual SQL:\n" + actual);
  }

  private static void assertDoesNotContain(String actual, String unexpected) {
    assertFalse(
        actual.contains(unexpected),
        () -> "Expected SQL not to contain:\n" + unexpected + "\n\nActual SQL:\n" + actual);
  }

  private static class TestQueryTranslator extends QueryTranslator {
    private final DocumentManager documentManager;

    TestQueryTranslator(
        DocumentManager documentManager,
        PackageManager packageManager,
        ArgumentsTranslator argumentsTranslator) {
      super(documentManager, packageManager, argumentsTranslator);
      this.documentManager = documentManager;
    }

    ObjectType objectType(String name) {
      return documentManager.getDocument().getDefinition(name).asObject();
    }

    Limit exposeArgumentsToLimit(FieldDefinition fieldDefinition, Field field) {
      return argumentsToLimit(fieldDefinition, field);
    }

    java.util.stream.Stream<net.sf.jsqlparser.statement.select.OrderByElement>
        exposeArgumentsToOrderByStream(FieldDefinition fieldDefinition, Field field, int level) {
      return argumentsToOrderByStream(fieldDefinition, field, level);
    }

    java.util.stream.Stream<Expression> exposeValueWithVariableToGroupByExpressionStream(
        ObjectType objectType, ValueWithVariable valueWithVariable, int level) {
      return valueWithVariableToGroupByExpressionStream(objectType, valueWithVariable, level);
    }

    java.util.stream.Stream<net.sf.jsqlparser.statement.select.OrderByElement>
        exposeValueWithVariableToOrderByStream(
            ObjectType objectType, ValueWithVariable valueWithVariable, int level) {
      return valueWithVariableToOrderByStream(objectType, valueWithVariable, level);
    }

    boolean exposeIsValidOrderByEnum(ValueWithVariable valueWithVariable) {
      return isValidOrderByEnum(valueWithVariable);
    }

    boolean exposeHasFunctionFieldInGroupBy(ObjectType objectType, ValueWithVariable valueWithVariable) {
      return hasFunctionFieldInGroupBy(objectType, valueWithVariable);
    }

    boolean exposeHasFunctionFieldInOrderBy(ObjectType objectType, ValueWithVariable valueWithVariable) {
      return hasFunctionFieldInOrderBy(objectType, valueWithVariable);
    }

    Expression exposeListFieldToGroupByExpression(
        ObjectType objectType, FieldDefinition fieldDefinition, Expression expression, int level) {
      return listFieldToGroupByExpression(objectType, fieldDefinition, expression, level);
    }

    Expression exposeObjectFieldToOrderByExpression(
        ObjectType objectType, FieldDefinition fieldDefinition, Expression expression, int level) {
      return objectFieldToOrderByExpression(objectType, fieldDefinition, expression, level);
    }

    Expression exposeListFieldToOrderByExpression(
        ObjectType objectType,
        FieldDefinition fieldDefinition,
        Expression expression,
        boolean asc,
        int level) {
      return listFieldToOrderByExpression(objectType, fieldDefinition, expression, asc, level);
    }
  }
}
