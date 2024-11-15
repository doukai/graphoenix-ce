package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Query
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query")
public class Query {
  /**
   * Query Field for __Schema
   */
  @Description("Query Field for __Schema")
  private __Schema __schema;

  /**
   * Query Field for __Schema List
   */
  @Description("Query Field for __Schema List")
  private Collection<__Schema> __schemaList;

  /**
   * Query Field for __Schema Connection
   */
  @Description("Query Field for __Schema Connection")
  private __SchemaConnection __schemaConnection;

  /**
   * Query Field for __Type
   */
  @Description("Query Field for __Type")
  private __Type __type;

  /**
   * Query Field for __Type List
   */
  @Description("Query Field for __Type List")
  private Collection<__Type> __typeList;

  /**
   * Query Field for __Type Connection
   */
  @Description("Query Field for __Type Connection")
  private __TypeConnection __typeConnection;

  /**
   * Query Field for __Field
   */
  @Description("Query Field for __Field")
  private __Field __field;

  /**
   * Query Field for __Field List
   */
  @Description("Query Field for __Field List")
  private Collection<__Field> __fieldList;

  /**
   * Query Field for __Field Connection
   */
  @Description("Query Field for __Field Connection")
  private __FieldConnection __fieldConnection;

  /**
   * Query Field for __InputValue
   */
  @Description("Query Field for __InputValue")
  private __InputValue __inputValue;

  /**
   * Query Field for __InputValue List
   */
  @Description("Query Field for __InputValue List")
  private Collection<__InputValue> __inputValueList;

  /**
   * Query Field for __InputValue Connection
   */
  @Description("Query Field for __InputValue Connection")
  private __InputValueConnection __inputValueConnection;

  /**
   * Query Field for __EnumValue
   */
  @Description("Query Field for __EnumValue")
  private __EnumValue __enumValue;

  /**
   * Query Field for __EnumValue List
   */
  @Description("Query Field for __EnumValue List")
  private Collection<__EnumValue> __enumValueList;

  /**
   * Query Field for __EnumValue Connection
   */
  @Description("Query Field for __EnumValue Connection")
  private __EnumValueConnection __enumValueConnection;

  /**
   * Query Field for __Directive
   */
  @Description("Query Field for __Directive")
  private __Directive __directive;

  /**
   * Query Field for __Directive List
   */
  @Description("Query Field for __Directive List")
  private Collection<__Directive> __directiveList;

  /**
   * Query Field for __Directive Connection
   */
  @Description("Query Field for __Directive Connection")
  private __DirectiveConnection __directiveConnection;

  /**
   * Query Field for Relationship Object between __Type and __Type
   */
  @Description("Query Field for Relationship Object between __Type and __Type")
  private __TypeInterfaces __typeInterfaces;

  /**
   * Query Field for Relationship Object between __Type and __Type List
   */
  @Description("Query Field for Relationship Object between __Type and __Type List")
  private Collection<__TypeInterfaces> __typeInterfacesList;

  /**
   * Query Field for Relationship Object between __Type and __Type Connection
   */
  @Description("Query Field for Relationship Object between __Type and __Type Connection")
  private __TypeInterfacesConnection __typeInterfacesConnection;

  /**
   * Query Field for Relationship Object between __Type and __Type
   */
  @Description("Query Field for Relationship Object between __Type and __Type")
  private __TypePossibleTypes __typePossibleTypes;

  /**
   * Query Field for Relationship Object between __Type and __Type List
   */
  @Description("Query Field for Relationship Object between __Type and __Type List")
  private Collection<__TypePossibleTypes> __typePossibleTypesList;

  /**
   * Query Field for Relationship Object between __Type and __Type Connection
   */
  @Description("Query Field for Relationship Object between __Type and __Type Connection")
  private __TypePossibleTypesConnection __typePossibleTypesConnection;

  /**
   * Query Field for Relationship Object between __Directive and locations
   */
  @Description("Query Field for Relationship Object between __Directive and locations")
  private __DirectiveLocationsRelation __directiveLocationsRelation;

  /**
   * Query Field for Relationship Object between __Directive and locations List
   */
  @Description("Query Field for Relationship Object between __Directive and locations List")
  private Collection<__DirectiveLocationsRelation> __directiveLocationsRelationList;

  /**
   * Query Field for Relationship Object between __Directive and locations Connection
   */
  @Description("Query Field for Relationship Object between __Directive and locations Connection")
  private __DirectiveLocationsRelationConnection __directiveLocationsRelationConnection;

  public __Schema get__schema() {
    return this.__schema;
  }

  public void set__schema(__Schema __schema) {
    this.__schema = __schema;
  }

  public Collection<__Schema> get__schemaList() {
    return this.__schemaList;
  }

  public void set__schemaList(Collection<__Schema> __schemaList) {
    this.__schemaList = __schemaList;
  }

  public __SchemaConnection get__schemaConnection() {
    return this.__schemaConnection;
  }

  public void set__schemaConnection(__SchemaConnection __schemaConnection) {
    this.__schemaConnection = __schemaConnection;
  }

  public __Type get__type() {
    return this.__type;
  }

  public void set__type(__Type __type) {
    this.__type = __type;
  }

  public Collection<__Type> get__typeList() {
    return this.__typeList;
  }

  public void set__typeList(Collection<__Type> __typeList) {
    this.__typeList = __typeList;
  }

  public __TypeConnection get__typeConnection() {
    return this.__typeConnection;
  }

  public void set__typeConnection(__TypeConnection __typeConnection) {
    this.__typeConnection = __typeConnection;
  }

  public __Field get__field() {
    return this.__field;
  }

  public void set__field(__Field __field) {
    this.__field = __field;
  }

  public Collection<__Field> get__fieldList() {
    return this.__fieldList;
  }

  public void set__fieldList(Collection<__Field> __fieldList) {
    this.__fieldList = __fieldList;
  }

  public __FieldConnection get__fieldConnection() {
    return this.__fieldConnection;
  }

  public void set__fieldConnection(__FieldConnection __fieldConnection) {
    this.__fieldConnection = __fieldConnection;
  }

  public __InputValue get__inputValue() {
    return this.__inputValue;
  }

  public void set__inputValue(__InputValue __inputValue) {
    this.__inputValue = __inputValue;
  }

  public Collection<__InputValue> get__inputValueList() {
    return this.__inputValueList;
  }

  public void set__inputValueList(Collection<__InputValue> __inputValueList) {
    this.__inputValueList = __inputValueList;
  }

  public __InputValueConnection get__inputValueConnection() {
    return this.__inputValueConnection;
  }

  public void set__inputValueConnection(__InputValueConnection __inputValueConnection) {
    this.__inputValueConnection = __inputValueConnection;
  }

  public __EnumValue get__enumValue() {
    return this.__enumValue;
  }

  public void set__enumValue(__EnumValue __enumValue) {
    this.__enumValue = __enumValue;
  }

  public Collection<__EnumValue> get__enumValueList() {
    return this.__enumValueList;
  }

  public void set__enumValueList(Collection<__EnumValue> __enumValueList) {
    this.__enumValueList = __enumValueList;
  }

  public __EnumValueConnection get__enumValueConnection() {
    return this.__enumValueConnection;
  }

  public void set__enumValueConnection(__EnumValueConnection __enumValueConnection) {
    this.__enumValueConnection = __enumValueConnection;
  }

  public __Directive get__directive() {
    return this.__directive;
  }

  public void set__directive(__Directive __directive) {
    this.__directive = __directive;
  }

  public Collection<__Directive> get__directiveList() {
    return this.__directiveList;
  }

  public void set__directiveList(Collection<__Directive> __directiveList) {
    this.__directiveList = __directiveList;
  }

  public __DirectiveConnection get__directiveConnection() {
    return this.__directiveConnection;
  }

  public void set__directiveConnection(__DirectiveConnection __directiveConnection) {
    this.__directiveConnection = __directiveConnection;
  }

  public __TypeInterfaces get__typeInterfaces() {
    return this.__typeInterfaces;
  }

  public void set__typeInterfaces(__TypeInterfaces __typeInterfaces) {
    this.__typeInterfaces = __typeInterfaces;
  }

  public Collection<__TypeInterfaces> get__typeInterfacesList() {
    return this.__typeInterfacesList;
  }

  public void set__typeInterfacesList(Collection<__TypeInterfaces> __typeInterfacesList) {
    this.__typeInterfacesList = __typeInterfacesList;
  }

  public __TypeInterfacesConnection get__typeInterfacesConnection() {
    return this.__typeInterfacesConnection;
  }

  public void set__typeInterfacesConnection(__TypeInterfacesConnection __typeInterfacesConnection) {
    this.__typeInterfacesConnection = __typeInterfacesConnection;
  }

  public __TypePossibleTypes get__typePossibleTypes() {
    return this.__typePossibleTypes;
  }

  public void set__typePossibleTypes(__TypePossibleTypes __typePossibleTypes) {
    this.__typePossibleTypes = __typePossibleTypes;
  }

  public Collection<__TypePossibleTypes> get__typePossibleTypesList() {
    return this.__typePossibleTypesList;
  }

  public void set__typePossibleTypesList(Collection<__TypePossibleTypes> __typePossibleTypesList) {
    this.__typePossibleTypesList = __typePossibleTypesList;
  }

  public __TypePossibleTypesConnection get__typePossibleTypesConnection() {
    return this.__typePossibleTypesConnection;
  }

  public void set__typePossibleTypesConnection(
      __TypePossibleTypesConnection __typePossibleTypesConnection) {
    this.__typePossibleTypesConnection = __typePossibleTypesConnection;
  }

  public __DirectiveLocationsRelation get__directiveLocationsRelation() {
    return this.__directiveLocationsRelation;
  }

  public void set__directiveLocationsRelation(
      __DirectiveLocationsRelation __directiveLocationsRelation) {
    this.__directiveLocationsRelation = __directiveLocationsRelation;
  }

  public Collection<__DirectiveLocationsRelation> get__directiveLocationsRelationList() {
    return this.__directiveLocationsRelationList;
  }

  public void set__directiveLocationsRelationList(
      Collection<__DirectiveLocationsRelation> __directiveLocationsRelationList) {
    this.__directiveLocationsRelationList = __directiveLocationsRelationList;
  }

  public __DirectiveLocationsRelationConnection get__directiveLocationsRelationConnection() {
    return this.__directiveLocationsRelationConnection;
  }

  public void set__directiveLocationsRelationConnection(
      __DirectiveLocationsRelationConnection __directiveLocationsRelationConnection) {
    this.__directiveLocationsRelationConnection = __directiveLocationsRelationConnection;
  }
}
