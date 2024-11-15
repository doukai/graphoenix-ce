package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.enumType.__TypeKind;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Mutation Input for __Type
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Mutation Input for __Type")
public @interface __TypeInput {
  /**
   * name
   */
  @Description("name")
  String name() default "";

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaInput1 ofSchema() default @__SchemaInput1;

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
  __FieldInput1[] fields() default {};

  /**
   * interfaces
   */
  @Description("interfaces")
  __TypeInput1[] interfaces() default {};

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  __TypeInput1[] possibleTypes() default {};

  /**
   * enumValues
   */
  @Description("enumValues")
  __EnumValueInput1[] enumValues() default {};

  /**
   * inputFields
   */
  @Description("inputFields")
  __InputValueInput1[] inputFields() default {};

  /**
   * ofType
   */
  @Description("ofType")
  __TypeInput1 ofType() default @__TypeInput1;

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
  String __typename() default "\"__Type\"";

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
  __TypeInterfacesInput1[] __typeInterfaces() default {};

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypePossibleTypesInput1[] __typePossibleTypes() default {};

  /**
   * Where
   */
  @Description("Where")
  __TypeExpression1 where() default @__TypeExpression1;

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

  String $where() default "";
}
