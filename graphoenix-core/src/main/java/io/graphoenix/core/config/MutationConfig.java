package io.graphoenix.core.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "mutation")
public class MutationConfig {

    @Optional
    private Boolean occ = false;

    private Boolean compensatingTransaction = false;

    public Boolean getOcc() {
        return occ;
    }

    public void setOcc(Boolean occ) {
        this.occ = occ;
    }

    public Boolean getCompensatingTransaction() {
        return compensatingTransaction;
    }

    public void setCompensatingTransaction(Boolean compensatingTransaction) {
        this.compensatingTransaction = compensatingTransaction;
    }
}
