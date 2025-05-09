package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.microprofile.graphql.Description;

/**
 * Order Input for __Type
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Order Input for __Type")
public @interface __TypeOrderBy1 {
  /**
   * name
   */
  @Description("name")
  Sort name() default Sort.ASC;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  __SchemaOrderBy2 ofSchema() default @__SchemaOrderBy2;

  /**
   * kind
   */
  @Description("kind")
  Sort kind() default Sort.ASC;

  /**
   * description
   */
  @Description("description")
  Sort description() default Sort.ASC;

  /**
   * fields
   */
  @Description("fields")
  __FieldOrderBy2 fields() default @__FieldOrderBy2;

  /**
   * interfaces
   */
  @Description("interfaces")
  __TypeOrderBy2 interfaces() default @__TypeOrderBy2;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  __TypeOrderBy2 possibleTypes() default @__TypeOrderBy2;

  /**
   * enumValues
   */
  @Description("enumValues")
  __EnumValueOrderBy2 enumValues() default @__EnumValueOrderBy2;

  /**
   * inputFields
   */
  @Description("inputFields")
  __InputValueOrderBy2 inputFields() default @__InputValueOrderBy2;

  /**
   * ofType
   */
  @Description("ofType")
  __TypeOrderBy2 ofType() default @__TypeOrderBy2;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  Sort isDeprecated() default Sort.ASC;

  /**
   * Version
   */
  @Description("Version")
  Sort version() default Sort.ASC;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  Sort realmId() default Sort.ASC;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  Sort createUserId() default Sort.ASC;

  /**
   * Create Time
   */
  @Description("Create Time")
  Sort createTime() default Sort.ASC;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  Sort updateUserId() default Sort.ASC;

  /**
   * Update Time
   */
  @Description("Update Time")
  Sort updateTime() default Sort.ASC;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  Sort createGroupId() default Sort.ASC;

  /**
   * Type Name
   */
  @Description("Type Name")
  Sort __typename() default Sort.ASC;

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  Sort schemaId() default Sort.ASC;

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  Sort ofTypeName() default Sort.ASC;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypeInterfacesOrderBy2 __typeInterfaces() default @__TypeInterfacesOrderBy2;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  __TypePossibleTypesOrderBy2 __typePossibleTypes() default @__TypePossibleTypesOrderBy2;

  /**
   * Aggregate Field for fields
   */
  @Description("Aggregate Field for fields")
  __FieldOrderBy2 fieldsAggregate() default @__FieldOrderBy2;

  /**
   * Aggregate Field for interfaces
   */
  @Description("Aggregate Field for interfaces")
  __TypeOrderBy2 interfacesAggregate() default @__TypeOrderBy2;

  /**
   * Aggregate Field for possibleTypes
   */
  @Description("Aggregate Field for possibleTypes")
  __TypeOrderBy2 possibleTypesAggregate() default @__TypeOrderBy2;

  /**
   * Aggregate Field for enumValues
   */
  @Description("Aggregate Field for enumValues")
  __EnumValueOrderBy2 enumValuesAggregate() default @__EnumValueOrderBy2;

  /**
   * Aggregate Field for inputFields
   */
  @Description("Aggregate Field for inputFields")
  __InputValueOrderBy2 inputFieldsAggregate() default @__InputValueOrderBy2;

  /**
   * Aggregate Field for Relationship Object between __Type and __Type
   */
  @Description("Aggregate Field for Relationship Object between __Type and __Type")
  __TypeInterfacesOrderBy2 __typeInterfacesAggregate() default @__TypeInterfacesOrderBy2;

  /**
   * Aggregate Field for Relationship Object between __Type and __Type
   */
  @Description("Aggregate Field for Relationship Object between __Type and __Type")
  __TypePossibleTypesOrderBy2 __typePossibleTypesAggregate() default @__TypePossibleTypesOrderBy2;

  /**
   * Count of __Type
   */
  @Description("Count of __Type")
  Sort nameCount() default Sort.ASC;

  /**
   * Max of name
   */
  @Description("Max of name")
  Sort nameMax() default Sort.ASC;

  /**
   * Min of name
   */
  @Description("Min of name")
  Sort nameMin() default Sort.ASC;

  /**
   * Count of kind
   */
  @Description("Count of kind")
  Sort kindCount() default Sort.ASC;

  /**
   * Max of kind
   */
  @Description("Max of kind")
  Sort kindMax() default Sort.ASC;

  /**
   * Min of kind
   */
  @Description("Min of kind")
  Sort kindMin() default Sort.ASC;

  /**
   * Count of description
   */
  @Description("Count of description")
  Sort descriptionCount() default Sort.ASC;

  /**
   * Max of description
   */
  @Description("Max of description")
  Sort descriptionMax() default Sort.ASC;

  /**
   * Min of description
   */
  @Description("Min of description")
  Sort descriptionMin() default Sort.ASC;

  /**
   * Count of ofType Reference
   */
  @Description("Count of ofType Reference")
  Sort ofTypeNameCount() default Sort.ASC;

  /**
   * Max of ofType Reference
   */
  @Description("Max of ofType Reference")
  Sort ofTypeNameMax() default Sort.ASC;

  /**
   * Min of ofType Reference
   */
  @Description("Min of ofType Reference")
  Sort ofTypeNameMin() default Sort.ASC;

  /**
   * Count of ofSchema Reference
   */
  @Description("Count of ofSchema Reference")
  Sort schemaIdCount() default Sort.ASC;

  /**
   * Sum of ofSchema Reference
   */
  @Description("Sum of ofSchema Reference")
  Sort schemaIdSum() default Sort.ASC;

  /**
   * Avg of ofSchema Reference
   */
  @Description("Avg of ofSchema Reference")
  Sort schemaIdAvg() default Sort.ASC;

  /**
   * Max of ofSchema Reference
   */
  @Description("Max of ofSchema Reference")
  Sort schemaIdMax() default Sort.ASC;

  /**
   * Min of ofSchema Reference
   */
  @Description("Min of ofSchema Reference")
  Sort schemaIdMin() default Sort.ASC;

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

  String $fieldsAggregate() default "";

  String $interfacesAggregate() default "";

  String $possibleTypesAggregate() default "";

  String $enumValuesAggregate() default "";

  String $inputFieldsAggregate() default "";

  String $__typeInterfacesAggregate() default "";

  String $__typePossibleTypesAggregate() default "";

  String $nameCount() default "";

  String $nameMax() default "";

  String $nameMin() default "";

  String $kindCount() default "";

  String $kindMax() default "";

  String $kindMin() default "";

  String $descriptionCount() default "";

  String $descriptionMax() default "";

  String $descriptionMin() default "";

  String $ofTypeNameCount() default "";

  String $ofTypeNameMax() default "";

  String $ofTypeNameMin() default "";

  String $schemaIdCount() default "";

  String $schemaIdSum() default "";

  String $schemaIdAvg() default "";

  String $schemaIdMax() default "";

  String $schemaIdMin() default "";
}
