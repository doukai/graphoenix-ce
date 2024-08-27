package io.graphoenix.file.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Type;

@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
public class Mutation {
  private File file;

  private Collection<File> fileList;

  private String singleUpload;

  private Collection<String> multipleUpload;

  public File getFile() {
    return this.file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public Collection<File> getFileList() {
    return this.fileList;
  }

  public void setFileList(Collection<File> fileList) {
    this.fileList = fileList;
  }

  public String getSingleUpload() {
    return this.singleUpload;
  }

  public void setSingleUpload(String singleUpload) {
    this.singleUpload = singleUpload;
  }

  public Collection<String> getMultipleUpload() {
    return this.multipleUpload;
  }

  public void setMultipleUpload(Collection<String> multipleUpload) {
    this.multipleUpload = multipleUpload;
  }
}
