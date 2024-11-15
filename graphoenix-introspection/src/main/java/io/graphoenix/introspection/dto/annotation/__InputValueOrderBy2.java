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
 * Order Input for __InputValue
 */
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Description("Order Input for __InputValue")
public @interface __InputValueOrderBy2 {
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
   * fieldId
   */
  @Description("fieldId")
  Sort fieldId() default Sort.ASC;

  /**
   * directiveName
   */
  @Description("directiveName")
  Sort directiveName() default Sort.ASC;

  /**
   * description
   */
  @Description("description")
  Sort description() default Sort.ASC;

  /**
   * defaultValue
   */
  @Description("defaultValue")
  Sort defaultValue() default Sort.ASC;

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
   * ofType Reference
   */
  @Description("ofType Reference")
  Sort ofTypeName() default Sort.ASC;

  /**
   * type Reference
   */
  @Description("type Reference")
  Sort typeName() default Sort.ASC;

  /**
   * Count of __InputValue
   */
  @Description("Count of __InputValue")
  Sort idCount() default Sort.ASC;

  /**
   * Max of id
   */
  @Description("Max of id")
  Sort idMax() default Sort.ASC;

  /**
   * Min of id
   */
  @Description("Min of id")
  Sort idMin() default Sort.ASC;

  /**
   * Count of name
   */
  @Description("Count of name")
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
   * Count of directiveName
   */
  @Description("Count of directiveName")
  Sort directiveNameCount() default Sort.ASC;

  /**
   * Max of directiveName
   */
  @Description("Max of directiveName")
  Sort directiveNameMax() default Sort.ASC;

  /**
   * Min of directiveName
   */
  @Description("Min of directiveName")
  Sort directiveNameMin() default Sort.ASC;

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
   * Count of defaultValue
   */
  @Description("Count of defaultValue")
  Sort defaultValueCount() default Sort.ASC;

  /**
   * Max of defaultValue
   */
  @Description("Max of defaultValue")
  Sort defaultValueMax() default Sort.ASC;

  /**
   * Min of defaultValue
   */
  @Description("Min of defaultValue")
  Sort defaultValueMin() default Sort.ASC;

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
   * Count of type Reference
   */
  @Description("Count of type Reference")
  Sort typeNameCount() default Sort.ASC;

  /**
   * Max of type Reference
   */
  @Description("Max of type Reference")
  Sort typeNameMax() default Sort.ASC;

  /**
   * Min of type Reference
   */
  @Description("Min of type Reference")
  Sort typeNameMin() default Sort.ASC;

  /**
   * Count of fieldId
   */
  @Description("Count of fieldId")
  Sort fieldIdCount() default Sort.ASC;

  /**
   * Sum of fieldId
   */
  @Description("Sum of fieldId")
  Sort fieldIdSum() default Sort.ASC;

  /**
   * Avg of fieldId
   */
  @Description("Avg of fieldId")
  Sort fieldIdAvg() default Sort.ASC;

  /**
   * Max of fieldId
   */
  @Description("Max of fieldId")
  Sort fieldIdMax() default Sort.ASC;

  /**
   * Min of fieldId
   */
  @Description("Min of fieldId")
  Sort fieldIdMin() default Sort.ASC;

  String $id() default "";

  String $name() default "";

  String $fieldId() default "";

  String $directiveName() default "";

  String $description() default "";

  String $defaultValue() default "";

  String $isDeprecated() default "";

  String $version() default "";

  String $realmId() default "";

  String $createUserId() default "";

  String $createTime() default "";

  String $updateUserId() default "";

  String $updateTime() default "";

  String $createGroupId() default "";

  String $__typename() default "";

  String $ofTypeName() default "";

  String $typeName() default "";

  String $idCount() default "";

  String $idMax() default "";

  String $idMin() default "";

  String $nameCount() default "";

  String $nameMax() default "";

  String $nameMin() default "";

  String $directiveNameCount() default "";

  String $directiveNameMax() default "";

  String $directiveNameMin() default "";

  String $descriptionCount() default "";

  String $descriptionMax() default "";

  String $descriptionMin() default "";

  String $defaultValueCount() default "";

  String $defaultValueMax() default "";

  String $defaultValueMin() default "";

  String $ofTypeNameCount() default "";

  String $ofTypeNameMax() default "";

  String $ofTypeNameMin() default "";

  String $typeNameCount() default "";

  String $typeNameMax() default "";

  String $typeNameMin() default "";

  String $fieldIdCount() default "";

  String $fieldIdSum() default "";

  String $fieldIdAvg() default "";

  String $fieldIdMax() default "";

  String $fieldIdMin() default "";
}
