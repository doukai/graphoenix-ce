package io.graphoenix.introspection.dto.annotation;

import io.graphoenix.core.dto.enumType.Sort;
import jakarta.annotation.Generated;
import java.lang.String;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface __TypeOrderBy1 {
  Sort name() default Sort.ASC;

  __SchemaOrderBy2 ofSchema() default @__SchemaOrderBy2;

  Sort kind() default Sort.ASC;

  Sort description() default Sort.ASC;

  __FieldOrderBy2 fields() default @__FieldOrderBy2;

  __TypeOrderBy2 interfaces() default @__TypeOrderBy2;

  __TypeOrderBy2 possibleTypes() default @__TypeOrderBy2;

  __EnumValueOrderBy2 enumValues() default @__EnumValueOrderBy2;

  __InputValueOrderBy2 inputFields() default @__InputValueOrderBy2;

  __TypeOrderBy2 ofType() default @__TypeOrderBy2;

  Sort isDeprecated() default Sort.ASC;

  Sort version() default Sort.ASC;

  Sort realmId() default Sort.ASC;

  Sort createUserId() default Sort.ASC;

  Sort createTime() default Sort.ASC;

  Sort updateUserId() default Sort.ASC;

  Sort updateTime() default Sort.ASC;

  Sort createGroupId() default Sort.ASC;

  Sort __typename() default Sort.ASC;

  Sort schemaId() default Sort.ASC;

  Sort ofTypeName() default Sort.ASC;

  __TypeInterfacesOrderBy2 __typeInterfaces() default @__TypeInterfacesOrderBy2;

  __TypePossibleTypesOrderBy2 __typePossibleTypes() default @__TypePossibleTypesOrderBy2;

  __FieldOrderBy2 fieldsAggregate() default @__FieldOrderBy2;

  __TypeOrderBy2 interfacesAggregate() default @__TypeOrderBy2;

  __TypeOrderBy2 possibleTypesAggregate() default @__TypeOrderBy2;

  __EnumValueOrderBy2 enumValuesAggregate() default @__EnumValueOrderBy2;

  __InputValueOrderBy2 inputFieldsAggregate() default @__InputValueOrderBy2;

  __TypeInterfacesOrderBy2 __typeInterfacesAggregate() default @__TypeInterfacesOrderBy2;

  __TypePossibleTypesOrderBy2 __typePossibleTypesAggregate() default @__TypePossibleTypesOrderBy2;

  Sort nameCount() default Sort.ASC;

  Sort nameMax() default Sort.ASC;

  Sort nameMin() default Sort.ASC;

  Sort kindCount() default Sort.ASC;

  Sort kindMax() default Sort.ASC;

  Sort kindMin() default Sort.ASC;

  Sort descriptionCount() default Sort.ASC;

  Sort descriptionMax() default Sort.ASC;

  Sort descriptionMin() default Sort.ASC;

  Sort ofTypeNameCount() default Sort.ASC;

  Sort ofTypeNameMax() default Sort.ASC;

  Sort ofTypeNameMin() default Sort.ASC;

  Sort schemaIdCount() default Sort.ASC;

  Sort schemaIdSum() default Sort.ASC;

  Sort schemaIdAvg() default Sort.ASC;

  Sort schemaIdMax() default Sort.ASC;

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
