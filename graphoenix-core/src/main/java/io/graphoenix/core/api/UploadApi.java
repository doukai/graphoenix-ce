package io.graphoenix.core.api;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;

import java.io.FileInputStream;
import java.util.List;

@GraphQLApi
public class UploadApi {

    @Mutation
    public String singleFile(FileInputStream file) {
        return "1";
    }

    @Mutation
    public List<String> multipleUpload(List<FileInputStream> files) {
        return List.of("1", "2", "3");
    }
}
