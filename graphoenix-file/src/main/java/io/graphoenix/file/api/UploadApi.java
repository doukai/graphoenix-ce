package io.graphoenix.file.api;

import io.graphoenix.spi.annotation.TypeName;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;

import java.util.List;

@GraphQLApi
public class UploadApi {

    @Mutation
    public String singleUpload(@TypeName("Upload") String file) {
        return file;
    }

    @Mutation
    public List<String> multipleUpload(List<@TypeName("Upload") String> files) {
        return files;
    }
}
