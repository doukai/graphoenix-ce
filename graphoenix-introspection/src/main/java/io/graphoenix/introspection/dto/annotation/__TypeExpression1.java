package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.annotation.IntExpression;
import io.graphoenix.core.dto.annotation.StringExpression;
import io.graphoenix.core.dto.enumType.Conditional;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Query Expression Input for __Type
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Query Expression Input for __Type")
public @interface __TypeExpression1 {
  /**
   * id
   */
  @Description("id")
  StringExpression id() default @StringExpression;

  /**
   * name
   */
  @Description("name")
  StringExpression name() default @StringExpression;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaExpression2 ofSchema() default @__SchemaExpression2;

  /**
   * kind
   */
  @Description("kind")
  __TypeKindExpression kind() default @__TypeKindExpression;

  /**
   * description
   */
  @Description("description")
  StringExpression description() default @StringExpression;

  /**
   * fields
   */
  @Description("fields")
  __FieldExpression2 fields() default @__FieldExpression2;

  /**
   * interfaces
   */
  @Description("interfaces")
  __TypeExpression2 interfaces() default @__TypeExpression2;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  __TypeExpression2 possibleTypes() default @__TypeExpression2;

  /**
   * enumValues
   */
  @Description("enumValues")
  __EnumValueExpression2 enumValues() default @__EnumValueExpression2;

  /**
   * inputFields
   */
  @Description("inputFields")
  __InputValueExpression2 inputFields() default @__InputValueExpression2;

  /**
   * ofType
   */
  @Description("ofType")
  __TypeExpression2 ofType() default @__TypeExpression2;

  /**
   * Include Deprecated
   */
  @Description("Include Deprecated")
  boolean includeDeprecated() default false;

  /**
   * Version
   */
  @Description("Version")
  IntExpression version() default @IntExpression;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  IntExpression realmId() default @IntExpression;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  StringExpression createUserId() default @StringExpression;

  /**
   * Create Time
   */
  @Description("Create Time")
  StringExpression createTime() default @StringExpression;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  StringExpression updateUserId() default @StringExpression;

  /**
   * Update Time
   */
  @Description("Update Time")
  StringExpression updateTime() default @StringExpression;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  StringExpression createGroupId() default @StringExpression;

  /**
   * Type Name
   */
  @Description("Type Name")
  StringExpression __typename() default @StringExpression;

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  IntExpression schemaId() default @IntExpression;

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  StringExpression ofTypeName() default @StringExpression;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypeInterfacesExpression2 __typeInterfaces() default @__TypeInterfacesExpression2;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypePossibleTypesExpression2 __typePossibleTypes() default @__TypePossibleTypesExpression2;

  /**
   * Not
   */
  @Description("Not")
  boolean not() default false;

  /**
   * Condition
   */
  @Description("Condition")
  Conditional cond() default Conditional.AND;

  /**
   * Expressions
   */
  @Description("Expressions")
  __TypeExpression2[] exs() default {};

  String $id() default "";

  String $name() default "";

  String $ofSchema() default "";

  String $kind() default "";

  String $description() default "";

  String $fields() default "";

  String $interfaces() default "";

  String $possibleTypes() default "";

  String $enumValues() default "";

  String $inputFields() default "";

  String $ofType() default "";

  String $includeDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $schemaId() default "";

  String $ofTypeName() default "";

  String $__typeInterfaces() default "";

  String $__typePossibleTypes() default "";

  String $not() default "";

  String $cond() default "";

  String $exs() default "";
}
