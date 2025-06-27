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
public @interface __TypeExpression {
  /**
   * name
   */
  @Description("name")
  StringExpression name() default @StringExpression;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaExpression1 ofSchema() default @__SchemaExpression1;

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
  __FieldExpression1 fields() default @__FieldExpression1;

  /**
   * interfaces
   */
  @Description("interfaces")
  __TypeExpression1 interfaces() default @__TypeExpression1;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  __TypeExpression1 possibleTypes() default @__TypeExpression1;

  /**
   * enumValues
   */
  @Description("enumValues")
  __EnumValueExpression1 enumValues() default @__EnumValueExpression1;

  /**
   * inputFields
   */
  @Description("inputFields")
  __InputValueExpression1 inputFields() default @__InputValueExpression1;

  /**
   * ofType
   */
  @Description("ofType")
  __TypeExpression1 ofType() default @__TypeExpression1;

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
  __TypeInterfacesExpression1 __typeInterfaces() default @__TypeInterfacesExpression1;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypePossibleTypesExpression1 __typePossibleTypes() default @__TypePossibleTypesExpression1;

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
  __TypeExpression1[] exs() default {};

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
