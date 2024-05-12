package io.graphoenix.core.api;

import io.graphoenix.core.config.MutationConfig;
import io.graphoenix.core.dto.inputObjectType.MetaInput;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Source;

import java.time.LocalDateTime;

@GraphQLApi
@ApplicationScoped
public class MetaInputApi {

    private final MutationConfig mutationConfig;

    @Inject
    public MetaInputApi(MutationConfig mutationConfig) {
        this.mutationConfig = mutationConfig;
    }

    public MetaInput invokeMetaInput(@Source MetaInput metaInput) {
        LocalDateTime now = LocalDateTime.now();
        if (metaInput.getCreateTime() == null) {
            metaInput.setCreateTime(now);
            if (mutationConfig.getOcc()) {
                metaInput.setVersion(0);
            }
        } else {
            metaInput.setUpdateTime(now);
            if (mutationConfig.getOcc()) {
                if (metaInput.getVersion() != null) {
                    metaInput.setVersion(metaInput.getVersion() + 1);
                } else {
                    metaInput.setVersion(0);
                }
            }
        }
        return metaInput;
    }
}
