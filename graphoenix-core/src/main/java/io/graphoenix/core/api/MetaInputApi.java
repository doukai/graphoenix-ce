package io.graphoenix.core.api;

import io.graphoenix.core.dto.inputObjectType.MetaInput;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Source;

import java.time.LocalDateTime;

@GraphQLApi
@ApplicationScoped
public class MetaInputApi {

    public MetaInput invokeMetaInput(@Source MetaInput metaInput) {
        LocalDateTime now = LocalDateTime.now();
        if (metaInput.getCreateTime() == null) {
            metaInput.setCreateTime(now);
            metaInput.setVersion(0);
        } else {
            metaInput.setUpdateTime(now);
            metaInput.setVersion(metaInput.getVersion() + 1);
        }
        return metaInput;
    }
}
