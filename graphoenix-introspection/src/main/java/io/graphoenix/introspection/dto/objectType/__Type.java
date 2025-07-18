package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.interfaceType.Meta;
import io.graphoenix.introspection.dto.enumType.__TypeKind;
import io.graphoenix.introspection.dto.inputObjectType.__TypeInput;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __Type implements Meta {
  @Id
  private String id;

  @NonNull
  private String name;

  private __Schema ofSchema;

  @NonNull
  private __TypeKind kind;

  private String description;

  private Collection<__Field> fields;

  private Collection<__Type> interfaces;

  private Collection<__Type> possibleTypes;

  private Collection<__EnumValue> enumValues;

  private Collection<__InputValue> inputFields;

  private __Type ofType;

  /**
   * Is Deprecated
   */
  @Description("Is Deprecated")
  private Boolean isDeprecated = false;

  /**
   * Version
   */
  @Description("Version")
  private Integer version;

  /**
   * Realm ID
   */
  @Description("Realm ID")
  private Integer realmId;

  /**
   * Create User ID
   */
  @Description("Create User ID")
  private String createUserId;

  /**
   * Create Time
   */
  @Description("Create Time")
  private LocalDateTime createTime;

  /**
   * Update User ID
   */
  @Description("Update User ID")
  private String updateUserId;

  /**
   * Update Time
   */
  @Description("Update Time")
  private LocalDateTime updateTime;

  /**
   * Create Group ID
   */
  @Description("Create Group ID")
  private String createGroupId;

  /**
   * Type Name
   */
  @Description("Type Name")
  private String __typename = "__Type";

  /**
   * ofSchema Reference
   */
  @Description("ofSchema Reference")
  private Integer schemaId;

  /**
   * ofType Reference
   */
  @Description("ofType Reference")
  private String ofTypeName;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  private Collection<__TypeInterfaces> __typeInterfaces;

  /**
   * Relationship Object between __Type and __Type
   */
  @Description("Relationship Object between __Type and __Type")
  private Collection<__TypePossibleTypes> __typePossibleTypes;

  /**
   * Aggregate Field for fields
   */
  @Description("Aggregate Field for fields")
  private __Field fieldsAggregate;

  /**
   * Connection Field for fields
   */
  @Description("Connection Field for fields")
  private __FieldConnection fieldsConnection;

  /**
   * Aggregate Field for interfaces
   */
  @Description("Aggregate Field for interfaces")
  private __Type interfacesAggregate;

  /**
   * Connection Field for interfaces
   */
  @Description("Connection Field for interfaces")
  private __TypeConnection interfacesConnection;

  /**
   * Aggregate Field for possibleTypes
   */
  @Description("Aggregate Field for possibleTypes")
  private __Type possibleTypesAggregate;

  /**
   * Connection Field for possibleTypes
   */
  @Description("Connection Field for possibleTypes")
  private __TypeConnection possibleTypesConnection;

  /**
   * Aggregate Field for enumValues
   */
  @Description("Aggregate Field for enumValues")
  private __EnumValue enumValuesAggregate;

  /**
   * Connection Field for enumValues
   */
  @Description("Connection Field for enumValues")
  private __EnumValueConnection enumValuesConnection;

  /**
   * Aggregate Field for inputFields
   */
  @Description("Aggregate Field for inputFields")
  private __InputValue inputFieldsAggregate;

  /**
   * Connection Field for inputFields
   */
  @Description("Connection Field for inputFields")
  private __InputValueConnection inputFieldsConnection;

  /**
   * Aggregate Field for Relationship Object between __Type and __Type
   */
  @Description("Aggregate Field for Relationship Object between __Type and __Type")
  private __TypeInterfaces __typeInterfacesAggregate;

  /**
   * Connection Field for Relationship Object between __Type and __Type
   */
  @Description("Connection Field for Relationship Object between __Type and __Type")
  private __TypeInterfacesConnection __typeInterfacesConnection;

  /**
   * Aggregate Field for Relationship Object between __Type and __Type
   */
  @Description("Aggregate Field for Relationship Object between __Type and __Type")
  private __TypePossibleTypes __typePossibleTypesAggregate;

  /**
   * Connection Field for Relationship Object between __Type and __Type
   */
  @Description("Connection Field for Relationship Object between __Type and __Type")
  private __TypePossibleTypesConnection __typePossibleTypesConnection;

  /**
   * Count of __Type
   */
  @Description("Count of __Type")
  private Integer idCount;

  /**
   * Max of id
   */
  @Description("Max of id")
  private Integer idMax;

  /**
   * Min of id
   */
  @Description("Min of id")
  private Integer idMin;

  /**
   * Count of name
   */
  @Description("Count of name")
  private Integer nameCount;

  /**
   * Max of name
   */
  @Description("Max of name")
  private String nameMax;

  /**
   * Min of name
   */
  @Description("Min of name")
  private String nameMin;

  /**
   * Count of kind
   */
  @Description("Count of kind")
  private Integer kindCount;

  /**
   * Max of kind
   */
  @Description("Max of kind")
  private __TypeKind kindMax;

  /**
   * Min of kind
   */
  @Description("Min of kind")
  private __TypeKind kindMin;

  /**
   * Count of description
   */
  @Description("Count of description")
  private Integer descriptionCount;

  /**
   * Max of description
   */
  @Description("Max of description")
  private String descriptionMax;

  /**
   * Min of description
   */
  @Description("Min of description")
  private String descriptionMin;

  /**
   * Count of ofType Reference
   */
  @Description("Count of ofType Reference")
  private Integer ofTypeNameCount;

  /**
   * Max of ofType Reference
   */
  @Description("Max of ofType Reference")
  private String ofTypeNameMax;

  /**
   * Min of ofType Reference
   */
  @Description("Min of ofType Reference")
  private String ofTypeNameMin;

  /**
   * Count of ofSchema Reference
   */
  @Description("Count of ofSchema Reference")
  private Integer schemaIdCount;

  /**
   * Sum of ofSchema Reference
   */
  @Description("Sum of ofSchema Reference")
  private Integer schemaIdSum;

  /**
   * Avg of ofSchema Reference
   */
  @Description("Avg of ofSchema Reference")
  private Integer schemaIdAvg;

  /**
   * Max of ofSchema Reference
   */
  @Description("Max of ofSchema Reference")
  private Integer schemaIdMax;

  /**
   * Min of ofSchema Reference
   */
  @Description("Min of ofSchema Reference")
  private Integer schemaIdMin;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public __Schema getOfSchema() {
    return this.ofSchema;
  }

  public void setOfSchema(__Schema ofSchema) {
    this.ofSchema = ofSchema;
  }

  public __TypeKind getKind() {
    return this.kind;
  }

  public void setKind(__TypeKind kind) {
    this.kind = kind;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Collection<__Field> getFields() {
    return this.fields;
  }

  public void setFields(Collection<__Field> fields) {
    this.fields = fields;
  }

  public Collection<__Type> getInterfaces() {
    return this.interfaces;
  }

  public void setInterfaces(Collection<__Type> interfaces) {
    this.interfaces = interfaces;
  }

  public Collection<__Type> getPossibleTypes() {
    return this.possibleTypes;
  }

  public void setPossibleTypes(Collection<__Type> possibleTypes) {
    this.possibleTypes = possibleTypes;
  }

  public Collection<__EnumValue> getEnumValues() {
    return this.enumValues;
  }

  public void setEnumValues(Collection<__EnumValue> enumValues) {
    this.enumValues = enumValues;
  }

  public Collection<__InputValue> getInputFields() {
    return this.inputFields;
  }

  public void setInputFields(Collection<__InputValue> inputFields) {
    this.inputFields = inputFields;
  }

  public __Type getOfType() {
    return this.ofType;
  }

  public void setOfType(__Type ofType) {
    this.ofType = ofType;
  }

  @Override
  public Boolean getIsDeprecated() {
    return this.isDeprecated;
  }

  @Override
  public void setIsDeprecated(Boolean isDeprecated) {
    this.isDeprecated = (Boolean)isDeprecated;
  }

  @Override
  public Integer getVersion() {
    return this.version;
  }

  @Override
  public void setVersion(Integer version) {
    this.version = (Integer)version;
  }

  @Override
  public Integer getRealmId() {
    return this.realmId;
  }

  @Override
  public void setRealmId(Integer realmId) {
    this.realmId = (Integer)realmId;
  }

  @Override
  public String getCreateUserId() {
    return this.createUserId;
  }

  @Override
  public void setCreateUserId(String createUserId) {
    this.createUserId = (String)createUserId;
  }

  @Override
  public LocalDateTime getCreateTime() {
    return this.createTime;
  }

  @Override
  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = (LocalDateTime)createTime;
  }

  @Override
  public String getUpdateUserId() {
    return this.updateUserId;
  }

  @Override
  public void setUpdateUserId(String updateUserId) {
    this.updateUserId = (String)updateUserId;
  }

  @Override
  public LocalDateTime getUpdateTime() {
    return this.updateTime;
  }

  @Override
  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = (LocalDateTime)updateTime;
  }

  @Override
  public String getCreateGroupId() {
    return this.createGroupId;
  }

  @Override
  public void setCreateGroupId(String createGroupId) {
    this.createGroupId = (String)createGroupId;
  }

  public String get__typename() {
    return this.__typename;
  }

  public void set__typename(String __typename) {
    this.__typename = __typename;
  }

  public Integer getSchemaId() {
    return this.schemaId;
  }

  public void setSchemaId(Integer schemaId) {
    this.schemaId = schemaId;
  }

  public String getOfTypeName() {
    return this.ofTypeName;
  }

  public void setOfTypeName(String ofTypeName) {
    this.ofTypeName = ofTypeName;
  }

  public Collection<__TypeInterfaces> get__typeInterfaces() {
    return this.__typeInterfaces;
  }

  public void set__typeInterfaces(Collection<__TypeInterfaces> __typeInterfaces) {
    this.__typeInterfaces = __typeInterfaces;
  }

  public Collection<__TypePossibleTypes> get__typePossibleTypes() {
    return this.__typePossibleTypes;
  }

  public void set__typePossibleTypes(Collection<__TypePossibleTypes> __typePossibleTypes) {
    this.__typePossibleTypes = __typePossibleTypes;
  }

  public __Field getFieldsAggregate() {
    return this.fieldsAggregate;
  }

  public void setFieldsAggregate(__Field fieldsAggregate) {
    this.fieldsAggregate = fieldsAggregate;
  }

  public __FieldConnection getFieldsConnection() {
    return this.fieldsConnection;
  }

  public void setFieldsConnection(__FieldConnection fieldsConnection) {
    this.fieldsConnection = fieldsConnection;
  }

  public __Type getInterfacesAggregate() {
    return this.interfacesAggregate;
  }

  public void setInterfacesAggregate(__Type interfacesAggregate) {
    this.interfacesAggregate = interfacesAggregate;
  }

  public __TypeConnection getInterfacesConnection() {
    return this.interfacesConnection;
  }

  public void setInterfacesConnection(__TypeConnection interfacesConnection) {
    this.interfacesConnection = interfacesConnection;
  }

  public __Type getPossibleTypesAggregate() {
    return this.possibleTypesAggregate;
  }

  public void setPossibleTypesAggregate(__Type possibleTypesAggregate) {
    this.possibleTypesAggregate = possibleTypesAggregate;
  }

  public __TypeConnection getPossibleTypesConnection() {
    return this.possibleTypesConnection;
  }

  public void setPossibleTypesConnection(__TypeConnection possibleTypesConnection) {
    this.possibleTypesConnection = possibleTypesConnection;
  }

  public __EnumValue getEnumValuesAggregate() {
    return this.enumValuesAggregate;
  }

  public void setEnumValuesAggregate(__EnumValue enumValuesAggregate) {
    this.enumValuesAggregate = enumValuesAggregate;
  }

  public __EnumValueConnection getEnumValuesConnection() {
    return this.enumValuesConnection;
  }

  public void setEnumValuesConnection(__EnumValueConnection enumValuesConnection) {
    this.enumValuesConnection = enumValuesConnection;
  }

  public __InputValue getInputFieldsAggregate() {
    return this.inputFieldsAggregate;
  }

  public void setInputFieldsAggregate(__InputValue inputFieldsAggregate) {
    this.inputFieldsAggregate = inputFieldsAggregate;
  }

  public __InputValueConnection getInputFieldsConnection() {
    return this.inputFieldsConnection;
  }

  public void setInputFieldsConnection(__InputValueConnection inputFieldsConnection) {
    this.inputFieldsConnection = inputFieldsConnection;
  }

  public __TypeInterfaces get__typeInterfacesAggregate() {
    return this.__typeInterfacesAggregate;
  }

  public void set__typeInterfacesAggregate(__TypeInterfaces __typeInterfacesAggregate) {
    this.__typeInterfacesAggregate = __typeInterfacesAggregate;
  }

  public __TypeInterfacesConnection get__typeInterfacesConnection() {
    return this.__typeInterfacesConnection;
  }

  public void set__typeInterfacesConnection(__TypeInterfacesConnection __typeInterfacesConnection) {
    this.__typeInterfacesConnection = __typeInterfacesConnection;
  }

  public __TypePossibleTypes get__typePossibleTypesAggregate() {
    return this.__typePossibleTypesAggregate;
  }

  public void set__typePossibleTypesAggregate(__TypePossibleTypes __typePossibleTypesAggregate) {
    this.__typePossibleTypesAggregate = __typePossibleTypesAggregate;
  }

  public __TypePossibleTypesConnection get__typePossibleTypesConnection() {
    return this.__typePossibleTypesConnection;
  }

  public void set__typePossibleTypesConnection(
      __TypePossibleTypesConnection __typePossibleTypesConnection) {
    this.__typePossibleTypesConnection = __typePossibleTypesConnection;
  }

  public Integer getIdCount() {
    return this.idCount;
  }

  public void setIdCount(Integer idCount) {
    this.idCount = idCount;
  }

  public Integer getIdMax() {
    return this.idMax;
  }

  public void setIdMax(Integer idMax) {
    this.idMax = idMax;
  }

  public Integer getIdMin() {
    return this.idMin;
  }

  public void setIdMin(Integer idMin) {
    this.idMin = idMin;
  }

  public Integer getNameCount() {
    return this.nameCount;
  }

  public void setNameCount(Integer nameCount) {
    this.nameCount = nameCount;
  }

  public String getNameMax() {
    return this.nameMax;
  }

  public void setNameMax(String nameMax) {
    this.nameMax = nameMax;
  }

  public String getNameMin() {
    return this.nameMin;
  }

  public void setNameMin(String nameMin) {
    this.nameMin = nameMin;
  }

  public Integer getKindCount() {
    return this.kindCount;
  }

  public void setKindCount(Integer kindCount) {
    this.kindCount = kindCount;
  }

  public __TypeKind getKindMax() {
    return this.kindMax;
  }

  public void setKindMax(__TypeKind kindMax) {
    this.kindMax = kindMax;
  }

  public __TypeKind getKindMin() {
    return this.kindMin;
  }

  public void setKindMin(__TypeKind kindMin) {
    this.kindMin = kindMin;
  }

  public Integer getDescriptionCount() {
    return this.descriptionCount;
  }

  public void setDescriptionCount(Integer descriptionCount) {
    this.descriptionCount = descriptionCount;
  }

  public String getDescriptionMax() {
    return this.descriptionMax;
  }

  public void setDescriptionMax(String descriptionMax) {
    this.descriptionMax = descriptionMax;
  }

  public String getDescriptionMin() {
    return this.descriptionMin;
  }

  public void setDescriptionMin(String descriptionMin) {
    this.descriptionMin = descriptionMin;
  }

  public Integer getOfTypeNameCount() {
    return this.ofTypeNameCount;
  }

  public void setOfTypeNameCount(Integer ofTypeNameCount) {
    this.ofTypeNameCount = ofTypeNameCount;
  }

  public String getOfTypeNameMax() {
    return this.ofTypeNameMax;
  }

  public void setOfTypeNameMax(String ofTypeNameMax) {
    this.ofTypeNameMax = ofTypeNameMax;
  }

  public String getOfTypeNameMin() {
    return this.ofTypeNameMin;
  }

  public void setOfTypeNameMin(String ofTypeNameMin) {
    this.ofTypeNameMin = ofTypeNameMin;
  }

  public Integer getSchemaIdCount() {
    return this.schemaIdCount;
  }

  public void setSchemaIdCount(Integer schemaIdCount) {
    this.schemaIdCount = schemaIdCount;
  }

  public Integer getSchemaIdSum() {
    return this.schemaIdSum;
  }

  public void setSchemaIdSum(Integer schemaIdSum) {
    this.schemaIdSum = schemaIdSum;
  }

  public Integer getSchemaIdAvg() {
    return this.schemaIdAvg;
  }

  public void setSchemaIdAvg(Integer schemaIdAvg) {
    this.schemaIdAvg = schemaIdAvg;
  }

  public Integer getSchemaIdMax() {
    return this.schemaIdMax;
  }

  public void setSchemaIdMax(Integer schemaIdMax) {
    this.schemaIdMax = schemaIdMax;
  }

  public Integer getSchemaIdMin() {
    return this.schemaIdMin;
  }

  public void setSchemaIdMin(Integer schemaIdMin) {
    this.schemaIdMin = schemaIdMin;
  }

  public __TypeInput toInput() {
    __TypeInput input = new __TypeInput();
    input.setId(this.getId());
    input.setName(this.getName());
    if(getOfSchema() != null) {
      input.setOfSchema(this.getOfSchema().toInput());
    }
    input.setKind(this.getKind());
    input.setDescription(this.getDescription());
    if(getFields() != null) {
      input.setFields(this.getFields().stream().map(item -> item.toInput()).collect(Collectors.toList()));
    }
    if(getInterfaces() != null) {
      input.setInterfaces(this.getInterfaces().stream().map(item -> item.toInput()).collect(Collectors.toList()));
    }
    if(getPossibleTypes() != null) {
      input.setPossibleTypes(this.getPossibleTypes().stream().map(item -> item.toInput()).collect(Collectors.toList()));
    }
    if(getEnumValues() != null) {
      input.setEnumValues(this.getEnumValues().stream().map(item -> item.toInput()).collect(Collectors.toList()));
    }
    if(getInputFields() != null) {
      input.setInputFields(this.getInputFields().stream().map(item -> item.toInput()).collect(Collectors.toList()));
    }
    if(getOfType() != null) {
      input.setOfType(this.getOfType().toInput());
    }
    input.setIsDeprecated(this.getIsDeprecated());
    input.setVersion(this.getVersion());
    input.setRealmId(this.getRealmId());
    input.setCreateUserId(this.getCreateUserId());
    input.setCreateTime(this.getCreateTime());
    input.setUpdateUserId(this.getUpdateUserId());
    input.setUpdateTime(this.getUpdateTime());
    input.setCreateGroupId(this.getCreateGroupId());
    input.set__typename(this.get__typename());
    input.setSchemaId(this.getSchemaId());
    input.setOfTypeName(this.getOfTypeName());
    if(get__typeInterfaces() != null) {
      input.set__typeInterfaces(this.get__typeInterfaces().stream().map(item -> item.toInput()).collect(Collectors.toList()));
    }
    if(get__typePossibleTypes() != null) {
      input.set__typePossibleTypes(this.get__typePossibleTypes().stream().map(item -> item.toInput()).collect(Collectors.toList()));
    }
    return input;
  }
}
