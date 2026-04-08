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
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Order Input for __Type")
public @interface __TypeOrderBy1 {
  /**
   * id
   */
  @Description("id")
  Sort id() default Sort.ASC;

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
  Sort idCount() default Sort.ASC;

  /**
   * Count of name
   */
  @Description("Count of name")
  Sort nameCount() default Sort.ASC;

  /**
   * Count of kind
   */
  @Description("Count of kind")
  Sort kindCount() default Sort.ASC;

  /**
   * Count of description
   */
  @Description("Count of description")
  Sort descriptionCount() default Sort.ASC;

  /**
   * Count of ofSchema Reference
   */
  @Description("Count of ofSchema Reference")
  Sort schemaIdCount() default Sort.ASC;

  /**
   * Count of ofType Reference
   */
  @Description("Count of ofType Reference")
  Sort ofTypeNameCount() default Sort.ASC;

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
   * Year of Create Time
   */
  @Description("Year of Create Time")
  Sort createTimeYear() default Sort.ASC;

  /**
   * Month of Create Time
   */
  @Description("Month of Create Time")
  Sort createTimeMonth() default Sort.ASC;

  /**
   * Day of Create Time
   */
  @Description("Day of Create Time")
  Sort createTimeDay() default Sort.ASC;

  /**
   * Week of Create Time
   */
  @Description("Week of Create Time")
  Sort createTimeWeek() default Sort.ASC;

  /**
   * Quarter of Create Time
   */
  @Description("Quarter of Create Time")
  Sort createTimeQuarter() default Sort.ASC;

  /**
   * Year of Update Time
   */
  @Description("Year of Update Time")
  Sort updateTimeYear() default Sort.ASC;

  /**
   * Month of Update Time
   */
  @Description("Month of Update Time")
  Sort updateTimeMonth() default Sort.ASC;

  /**
   * Day of Update Time
   */
  @Description("Day of Update Time")
  Sort updateTimeDay() default Sort.ASC;

  /**
   * Week of Update Time
   */
  @Description("Week of Update Time")
  Sort updateTimeWeek() default Sort.ASC;

  /**
   * Quarter of Update Time
   */
  @Description("Quarter of Update Time")
  Sort updateTimeQuarter() default Sort.ASC;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  __TypeOrderBy2[] obs() default {};

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

  String $idCount() default "";

  String $nameCount() default "";

  String $kindCount() default "";

  String $descriptionCount() default "";

  String $schemaIdCount() default "";

  String $ofTypeNameCount() default "";

  String $schemaIdMax() default "";

  String $schemaIdMin() default "";

  String $schemaIdSum() default "";

  String $schemaIdAvg() default "";

  String $createTimeYear() default "";

  String $createTimeMonth() default "";

  String $createTimeDay() default "";

  String $createTimeWeek() default "";

  String $createTimeQuarter() default "";

  String $updateTimeYear() default "";

  String $updateTimeMonth() default "";

  String $updateTimeDay() default "";

  String $updateTimeWeek() default "";

  String $updateTimeQuarter() default "";

  String $obs() default "";
}
