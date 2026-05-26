package io.graphoenix.sql.translator;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.operation.Operation;
import java.io.IOException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MutationTranslatorTest {

  private static final String TEST_SCHEMA =
      lines(
          "type Query {",
          "  users: [User]",
          "}",
          "",
          "type Mutation {",
          "  createUser(input: UserInput): User",
          "}",
          "",
          "type User {",
          "  id: ID",
          "  name: String",
          "  isDeprecated: Boolean",
          "  realmId: ID",
          "  createUserId: ID",
          "  createTime: String",
          "  createGroupId: ID",
          "  permissions: [Permission] @map(from: \"id\", with: {type: \"UserPermission\", from: \"userId\", to: \"permissionId\"}, to: \"id\")",
          "  tags: [String] @map(from: \"id\", with: {type: \"UserTag\", from: \"userId\", to: \"tag\"})",
          "}",
          "",
          "type Permission {",
          "  id: ID",
          "  code: String",
          "  isDeprecated: Boolean",
          "}",
          "",
          "type UserPermission {",
          "  id: ID",
          "  userId: ID",
          "  permissionId: ID",
          "  isDeprecated: Boolean",
          "  realmId: ID",
          "  createUserId: ID",
          "  createTime: String",
          "  createGroupId: ID",
          "}",
          "",
          "type UserTag {",
          "  id: ID",
          "  userId: ID",
          "  tag: String",
          "  isDeprecated: Boolean",
          "  realmId: ID",
          "  createUserId: ID",
          "  createTime: String",
          "  createGroupId: ID",
          "}",
          "",
          "input UserInput {",
          "  id: ID",
          "  name: String",
          "  permissions: [PermissionInput]",
          "  tags: [String]",
          "}",
          "",
          "input PermissionInput {",
          "  id: ID",
          "  code: String",
          "}");

  private final MutationTranslator translator = newTranslator();

  @Test
  void mapWithObjectRelationInsertCopiesParentCreateMetadata() {
    String sql =
        translator
            .operationToStatementSQLStream(
                Operation.fromString(
                    lines(
                        "mutation {",
                        "  createUser(input: {id: \"u1\", permissions: [{id: \"p1\"}]}) { id }",
                        "}")))
            .collect(Collectors.joining(";"));

    assertContains(
        sql,
        "INSERT INTO `user_permission` (`user_id`, `permission_id`, `is_deprecated`, `create_time`, `create_user_id`, `create_group_id`, `realm_id`)");
    assertContains(sql, "(SELECT `user`.`create_time` FROM `user` WHERE `user`.`id` = 'u1')");
    assertContains(sql, "(SELECT `user`.`create_user_id` FROM `user` WHERE `user`.`id` = 'u1')");
    assertContains(sql, "(SELECT `user`.`create_group_id` FROM `user` WHERE `user`.`id` = 'u1')");
    assertContains(sql, "(SELECT `user`.`realm_id` FROM `user` WHERE `user`.`id` = 'u1')");
  }

  @Test
  void mapWithLeafRelationInsertCopiesParentCreateMetadata() {
    String sql =
        translator
            .operationToStatementSQLStream(
                Operation.fromString(
                    lines(
                        "mutation {",
                        "  createUser(input: {id: \"u1\", tags: [\"admin\"]}) { id }",
                        "}")))
            .collect(Collectors.joining(";"));

    assertContains(
        sql,
        "INSERT INTO `user_tag` (`user_id`, `tag`, `is_deprecated`, `create_time`, `create_user_id`, `create_group_id`, `realm_id`)");
    assertContains(sql, "(SELECT `user`.`create_time` FROM `user` WHERE `user`.`id` = 'u1')");
    assertContains(sql, "(SELECT `user`.`create_user_id` FROM `user` WHERE `user`.`id` = 'u1')");
    assertContains(sql, "(SELECT `user`.`create_group_id` FROM `user` WHERE `user`.`id` = 'u1')");
    assertContains(sql, "(SELECT `user`.`realm_id` FROM `user` WHERE `user`.`id` = 'u1')");
  }

  @Test
  void mapWithLeafVariableRelationInsertSelectCopiesParentCreateMetadata() {
    String sql =
        translator
            .operationToStatementSQLStream(
                Operation.fromString(
                    lines(
                        "mutation($tags: [String]) {",
                        "  createUser(input: {id: \"u1\", tags: $tags}) { id }",
                        "}")))
            .collect(Collectors.joining(";"));

    assertContains(
        sql,
        "INSERT INTO `user_tag` (`user_id`, `tag`, `is_deprecated`, `create_time`, `create_user_id`, `create_group_id`, `realm_id`)");
    assertContains(
        sql,
        "SELECT 'u1', tags, 0, (SELECT `user`.`create_time` FROM `user` WHERE `user`.`id` = 'u1')");
    assertContains(sql, "JSON_TABLE(:tags");
    assertContains(sql, "(SELECT `user`.`create_user_id` FROM `user` WHERE `user`.`id` = 'u1')");
    assertContains(sql, "(SELECT `user`.`create_group_id` FROM `user` WHERE `user`.`id` = 'u1')");
    assertContains(sql, "(SELECT `user`.`realm_id` FROM `user` WHERE `user`.`id` = 'u1')");
  }

  private static MutationTranslator newTranslator() {
    try {
      PackageManager packageManager = allocateWithoutConstructor(PackageManager.class);
      DocumentManager documentManager =
          new DocumentManager(packageManager)
              .setDocument(
                  new Document()
                      .addDefinitionsByFileName("graphql/core.gql")
                      .addDefinitions(TEST_SCHEMA));
      ArgumentsTranslator argumentsTranslator = new ArgumentsTranslator(documentManager);
      TypeTranslator typeTranslator = new TypeTranslator(documentManager, packageManager);
      return new MutationTranslator(
          documentManager, packageManager, argumentsTranslator, typeTranslator);
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
}
