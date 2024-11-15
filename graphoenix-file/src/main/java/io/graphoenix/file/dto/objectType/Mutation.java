package io.graphoenix.file.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Mutation
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Mutation")
public class Mutation {
  /**
   * Mutation Field for 文件
   */
  @Description("Mutation Field for 文件")
  private File file;

  /**
   * Mutation Field for 文件 List
   */
  @Description("Mutation Field for 文件 List")
  private Collection<File> fileList;

  private File singleUpload;

  private Collection<File> multipleUpload;

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

  public File getSingleUpload() {
    return this.singleUpload;
  }

  public void setSingleUpload(File singleUpload) {
    this.singleUpload = singleUpload;
  }

  public Collection<File> getMultipleUpload() {
    return this.multipleUpload;
  }

  public void setMultipleUpload(Collection<File> multipleUpload) {
    this.multipleUpload = multipleUpload;
  }
}
