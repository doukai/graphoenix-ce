package io.graphoenix.core.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import java.util.Map;
import java.util.Set;

import static io.graphoenix.core.handler.PackageManager.LOAD_BALANCE_RANDOM;

@ConfigProperties(prefix = "package")
public class PackageConfig {

    @Optional
    private String packageName;

    @Optional
    private String packageLoadBalance = LOAD_BALANCE_RANDOM;

    @Optional
    private Map<String, Object> members;

    @Optional
    private Set<String> localPackageNames;

    @Optional
    private String grpcPackageName;

    @Optional
    private String objectTypePackageName;

    @Optional
    private String interfaceTypePackageName;

    @Optional
    private String unionTypePackageName;

    @Optional
    private String enumTypePackageName;

    @Optional
    private String inputObjectTypePackageName;

    @Optional
    private String directivePackageName;

    @Optional
    private String annotationPackageName;

    @Optional
    private String modulePackageName;

    @Optional
    private String handlerPackageName;

    @Optional
    private String operationPackageName;

    @Optional
    private String grpcObjectTypePackageName;

    @Optional
    private String grpcInterfaceTypePackageName;

    @Optional
    private String grpcUnionTypePackageName;

    @Optional
    private String grpcEnumTypePackageName;

    @Optional
    private String grpcInputObjectTypePackageName;

    @Optional
    private String grpcDirectivePackageName;

    @Optional
    private String grpcHandlerPackageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageLoadBalance() {
        return packageLoadBalance;
    }

    public void setPackageLoadBalance(String packageLoadBalance) {
        this.packageLoadBalance = packageLoadBalance;
    }

    public Set<String> getLocalPackageNames() {
        return localPackageNames;
    }

    public Map<String, Object> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Object> members) {
        this.members = members;
    }

    public void setLocalPackageNames(Set<String> localPackageNames) {
        this.localPackageNames = localPackageNames;
    }

    public String getGrpcPackageName() {
        return grpcPackageName != null ? grpcPackageName : packageName + ".grpc";
    }

    public void setGrpcPackageName(String grpcPackageName) {
        this.grpcPackageName = grpcPackageName;
    }

    public String getObjectTypePackageName() {
        return objectTypePackageName != null ? objectTypePackageName : packageName + ".dto.objectType";
    }

    public void setObjectTypePackageName(String objectTypePackageName) {
        this.objectTypePackageName = objectTypePackageName;
    }

    public String getInterfaceTypePackageName() {
        return interfaceTypePackageName != null ? interfaceTypePackageName : packageName + ".dto.interfaceType";
    }

    public void setInterfaceTypePackageName(String interfaceTypePackageName) {
        this.interfaceTypePackageName = interfaceTypePackageName;
    }

    public String getUnionTypePackageName() {
        return unionTypePackageName != null ? unionTypePackageName : packageName + ".dto.unionType";
    }

    public void setUnionTypePackageName(String unionTypePackageName) {
        this.unionTypePackageName = unionTypePackageName;
    }

    public String getEnumTypePackageName() {
        return enumTypePackageName != null ? enumTypePackageName : packageName + ".dto.enumType";
    }

    public void setEnumTypePackageName(String enumTypePackageName) {
        this.enumTypePackageName = enumTypePackageName;
    }

    public String getInputObjectTypePackageName() {
        return inputObjectTypePackageName != null ? inputObjectTypePackageName : packageName + ".dto.inputObjectType";
    }

    public void setInputObjectTypePackageName(String inputObjectTypePackageName) {
        this.inputObjectTypePackageName = inputObjectTypePackageName;
    }

    public String getDirectivePackageName() {
        return directivePackageName != null ? directivePackageName : packageName + ".dto.directive";
    }

    public void setDirectivePackageName(String directivePackageName) {
        this.directivePackageName = directivePackageName;
    }

    public String getAnnotationPackageName() {
        return annotationPackageName != null ? annotationPackageName : packageName + ".dto.annotation";
    }

    public void setAnnotationPackageName(String annotationPackageName) {
        this.annotationPackageName = annotationPackageName;
    }

    public String getModulePackageName() {
        return modulePackageName != null ? modulePackageName : packageName + ".module";
    }

    public void setModulePackageName(String modulePackageName) {
        this.modulePackageName = modulePackageName;
    }

    public String getHandlerPackageName() {
        return handlerPackageName != null ? handlerPackageName : packageName + ".handler";
    }

    public void setHandlerPackageName(String handlerPackageName) {
        this.handlerPackageName = handlerPackageName;
    }

    public String getOperationPackageName() {
        return operationPackageName != null ? operationPackageName : packageName + ".operation";
    }

    public void setOperationPackageName(String operationPackageName) {
        this.operationPackageName = operationPackageName;
    }

    public String getGrpcObjectTypePackageName() {
        return grpcObjectTypePackageName != null ? grpcObjectTypePackageName : getObjectTypePackageName() + ".grpc";
    }

    public void setGrpcObjectTypePackageName(String grpcObjectTypePackageName) {
        this.grpcObjectTypePackageName = grpcObjectTypePackageName;
    }

    public String getGrpcInterfaceTypePackageName() {
        return grpcInterfaceTypePackageName != null ? grpcInterfaceTypePackageName : getInterfaceTypePackageName() + ".grpc";
    }

    public void setGrpcInterfaceTypePackageName(String grpcInterfaceTypePackageName) {
        this.grpcInterfaceTypePackageName = grpcInterfaceTypePackageName;
    }

    public String getGrpcUnionTypePackageName() {
        return grpcUnionTypePackageName != null ? grpcUnionTypePackageName : getUnionTypePackageName() + ".grpc";
    }

    public void setGrpcUnionTypePackageName(String grpcUnionTypePackageName) {
        this.grpcUnionTypePackageName = grpcUnionTypePackageName;
    }

    public String getGrpcEnumTypePackageName() {
        return grpcEnumTypePackageName != null ? grpcEnumTypePackageName : getEnumTypePackageName() + ".grpc";
    }

    public void setGrpcEnumTypePackageName(String grpcEnumTypePackageName) {
        this.grpcEnumTypePackageName = grpcEnumTypePackageName;
    }

    public String getGrpcInputObjectTypePackageName() {
        return grpcInputObjectTypePackageName != null ? grpcInputObjectTypePackageName : getInputObjectTypePackageName() + ".grpc";
    }

    public void setGrpcInputObjectTypePackageName(String grpcInputObjectTypePackageName) {
        this.grpcInputObjectTypePackageName = grpcInputObjectTypePackageName;
    }

    public String getGrpcDirectivePackageName() {
        return grpcDirectivePackageName != null ? grpcDirectivePackageName : getDirectivePackageName() + ".grpc";
    }

    public void setGrpcDirectivePackageName(String grpcDirectivePackageName) {
        this.grpcDirectivePackageName = grpcDirectivePackageName;
    }

    public String getGrpcHandlerPackageName() {
        return grpcHandlerPackageName != null ? grpcHandlerPackageName : getHandlerPackageName() + ".grpc";
    }

    public void setGrpcHandlerPackageName(String grpcHandlerPackageName) {
        this.grpcHandlerPackageName = grpcHandlerPackageName;
    }
}
