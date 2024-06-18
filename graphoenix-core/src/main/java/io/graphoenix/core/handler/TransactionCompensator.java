package io.graphoenix.core.handler;

import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.enterprise.context.Dependent;
import jakarta.json.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Dependent
public class TransactionCompensator {

    private Operation backupOperation;

    private JsonValue backup;

    private final Map<String, List<JsonValue>> typeValueListMap = new HashMap<>();

    private final Map<String, List<FetchItem>> newTypeFetchItemListMap = new HashMap<>();

    public Operation getBackupOperation() {
        return backupOperation;
    }

    public TransactionCompensator setBackupOperation(Operation backupOperation) {
        this.backupOperation = backupOperation;
        return this;
    }

    public JsonValue getBackup() {
        return backup;
    }

    public TransactionCompensator setBackup(JsonValue backup) {
        this.backup = backup;
        return this;
    }

    public void addNewTypePath(String typeName, FetchItem fetchItem) {
        List<FetchItem> fetchItemList = newTypeFetchItemListMap.computeIfAbsent(typeName, k -> new ArrayList<>());
        fetchItemList.add(fetchItem);
    }

    public void addTypeValue(String typeName, JsonValue jsonValue) {
        List<JsonValue> valueList = typeValueListMap.computeIfAbsent(typeName, k -> new ArrayList<>());
        valueList.add(jsonValue);
    }

    public TransactionCompensator addTypeFetchItemListMap(Map<String, List<FetchItem>> newTypeFetchItemListMap) {
        this.newTypeFetchItemListMap.putAll(newTypeFetchItemListMap);
        return this;
    }

    public TransactionCompensator addTypeValueListMap(Map<String, List<JsonValue>> typeValueListMap) {
        this.typeValueListMap.putAll(typeValueListMap);
        return this;
    }
}
