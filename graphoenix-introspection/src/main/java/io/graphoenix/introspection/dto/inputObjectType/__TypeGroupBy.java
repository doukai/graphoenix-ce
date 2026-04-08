package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Group Input for __Type
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Group Input for __Type")
public class __TypeGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  private Collection<String> by;

  /**
   * ofSchema
   */
  @Description("ofSchema")
  private __SchemaGroupBy ofSchema;

  /**
   * fields
   */
  @Description("fields")
  private __FieldGroupBy fields;

  /**
   * interfaces
   */
  @Description("interfaces")
  private __TypeGroupBy interfaces;

  /**
   * possibleTypes
   */
  @Description("possibleTypes")
  private __TypeGroupBy possibleTypes;

  /**
   * enumValues
   */
  @Description("enumValues")
  private __EnumValueGroupBy enumValues;

  /**
   * inputFields
   */
  @Description("inputFields")
  private __InputValueGroupBy inputFields;

  /**
   * ofType
   */
  @Description("ofType")
  private __TypeGroupBy ofType;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  private __TypeInterfacesGroupBy __typeInterfaces;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  private __TypePossibleTypesGroupBy __typePossibleTypes;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<__TypeGroupBy> gbs;

  public Collection<String> getBy() {
    return this.by;
  }

  public void setBy(Collection<String> by) {
    this.by = by;
  }

  public __SchemaGroupBy getOfSchema() {
    return this.ofSchema;
  }

  public void setOfSchema(__SchemaGroupBy ofSchema) {
    this.ofSchema = ofSchema;
  }

  public __FieldGroupBy getFields() {
    return this.fields;
  }

  public void setFields(__FieldGroupBy fields) {
    this.fields = fields;
  }

  public __TypeGroupBy getInterfaces() {
    return this.interfaces;
  }

  public void setInterfaces(__TypeGroupBy interfaces) {
    this.interfaces = interfaces;
  }

  public __TypeGroupBy getPossibleTypes() {
    return this.possibleTypes;
  }

  public void setPossibleTypes(__TypeGroupBy possibleTypes) {
    this.possibleTypes = possibleTypes;
  }

  public __EnumValueGroupBy getEnumValues() {
    return this.enumValues;
  }

  public void setEnumValues(__EnumValueGroupBy enumValues) {
    this.enumValues = enumValues;
  }

  public __InputValueGroupBy getInputFields() {
    return this.inputFields;
  }

  public void setInputFields(__InputValueGroupBy inputFields) {
    this.inputFields = inputFields;
  }

  public __TypeGroupBy getOfType() {
    return this.ofType;
  }

  public void setOfType(__TypeGroupBy ofType) {
    this.ofType = ofType;
  }

  public __TypeInterfacesGroupBy get__typeInterfaces() {
    return this.__typeInterfaces;
  }

  public void set__typeInterfaces(__TypeInterfacesGroupBy __typeInterfaces) {
    this.__typeInterfaces = __typeInterfaces;
  }

  public __TypePossibleTypesGroupBy get__typePossibleTypes() {
    return this.__typePossibleTypes;
  }

  public void set__typePossibleTypes(__TypePossibleTypesGroupBy __typePossibleTypes) {
    this.__typePossibleTypes = __typePossibleTypes;
  }

  public Collection<__TypeGroupBy> getGbs() {
    return this.gbs;
  }

  public void setGbs(Collection<__TypeGroupBy> gbs) {
    this.gbs = gbs;
  }
}
