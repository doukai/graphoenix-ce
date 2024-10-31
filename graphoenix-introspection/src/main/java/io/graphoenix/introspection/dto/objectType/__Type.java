package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.enumType.__TypeKind;
import io.graphoenix.core.dto.interfaceType.Meta;
import jakarta.annotation.Generated;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class __Type implements Meta {
  @Id
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

  private Boolean isDeprecated = false;

  private Integer version;

  private Integer realmId;

  private String createUserId;

  private LocalDateTime createTime;

  private String updateUserId;

  private LocalDateTime updateTime;

  private String createGroupId;

  private String __typename = "__Type";

  private Integer schemaId;

  private String ofTypeName;

  private Collection<__TypeInterfaces> __typeInterfaces;

  private Collection<__TypePossibleTypes> __typePossibleTypes;

  private __Field fieldsAggregate;

  private __FieldConnection fieldsConnection;

  private __Type interfacesAggregate;

  private __TypeConnection interfacesConnection;

  private __Type possibleTypesAggregate;

  private __TypeConnection possibleTypesConnection;

  private __EnumValue enumValuesAggregate;

  private __EnumValueConnection enumValuesConnection;

  private __InputValue inputFieldsAggregate;

  private __InputValueConnection inputFieldsConnection;

  private __TypeInterfaces __typeInterfacesAggregate;

  private __TypeInterfacesConnection __typeInterfacesConnection;

  private __TypePossibleTypes __typePossibleTypesAggregate;

  private __TypePossibleTypesConnection __typePossibleTypesConnection;

  private Integer nameCount;

  private String nameMax;

  private String nameMin;

  private Integer kindCount;

  private __TypeKind kindMax;

  private __TypeKind kindMin;

  private Integer descriptionCount;

  private String descriptionMax;

  private String descriptionMin;

  private Integer ofTypeNameCount;

  private String ofTypeNameMax;

  private String ofTypeNameMin;

  private Integer schemaIdCount;

  private Integer schemaIdSum;

  private Integer schemaIdAvg;

  private Integer schemaIdMax;

  private Integer schemaIdMin;

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
}
