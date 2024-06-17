package io.graphoenix.core.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.enterprise.context.Dependent;
import jakarta.json.JsonValue;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Dependent
public class TransactionCompensator {

    private Operation backupOperation;

    private JsonValue backup;

    private final Map<String, List<Tuple2<String, String>>> newTypePathListMap = new HashMap<>();

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

    public void addNewTypePath(String typeName, String fieldName, String path) {
        List<Tuple2<String, String>> pathList = newTypePathListMap.computeIfAbsent(typeName, k -> new ArrayList<>());
        pathList.add(Tuples.of(fieldName, path));
    }
}
