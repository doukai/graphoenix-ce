package io.graphoenix.core.handler;

import io.graphoenix.spi.graphql.operation.Operation;
import jakarta.enterprise.context.Dependent;
import jakarta.json.JsonValue;

@Dependent
public class TransactionCompensator {

    private Operation backupOperation;

    private JsonValue backup;

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
}
