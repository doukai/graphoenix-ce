package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.introspection.dto.enumType.__TypeKind;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Mutation Arguments for __Type
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation Arguments for __Type")
public @interface __TypeMutationArguments {
  /**
   * name
   */
  @Description("name")
  String name() default "";

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaInput ofSchema() default @__SchemaInput;

  /**
   * kind
   */
  @Description("kind")
  __TypeKind kind() default __TypeKind.SCALAR;

  /**
   * description
   */
  @Description("description")
  String description() default "";

  /**
   * fields
   */
  @Description("fields")
  __FieldInput[] fields() default {};

  /**
   * interfaces
   */
  @Description("interfaces")
  __TypeInput[] interfaces() default {};

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  __TypeInput[] possibleTypes() default {};

  /**
   * enumValues
   */
  @Description("enumValues")
  __EnumValueInput[] enumValues() default {};

  /**
   * inputFields
   */
  @Description("inputFields")
  __InputValueInput[] inputFields() default {};

  /**
   * ofType
   */
  @Description("ofType")
  __TypeInput ofType() default @__TypeInput;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  boolean isDeprecated() default false;

  /**
   * Version
   */
  @Description("Version")
  int version() default 0;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  int realmId() default 0;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  String createUserId() default "";

  /**
   * Create Time
   */
  @Description("Create Time")
  String createTime() default "";

  /**
   * Update User ID
   */
  @Description("Update User ID")
  String updateUserId() default "";

  /**
   * Update Time
   */
  @Description("Update Time")
  String updateTime() default "";

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  String createGroupId() default "";

  /**
   * Type Name
   */
  @Description("Type Name")
  String __typename() default "__Type";

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  int schemaId() default 0;

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  String ofTypeName() default "";

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypeInterfacesInput[] __typeInterfaces() default {};

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypePossibleTypesInput[] __typePossibleTypes() default {};

  /**
   * Input
   */
  @Description("Input")
  __TypeInput input() default @__TypeInput;

  /**
   * Where
   */
  @Description("Where")
  __TypeExpression where() default @__TypeExpression;

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

  String $isDeprecated() default "";

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

  String $input() default "";

  String $where() default "";
}
